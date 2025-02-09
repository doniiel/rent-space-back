package com.rentspace.bookingservice.entity;

import com.rentspace.bookingservice.util.Status;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment_transactions")
@Getter @Setter
@Builder @ToString
@AllArgsConstructor
@NoArgsConstructor
public class PaymentTransactions implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long bookingId;
    private Long userId;
    private BigDecimal amount;
    private Status status;
    private LocalDateTime createdDate;
}
