package com.rentspace.userservice.service.impl;

import com.rentspace.core.event.PasswordResetEvent;
import com.rentspace.userservice.entity.token.PasswordResetToken;
import com.rentspace.userservice.entity.user.User;
import com.rentspace.userservice.exception.InvalidCredentialsException;
import com.rentspace.userservice.exception.UserNotFoundException;
import com.rentspace.userservice.repository.PasswordResetTokenRepository;
import com.rentspace.userservice.repository.UserRepository;
import com.rentspace.userservice.service.PasswordResetService;
import com.rentspace.userservice.util.EmailTemplateUtil;
import com.rentspace.userservice.util.TokenGenerationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {
    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final PasswordEncoder passwordEncoder;
    private final TokenGenerationUtil tokenGenerationUtil;
    private final EmailTemplateUtil emailTemplateUtil;

    private static final String INVALID_RESET_CODE_MESSAGE = "Invalid or expired reset code";

    @Value("${event.topic.password-reset}")
    private String topicName;

    @Override
    @Transactional
    public void sendResetCode(String email) {
        log.info("Attempting to send password reset code for email: {}", email);
        User user = findUserByEmailOrThrow(email);
        PasswordResetToken token = createOrUpdateResetToken(user);
        PasswordResetEvent event = buildPasswordResetEvent(user, token.getToken());
        sendKafkaEvent(event);
        log.info("Password reset code sent successfully for email: {}", email);
    }

    @Override
    @Transactional
    public void resetPassword(String email, String resetCode, String newPassword) {
        log.info("Attempting to reset password for email: {} with reset code: {}", email, resetCode);
        PasswordResetToken resetToken = findValidResetTokenOrThrow(email, resetCode);
        updateUserPassword(resetToken.getUser(), newPassword);
        tokenRepository.delete(resetToken);
        log.info("Password reset successfully for email: {}", email);
    }

    private User findUserByEmailOrThrow(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }

    private PasswordResetToken createOrUpdateResetToken(User user) {
        return tokenRepository.findByUser(user)
                .map(this::updateExistingTokenIfValid)
                .orElseGet(() -> createNewResetToken(user));
    }

    private PasswordResetToken createNewResetToken(User user) {
        PasswordResetToken token = PasswordResetToken.builder()
                .token(tokenGenerationUtil.generateCode())
                .user(user)
                .expiryDate(tokenGenerationUtil.getPasswordResetExpiryDate())
                .build();
        return tokenRepository.save(token);
    }

    private PasswordResetToken updateExistingTokenIfValid(PasswordResetToken token) {
        if (!token.isExpired()) {
            log.debug("Reusing existing valid token for user: {}", token.getUser().getEmail());
            return token;
        }
        token.setToken(tokenGenerationUtil.generateCode());
        token.setExpiryDate(tokenGenerationUtil.getPasswordResetExpiryDate());
        return tokenRepository.save(token);
    }

    private PasswordResetEvent buildPasswordResetEvent(User user, String resetCode) {
        return PasswordResetEvent.builder()
                .email(user.getEmail())
                .subject(emailTemplateUtil.getPasswordResetSubject())
                .message(emailTemplateUtil.getPasswordResetMessage(resetCode))
                .build();
    }

    private void sendKafkaEvent(PasswordResetEvent event) {
        try {
            kafkaTemplate.send(topicName, event.getEmail(), event)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error("Failed to send password reset event for email: {}. Error: {}", event.getEmail(), ex.getMessage());
                        } else {
                            log.debug("Kafka event sent successfully for email: {}, Topic={}, Offset={}",
                                    event.getEmail(), result.getRecordMetadata().topic(), result.getRecordMetadata().offset());
                        }
                    });
        } catch (Exception e) {
            log.error("Immediate failure sending Kafka event for email: {}", event.getEmail(), e);
            throw new RuntimeException("Failed to send password reset notification", e);
        }
    }

    private PasswordResetToken findValidResetTokenOrThrow(String email, String resetCode) {
        PasswordResetToken token = tokenRepository.findByTokenAndUser_Email(resetCode, email)
                .orElseThrow(() -> new InvalidCredentialsException(INVALID_RESET_CODE_MESSAGE));
        if (token.isExpired()) {
            tokenRepository.delete(token);
            throw new InvalidCredentialsException(INVALID_RESET_CODE_MESSAGE);
        }
        return token;
    }

    private void updateUserPassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}