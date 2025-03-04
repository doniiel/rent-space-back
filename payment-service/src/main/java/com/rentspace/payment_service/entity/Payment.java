package com.rentspace.payment_service.entity;

import com.rentspace.payment_service.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "payments")
@Getter @Setter
@Builder @ToString
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Long userId;
    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(STRING)
    @Column(nullable = false)
    private Currency currency;

    @Enumerated(STRING)
    private PaymentStatus status;

    @CreationTimestamp
    @Column(updatable = false, insertable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updatedDate;
}
