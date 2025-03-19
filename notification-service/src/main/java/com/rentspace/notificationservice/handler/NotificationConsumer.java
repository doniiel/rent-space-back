package com.rentspace.notificationservice.handler;

import com.rentspace.core.event.AccountVerificationEvent;
import com.rentspace.core.event.PasswordResetEvent;
import com.rentspace.notificationservice.entity.Notifications;
import com.rentspace.notificationservice.enums.NotificationStatus;
import com.rentspace.notificationservice.enums.NotificationType;
import com.rentspace.notificationservice.service.EmailService;
import com.rentspace.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationConsumer {

    private final EmailService emailService;
    private final NotificationService notificationService;

    @KafkaListener(topics = "${event.topic.verification}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleVerificationEvent(@Payload AccountVerificationEvent event) {
        Map<String, Object> emailVariables = new HashMap<>();
        emailVariables.put("name", event.getEmail().split("@")[0]);
        emailVariables.put("verificationCode", extractVerificationCode(event.getMessage()));

        processNotification(event.getEmail(), event.getSubject(), "email-verification", emailVariables, NotificationType.ACCOUNT_VERIFICATION);
    }

    @KafkaListener(topics = "${event.topic.password-reset}", groupId = "${spring.kafka.consumer.group-id}")
    public void handlePasswordResetEvent(@Payload PasswordResetEvent event) {
        Map<String, Object> emailVariables = new HashMap<>();
        emailVariables.put("name", event.getEmail().split("@")[0]);
        emailVariables.put("resetCode", extractVerificationCode(event.getMessage()));

        processNotification(event.getEmail(), event.getSubject(), "email-password-reset", emailVariables, NotificationType.PASSWORD_RESET);
    }

    private void processNotification(String email, String subject, String templateName, Map<String, Object> emailVariables, NotificationType type) {
        log.info("Processing {} event for email: {}", type, email);

        Notifications notification = notificationService.saveNotification(email, subject, type);

        try {
            emailService.sendEmail(email, subject, templateName, emailVariables);
            log.info("{} email sent successfully to {}", type, email);
            notificationService.updateNotificationStatus(notification.getId(), NotificationStatus.SENT);
        } catch (Exception e) {
            log.error("Failed to send {} email to {}. Error: ", type, email, e);
            notificationService.updateNotificationStatus(notification.getId(), NotificationStatus.FAILED);
        }
    }
    private String extractVerificationCode(String message) {
        if (message != null && message.matches(".*\\d+$")) {
            return message.replaceAll(".*?(\\d+)$", "$1");
        }
        return "ERROR_CODE";
    }
}
