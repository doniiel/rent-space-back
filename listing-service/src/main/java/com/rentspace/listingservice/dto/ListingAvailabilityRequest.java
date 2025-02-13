package com.rentspace.listingservice.dto;

import lombok.Data;

import java.time.LocalDate;
@Data
public class ListingAvailabilityRequest {
    private Long listingId;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean available;
}
