package com.rentspace.notificationservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "DTO for sending an email notification")
public class EmailRequest {

    @NotBlank(message = "Recipient email is cannot be empty")
    @Email(message = "Email must be valid")
    @Size(max = 255, message = "Email cannot exceed 255 characters")
    @Schema(description = "Recipient`s email address", example = "6Nk0r@example.com")
    private String to;

    @NotBlank(message = "Subject cannot be empty")
    @Size(max = 255, message = "Subject cannot exceed 255 characters")
    @Schema(description = "Email subject", example = "Account verification")
    private String subject;

    @NotBlank(message = "Text cannot be empty")
    @Size(max = 5000, message = "Text cannot exceed 5000 characters")
    @Schema(description = "Email body text", example = "Please verify your account by clicking the link below.")
    private String text;
}
