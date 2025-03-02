package com.rentspace.bookingservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class BookingAlreadyExistsException extends RuntimeException {
    public BookingAlreadyExistsException(String entity, String field, Long id) {
        super(String.format("%s with %s = %d not found", entity, field, id));
    }
    public BookingAlreadyExistsException(String message) {
        super(message);
    }
}
