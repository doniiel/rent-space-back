package com.rentspace.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserRequest {

    @Schema(description = "User name", example = "John Doe")
    @NotBlank(message = "Name cannot be empty.")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String username;

    @Schema(description = "User first name", example = "John")
    @Size(min = 2, max = 70, message = "Name must be between 2 and 70 characters")
    private String firstname;

    @Schema(description = "User last name", example = "Doe")
    @Size(min = 2, max = 70, message = "Name must be between 2 and 70 characters")
    private String lastname;

    @Schema(description = "User password", example = "ldfn5454,.")
    @NotBlank(message = "Password cannot be empty.")
    @Size(min = 8, max = 50, message = "Password must be between 8 and 50 characters")
    private String password;

    @Schema (description = "User email", example = "5Xl7g@example.com")
    @NotBlank(message = "Email cannot be empty.")
    @Email(message = "Email should be valid.")
    private String email;

    @Schema(description = "User mobile number", example = "12345678910")
    @NotBlank(message = "User number cannot be null or empty")
    @Pattern(regexp = "(^$|[0-9]{11})", message = "Account number must be 10 digits")
    private String phone;

    @Schema(description = "User role", example = "ADMIN")
    private String role;
}
