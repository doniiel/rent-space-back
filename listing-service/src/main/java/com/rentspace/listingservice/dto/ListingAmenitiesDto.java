package com.rentspace.listingservice.dto;

import com.rentspace.listingservice.enums.AmenityType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Schema(description = "Response object representing amenities associated with a listing")
public class ListingAmenitiesDto {

    @Schema(description = "Unique identifier of the listing amenities", example = "1")
    private Long id;

    @Schema(description = "Identifier of the associated listing", example = "1")
    private Long listingId;

    @Schema(description = "Set of amenity types", example = "[\"WIFI\", \"PARKING\"]")
    private Set<AmenityType> amenityTypes;

    @Schema(description = "Timestamp when the amenities were created", example = "2025-03-17T10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the amenities were last updated", example = "2025-03-17T12:00:00")
    private LocalDateTime updatedAt;
}
