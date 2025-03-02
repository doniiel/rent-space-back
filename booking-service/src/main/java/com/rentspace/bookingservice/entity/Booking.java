package com.rentspace.bookingservice.entity;

import com.rentspace.bookingservice.enums.BookingStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.STRING;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bookings")
public class Booking extends BaseEntity implements Serializable {
    private Long listingId;
    private Long userId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @Positive
    private Double totalPrice;

    @Enumerated(STRING)
    private BookingStatus status;
}
