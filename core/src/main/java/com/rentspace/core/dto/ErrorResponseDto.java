package com.rentspace.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;


@Schema(
        name = "ErrorResponse",
        description = "Schema to hold error response information"
)
@Data
@AllArgsConstructor
public class ErrorResponseDto {

    @Schema(
            description = "API path invoked by the client",
            example = "/api/v1/users/1"
    )
    private String apiPath;
    @Schema(
            description = "HTTP status code representing the error",
            example = "404"
    )
    private int errorCode;
    @Schema(
            description = "Error message describing the issue",
            example = "User with ID 1 not found."
    )
    private String message;
    @Schema(
            description = "Timestamp when the error occurred",
            example = "2023-10-05T14:48:00"
    )
    private LocalDateTime errorTime;
}