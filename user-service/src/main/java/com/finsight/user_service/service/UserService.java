package com.finsight.user_service.service;

import com.finsight.user_service.dto.LoginRequestDto;
import com.finsight.user_service.dto.LoginResponseDto;
import com.finsight.user_service.dto.RegisterRequestDto;
import com.finsight.user_service.dto.RegisterResponseDto;


public interface UserService {
    RegisterResponseDto registerUser(RegisterRequestDto registerRequestDto);
    LoginResponseDto loginUser(LoginRequestDto loginRequestDto);
}
