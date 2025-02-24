package com.rentspace.notification_service.service.impl;

import com.rentspace.notification_service.entity.Notifications;
import com.rentspace.notification_service.enums.NotificationStatus;
import com.rentspace.notification_service.enums.NotificationType;
import com.rentspace.notification_service.repository.NotificationRepository;
import com.rentspace.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;

    @Override
    public Notifications saveNotification(String email, String message, NotificationType type) {
        Notifications notification = Notifications.builder()
                .email(email)
                .message(message)
                .type(type)
                .status(NotificationStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();
        return notificationRepository.save(notification);
    }

    @Override
    public void updateNotificationStatus(Long id, NotificationStatus status) {
        notificationRepository.findById(id).ifPresent(notification -> {
            notification.setStatus(status);
            notification.setUpdatedAt(LocalDateTime.now());
            notificationRepository.save(notification);
        });
    }
}
