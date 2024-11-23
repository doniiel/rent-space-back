package com.rentspace.userservice.service;

import com.rentspace.userservice.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User createUser(User user);
    Optional<User> getUserByEmail(String email);
    Optional<User> getUserById(UUID id);
    User updateUser(UUID id, User userDetails);
    void deleteUser(UUID id);
}
