package com.rentspace.userservice.util;

import org.springframework.beans.factory.annotation.Value;

public class VerificationTokenUtil {

    @Value("${app.confirm-account-url}")
    private String confirmAccountUrl;

    private static VerificationTokenUtil instance;

    private void init() {
        instance = this;
    }

    public static String generateConfirmAccountUrl(String token) {
        return instance.confirmAccountUrl + token;
    }
}
