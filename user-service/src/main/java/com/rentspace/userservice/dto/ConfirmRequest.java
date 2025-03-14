package com.rentspace.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(name = "ConfirmRequest", description = "Request to confirm an action with a verification code")
public class ConfirmRequest {
    @NotNull(message = "Confirm code cannot be null")
    @Size(min = 6, max = 6, message = "Confirm code must be exactly 6 characters")
    @Pattern(regexp = "^[0-9]{6}$", message = "Confirm code must contain only digits")
    @Schema(description = "6-digit confirmation code", example = "123456")
    private String confirmCode;
}
