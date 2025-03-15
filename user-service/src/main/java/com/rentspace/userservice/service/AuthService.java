package com.rentspace.userservice.service;

import com.rentspace.userservice.dto.AuthResponseDto;
import com.rentspace.userservice.dto.RegisterRequest;
import com.rentspace.userservice.dto.TokenResponseDto;
import com.rentspace.userservice.entity.user.User;

import java.util.Map;

public interface AuthService {

    TokenResponseDto authenticateAndGenerateTokens(String username, String password); // DONE
    AuthResponseDto registerUserAndGenerateTokens(RegisterRequest request); // DONE
    TokenResponseDto refreshAccessToken(String token); // DONE
    User findUserByUsername(String username);
}
