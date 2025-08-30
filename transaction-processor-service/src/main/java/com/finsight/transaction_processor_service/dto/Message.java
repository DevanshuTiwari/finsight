package com.finsight.transaction_processor_service.dto;

public record Message(
        String role,
        String content
) {
}
