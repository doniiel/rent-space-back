package com.rentspace.notificationservice.service.impl;

import com.rentspace.notificationservice.entity.Notifications;
import com.rentspace.notificationservice.enums.NotificationStatus;
import com.rentspace.notificationservice.enums.NotificationType;
import com.rentspace.notificationservice.exception.NotificationNotFound;
import com.rentspace.notificationservice.repository.NotificationRepository;
import com.rentspace.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;

    @Override
    public Notifications saveNotification(String email, String message, NotificationType type) {
        Notifications notification = Notifications.builder()
                .email(email)
                .message(message)
                .type(type)
                .status(NotificationStatus.PENDING)
                .build();
        return notificationRepository.save(notification);
    }

    @Override
    public void updateNotificationStatus(Long id, NotificationStatus status) {
        notificationRepository.findById(id).ifPresentOrElse(
                notification -> {
                    notification.setStatus(status);
                    notificationRepository.save(notification);
                    log.info("Notification {} updated to status {}", id, status);
                },
                () -> {
                    throw new NotificationNotFound("Notification", "id", id);
                }
        );
    }
}
