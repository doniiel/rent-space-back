package com.rentspace.listingservice.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ListingAvailabilityDto {
    private Long id;
    private Long listingId;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean available;
}