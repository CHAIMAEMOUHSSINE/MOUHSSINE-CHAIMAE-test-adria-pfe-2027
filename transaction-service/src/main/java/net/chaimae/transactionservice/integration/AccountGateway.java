package net.chaimae.transactionservice.integration;

import java.util.UUID;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import net.chaimae.transactionservice.service.TransactionException;
import org.springframework.stereotype.Service;

@Service
public class AccountGateway {

    private final AccountClient client;

    public AccountGateway(AccountClient client) {
        this.client = client;
    }

    @CircuitBreaker(name = "accountService", fallbackMethod = "accountFallback")
    public AccountDto getAccount(UUID id) {
        return client.getAccount(id);
    }

    @CircuitBreaker(name = "accountService", fallbackMethod = "accountFallback")
    public AccountDto updateBalance(UUID id, BalanceUpdateRequest request) {
        return client.updateBalance(id, request);
    }

    private AccountDto accountFallback(UUID id, Throwable cause) {
        throw new TransactionException("Account service is temporarily unavailable");
    }

    private AccountDto accountFallback(UUID id, BalanceUpdateRequest request, Throwable cause) {
        throw new TransactionException("Account service is temporarily unavailable");
    }
}
