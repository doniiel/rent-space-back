package com.rentspace.listingservice.dto;

import com.rentspace.listingservice.enums.AmenityType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
public class ListingAmenitiesDto {
    private Long id;
    private Long listingId;
    private Set<AmenityType> amenityTypes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
