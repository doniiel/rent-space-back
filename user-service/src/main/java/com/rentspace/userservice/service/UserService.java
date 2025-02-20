package com.rentspace.userservice.service;

import com.rentspace.userservice.dto.UpdateUserRequest;
import com.rentspace.userservice.dto.UserCreateRequest;
import com.rentspace.userservice.dto.UserDto;
import com.rentspace.userservice.entity.user.User;

public interface UserService {
    UserDto createUser(UserCreateRequest request); // NEW VERSION
    User getUserByUsername(String username);
    UserDto getUserByEmail(String email);
    UserDto getUserById(Long userId);
    UserDto updateUser(Long userId, UpdateUserRequest request);
    void deleteUser(Long userId);
}
