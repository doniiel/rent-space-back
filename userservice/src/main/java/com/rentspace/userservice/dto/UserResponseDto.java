package com.rentspace.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(
    name = "UserResponse",
    description = "Shema to hold user information"
)
public class UserResponseDto {
    @Schema(
        description = "User ID", example = "1"
    )
    private Long id;
    @Schema(
        description = "User name", example = "John Doe"
    )
    private String name;
    @Schema(
        description = "User email", example = "5Xl7g@example.com"
    )
    private String email;
    @Schema(
        description = "User mobile number", example = "1234567890"
    )
    private String mobileNumber;
    @Schema(
        description = "User role", example = "ADMIN"
    )
    private String role;
}
