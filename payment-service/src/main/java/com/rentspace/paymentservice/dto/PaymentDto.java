package com.rentspace.paymentservice.dto;

import com.rentspace.core.enums.PaymentStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Currency;

@Data
@Builder
public class PaymentDto {
    private Long id;
    private Long userId;
    private Long bookingId;
    private Double amount;
    private String currency;
    private PaymentStatus paymentStatus;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
