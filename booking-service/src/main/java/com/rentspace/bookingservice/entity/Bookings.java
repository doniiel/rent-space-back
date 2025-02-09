package com.rentspace.bookingservice.entity;

import com.rentspace.bookingservice.util.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.GenerationType.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bookings")
public class Bookings {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Long listingId;
    private Long userId;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Double totalPrice;
    @Enumerated(STRING)
    private Status status;
}
