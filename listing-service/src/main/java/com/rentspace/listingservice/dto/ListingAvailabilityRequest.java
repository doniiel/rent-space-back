package com.rentspace.listingservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Request object for setting listing availability")
public class ListingAvailabilityRequest {

    @NotNull(message = "Listing ID cannot be null.")
    @Schema(description = "Identifier of the listing to set availability for", example = "1")
    private Long listingId;

    @NotNull(message = "Start date cannot be null.")
    @Schema(description = "Start date and time of the availability period", example = "2025-04-01T10:00:00")
    private LocalDateTime startDate;

    @NotNull(message = "End date cannot be null.")
    @Schema(description = "End date and time of the availability period", example = "2025-04-05T12:00:00")
    private LocalDateTime endDate;

    @Schema(description = "Indicates whether the listing is available during this period", example = "true")
    private boolean available;

    @AssertTrue(message = "Start date must be before end date.")
    @Schema(hidden = true)
    public boolean isDateRangeValid() {
        return startDate != null && endDate != null && startDate.isBefore(endDate);
    }
}
