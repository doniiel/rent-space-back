package com.rentspace.core.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentCreateEvent {
    private Long paymentTransactionId;
    private Long bookingId;
}
