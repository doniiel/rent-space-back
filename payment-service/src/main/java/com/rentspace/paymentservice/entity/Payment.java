package com.rentspace.paymentservice.entity;

import com.rentspace.core.enums.PaymentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;
import java.util.Currency;

import static jakarta.persistence.EnumType.STRING;

@Entity
@Table(name = "payments")
@Getter @Setter
@Builder @ToString
@AllArgsConstructor
@NoArgsConstructor
public class Payment extends BaseEntity implements Serializable {
    private Long userId;
    private Long bookingId;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private String currency;

    @Enumerated(STRING)
    private PaymentStatus status;
}
