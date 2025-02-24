package com.rentspace.userservice.util;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;

public class PasswordResetTokenUtil {
    private static final int EXPIRY_TIME_MINUTES = 10;
    private static final SecureRandom RANDOM = new SecureRandom();


    public static String generateResetCode() {
        int code = 100000 + RANDOM.nextInt(900000);
        return String.valueOf(code);
    }

    public static LocalDateTime getExpiryDate() {
        return LocalDateTime.now().plusMinutes(EXPIRY_TIME_MINUTES);
    }

    public static boolean isTokenExpired(LocalDateTime expiryDate) {
        return expiryDate.isBefore(LocalDateTime.now());
    }
}
