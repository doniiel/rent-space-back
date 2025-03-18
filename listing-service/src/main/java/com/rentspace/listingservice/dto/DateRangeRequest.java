package com.rentspace.listingservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "Request object for specifying a date range")
public class DateRangeRequest {

    @NotNull(message = "Start date cannot be null")
    @Schema(description = "Start date and time of range", example = "2025-04-01T10:00:00")
    private LocalDateTime startDate;

    @NotNull(message = "End date cannot be null")
    @Schema(description = "End date and time of the range", example = "2025-04-05T12:00:00")
    private LocalDateTime endDate;

    @AssertTrue(message = "Start date must be before end date.")
    @Schema(hidden = true)
    public boolean isDateRangeValid() {
        return startDate != null && endDate != null && startDate.isBefore(endDate);
    }
}
