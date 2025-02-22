package com.rentspace.userservice.controller;

import com.rentspace.userservice.dto.LoginRequest;
import com.rentspace.userservice.dto.RegisterRequest;
import com.rentspace.userservice.service.AccountVerificationService;
import com.rentspace.userservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AccountVerificationService verificationService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authService.authenticateAndGenerateTokens(request.getUsername(), request.getPassword()));
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody RegisterRequest request) {
        Map<String, Object> response = authService.registerUserAndGenerateTokens(request);
        verificationService.sendVerificationEmail(authService.findUserByUsername(request.getUsername()));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }


    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refreshToken(@RequestHeader("Refresh-Token") String refreshToken) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(authService.refreshAccessToken(refreshToken));
    }

    @GetMapping("/confirm")
    public ResponseEntity<String> confirmAccount(@RequestParam String token) {
        verificationService.confirmAccount(token);
        return ResponseEntity.ok("Account successfully confirmed!");
    }
}

