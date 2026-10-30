package net.chaimae.transactionservice.repository;

import java.util.List;
import java.util.UUID;

import net.chaimae.transactionservice.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findByAccountIdOrderByCreatedAtDesc(UUID accountId);
}
