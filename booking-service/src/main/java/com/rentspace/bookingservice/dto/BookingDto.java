package com.rentspace.bookingservice.dto;

import com.rentspace.bookingservice.enums.BookingStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@Schema(description = "Data Transfer Object representing a booking")
public class BookingDto {

    @Schema(description = "Unique identifier of the booking", example = "1")
    private Long id;

    @Schema(description = "Identifier of the listing being booked", example = "10")
    private Long listingId;

    @Schema(description = "Identifier of the user who made the booking", example = "5")
    private Long userId;

    @Schema(description = "Start date and time of the booking", example = "2025-03-20T10:00:00")
    private LocalDateTime startDate;

    @Schema(description = "End date and time of the booking", example = "2025-03-20T12:00:00")
    private LocalDateTime endDate;

    @Schema(description = "Total price of the booking", example = "150.50")
    private Double totalPrice;

    @Schema(description = "Current status of the booking", example = "PENDING")
    private BookingStatus status;

    @Schema(description = "Timestamp when the booking was created", example = "2025-03-19T09:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the booking was last updated", example = "2025-03-19T09:05:00")
    private LocalDateTime updatedAt;
}