package com.rentspace.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(name = "UpdateUserRequest", description = "Request to update user information")
public class UpdateUserRequest {
    @NotBlank(message = "Username cannot be empty.")
    @Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
    @Schema(description = "User`s username", example = "John Doe")
    private String username;

    @NotBlank(message = "Firstname cannot be empty.")
    @Size(min = 2, max = 70, message = "Firstname must be between 2 and 70 characters")
    @Schema(description = "User`s first name", example = "John")
    private String firstname;

    @NotBlank(message = "Lastname cannot be empty.")
    @Size(min = 2, max = 70, message = "Lastname must be between 2 and 70 characters")
    @Schema(description = "User`s last name", example = "Doe")
    private String lastname;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}$", message = "Password must contain at least one letter and one number")
    @Schema(description = "User`s password", example = "password123")
    private String password;

    @NotBlank(message = "Email cannot be empty.")
    @Email(message = "Email should be valid.")
    @Schema (description = "User`s email address", example = "5Xl7g@example.com")
    private String email;

    @NotBlank(message = "User number cannot be null or empty")
    @Pattern(regexp = "(^$|[0-9]{11})", message = "Account number must be 10 digits")
    @Schema(description = "User`s phone number", example = "12345678910")
    private String phone;

    @Schema(description = "User role", example = "ADMIN", allowableValues = {"USER", "MANAGER", "ADMIN"})
    private String role;
}
