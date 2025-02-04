package com.rentspace.userservice.service;

import com.rentspace.userservice.dto.UserCreateDto;
import com.rentspace.userservice.dto.UserCreateRequest;
import com.rentspace.userservice.dto.UserDto;

public interface UserService {
    UserDto createUser(UserCreateRequest request); // NEW VERSION
    UserDto getUserByEmail(String email);
    UserDto getUserById(Long userId);
    UserDto updateUser(Long userId, UserCreateDto userCreateDto);
    void deleteUser(Long userId);
}
