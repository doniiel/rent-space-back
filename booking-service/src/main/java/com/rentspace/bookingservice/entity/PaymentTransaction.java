package com.rentspace.bookingservice.entity;

import com.rentspace.core.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

import static jakarta.persistence.EnumType.STRING;

@Entity
@Table(name = "payment_transactions")
@Getter @Setter
@Builder @ToString
@AllArgsConstructor
@NoArgsConstructor
public class PaymentTransaction extends BaseEntity implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    private Long userId;
    private Double amount;

    @Enumerated(STRING)
    private PaymentStatus status;
}
