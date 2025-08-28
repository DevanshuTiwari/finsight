package com.finsight.account_service.service;


import com.finsight.account_service.dto.UploadResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AccountServiceImpl implements AccountService {

    private final CsvFileProcessingService fileProcessingService;

    @Autowired
    public AccountServiceImpl(CsvFileProcessingService fileProcessingService) {
        this.fileProcessingService = fileProcessingService;
    }

    @Override
    public UploadResponse importTransactionsFromCsv(MultipartFile file, Long userId) {
        int count = fileProcessingService.processCsvFile(file, userId);
        String message = count + " transactions imported successfully.";
        return new UploadResponse(message, count);
    }
}
