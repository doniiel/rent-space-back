package com.rentspace.core.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(NOT_FOUND)
public class PaymentNotFoundException extends RuntimeException {
    public PaymentNotFoundException(String entity, String field, Object value) {
      super(format("%s with %s '%s' not found.", entity, field, value));
    }
    public PaymentNotFoundException(String message) {
        super(message);
    }
}
