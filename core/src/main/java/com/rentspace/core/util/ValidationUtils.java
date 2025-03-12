package com.rentspace.core.util;

import java.time.LocalDateTime;

public final class ValidationUtils {
    private ValidationUtils() {}

    public static void validateDateRange(LocalDateTime startDate, LocalDateTime endDate, String errorMessage) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Dates must be not null");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public static void validateDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        validateDateRange(startDate, endDate, "Start date must be before end date");
    }
}
