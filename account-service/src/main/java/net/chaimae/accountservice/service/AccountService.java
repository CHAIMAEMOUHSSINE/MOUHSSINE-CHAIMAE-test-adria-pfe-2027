package net.chaimae.accountservice.service;


import java.util.List;
import java.util.UUID;


import net.chaimae.accountservice.Account.Account;
import net.chaimae.accountservice.Account.AccountStatus;
import net.chaimae.accountservice.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Couche service unique, appelée par les connecteurs REST, GraphQL et MCP. */
@Service
@Transactional
public class AccountService {

    private final AccountRepository repository;

    public AccountService(AccountRepository repository) {
        this.repository = repository;
    }

    public Account create(CreateAccountCommand cmd) {
        Account account = new Account(cmd.customerReference(), cmd.currency().toUpperCase(), cmd.initialBalance());
        return repository.save(account);
    }

    @Transactional(readOnly = true)
    public List<Account> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Account get(UUID id) {
        return repository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
    }

    public Account changeStatus(UUID id, AccountStatus status) {
        Account account = repository.findByIdForUpdate(id).orElseThrow(() -> new AccountNotFoundException(id));
        account.setStatus(status);
        return account;
    }

    /**
     * Applique un mouvement (delta négatif = débit, positif = crédit).
     * Refuse un solde négatif.
     */
    public Account applyBalanceDelta(UUID id, double delta) {
        Account account = repository.findByIdForUpdate(id).orElseThrow(() -> new AccountNotFoundException(id));
        double newBalance = Math.round((account.getBalance() + delta) * 100.0) / 100.0;
        if (newBalance < 0) {
            throw new InsufficientFundsException(id, account.getBalance(), delta);
        }
        account.setBalance(newBalance);
        return account;
    }
}
