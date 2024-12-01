package com.rentspace.userservice.service;

import com.rentspace.userservice.dto.UserDto;
import com.rentspace.userservice.entity.User;
import com.rentspace.userservice.exception.UserAlreadyExistsException;
import com.rentspace.userservice.exception.UserNotFoundException;
import com.rentspace.userservice.mapper.UserMapper;
import com.rentspace.userservice.repository.UserRepository;
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
    public String createUser(UserDto userDto) {
        log.info("Creating new user: {}", userDto);

        if (userRepository.existsByEmail(userDto.getEmail()) || userRepository.existsByMobileNumber(userDto.getMobileNumber())) {
            throw new UserAlreadyExistsException("User", "email/phone", userDto.getEmail());
        }
        User savedUser = userRepository.save(userMapper.toEntity(userDto));

        log.info("User created successfully with ID: {}", savedUser.getId());
        return format("User created successfully with ID: %s", savedUser.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserByEmail(String email) {
        log.info("Fetching user with email: {}", email);

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException("User", "email", email)
        );
        UserDto userDto = userMapper.toDto(user);
        log.info("User fetched successfully with Email: {}", userDto.getEmail());
        return userDto;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserById(Long userId) {
        log.info("Fetching user with ID: {}", userId);

        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("User", "userId", userId)
        );
        UserDto userDto = userMapper.toDto(user);
        log.info("User fetched successfully with ID: {}", userDto.getId());
        return userDto;
    }

    @Override
    @Transactional
    public UserDto updateUser(UserDto userDto) {
        log.info("Updating user with ID: {}", userDto.getId());

        User user = userRepository.findById(userDto.getId()).orElseThrow(
                () -> new UserNotFoundException("User", "userId", userDto.getId())
        );
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setRole(userDto.getRole());
        user.setMobileNumber(userDto.getMobileNumber());

        User savedUser = userRepository.save(updateDBUser(user, userDto));

        log.info("User updated successfully with ID: {}", savedUser.getId());
        return userMapper.toDto(savedUser);
    }

    @Override
    @Transactional
    public String deleteUser(Long userId) {
        log.info("Deleting user with ID: {}", userId);

        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User", "userId", userId);
        }
        userRepository.deleteById(userId);

        log.info("User deleted successfully with ID: {}", userId);
        return format("User deleted successfully with ID: %s", userId);
    }

    private User updateDBUser(User user, UserDto userDto) {
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setRole(userDto.getRole());
        user.setMobileNumber(userDto.getMobileNumber());
        return user;
    }
}
