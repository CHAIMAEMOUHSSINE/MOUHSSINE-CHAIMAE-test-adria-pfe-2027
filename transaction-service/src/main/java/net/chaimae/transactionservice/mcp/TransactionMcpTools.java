package net.chaimae.transactionservice.mcp;

import java.util.List;
import java.util.UUID;

import net.chaimae.transactionservice.domain.Transaction;
import net.chaimae.transactionservice.service.TransactionService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

@Service
public class TransactionMcpTools {

    private final TransactionService service;

    public TransactionMcpTools(TransactionService service) {
        this.service = service;
    }

    @Tool(description = "List transactions for a bank account")
    public List<Transaction> listTransactions(UUID accountId) {
        return service.findByAccount(accountId);
    }
}
