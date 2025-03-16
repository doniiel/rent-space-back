package com.rentspace.userservice.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class VerificationTokenUtil {

    @Value("${verification.base-url}")
    private String verificationBaseUrl;

    public String generateVerificationUrl(String token) {
        return verificationBaseUrl + "/verify?token=" + token;
    }
}
