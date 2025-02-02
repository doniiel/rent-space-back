package com.rentspace.userservice.service;

import com.rentspace.userservice.dto.UserCreateDto;
import com.rentspace.userservice.dto.UserResponseDto;

public interface UserService {
    UserResponseDto createUser(UserCreateDto userCreateDto);

    UserResponseDto getUserByEmail(String email);

    UserResponseDto getUserById(Long userId);

    UserResponseDto updateUser(Long userId, UserCreateDto userCreateDto);

    void deleteUser(Long userId);
}
