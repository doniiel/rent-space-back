package com.rentspace.listingservice.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ListingAvailabilityRequest {
    private Long listingId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean available;
}
