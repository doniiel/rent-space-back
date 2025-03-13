package com.rentspace.paymentservice.dto;

import com.rentspace.core.enums.PaymentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentStatusUpdateRequest {
    @NotNull(message = "Payment status is required")
    private PaymentStatus status;
}
