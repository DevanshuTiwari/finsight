package com.finsight.transaction_processor_service.service;


import com.finsight.transaction_processor_service.dto.Message;
import com.finsight.transaction_processor_service.dto.OpenAiRequest;
import com.finsight.transaction_processor_service.dto.OpenAiResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class AiCategorizationServiceImpl implements AiCategorizationService {

    private final RestTemplate restTemplate;

    // The official API endpoint for OpenAI's chat models
    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

    public AiCategorizationServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String categorizeTransaction(String transactionDescription) {
        // 1. We construct a very specific "prompt" to instruct the AI.
        String prompt = """
                You are an expert financial assistant. Based on the following transaction description,
                please provide a single, appropriate category.
                
                The available categories are:
                Groceries, Restaurants & Dining, Transportation, Utilities, Rent/Mortgage,
                Shopping, Entertainment, Health & Wellness, Travel, Income, Transfers,
                Fees & Charges, Other.
                
                Transaction Description: "%s"
                
                Return only the single, most appropriate category name and nothing else.
                """.formatted(transactionDescription);

        // 2. We create the request body using our DTOs.
        OpenAiRequest request = new OpenAiRequest(
                "gpt-3.5-turbo", // A fast and cost-effective model
                List.of(new Message("user", prompt))
        );

        // 3. We make the API call using the RestTemplate bean we configured earlier.
        OpenAiResponse response = restTemplate.postForObject(OPENAI_API_URL, request, OpenAiResponse.class);

        // 4. We safely parse the response to get the AI's answer.
        if (response == null || response.choices() == null || response.choices().isEmpty()) {
            // If the AI gives no answer, we fall back to "Uncategorized".
            return "Uncategorized";
        }

        // We trim any extra whitespace or newlines from the AI's response.
        return response.choices().get(0).message().content().trim();
    }
}
