package com.rentspace.notification_service.service;

import com.rentspace.notification_service.entity.Notifications;
import com.rentspace.notification_service.enums.NotificationStatus;
import com.rentspace.notification_service.enums.NotificationType;

public interface NotificationService {
    Notifications saveNotification(String email, String message, NotificationType type);
    void updateNotificationStatus(Long id, NotificationStatus status);
}
