package com.rentspace.bookingservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PaymentTransactionNotFound extends RuntimeException {
  public PaymentTransactionNotFound(String entity, String field, Long id) {
    super(String.format("%s with %s = %d not found", entity, field, id));
  }
  public PaymentTransactionNotFound(String message) {
    super(message);
  }
}
