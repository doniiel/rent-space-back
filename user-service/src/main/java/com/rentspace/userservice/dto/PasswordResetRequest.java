package com.rentspace.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PasswordResetRequest {
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;
    @NotBlank(message = "Reset code cannot be blank")
    private String resetCode;
    @NotBlank(message = "New password cannot be blank")
    private String newPassword;
}
