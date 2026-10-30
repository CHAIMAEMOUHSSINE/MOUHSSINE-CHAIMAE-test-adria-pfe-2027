package net.chaimae.transactionservice.web.graphql;

import java.util.List;
import java.util.UUID;

import net.chaimae.transactionservice.domain.Transaction;
import net.chaimae.transactionservice.service.CreateTransactionCommand;
import net.chaimae.transactionservice.service.TransactionService;
import net.chaimae.transactionservice.service.TransferCommand;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class TransactionGraphqlController {

    private final TransactionService service;

    public TransactionGraphqlController(TransactionService service) {
        this.service = service;
    }

    @QueryMapping
    public List<Transaction> transactions() {
        return service.findAll();
    }

    @QueryMapping
    public List<Transaction> transactionsByAccount(@Argument UUID accountId) {
        return service.findByAccount(accountId);
    }

    @MutationMapping
    public Transaction createTransaction(@Argument CreateTransactionCommand input) {
        return service.create(input);
    }

    @MutationMapping
    public List<Transaction> transfer(@Argument TransferCommand input) {
        return service.transfer(input);
    }
}
