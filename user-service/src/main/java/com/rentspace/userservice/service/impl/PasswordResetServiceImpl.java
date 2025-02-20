package com.rentspace.userservice.service.impl;

import com.rentspace.userservice.entity.user.User;
import com.rentspace.userservice.exception.InvalidCredentialsException;
import com.rentspace.userservice.exception.UserNotFoundException;
import com.rentspace.userservice.repository.UserRepository;
import com.rentspace.userservice.service.PasswordResetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class PasswordResetServiceImpl implements PasswordResetService {
    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    public void sendResetCode(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException("User not found"));
        String resetCode = UUID.randomUUID().toString();
        PasswordResetToken token = new PasswordResetToken(resetCode, user, LocalDateTime.now().plusMinutes(10));
        tokenRepository.save(token);

        emailService.sendEmail(user.getEmail(), "Password Reset", "Your reset code: " + resetCode);
    }

    @Override
    public boolean validateResetCode(String email, String resetCode) {
        Optional<PasswordResetToken> token = tokenRepository.findByTokenAndUserEmail(code, email);
        return token.isPresent() && token.get().getExpiryDate().isAfter(LocalDateTime.now());
    }

    @Override
    public void resetPassword(String email, String resetCode, String newPassword) {
        if (!validateResetCode(email, resetCode)) {
            throw new InvalidCredentialsException("Invalid password reset code");
        }

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException("User not found"));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        tokenRepository.deleteByUserEmail(email);
    }

}
