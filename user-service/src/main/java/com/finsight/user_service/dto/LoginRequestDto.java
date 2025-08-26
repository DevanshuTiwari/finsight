package com.finsight.user_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


public record LoginRequestDto(
        @NotBlank @Email
        String email,
        @NotBlank
        String password
) {}
