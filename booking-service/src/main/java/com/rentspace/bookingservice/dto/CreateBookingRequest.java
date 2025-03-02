package com.rentspace.bookingservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDateTime;

@Data
public class CreateBookingRequest {
    @NotNull
    private Long listingId;
    @NotNull
    private Long userId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
