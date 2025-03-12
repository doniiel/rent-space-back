package com.rentspace.paymentservice.dto;

import lombok.Data;

@Data
public class PaymentCreateRequest {
    private Long userId;
    private Long bookingId;
    private String currency;
    private double amount;
}
