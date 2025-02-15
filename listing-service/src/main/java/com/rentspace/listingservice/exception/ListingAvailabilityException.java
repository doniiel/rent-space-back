package com.rentspace.listingservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ListingAvailabilityException extends RuntimeException {
    public ListingAvailabilityException(String message) {
        super(message);
    }
}
