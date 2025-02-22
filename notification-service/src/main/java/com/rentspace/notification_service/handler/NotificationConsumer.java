package com.rentspace.notification_service.handler;

import com.rentspace.notification_service.repository.NotificationRepository;
import com.rentspace.notification_service.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationConsumer {
    private final EmailService emailService;
    private final NotificationRepository repository;
}