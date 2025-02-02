package com.rentspace.userservice.service.impl;

import com.rentspace.userservice.dto.UserCreateDto;
import com.rentspace.userservice.dto.UserResponseDto;
import com.rentspace.userservice.entity.User;
import com.rentspace.userservice.exception.UserAlreadyExistsException;
import com.rentspace.userservice.exception.UserNotFoundException;
import com.rentspace.userservice.mapper.UserMapper;
import com.rentspace.userservice.repository.UserRepository;
import com.rentspace.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserResponseDto createUser(UserCreateDto userDto) {
        log.info("Creating new user: {}", userDto);

        if (userRepository.existsByEmail(userDto.getEmail()) || userRepository.existsByMobileNumber(userDto.getMobileNumber())) {
            throw new UserAlreadyExistsException("User", "email/phone", userDto.getEmail());
        }
        User user = userMapper.toEntity(userDto);
        User savedUser = userRepository.save(user);

        log.info("User created successfully with ID: {}", savedUser.getId());
        return userMapper.toResponseDto(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUserByEmail(String email) {
        log.info("Fetching user with email: {}", email);

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException("User", "email", email)
        );
        UserResponseDto userResponseDto = userMapper.toResponseDto(user);
        log.info("User fetched successfully with Email: {}", userResponseDto.getEmail());
        return userResponseDto;
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUserById(Long userId) {
        log.info("Fetching user with ID: {}", userId);

        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("User", "userId", userId)
        );
        UserResponseDto userResponseDto = userMapper.toResponseDto(user);
        log.info("User fetched successfully with ID: {}", userResponseDto.getId());
        return userResponseDto;
    }

    @Override
    @Transactional
    public UserResponseDto updateUser(Long userId, UserCreateDto userDto) {
        log.info("Updating user with ID: {}", userId);

        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("User", "userId", userId)
        );

        if (!user.getEmail().equals(userDto.getEmail()) && userRepository.existsByEmail(userDto.getEmail())) {
            throw new UserAlreadyExistsException("User", "email", userDto.getEmail());
        }
        if (!user.getMobileNumber().equals(userDto.getMobileNumber()) && userRepository.existsByMobileNumber(userDto.getMobileNumber())) {
            throw new UserAlreadyExistsException("User", "mobileNumber", userDto.getMobileNumber());
        }

        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setRole(userDto.getRole());
        user.setMobileNumber(userDto.getMobileNumber());

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
}
