package com.rentspace.bookingservice.exception;

public class PaymentTransactionNotFound extends RuntimeException {
  public PaymentTransactionNotFound(String message, String field, Long id) {
    super(message + " " + field + " " + id);
  }
  public PaymentTransactionNotFound(String message) {
    super(message);
  }
}
