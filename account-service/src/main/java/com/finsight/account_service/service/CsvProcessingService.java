package com.finsight.account_service.service;


import com.finsight.account_service.exception.CsvProcessingException;
import com.finsight.account_service.model.Transaction;
import com.finsight.account_service.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CsvProcessingService implements FileProcessingService {

    private final TransactionRepository transactionRepository;

    public CsvProcessingService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public int processCsvFile(MultipartFile file, Long userId) {
        // 1. Basic File Validation
        if (file.isEmpty()) {
            throw new CsvProcessingException("File is empty. Please upload a valid CSV file.");
        }
        if (!"text/csv".equals(file.getContentType())) {
            throw new CsvProcessingException("Invalid file type. Please upload a CSV file.");
        }

        List<Transaction> transactions = new ArrayList<>();

        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            // 2. Configure the CSV Parser
            CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                    .setHeader("Date", "Description", "Amount") // Define expected headers
                    .setSkipHeaderRecord(true) // Skip the first line (header row)
                    .setIgnoreHeaderCase(true)
                    .setTrim(true)
                    .build();

            CSVParser csvParser = new CSVParser(reader, csvFormat);

            // 3. Loop through each row in the CSV
            for (CSVRecord csvRecord : csvParser) {
                Transaction transaction = new Transaction();
                transaction.setUserId(userId);

                // 4. Robustly parse each field with error handling
                try {
                    String dateStr = csvRecord.get("Date");
                    transaction.setTransactionDate(LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("MM/dd/yyyy")));

                    transaction.setDescription(csvRecord.get("Description"));

                    BigDecimal amount = new BigDecimal(csvRecord.get("Amount"));
                    transaction.setAmount(amount);

                    // Determine type based on amount
                    transaction.setType(amount.compareTo(BigDecimal.ZERO) < 0 ? "DEBIT" : "CREDIT");
                    transaction.setCategory("Uncategorized"); // Default category

                    transactions.add(transaction);

                } catch (Exception e) {
                    // If one row is bad, log it and continue with the rest of the file
                    log.error("Skipping malformed row {}: {}", csvRecord.getRecordNumber(), e.getMessage());
                }
            }

            // 5. Save all valid transactions to the database in one batch
            transactionRepository.saveAll(transactions);
            return transactions.size();

        } catch (Exception e) {
            // Handle errors related to reading the file or parsing the whole thing
            throw new CsvProcessingException("Failed to process CSV file: " + e.getMessage());
        }
    }

}
