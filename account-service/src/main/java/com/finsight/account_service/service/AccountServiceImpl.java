package com.finsight.account_service.service;


import com.finsight.account_service.dto.UploadResponse;
import com.finsight.account_service.exception.TransactionNotFoundException;
import com.finsight.account_service.model.Transaction;
import com.finsight.account_service.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AccountServiceImpl implements AccountService {

    private final CsvFileProcessingService fileProcessingService;
    private final TransactionRepository transactionRepository;

    @Autowired
    public AccountServiceImpl(CsvFileProcessingService fileProcessingService, TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
        this.fileProcessingService = fileProcessingService;
    }

    @Override
    public UploadResponse importTransactionsFromCsv(MultipartFile file, Long userId) {
        int count = fileProcessingService.processCsvFile(file, userId);
        String message = count + " transactions imported successfully.";
        return new UploadResponse(message, count);
    }

    @Override
    public void updateTransactionCategory(Long transactionId, String category) {
        // Find the transaction by its ID, or throw an exception if it's not found
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found with id: " + transactionId));

        // Update the category and save the transaction
        transaction.setCategory(category);
        transactionRepository.save(transaction);
    }
}
