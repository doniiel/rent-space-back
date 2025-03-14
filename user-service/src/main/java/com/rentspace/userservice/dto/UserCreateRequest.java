package com.rentspace.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(name = "UserCreateRequest", description = "Request to create a new user")
public class UserCreateRequest {
    @NotBlank(message = "Username cannot be empty")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    @Schema(description = "User`s username", example = "JohnDoe")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}$", message = "Password must contain at least one letter and one number")
    @Schema(description = "User`s password", example = "password123")
    private String password;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    @Schema(description = "User`s email address", example = "daniyal5@gmail.com")
    private String email;

    @NotBlank(message = "Phone cannot be empty")
    @Pattern(regexp = "(^$|[0-9]{11})", message = "Account number must be 10 digits")
    @Schema(description = "User`s phone number", example = "12345678910")
    private String phone;
}
