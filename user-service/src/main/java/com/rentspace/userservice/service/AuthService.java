package com.rentspace.userservice.service;

import com.rentspace.userservice.dto.UserLoginDto;

public interface AuthService {
    String authenticateUser(UserLoginDto userLoginDto);
}
