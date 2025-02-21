package com.rentspace.userservice.util;

import java.time.LocalDateTime;
import java.util.UUID;

public class PasswordResetTokenUtil {
    private static final int EXPIRY_TIME_MINUTES = 10;

    public static String generateResetCode() {
        return UUID.randomUUID().toString();
    }

    public static LocalDateTime getExpiryDate() {
        return LocalDateTime.now().plusMinutes(EXPIRY_TIME_MINUTES);
    }

    public static boolean isTokenExpired(LocalDateTime expiryDate) {
        return expiryDate.isBefore(LocalDateTime.now());
    }
}
