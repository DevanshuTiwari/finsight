package com.finsight.account_service.service;

import com.finsight.account_service.dto.UploadResponse;
import org.springframework.web.multipart.MultipartFile;

public interface AccountService {
    UploadResponse importTransactionsFromCsv(MultipartFile file, Long userId);
    void updateTransactionCategory(Long transactionId, String category);
}
