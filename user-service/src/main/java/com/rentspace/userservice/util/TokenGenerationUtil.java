package com.rentspace.userservice.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Component
public class TokenGenerationUtil {
    private static final SecureRandom RANDOM = new SecureRandom();

    @Value("${token.password-reset.expiry-minutes}")
    private int passwordResetExpiryMinutes;

    @Value("${token.verification.expiry-hours}")
    private int verificationExpiryHours;

    public String generateCode() {
        int code = 100000 + RANDOM.nextInt(900000);
        return String.valueOf(code);
    }

    public LocalDateTime getPasswordResetExpiryDate() {
        return LocalDateTime.now().plusMinutes(passwordResetExpiryMinutes);
    }

    public LocalDateTime getVerificationExpiryDate() {
        return LocalDateTime.now().plusHours(verificationExpiryHours);
    }
}
