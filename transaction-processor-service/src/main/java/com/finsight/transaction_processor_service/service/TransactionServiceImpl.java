package com.finsight.transaction_processor_service.service;

import com.finsight.common.dto.TransactionCreatedEvent;
import com.finsight.common.dto.UpdateCategoryRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final AiCategorizationService aiService;
    private final WebClient webClient; // Inject WebClient

    public TransactionServiceImpl(AiCategorizationService aiService, WebClient webClient) {
        this.aiService = aiService;
        this.webClient = webClient;
    }

    @KafkaListener(topics = "raw-transactions", groupId = "transaction-processor")
    public void handleTransactionCreatedEvent(TransactionCreatedEvent event) {
        log.info("Processing event for transaction ID: {}", event.transactionId());

        try {
            // 1. Call the AI service to get the category
            String category = aiService.categorizeTransaction(event.description());
            log.info("--> Transaction ID: [{}], AI Category: [{}]", event.transactionId(), category);

            // 2. Define the URI for the update endpoint in account-service
            String uri = "/api/v1/accounts/transactions/" + event.transactionId() + "/category";

            // 3. Create the request body DTO
            UpdateCategoryRequest requestDto = new UpdateCategoryRequest(category);

            // 4. Make the API call to save the category
            webClient.put()
                    .uri(uri)
                    .bodyValue(requestDto)
                    .retrieve() // Executes the request
                    .toBodilessEntity()
                    .block();

            log.info("Successfully updated category for transaction ID: {}", event.transactionId());

        } catch (Exception e) {
            log.error("Error processing transaction ID {}: {}", event.transactionId(), e.getMessage());
            throw new RuntimeException("Failed to process transaction", e);
        }
    }
}
