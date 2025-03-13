package com.rentspace.userservice.controller;

import com.rentspace.userservice.dto.UpdateUserRequest;
import com.rentspace.userservice.dto.UserCreateRequest;
import com.rentspace.userservice.dto.UserDto;
import com.rentspace.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@Tag(name = "Users REST API in RentSpace", description = "APIs to manage users")
@RestController
@RequestMapping("/api/v1/users")
@Validated
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "Get user by email", description = "Retrieve user by email")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    @GetMapping
    public ResponseEntity<UserDto> getUserByEmail(@RequestParam @Email @NotBlank String email) {
        return ResponseEntity
                .status(OK)
                .body(userService.getUserByEmail(email));
    }

    @Operation(summary = "Get user by ID", description = "Retrieve user by ID")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long userId) {
        return ResponseEntity
                .status(OK)
                .body(userService.getUserById(userId));
    }

    @Operation(summary = "Create user", description = "Create a new user")
    @PreAuthorize("permitAll()")
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserCreateRequest request) {
        return ResponseEntity
                .status(CREATED)
                .body(userService.createUser(request));
    }

    @Operation(summary = "Update user", description = "Partially update user details")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN') or #userId == authentication.principal.id")
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long userId,
                                              @Valid @RequestBody UpdateUserRequest request) {
        return ResponseEntity
                .status(OK)
                .body(userService.updateUser(userId, request));
    }

    @Operation(summary = "Delete user", description = "Delete a user")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity
                .status(NO_CONTENT)
                .build();
    }
}
