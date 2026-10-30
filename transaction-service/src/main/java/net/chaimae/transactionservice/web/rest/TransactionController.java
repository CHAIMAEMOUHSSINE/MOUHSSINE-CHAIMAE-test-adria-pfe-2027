package net.chaimae.transactionservice.web.rest;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import net.chaimae.transactionservice.domain.Transaction;
import net.chaimae.transactionservice.service.CreateTransactionCommand;
import net.chaimae.transactionservice.service.TransactionService;
import net.chaimae.transactionservice.service.TransferCommand;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @GetMapping
    public List<Transaction> list() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Transaction get(@PathVariable UUID id) {
        return service.get(id);
    }

    @GetMapping("/account/{accountId}")
    public List<Transaction> byAccount(@PathVariable UUID accountId) {
        return service.findByAccount(accountId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Transaction create(@Valid @RequestBody CreateTransactionCommand command) {
        return service.create(command);
    }

    @PostMapping("/transfer")
    @ResponseStatus(HttpStatus.CREATED)
    public List<Transaction> transfer(@Valid @RequestBody TransferCommand command) {
        return service.transfer(command);
    }
}
