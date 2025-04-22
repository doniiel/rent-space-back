package com.rentspace.core.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentCreatedEvent {
    private Long bookingId;
    private Long userId;
    private Long listingId;
    private Double totalPrice;
    private String currency;
}
