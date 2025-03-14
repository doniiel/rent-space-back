package com.rentspace.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(name = "UserResponse", description = "Shema to hold user information")
public class UserDto {
    @NotNull(message = "User ID cannot be null")
    @Schema(description = "User ID", example = "1")
    private Long id;

    @NotNull(message = "Username cannot be null")
    @Schema(description = "User`s username", example = "John Doe")
    private String username;

    @NotNull(message = "Email cannot be null")
    @Schema(description = "User`s email", example = "5Xl7g@example.com")
    private String email;

    @NotNull(message = "Phone cannot be null")
    @Schema(description = "User`s phone number", example = "1234567890")
    private String phone;

    @Schema(description = "User`s role", example = "ADMIN", allowableValues = {"USER", "MANAGER", "ADMIN"})
    private String role;
}
