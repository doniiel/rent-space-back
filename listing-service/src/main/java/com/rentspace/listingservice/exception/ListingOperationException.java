package com.rentspace.listingservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ListingOperationException extends RuntimeException {
    public ListingOperationException(String message) {
        super(message);
    }
}
