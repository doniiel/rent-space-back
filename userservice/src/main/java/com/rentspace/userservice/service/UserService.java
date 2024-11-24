package com.rentspace.userservice.service;

import com.rentspace.userservice.dto.UserDto;

public interface UserService {
    String createUser(UserDto userDto);

    UserDto getUserByEmail(String email);

    UserDto getUserById(Long userId);

    UserDto updateUser(UserDto userDto);

    String deleteUser(Long userId);
}
