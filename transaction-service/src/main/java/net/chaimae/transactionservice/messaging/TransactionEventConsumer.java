package net.chaimae.transactionservice.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(TransactionEventConsumer.class);

    @KafkaListener(topics = "${bank.kafka.transactions-topic:bank.transactions.created}",
            groupId = "${bank.kafka.consumer-group:transaction-audit}")
    public void consume(TransactionCreatedEvent event) {
        log.info("TransactionCreated event received: eventId={}, transactionId={}, accountId={}, amount={}, currency={}",
                event.eventId(), event.transactionId(), event.accountId(), event.amount(), event.currency());
    }
}
