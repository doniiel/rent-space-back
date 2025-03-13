package com.rentspace.paymentservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class PaymentCreateRequest {
    @NotNull(message = "User ID cannot be null")
    private Long userId;
    @NotNull(message = "Booking ID cannot be null")
    private Long bookingId;
    @NotNull(message = "Currency cannot be null")
    private String currency;
    @Positive(message = "Amount must be positive")
    private double amount;
}
