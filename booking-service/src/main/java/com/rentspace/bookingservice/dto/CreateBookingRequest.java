package com.rentspace.bookingservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Request object for creating a new booking")
public class CreateBookingRequest {

    @NotNull(message = "Listing ID cannot be null")
    @Schema(description = "Identifier of the listing to book", example = "10")
    private Long listingId;

    @NotNull(message = "User ID cannot be null")
    @Schema(description = "Identifier of the user making the booking", example = "5")
    private Long userId;

    @NotNull(message = "Start date cannot be null")
    @Future(message = "Start date must be in the future")
    @Schema(description = "Start date and time of the booking", example = "2025-03-20T10:00:00")
    private LocalDateTime startDate;

    @NotNull(message = "End date cannot be null")
    @Future(message = "End date must be in the future")
    @Schema(description = "End date and time of the booking", example = "2025-03-20T12:00:00")
    private LocalDateTime endDate;

    @Positive(message = "Total price must be greater than zero")
    @Schema(description = "Total price of the booking", example = "150.50")
    private Double totalPrice;
}