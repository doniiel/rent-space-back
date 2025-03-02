package com.rentspace.bookingservice.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateBookingRequest {
    @NotNull(message = "Listing ID cannot be null")
    private Long listingId;

    @NotNull(message = "User ID cannot be null")
    private Long userId;

    @NotNull(message = "Start date cannot be null")
    @Future(message = "Start date must be in the future")
    private LocalDateTime startDate;

    @NotNull(message = "End date cannot be null")
    @Future(message = "End date must be in the future")
    private LocalDateTime endDate;

    @Positive(message = "Total price must be greater than zero")
    private Double totalPrice;
}
