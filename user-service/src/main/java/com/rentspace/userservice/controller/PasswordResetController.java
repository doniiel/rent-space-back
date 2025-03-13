package com.rentspace.userservice.controller;

import com.rentspace.userservice.dto.ForgotPasswordRequest;
import com.rentspace.userservice.dto.PasswordResetRequest;
import com.rentspace.userservice.service.PasswordResetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.OK;

@Tag(name = "Password Reset REST API in RentSpace", description = "API for password reset operations")
@RestController
@RequestMapping("/api/v1/auth/password")
@RequiredArgsConstructor
public class PasswordResetController {
    private final PasswordResetService passwordResetService;

    @Operation(summary = "Request password reset", description = "Send a reset code to the user's email")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Reset code sent successfully"),
            @ApiResponse(responseCode = "404", description = "Email not found")
    })
    @PostMapping("/forgot")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        passwordResetService.sendResetCode(request.getEmail());
        return ResponseEntity
                .status(ACCEPTED)
                .body("Reset code sent to email.");
    }

    @Operation(summary = "Reset password", description = "Reset the user's password using a reset code")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Password reset successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid reset code or data"),
            @ApiResponse(responseCode = "404", description = "Email not found")
    })
    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordResetRequest request) {
        passwordResetService.resetPassword(request.getEmail(), request.getResetCode(), request.getNewPassword());
        return ResponseEntity
                .status(OK)
                .body("Password successfully reset.");
    }
}
