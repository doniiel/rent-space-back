package com.rentspace.userservice.service.impl;

import com.rentspace.userservice.dto.UpdateUserRequest;
import com.rentspace.userservice.dto.UserCreateRequest;
import com.rentspace.userservice.dto.UserDto;
import com.rentspace.userservice.entity.User;
import com.rentspace.userservice.enums.Role;
import com.rentspace.userservice.exception.UserAlreadyExistsException;
import com.rentspace.userservice.exception.UserNotFoundException;
import com.rentspace.userservice.mapper.UserMapper;
import com.rentspace.userservice.repository.UserRepository;
import com.rentspace.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserDto createUser(UserCreateRequest request) {
        log.info("Creating new user {}", request);

        validateEmailAndPhone(request.getEmail(), request.getPhone());
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("User", "username", request.getUsername());
        }

        User user = userMapper.toEntity(request);
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        User savedUser = userRepository.save(user);

        log.info("User created successfully with ID: {}", savedUser.getId());
        return userMapper.toResponseDto(user);
    }


    @Override
    @Transactional(readOnly = true)
    public UserDto getUserByEmail(String email) {
        log.info("Fetching user with email: {}", email);

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException("User", "email", email)
        );
        UserDto userDto = userMapper.toResponseDto(user);
        log.info("User fetched successfully with Email: {}", userDto.getEmail());
        return userDto;
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        log.info("Fetching user with username: {}", username);
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException("User", "username", username)
        );
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserById(Long userId) {
        log.info("Fetching user with ID: {}", userId);
        User user = getExistingUser(userId);
        return userMapper.toResponseDto(user);
    }
    @Override
    @Transactional
    public UserDto updateUser(Long userId, UpdateUserRequest request) {
        log.info("Updating user with ID: {}", userId);

        var user = getExistingUser(userId);
        validateUniqueEmailAndPhone(user, request);
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserNotFoundException("User", "username", request.getUsername());
        }
        user.setUsername(request.getUsername());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        user.setRole(Role.valueOf(request.getRole()));

        User updatedUser = userRepository.save(user);
        log.info("User updated successfully with ID: {}", updatedUser.getId());
        return userMapper.toResponseDto(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        log.info("Deleting user with ID: {}", userId);
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User", "userId", userId);
        }
        userRepository.deleteById(userId);
        log.info("User deleted successfully with ID: {}", userId);
    }

    private User getExistingUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User", "userId", userId));
    }

    private void validateEmailAndPhone(String email, String phone) {
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException("User", "email", email);
        }
        if (userRepository.existsByPhone(phone)) {
            throw new UserAlreadyExistsException("User", "phone", phone);
        }
    }

    private void validateUniqueEmailAndPhone(User user, UpdateUserRequest request) {
        if (!user.getEmail().equals(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("User", "email", request.getEmail());
        }
        if (!user.getPhone().equals(request.getPhone()) && userRepository.existsByPhone(request.getPhone())) {
            throw new UserAlreadyExistsException("User", "mobileNumber", request.getPhone());
        }
    }
}
