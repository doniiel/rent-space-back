package com.rentspace.userservice.service;

import com.rentspace.userservice.dto.RegisterRequest;
import com.rentspace.userservice.entity.user.User;

import java.util.Map;

public interface AuthService {

    Map<String, String> authenticateAndGenerateTokens(String username, String password); // DONE
    Map<String, Object> registerUserAndGenerateTokens(RegisterRequest request); // DONE
    Map<String, String> refreshAccessToken(String token); // DONE
    User findUserByUsername(String username);
}
