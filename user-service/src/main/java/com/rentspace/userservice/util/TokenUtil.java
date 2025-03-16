package com.rentspace.userservice.util;

import org.springframework.stereotype.Component;

@Component
public class TokenUtil {
    public static final String HEADER = "Authorization";
    public static final String PREFIX = "Bearer ";
    public static final int PREFIX_LENGTH = PREFIX.length();

    public String extractTokenFromHeader(String authHeader) {
        if (authHeader != null && authHeader.startsWith(PREFIX)) {
            return authHeader.substring(PREFIX_LENGTH);
        }
        return null;
    }
}
