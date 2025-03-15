package com.rentspace.userservice.service.impl;

import com.rentspace.core.event.AccountVerificationEvent;
import com.rentspace.userservice.entity.token.VerificationToken;
import com.rentspace.userservice.entity.user.User;
import com.rentspace.userservice.exception.InvalidCredentialsException;
import com.rentspace.userservice.repository.UserRepository;
import com.rentspace.userservice.repository.VerificationTokenRepository;
import com.rentspace.userservice.service.AccountVerificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.rentspace.userservice.util.EmailTemplateUtil.VERIFICATION_SUBJECT;
import static com.rentspace.userservice.util.EmailTemplateUtil.getVerificationMessage;
import static com.rentspace.userservice.util.VerificationTokenUtil.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountVerificationServiceImpl implements AccountVerificationService {
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String INVALID_TOKEN_MESSAGE = "Invalid verification token";
    private static final String EXPIRED_TOKEN_MESSAGE = "Verification token has expired";
    private static final String KAFKA_SEND_ERROR_MESSAGE = "Failed to send verification email";

    @Value("${event.topic.verification}")
    private String topicName;

    @Value("${verification.base-url}")
    private String verificationBaseUrl;

    @Override
    @Transactional
    public void sendVerificationEmail(User user) {
        log.info("Starting verification email process for user: {}", user.getEmail());

        VerificationToken verificationToken = createOrUpdateVerificationToken(user);
        String verificationLink = generateVerificationUrl(verificationToken.getToken());
        AccountVerificationEvent event = buildVerificationEvent(user.getEmail(), verificationLink);

        sendVerificationEventToKafka(event, user.getEmail());
        log.info("Verification email process completed for user: {}", user.getEmail());
    }

    @Override
    @Transactional
    public void confirmAccount(String token) {
        log.info("Attempting to confirm account with token {}", token);

        VerificationToken verificationToken = findValidVerificationTokenOrThrow(token);
        verifyUser(verificationToken.getUser());
        verificationTokenRepository.delete(verificationToken);

        log.info("Account successfully verified for user: {}", verificationToken.getUser().getEmail());
    }

    private VerificationToken createOrUpdateVerificationToken(User user) {
        return verificationTokenRepository.findByUser(user)
                .map(this::updateExistingTokenIfValid)
                .orElseGet(() -> createNewVerificationToken(user));
    }

    private VerificationToken createNewVerificationToken(User user) {
        VerificationToken token = VerificationToken.builder()
                .token(generateVerificationToken())
                .user(user)
                .expiryDate(getExpiryDate())
                .build();
        return verificationTokenRepository.save(token);
    }

    private VerificationToken updateExistingTokenIfValid(VerificationToken token) {
        if (!token.isExpired()) {
            log.debug("Reusing existing valid token for user: {}", token.getUser().getEmail());
            return token;
        }
        token.setToken(generateVerificationToken());
        token.setExpiryDate(getExpiryDate());
        return verificationTokenRepository.save(token);
    }

    private String generateVerificationUrl(String token) {
        return verificationBaseUrl + "/verify?token=" + token;
    }

    private AccountVerificationEvent buildVerificationEvent(String email, String verificationLink) {
        return AccountVerificationEvent.builder()
                .email(email)
                .subject(VERIFICATION_SUBJECT)
                .message(getVerificationMessage(verificationLink))
                .build();
    }

    private void sendVerificationEventToKafka(AccountVerificationEvent event, String email) {
        try {
            kafkaTemplate.send(topicName, email, event)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error("Failed to send verification event to Kafka for email: {}. Error: {}", email, ex.getMessage());
                        }
                        else {
                            log.debug("Verification event sent to Kafka: Topic={}, Offset={}",
                                    result.getRecordMetadata().topic(), result.getRecordMetadata().offset());
                        }
                    });
        } catch (Exception e) {
            log.error("Immediate failure sending verification event to Kafka for email: {}", email, e);
            throw new RuntimeException(KAFKA_SEND_ERROR_MESSAGE, e);
        }
    }

    private VerificationToken findValidVerificationTokenOrThrow(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> {
                    log.warn("Invalid verification token: {}", token);
                    return new InvalidCredentialsException(INVALID_TOKEN_MESSAGE);
                });

        if (verificationToken.isExpired()) {
            verificationTokenRepository.delete(verificationToken);
            log.warn("Expired verification token: {}", token);
            throw new InvalidCredentialsException(EXPIRED_TOKEN_MESSAGE);
        }
        return verificationToken;
    }

    private void verifyUser(User user) {
        user.setVerified(true);
        userRepository.save(user);
    }
}
