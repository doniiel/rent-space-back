package com.rentspace.userservice.controller;

import com.rentspace.userservice.dto.UserDto;
import com.rentspace.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Tag(name = "Users REST API in RentSpace",
        description = "REST APIs in RentSpace to CREATE, READ, UPDATE and DELETE users")
@RestController
@RequestMapping("/api/v1/users")
@Validated
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(
            summary ="Get user by email",
            description = "Get User by Email inside RentSpace"
    )
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable @Email @NotBlank String email) {
        return ResponseEntity
                .status(OK)
                .body(userService.getUserByEmail(email));
    }

    @Operation(
            summary ="Get user by ID",
            description = "Get User by ID inside RentSpace"
    )
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long userId) {
        return ResponseEntity
                .status(OK)
                .body(userService.getUserById(userId));
    }

    @Operation(
            summary ="Create user",
            description = "Create User inside RentSpace"
    )
    @PostMapping
    public ResponseEntity<String> createUser(@Valid @RequestBody UserDto userDto) {
        return ResponseEntity
                .status(CREATED)
                .body(userService.createUser(userDto));
    }

    @Operation(
            summary ="Update user",
            description = "Update User inside RentSpace"
    )
    @PutMapping()
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto) {
        return ResponseEntity
                .status(OK)
                .body(userService.updateUser(userDto));
    }

    @Operation(
            summary ="Delete user",
            description = "Delete User inside RentSpace"
    )
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        return ResponseEntity
                .status(OK)
                .body(userService.deleteUser(userId));
    }
}
