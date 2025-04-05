package com.rentspace.userservice.service.impl;

import com.rentspace.core.dto.UserDto;
import com.rentspace.core.enums.Currency;
import com.rentspace.core.enums.Language;
import com.rentspace.userservice.dto.UpdateUserProfilesRequest;
import com.rentspace.userservice.dto.UpdateUserRequest;
import com.rentspace.userservice.dto.UserCreateRequest;
import com.rentspace.userservice.entity.user.User;
import com.rentspace.userservice.entity.user.UserProfiles;
import com.rentspace.userservice.enums.Role;
import com.rentspace.userservice.exception.UserAlreadyExistsException;
import com.rentspace.userservice.exception.UserNotFoundException;
import com.rentspace.userservice.mapper.UserMapper;
import com.rentspace.userservice.repository.UserRepository;
import com.rentspace.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    @Transactional
    @Caching(put = {
            @CachePut(value = "user", key = "#result.id"),
            @CachePut(value = "user", key = "#result.username"),
            @CachePut(value = "user", key = "#result.email")
            }
    )
    public UserDto createUser(UserCreateRequest request) {
        log.info("Attempting to create user with username: {}", request.getUsername());
        checkUserUniqueness(request.getUsername(), request.getEmail(), request.getPhone());
        User user = buildUserFromRequest(request);
        UserProfiles defaultProfiles = buildDefaultUserProfiles(user);
        user.setProfile(defaultProfiles);
        User savedUser = userRepository.save(user);
        log.info("User created successfully with ID: {}", savedUser.getId());
        return userMapper.toResponseDto(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "user", key = "#email", unless = "#result == null")
    public UserDto getUserByEmail(String email) {
        log.info("Fetching user with email: {}", email);
        return userMapper.toResponseDto(findUserByEmailOrThrow(email));
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "user", key = "#username", unless = "#result == null")
    public UserDto getUserByUsername(String username) {
        log.info("Fetching user with username: {}", username);
        User user = findUserByUsernameOrThrow(username);
        return userMapper.toResponseDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "user", key = "#userId", unless = "#result == null")
    public UserDto getUserById(Long userId) {
        log.info("Fetching user with ID: {}", userId);
        return userMapper.toResponseDto(findUserByIdOrThrow(userId));
    }

    @Override
    @Transactional
    @Caching(
            put = { @CachePut(value = "user", key = "#userId"),
                    @CachePut(value = "user", key = "#result.email"),
                    @CachePut(value = "user", key = "#result.username")
            },
            evict = {
                    @CacheEvict(value = "user", key = "#user.email"),
                    @CacheEvict(value = "user", key = "#user.username")
            }
    )
    public UserDto updateUser(Long userId, UpdateUserRequest request) {
        log.info("Updating user with ID: {}", userId);
        User user = findUserByIdOrThrow(userId);
        updateUserFields(user, request);
        User updatedUser = userRepository.save(user);
        log.info("User updated successfully with ID: {}", updatedUser.getId());
        return userMapper.toResponseDto(updatedUser);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "user", key = "#userId"),
            @CacheEvict(value = "user", key = "#user.email"),
            @CacheEvict(value = "user", key = "#user.username")
        }
    )
    public void deleteUser(Long userId) {
        log.info("Deleting user with ID: {}", userId);
        User user = findUserByIdOrThrow(userId);
        userRepository.delete(user);
        log.info("User deleted successfully with ID: {}", userId);
    }

    @Override
    @Transactional
    @Caching(put = {
            @CachePut(value = "user", key = "#userId"),
            @CachePut(value = "user", key = "#result.email"),
            @CachePut(value = "user", key = "#result.username")
    })
    public UserDto updateUserProfile(Long userId, UpdateUserProfilesRequest request) {
        log.info("Updating user profile with ID: {}", userId);
        User user = findUserByIdOrThrow(userId);
        UserProfiles userProfiles = user.getProfile();
        if (userProfiles == null) {
            userProfiles = buildDefaultUserProfiles(user);
            user.setProfile(userProfiles);
        }
        updateUserProfileFields(userProfiles, request);
        User updatedUser = userRepository.save(user);
        log.info("User profile updated successfully with ID: {}", updatedUser.getId());
        return userMapper.toResponseDto(updatedUser);
    }

    private User buildUserFromRequest(UserCreateRequest request) {
        return User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .role(Role.USER)
                .build();
    }

    private UserProfiles buildDefaultUserProfiles(User user) {
        return UserProfiles.builder()
                .user(user)
                .language(Language.ENGLISH)
                .currency(Currency.USD)
                .build();
    }

    private void checkUserUniqueness(String username, String email, String phone) {
        if (userRepository.existsByUsername(username)) {
            throw new UserAlreadyExistsException("User", "username", username);
        }
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException("User", "email", email);
        }
        if (userRepository.existsByPhone(phone)) {
            throw new UserAlreadyExistsException("User", "phone", phone);
        }
    }

    private void updateUserFields(User user, UpdateUserRequest request) {
        checkUniqueFieldsForUpdate(user, request);

        Optional.ofNullable(request.getUsername()).ifPresent(user::setUsername);
        Optional.ofNullable(request.getEmail()).ifPresent(user::setEmail);
        Optional.ofNullable(request.getPhone()).ifPresent(user::setPhone);
        Optional.ofNullable(request.getFirstname()).ifPresent(user::setFirstname);
        Optional.ofNullable(request.getLastname()).ifPresent(user::setLastname);
        Optional.ofNullable(request.getPassword())
                .filter(password -> !password.isEmpty())
                .map(passwordEncoder::encode)
                .ifPresent(user::setPassword);
        Optional.ofNullable(request.getRole())
                .map(Role::valueOf)
                .ifPresent(user::setRole);
    }

    private void checkUniqueFieldsForUpdate(User user, UpdateUserRequest request) {
        Optional.ofNullable(request.getUsername())
                .filter(username -> !username.equals(user.getUsername()))
                .filter(userRepository::existsByUsername)
                .ifPresent(username -> {
                    throw new UserAlreadyExistsException("User", "username", username);
                });

        Optional.ofNullable(request.getEmail())
                .filter(email -> !email.equals(user.getEmail()))
                .filter(userRepository::existsByEmail)
                .ifPresent(email -> {
                    throw new UserAlreadyExistsException("User", "email", email);
                });

        Optional.ofNullable(request.getPhone())
                .filter(phone -> !phone.equals(user.getPhone()))
                .filter(userRepository::existsByPhone)
                .ifPresent(phone -> {
                    throw new UserAlreadyExistsException("User", "phone", phone);
                });
    }

    private void updateUserProfileFields(UserProfiles userProfiles, UpdateUserProfilesRequest request) {
        Optional.ofNullable(request.getBio()).ifPresent(userProfiles::setBio);
        Optional.ofNullable(request.getAvatarUrl()).ifPresent(userProfiles::setAvatarUrl);
        Optional.ofNullable(request.getLanguage()).ifPresent(userProfiles::setLanguage);
        Optional.ofNullable(request.getCurrency()).ifPresent(userProfiles::setCurrency);
    }

    private User findUserByIdOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User", "id", userId));
    }

    private User findUserByUsernameOrThrow(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User", "username", username));
    }

    private User findUserByEmailOrThrow(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User", "email", email));
    }
}
