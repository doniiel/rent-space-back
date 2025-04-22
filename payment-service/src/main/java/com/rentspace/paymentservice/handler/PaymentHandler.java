package com.rentspace.paymentservice.handler;

import com.rentspace.core.event.BookingCreatedEvent;
import com.rentspace.paymentservice.dto.PaymentDto;
import com.rentspace.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentHandler {
    private final PaymentService paymentService;

    @Transactional
    @KafkaListener(topics = "${event.topic.payment.create-request}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleBookingCreatedEvent(@Payload BookingCreatedEvent event) {
        log.info("Received payment create request for booking: {}", event.getBookingId());

        try {
            PaymentDto payment = paymentService.createPayment(
                    event.getUserId(),
                    event.getBookingId(),
                    event.getCurrency(),
                    event.getTotalPrice()
            );
            log.info("Created payment ID: {} for booking ID: {}", payment.getId(), event.getBookingId());
        } catch (Exception e) {
            log.error("Failed to create payment for booking ID: {}", event.getBookingId(), e);
            throw e;
        }
    }
}