package com.rentspace.userservice.service.impl;

import com.rentspace.userservice.dto.UserLoginDto;
import com.rentspace.userservice.entity.User;
import com.rentspace.userservice.exception.UserNotFoundException;
import com.rentspace.userservice.service.AuthService;
import com.rentspace.userservice.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    public String authenticateUser(UserLoginDto userLoginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginDto.getUsername(), userLoginDto.getPassword())
        );

        if (authentication.isAuthenticated()) {
            User user = (User) authentication.getPrincipal();
            return jwtTokenUtil.generateToken(user.getUsername());
        } else {
            throw new UserNotFoundException("Invalid credentials");
        }
    }
}
