package net.chaimae.transactionservice.messaging;

import net.chaimae.transactionservice.domain.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class TransactionEventPublisher {

    private final KafkaTemplate<String, TransactionCreatedEvent> kafkaTemplate;
    private final String topic;

    public TransactionEventPublisher(KafkaTemplate<String, TransactionCreatedEvent> kafkaTemplate,
            @Value("${bank.kafka.transactions-topic:bank.transactions.created}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void publish(Transaction transaction) {
        kafkaTemplate.send(topic, transaction.getAccountId().toString(), TransactionCreatedEvent.from(transaction));
    }
}
