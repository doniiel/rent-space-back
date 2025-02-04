package com.rentspace.userservice.service.impl;

import com.rentspace.userservice.dto.RegisterRequest;
import com.rentspace.userservice.dto.UserCreateRequest;
import com.rentspace.userservice.entity.User;
import com.rentspace.userservice.exception.UserNotFoundException;
import com.rentspace.userservice.mapper.UserMapper;
import com.rentspace.userservice.service.AuthService;
import com.rentspace.userservice.service.UserService;
import com.rentspace.userservice.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtUtil jwtTokenUtil;

    @Override
    public Map<String, String> login(String username, String password) {
        User user = authenticateUser(username, password);

        Map<String, String> tokens = new HashMap<>();
        String accessToken = jwtTokenUtil.generateAccessToken(user.getUsername(), new HashMap<>(), user.getRole().name());
        String refreshToken = jwtTokenUtil.generateRefreshToken(user.getUsername());

        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);

        return tokens;
    }

    @Override
    public Map<String, Object> register(RegisterRequest request) {
        Map<String, Object> result = new HashMap<>();

        UserCreateRequest userCreate = UserCreateRequest.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .email(request.getEmail())
                .phone(request.getPhone())
                .build();
        var user = userService.createUser(userCreate);

        String accessToken = jwtTokenUtil.generateAccessToken(user.getUsername(), new HashMap<>(), user.getRole());
        String refreshToken = jwtTokenUtil.generateRefreshToken(user.getUsername());

        result.put("access_token", accessToken);
        result.put("refresh_token", refreshToken);
        result.put("user", user);
        return result;
    }

    @Override
    public Map<String, String> refresh(String token) {
        String username = jwtTokenUtil.extractUsername(token);
        String role = jwtTokenUtil.extractRole(token);
        Map<String, String> tokens = new HashMap<>();

        if (username != null) {
            String newAccessToken = jwtTokenUtil.generateAccessToken(username, new HashMap<>(), role);
            String newRefreshToken = jwtTokenUtil.generateRefreshToken(username);

            tokens.put("access_token", newAccessToken);
            tokens.put("refresh_token", newRefreshToken);
        }
        return tokens;
    }

    private User authenticateUser(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        if (authentication.isAuthenticated()) {
            var user = (User) authentication.getPrincipal();
            return user;
        } else {
            throw new UserNotFoundException("Invalid credentials");
        }
    }
}
