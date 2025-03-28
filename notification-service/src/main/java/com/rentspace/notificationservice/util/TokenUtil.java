package com.rentspace.notificationservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TokenUtil {

    public static final String HEADER = "Authorization";
    public static final String PREFIX = "Bearer ";

    public static String extractToken(String header) {
        if (header == null || !header.startsWith(PREFIX)) {
            return null;
        }
        return header.substring(PREFIX.length());
    }

    public static boolean isValidBearerHeader(String header) {
        return header != null && header.startsWith(PREFIX);
    }
}