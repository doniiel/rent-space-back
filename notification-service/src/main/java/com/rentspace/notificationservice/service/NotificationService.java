package com.rentspace.notificationservice.service;

import com.rentspace.notificationservice.entity.Notifications;
import com.rentspace.notificationservice.enums.NotificationStatus;
import com.rentspace.notificationservice.enums.NotificationType;

public interface NotificationService {
    Notifications saveNotification(String email, String message, NotificationType type);
    void updateNotificationStatus(Long id, NotificationStatus status);
}
