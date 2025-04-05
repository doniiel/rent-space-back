package com.rentspace.userservice.service;

import com.rentspace.core.dto.UserDto;
import com.rentspace.userservice.dto.UpdateUserProfilesRequest;
import com.rentspace.userservice.dto.UpdateUserRequest;
import com.rentspace.userservice.dto.UserCreateRequest;

public interface UserService {
    UserDto createUser(UserCreateRequest request); // NEW VERSION
    UserDto getUserByUsername(String username);
    UserDto getUserByEmail(String email);
    UserDto getUserById(Long userId);
    UserDto updateUser(Long userId, UpdateUserRequest request);
    void deleteUser(Long userId);
    UserDto updateUserProfile(Long userId, UpdateUserProfilesRequest request);
}
