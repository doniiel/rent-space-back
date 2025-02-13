package com.rentspace.listingservice.dto;

import com.rentspace.listingservice.enums.AmenityType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ListingAmenitiesDto {
    private Long id;
    private Long listingId;
    private AmenityType amenityType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
