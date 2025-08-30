package com.finsight.transaction_processor_service.dto;

import java.util.List;

public record OpenAiResponse(
        List<Choice> choices
) {
}
