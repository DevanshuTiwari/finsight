package com.finsight.account_service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionCreatedEvent(
        Long transactionId,
        Long userId,
        String description,
        BigDecimal amount,
        LocalDate transactionDate
) {
}
