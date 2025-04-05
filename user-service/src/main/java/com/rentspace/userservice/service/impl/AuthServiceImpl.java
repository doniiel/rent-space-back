package com.rentspace.userservice.service.impl;

import com.rentspace.core.dto.UserDto;
import com.rentspace.userservice.dto.AuthResponseDto;
import com.rentspace.userservice.dto.RegisterRequest;
import com.rentspace.userservice.dto.TokenResponseDto;
import com.rentspace.userservice.dto.UserCreateRequest;
import com.rentspace.userservice.entity.token.Token;
import com.rentspace.userservice.entity.user.User;
import com.rentspace.userservice.exception.InvalidCredentialsException;
import com.rentspace.userservice.jwt.JwtService;
import com.rentspace.userservice.mapper.UserMapper;
import com.rentspace.userservice.repository.TokenRepository;
import com.rentspace.userservice.service.AccountVerificationService;
import com.rentspace.userservice.service.AuthService;
import com.rentspace.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AccountVerificationService accountVerificationService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final UserMapper mapper;

    @Value("${jwt.access-expiration-time}")
    private long accessTokenExpirationTimeMs;

    @Override
    @Transactional(readOnly = true)
    public TokenResponseDto authenticateAndGenerateTokens(String username, String password) {
        log.info("Attempting to authenticate user: {}", username);
        User user = authenticateUser(username, password);
        TokenResponseDto tokens = generateTokens(user);
        log.info("User authenticated successfully with username: {}", username);
        return tokens;
    }

    @Override
    @Transactional
    public AuthResponseDto registerUserAndGenerateTokens(RegisterRequest request) {
        log.info("Attempting to register user: {}", request.getUsername());

        UserCreateRequest userCreate = mapToUserCreateRequest(request);
        UserDto userDto = userService.createUser(userCreate);
        User user = findUserByUsername(userDto.getUsername());
        TokenResponseDto tokens = generateTokens(user);
        accountVerificationService.sendVerificationEmail(user);

        log.info("User registered successfully: {}", userDto.getUsername());
        return AuthResponseDto.builder()
                .accessToken(tokens.getAccessToken())
                .refreshToken(tokens.getRefreshToken())
                .user(userDto)
                .build();
    }

    @Override
    @Transactional
    public TokenResponseDto refreshAccessToken(String refreshToken) {
        log.info("Attempting to refresh access token");

        validateRefreshToken(refreshToken);
        String username = jwtService.extractUsername(refreshToken);
        UserDto userDto = userService.getUserByUsername(username);
        User user = mapper.toEntity(userDto);
        TokenResponseDto tokens = generateTokens(user);

        log.info("Access token refreshed successfully for user: {}", username);
        return tokens;
    }

    @Override
    @Transactional(readOnly = true)
    public User findUserByUsername(String username) {
        log.info("Fetching user by username: {}", username);
        UserDto userDto = userService.getUserByUsername(username);
        return mapper.toEntity(userDto);
    }

    private User authenticateUser(String username, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            return (User) authentication.getPrincipal();
        } catch (AuthenticationException e) {
            log.error("Authentication failed for user: {}", username, e);
            throw new InvalidCredentialsException("Invalid username or password");
        }
    }

    private TokenResponseDto generateTokens(User user) {
        String accessToken = jwtService.generateAccessToken(user.getUsername(), new HashMap<>(), user.getRole().name());
        String refreshToken =  jwtService.generateRefreshToken(user.getUsername());
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);
        return TokenResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        Token token = Token.builder()
                .token(jwtToken)
                .user(user)
                .expired(false)
                .revoked(false)
                .expiryDate(LocalDateTime.now().plusSeconds(accessTokenExpirationTimeMs / 1000))
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        List<Token> validUserTokens = tokenRepository.findAllByValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty()) {
            return;
        }
        validUserTokens.forEach(token -> {
            token.setRevoked(true);
            token.setExpired(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    private UserCreateRequest mapToUserCreateRequest(RegisterRequest request) {
        return UserCreateRequest.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .email(request.getEmail())
                .phone(request.getPhone())
                .build();
    }

    private void validateRefreshToken(String refreshToken) {
        try {
            String username = jwtService.extractUsername(refreshToken);
            if (!jwtService.validateToken(refreshToken, username)) {
                log.warn("Invalid or expected refresh token for user: {}", username);
                throw new InvalidCredentialsException("Invalid or expired refresh token");
            }
        } catch (Exception e) {
            log.error("Failed to validate refresh token", e);
            throw new InvalidCredentialsException("Invalid refresh token");
        }
    }
}
