package net.chaimae.transactionservice.messaging;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import net.chaimae.transactionservice.domain.Transaction;

public record TransactionCreatedEvent(
        UUID eventId,
        String eventType,
        UUID transactionId,
        UUID accountId,
        BigDecimal amount,
        String currency,
        Instant occurredAt) {

    public static TransactionCreatedEvent from(Transaction transaction) {
        return new TransactionCreatedEvent(UUID.randomUUID(), "TransactionCreated", transaction.getId(),
                transaction.getAccountId(), transaction.getAmount(), transaction.getCurrency(), transaction.getCreatedAt());
    }
}
