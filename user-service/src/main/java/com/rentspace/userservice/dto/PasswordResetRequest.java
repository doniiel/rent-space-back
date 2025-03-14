package com.rentspace.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(name = "PasswordResetRequest", description = "Request to reset a user`s password")
public class PasswordResetRequest {
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    @Schema(description = "User`s email address", example = "daniyal5@gmail.com")
    private String email;

    @NotBlank(message = "Reset code cannot be blank")
    @Size(min = 6, max = 6, message = "Reset code must be exactly 6 characters")
    @Pattern(regexp = "^[0-9]{6}$", message = "Reset code must contain only digits")
    @Schema(description = "6-digit reset code", example = "123456")
    private String resetCode;

    @NotBlank(message = "New password cannot be blank")
    @Size(min = 8, max = 20, message = "New password must be between 8 and 20 characters")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}$", message = "Password must contain at least one letter and one number")
    @Schema(description = "User`s new password", example = "password123")
    private String newPassword;
}
