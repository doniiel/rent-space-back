package com.rentspace.bookingservice.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentTransactionDto {
    private Long id;
    private Long userId;
    private Long bookingId;
    private BigDecimal amount;
    private PaymentStatus status;
}
