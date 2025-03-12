package com.rentspace.paymentservice.exception;


import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(NOT_FOUND)
public class PaymentNotFoundException extends RuntimeException {
    public PaymentNotFoundException(String message, String field, Object value) {
        super(String.format("%s not found with %s : '%s'", message, field, value));
    }
    public PaymentNotFoundException(String message) {
        super(message);
    }
}
