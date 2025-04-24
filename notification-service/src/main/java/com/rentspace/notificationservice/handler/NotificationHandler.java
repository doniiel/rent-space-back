package com.rentspace.notificationservice.handler;

import com.rentspace.core.dto.UserDto;
import com.rentspace.core.event.AccountVerificationEvent;
import com.rentspace.core.event.ListingNotificationEvent;
import com.rentspace.core.event.NotificationEvent;
import com.rentspace.core.event.PasswordResetEvent;
import com.rentspace.notificationservice.client.UserClient;
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
public class NotificationHandler {
    private final NotificationService notificationService;
    private final EmailService emailService;
    private final UserClient userClient;

    @KafkaListener(topics = "${event.topic.notification.verification}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleVerificationEvent(@Payload AccountVerificationEvent event) {
        Map<String, Object> emailVariables = new HashMap<>();
        emailVariables.put("name", event.getEmail().split("@")[0]);
        emailVariables.put("verificationCode", extractVerificationCode(event.getMessage()));

        processNotification(event.getEmail(), event.getSubject(), "email-verification", emailVariables, NotificationType.ACCOUNT_VERIFICATION);
    }

    @KafkaListener(topics = "${event.topic.notification.password-reset}", groupId = "${spring.kafka.consumer.group-id}")
    public void handlePasswordResetEvent(@Payload PasswordResetEvent event) {
        Map<String, Object> emailVariables = new HashMap<>();
        emailVariables.put("name", event.getEmail().split("@")[0]);
        emailVariables.put("resetCode", extractVerificationCode(event.getMessage()));

        processNotification(event.getEmail(), event.getSubject(), "email-password-reset", emailVariables, NotificationType.PASSWORD_RESET);
    }

    @KafkaListener(topics = "${event.topic.notification.send-request}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleNotificationEvent(@Payload NotificationEvent event) {
        String email = getEmailByUserId(event.getUserId());

        String subject = "RentSpace Notification";
        Map<String, Object> emailVariables = new HashMap<>();
        emailVariables.put("message", event.getMessage());

        processNotification(email, subject, "general-notification", emailVariables, NotificationType.GENERAL);
    }

    @KafkaListener(topics = "${event.topic.notification.listing-request}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleListingNotificationEvent(@Payload ListingNotificationEvent event) {
        String email = getEmailByUserId(event.getUserId());
        if (email == null || email.isEmpty()) {
            log.warn("Email is empty for userId: {}. Skipping notification", event.getUserId());
            return;
        }
        String subject = "";
        String templateName = "";
        NotificationType type = NotificationType.LISTING;

        switch (event.getEventType()) {
            case "listing-created":
                subject = "RentSpace - New listing created";
                templateName = "listing-modified";
                break;
            case "listing-updated":
                subject = "RentSpace - Listing updated";
                templateName = "listing-modified";
                break;
            case "listing-deleted":
                subject = "RentSpace - Listing deleted";
                templateName = "listing-modified";
                break;
            case "listing-blocked":
                subject = "RentSpace - Listing blocked";
                templateName = "listing-block";
                break;
            case "listing-unblocked":
                subject = "RentSpace - Listing unblocked";
                templateName = "listing-block";
                break;
            case "listing-rating-updated":
                subject = "RentSpace - Listing rating updated";
                templateName = "listing-rating";
                break;
        }

        Map<String, Object> emailVariables = new HashMap<>();
        emailVariables.put("message", event.getMessage());
        emailVariables.put("listingId", event.getListingId());

        if (!subject.isEmpty()  && !templateName.isEmpty()) {
            processNotification(email, subject, templateName, emailVariables, type);
        }
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

    private String getEmailByUserId(Long userId) {
        try {
            UserDto user = userClient.getUserById(userId);
            return user.getEmail();
        } catch (Exception e) {
            log.error("Failed to get email by userId: {}", userId);
            return null;
        }
    }
}
