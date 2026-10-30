package net.chaimae.transactionservice.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import net.chaimae.transactionservice.domain.Transaction;
import net.chaimae.transactionservice.integration.AccountGateway;
import net.chaimae.transactionservice.integration.AccountDto;
import net.chaimae.transactionservice.integration.BalanceUpdateRequest;
import net.chaimae.transactionservice.messaging.TransactionEventPublisher;
import net.chaimae.transactionservice.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService {

    private final TransactionRepository repository;
    private final AccountGateway accountClient;
    private final TransactionEventPublisher eventPublisher;
    private final BigDecimal maxAmount;

    public TransactionService(TransactionRepository repository, AccountGateway accountClient,
            TransactionEventPublisher eventPublisher,
            @Value("${bank.transaction.max-amount:1000}") BigDecimal maxAmount) {
        this.repository = repository;
        this.accountClient = accountClient;
        this.eventPublisher = eventPublisher;
        this.maxAmount = maxAmount;
    }

    @Transactional
    public Transaction create(CreateTransactionCommand command) {
        validateAmount(command.amount());
        AccountDto account = accountClient.getAccount(command.accountId());
        validateAccount(account, command.currency());
        accountClient.updateBalance(account.id(), new BalanceUpdateRequest(command.amount().negate().doubleValue()));
        Transaction transaction = repository.save(new Transaction(account.id(), command.amount(),
                normalizeCurrency(command.currency()), command.merchantCategory(), command.country()));
        eventPublisher.publish(transaction);
        return transaction;
    }

    @Transactional(readOnly = true)
    public List<Transaction> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Transaction> findByAccount(UUID accountId) {
        return repository.findByAccountIdOrderByCreatedAtDesc(accountId);
    }

    @Transactional(readOnly = true)
    public Transaction get(UUID id) {
        return repository.findById(id).orElseThrow(() -> new TransactionException("Transaction not found: " + id));
    }

    @Transactional
    public List<Transaction> transfer(TransferCommand command) {
        if (command.sourceAccountId().equals(command.destinationAccountId())) {
            throw new TransactionException("Source and destination accounts must be different");
        }
        validateAmount(command.amount());
        AccountDto source = accountClient.getAccount(command.sourceAccountId());
        AccountDto destination = accountClient.getAccount(command.destinationAccountId());
        validateAccount(source, command.currency());
        validateAccount(destination, command.currency());
        accountClient.updateBalance(source.id(), new BalanceUpdateRequest(command.amount().negate().doubleValue()));
        try {
            accountClient.updateBalance(destination.id(), new BalanceUpdateRequest(command.amount().doubleValue()));
        } catch (RuntimeException exception) {
            accountClient.updateBalance(source.id(), new BalanceUpdateRequest(command.amount().doubleValue()));
            throw exception;
        }
        Transaction debit = repository.save(new Transaction(source.id(), command.amount(),
                normalizeCurrency(command.currency()), "TRANSFER", "N/A"));
        Transaction credit = repository.save(new Transaction(destination.id(), command.amount(),
                normalizeCurrency(command.currency()), "TRANSFER", "N/A"));
        eventPublisher.publish(debit);
        eventPublisher.publish(credit);
        return List.of(debit, credit);
    }

    private void validateAmount(BigDecimal amount) {
        if (amount.signum() <= 0 || amount.compareTo(maxAmount) > 0) {
            throw new TransactionException("Amount must be positive and no greater than " + maxAmount);
        }
    }

    private void validateAccount(AccountDto account, String currency) {
        if (!"ACTIVE".equals(account.status())) {
            throw new TransactionException("Account is not active: " + account.id());
        }
        if (!account.currency().equalsIgnoreCase(currency)) {
            throw new TransactionException("Transaction currency does not match account currency");
        }
    }

    private String normalizeCurrency(String currency) {
        return currency.toUpperCase(Locale.ROOT);
    }
}
