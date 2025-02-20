package com.rentspace.userservice.controller;

import com.rentspace.userservice.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/v1/auth/password")
@RequiredArgsConstructor
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    @PostMapping("/forgot")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        passwordResetService.sendResetCode(email);
        return ResponseEntity.status(OK).body("Reset code sent to email.");
    }

    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(@RequestParam String email,
                                                @RequestParam String resetCode,
                                                @RequestParam String newPassword) {
        passwordResetService.resetPassword(email, resetCode, newPassword);
        return ResponseEntity.status(OK).body("Password successfully reset.");
    }
}
