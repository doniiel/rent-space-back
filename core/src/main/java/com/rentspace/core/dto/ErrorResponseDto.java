package com.rentspace.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Schema(name = "ErrorResponse", description = "Schema to hold error response information")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseDto {
    @NotNull(message = "API path cannot be null")
    @Schema(description = "API path invoked by the client", example = "/api/v1/users/1")
    private String apiPath;

    @Schema(description = "HTTP status code representing the error", example = "404", allowableValues = {"400", "401", "403", "404", "500"})
    private int errorCode;

    @NotNull(message = "Error message cannot be null")
    @Schema(description = "Error message describing the issue", example = "User with ID 1 not found.")
    private String message;

    @NotNull(message = "Error time cannot be null")
    @Schema(description = "Timestamp when the error occurred", example = "2023-10-05T14:48:00", format = "date-time")
    private LocalDateTime errorTime;
}