package com.finsight.user_service.dto;

public record LoginResponseDto(
        String accessToken,
        String message
) {
}
