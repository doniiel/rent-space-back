package com.rentspace.listingservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Response object representing the availability period of a listing")
public class ListingAvailabilityDto {

    @Schema(description = "Unique identifier of the availability record", example = "1")
    private Long id;

    @Schema(description = "Identifier of the associated listing", example = "1")
    private Long listingId;

    @Schema(description = "Start date and time of the availability period", example = "2025-04-01T10:00:00")
    private LocalDateTime startDate;

    @Schema(description = "End date and time of the availability period", example = "2025-04-05T12:00:00")
    private LocalDateTime endDate;

    @Schema(description = "Indicates whether the listing is available", example = "true")
    private boolean available;
}