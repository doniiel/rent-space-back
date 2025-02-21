package com.rentspace.userservice.service.impl;

import com.rentspace.userservice.entity.token.PasswordResetToken;
import com.rentspace.userservice.entity.user.User;
import com.rentspace.userservice.event.PasswordResetEvent;
import com.rentspace.userservice.exception.InvalidCredentialsException;
import com.rentspace.userservice.exception.UserNotFoundException;
import com.rentspace.userservice.repository.PasswordResetTokenRepository;
import com.rentspace.userservice.repository.UserRepository;
import com.rentspace.userservice.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.rentspace.userservice.util.EmailTemplateUtil.PASSWORD_RESET_SUBJECT;
import static com.rentspace.userservice.util.EmailTemplateUtil.getPasswordResetMessage;
import static com.rentspace.userservice.util.PasswordResetTokenUtil.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {
    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final PasswordEncoder passwordEncoder;

    @Value("${event.topic.password-reset}")
    private String topicName;

    @Override
    public void sendResetCode(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException("User not found"));

        String resetCode = generateResetCode();
        PasswordResetToken token = PasswordResetToken.builder()
                .token(resetCode)
                .user(user)
                .expiryDate(getExpiryDate())
                .createdDate(LocalDateTime.now())
                .build();
        tokenRepository.save(token);

        PasswordResetEvent event = PasswordResetEvent.builder()
                .email(user.getEmail())
                .subject(PASSWORD_RESET_SUBJECT)
                .message(getPasswordResetMessage(resetCode))
                .build();

        kafkaTemplate.send(topicName, user.getEmail(), event);
    }

    @Override
    public void resetPassword(String email, String resetCode, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByTokenAndUser_Email(resetCode, email).orElseThrow(
                () -> new InvalidCredentialsException("Invalid or expired reset code"));

        if (isTokenExpired(resetToken.getExpiryDate())) {
            throw new InvalidCredentialsException("Reset code has expired");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        tokenRepository.delete(resetToken);
    }
}
