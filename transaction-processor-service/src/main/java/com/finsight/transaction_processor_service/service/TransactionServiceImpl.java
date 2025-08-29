package com.finsight.transaction_processor_service.service;

import com.finsight.transaction_processor_service.dto.TransactionCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    @Override
    @KafkaListener(topics = "raw-transactions", groupId = "transaction-processor")
    public void handleTransactionCreatedEvent(TransactionCreatedEvent event) {
        log.info("Received event for transaction ID: {}", event.transactionId());
    }
}
