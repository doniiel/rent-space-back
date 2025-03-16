package com.rentspace.userservice.controller;

import com.rentspace.userservice.dto.*;
import com.rentspace.userservice.service.AccountVerificationService;
import com.rentspace.userservice.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Tag(name = "Authentication REST API in RentSpace", description = "API for user authentication and registration")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final AccountVerificationService verificationService;

    @Operation(summary = "User login", description = "Authenticate a user and return access and refresh tokens")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User authenticated successfully"),
            @ApiResponse(responseCode = "401", description = "Invalid username or password")
    })
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity
                .status(OK)
                .body(authService.authenticateAndGenerateTokens(request.getUsername(), request.getPassword()));
    }

    @Operation(summary = "User registration", description = "Register a new user and send verification email")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid registration data"),
            @ApiResponse(responseCode = "409", description = "Username or email already exists")
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity
                .status(CREATED)
                .body(authService.registerUserAndGenerateTokens(request));
    }

    @Operation(summary = "Refresh access token", description = "Generate a new access token using a refresh token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Access token refreshed successfully"),
            @ApiResponse(responseCode = "401", description = "Invalid or expired refresh token")
    })
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDto> refreshToken(@RequestHeader("Refresh-Token") String refreshToken) {
        return ResponseEntity
                .status(OK)
                .body(authService.refreshAccessToken(refreshToken));
    }

    @Operation(summary = "Confirm user account", description = "Confirm a user account using a verification token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account confirmed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid or expired token")
    })
    @PostMapping("/confirm")
    public ResponseEntity<String> confirmAccount(@RequestBody ConfirmRequest request) {
        verificationService.confirmAccount(request.getConfirmCode());
        return ResponseEntity
                .status(OK)
                .body("Account successfully confirmed!");
    }
}

