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

    @Value("${event.topic.verification}")
    private String topicName;

    @Override
    public void sendVerificationEmail(User user) {
        String token = generateVerificationToken();
        VerificationToken verificationToken = VerificationToken.builder()
                .token(token)
                .user(user)
                .expiryDate(getExpiryDate())
                .build();

        verificationTokenRepository.save(verificationToken);
        log.info("✅ Verification token [{}] saved for user [{}]", token, user.getEmail());

        String link = generateConfirmAccountUrl(token);
        AccountVerificationEvent event = AccountVerificationEvent.builder()
                .email(user.getEmail())
                .subject(VERIFICATION_SUBJECT)
                .message(getVerificationMessage(link))
                .build();

        log.info("📨 Sending verification email to Kafka: Topic [{}], Key [{}]", topicName, user.getEmail());

        kafkaTemplate.send(topicName, user.getEmail(), event).whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("✅ Successfully sent verification event to Kafka: {}", result.getRecordMetadata());
            } else {
                log.error("❌ Failed to send verification event to Kafka for user [{}]: {}", user.getEmail(), ex.getMessage());
            }
        });
    }

    @Override
    public void confirmAccount(String token) {
        log.info("🔍 Attempting to confirm account with token [{}]", token);

        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> {
                    log.warn("❌ Invalid verification token: [{}]", token);
                    return new InvalidCredentialsException("Invalid verification token");
                });

        if (isTokenExpired(verificationToken.getExpiryDate())) {
            log.warn("❌ Expired verification token [{}]", token);
            throw new InvalidCredentialsException("Verification token has expired");
        }

        User user = verificationToken.getUser();
        user.setVerified(true);
        userRepository.save(user);
        verificationTokenRepository.delete(verificationToken);

        log.info("✅ User [{}] successfully verified", user.getEmail());
    }
}
