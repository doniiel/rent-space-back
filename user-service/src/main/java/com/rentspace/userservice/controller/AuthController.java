package com.rentspace.userservice.controller;

import com.rentspace.userservice.dto.UserCreateDto;
import com.rentspace.userservice.dto.UserLoginDto;
import com.rentspace.userservice.dto.UserResponseDto;
import com.rentspace.userservice.service.AuthService;
import com.rentspace.userservice.service.UserService;
import com.rentspace.userservice.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginDto userLoginDto) {
        String token = authService.authenticateUser(userLoginDto);
        return ResponseEntity.status(OK).body(token);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@RequestBody UserCreateDto userCreateDto) {
        return ResponseEntity.status(CREATED).body(userService.createUser(userCreateDto));
    }
}

