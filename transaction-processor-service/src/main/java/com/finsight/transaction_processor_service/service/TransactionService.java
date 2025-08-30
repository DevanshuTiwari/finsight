package com.finsight.transaction_processor_service.service;

import com.finsight.common.dto.TransactionCreatedEvent;

public interface TransactionService {
    void handleTransactionCreatedEvent(TransactionCreatedEvent event);
}
