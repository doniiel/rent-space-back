package com.rentspace.listingservice.exception;

public class InvalidDateRangeException extends RuntimeException {
    public InvalidDateRangeException(String startDate, String endDate) {
        super(String.format("Invalid date range: %s to %s", startDate, endDate));
    }
}
