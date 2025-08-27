package com.finsight.account_service.dto;

public record UploadResponse(
        String message,
        int transactionsImported
) {
}
