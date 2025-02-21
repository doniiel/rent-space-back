package com.rentspace.userservice.service.impl;

import com.rentspace.userservice.entity.token.VerificationToken;
import com.rentspace.userservice.entity.user.User;
import com.rentspace.userservice.event.AccountVerificationEvent;
import com.rentspace.userservice.exception.InvalidCredentialsException;
import com.rentspace.userservice.repository.UserRepository;
import com.rentspace.userservice.repository.VerificationTokenRepository;
import com.rentspace.userservice.service.AccountVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.rentspace.userservice.util.EmailTemplateUtil.VERIFICATION_SUBJECT;
import static com.rentspace.userservice.util.EmailTemplateUtil.getVerificationMessage;
import static com.rentspace.userservice.util.VerificationTokenUtil.*;

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

        String link = generateConfirmAccountUrl(token);

        AccountVerificationEvent event = AccountVerificationEvent.builder()
                .email(user.getEmail())
                .subject(VERIFICATION_SUBJECT)
                .message(getVerificationMessage(link))
                .build();
        kafkaTemplate.send(topicName, user.getEmail(), event);

    }

    @Override
    public void confirmAccount(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token).orElseThrow(
                () -> new InvalidCredentialsException("Invalid verification token"));
        if (isTokenExpired(verificationToken.getExpiryDate())) {
            throw new InvalidCredentialsException("Verification token has expired");
        }

        User user = verificationToken.getUser();
        user.setVerified(true);
        userRepository.save(user);
        verificationTokenRepository.delete(verificationToken);
    }
}
