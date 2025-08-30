package com.finsight.transaction_processor_service.dto;

import java.util.List;

public record OpenAiRequest(
        String model,
        List<Message> messages
) {
}
