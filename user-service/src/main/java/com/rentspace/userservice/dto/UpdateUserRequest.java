package com.rentspace.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(name = "UpdateUserRequest", description = "Request to update user information")
public class UpdateUserRequest {
    @Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
    private String username;

    @Size(min = 2, max = 70, message = "Firstname must be between 2 and 70 characters")
    private String firstname;

    @Size(min = 2, max = 70, message = "Lastname must be between 2 and 70 characters")
    private String lastname;

    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}$", message = "Password must contain at least one letter and one number")
    private String password;

    @Email(message = "Email should be valid")
    private String email;

    @Pattern(regexp = "[0-9]{11}", message = "Phone number must be 11 digits")
    private String phone;

    private String role;
}