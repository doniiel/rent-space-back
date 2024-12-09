package com.rentspace.userservice.dto;

import com.rentspace.userservice.util.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDto {

    private Long id;

    @NotBlank(message = "Name cannot be empty.")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @NotBlank(message = "Email cannot be empty.")
    @Email(message = "Email should be valid.")
    private String email;

    @NotBlank(message = "Password cannot be empty.")
    @Size(min = 8, max = 50, message = "Password must be between 8 and 50 characters")
    private String password;

    @NotNull(message = "Role cannot be null.")
    private Role role;
}
