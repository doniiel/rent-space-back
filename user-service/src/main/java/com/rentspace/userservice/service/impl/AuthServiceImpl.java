package com.rentspace.userservice.service.impl;

import com.rentspace.userservice.dto.RegisterRequest;
import com.rentspace.userservice.dto.UserCreateRequest;
import com.rentspace.userservice.entity.Token;
import com.rentspace.userservice.entity.User;
import com.rentspace.userservice.exception.InvalidCredentialsException;
import com.rentspace.userservice.exception.UserNotFoundException;
import com.rentspace.userservice.repository.TokenRepository;
import com.rentspace.userservice.service.AuthService;
import com.rentspace.userservice.service.UserService;
import com.rentspace.userservice.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtTokenUtil;
    private final TokenRepository tokenRepository;


    @Override
    public Map<String, String> authenticateAndGenerateTokens(String username, String password) {
        User user = authenticateUser(username, password);
        return generateTokens(user.getUsername(), user.getRole().name());
    }

    @Override
    public Map<String, Object> registerUserAndGenerateTokens(RegisterRequest request) {
        UserCreateRequest userCreate = UserCreateRequest.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .email(request.getEmail())
                .phone(request.getPhone())
                .build();

        var user = userService.createUser(userCreate);
        Map<String, Object> result = new HashMap<>(generateTokens(user.getUsername(), user.getRole()));
        result.put("user", user);
        return result;
    }

    @Override
    public Map<String, String> refreshAccessToken(String token) {
        String username = jwtTokenUtil.extractUsername(token);
        String role = jwtTokenUtil.extractRole(token);
        return generateTokens(username, role);
    }

    private User authenticateUser(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        if (!authentication.isAuthenticated()) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        return (User) authentication.getPrincipal();
    }

    private Map<String, String> generateTokens(String username, String role) {
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", jwtTokenUtil.generateAccessToken(username, new HashMap<>(), role));
        tokens.put("refresh_token", jwtTokenUtil.generateRefreshToken(username));
        var user = userService.getUserByUsername(username);
        revokeAllUserTokens(user);
        saveUserToken(user, tokens.get("access_token"));
        return tokens;
    }
    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .token(jwtToken)
                .user(user)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllByValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty()) return;
        validUserTokens.forEach(token -> {
            token.setRevoked(true);
            token.setExpired(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}
