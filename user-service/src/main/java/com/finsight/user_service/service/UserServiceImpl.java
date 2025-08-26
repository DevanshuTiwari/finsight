package com.finsight.user_service.service;

import com.finsight.user_service.dto.LoginRequestDto;
import com.finsight.user_service.dto.LoginResponseDto;
import com.finsight.user_service.dto.RegisterRequestDto;
import com.finsight.user_service.dto.RegisterResponseDto;
import com.finsight.user_service.exception.InvalidCredentialsException;
import com.finsight.user_service.exception.UserAlreadyExistsException;
import com.finsight.user_service.model.User;
import com.finsight.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtTokenProvider;
    }

    @Override
    public RegisterResponseDto registerUser(RegisterRequestDto registerRequestDto) {
        if (userRepository.findByEmail(registerRequestDto.email()).isPresent()) {
            throw new UserAlreadyExistsException("User already exists with email: " + registerRequestDto.email());
        }

        User user = new User();
        user.setFirstName(registerRequestDto.firstName());
        user.setLastName(registerRequestDto.lastName());
        user.setEmail(registerRequestDto.email());
        user.setPassword(passwordEncoder.encode(registerRequestDto.password()));

        userRepository.save(user);

        return new RegisterResponseDto("User registered successfully");
    }

    @Override
    public LoginResponseDto loginUser(LoginRequestDto loginRequestDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDto.email(),
                            loginRequestDto.password()
                    )
            );

            // If we get here, authentication was successful
            String token = jwtService.generateToken(loginRequestDto.email());
            return new LoginResponseDto(token, "Login successful");

        } catch (AuthenticationException e) {
            throw new InvalidCredentialsException("Invalid email or password");
        }
    }
}
