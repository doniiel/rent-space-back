package com.rentspace.bookingservice.dto;

import jakarta.validation.constraints.Future;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDateTime;

@Data
public class BookingRequestDto {

    @NonNull
    private Long userId;
    @NonNull
    private Long listingId;
    @NonNull
    @Future
    private LocalDateTime startDate;
    @NonNull
    @Future
    private LocalDateTime endDate;
}
