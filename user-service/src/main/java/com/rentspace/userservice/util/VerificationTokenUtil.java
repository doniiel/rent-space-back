package com.rentspace.userservice.util;

import org.springframework.beans.factory.annotation.Value;

import java.security.SecureRandom;
import java.time.LocalDateTime;

public class VerificationTokenUtil {
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int EXPIRY_TIME_HOURS = 24;
    private static String confirmAccountUrl;

    @Value("${app.confirm-account-url}")
    public void setConfirmAccountUrl(String url) {
        confirmAccountUrl = url;
    }
    public static String generateVerificationToken() {
        int code = 100000 + RANDOM.nextInt(900000);
        return String.valueOf(code);
    }

    public static LocalDateTime getExpiryDate() {
        return LocalDateTime.now().plusHours(EXPIRY_TIME_HOURS);
    }

    public static boolean isTokenExpired(LocalDateTime expiryDate) {
        return expiryDate.isBefore(LocalDateTime.now());
    }

    public static String generateConfirmAccountUrl(String token) {
        return confirmAccountUrl + token;
    }
}
