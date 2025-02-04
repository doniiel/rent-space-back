package com.rentspace.userservice.service;

import com.rentspace.userservice.dto.RegisterRequest;

import java.util.Map;

public interface AuthService {

    Map<String, String> authenticateAndGenerateTokens(String username, String password); // DONE
    Map<String, Object> registerUserAndGenerateTokens(RegisterRequest request); // DONE
    Map<String, String> refreshAccessToken(String token); // DONE

}
