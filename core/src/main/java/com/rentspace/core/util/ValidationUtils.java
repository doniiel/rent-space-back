package com.rentspace.core.util;

import java.time.LocalDateTime;
import java.util.Currency;

public final class ValidationUtils {
    private ValidationUtils() {
        // Приватный конструктор для предотвращения создания экземпляров
    }

    public static void validateDateRange(LocalDateTime startDate, LocalDateTime endDate, String errorMessage) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Dates must not be null");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public static void validateDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        validateDateRange(startDate, endDate, "Start date must be before end date");
    }

    public static void validateNotNull(Object value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " must not be null");
        }
    }

    public static void validatePositive(double value, String fieldName) {
        if (value <= 0) {
            throw new IllegalArgumentException(fieldName + " must be positive");
        }
    }

    public static void validateCurrency(String currency, String fieldName) {
        validateNotNull(currency, fieldName);
        try {
            Currency.getInstance(currency);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(fieldName + " must be a valid ISO 4217 currency code (e.g., 'USD')");
        }
    }
}