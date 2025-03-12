package com.rentspace.paymentservice.handler;

import com.rentspace.core.event.BookingCreatedEvent;
import com.rentspace.paymentservice.dto.PaymentDto;
import com.rentspace.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentHandler {
    private final PaymentService paymentService;

    @KafkaListener(topics = "${event.topic.payment}", groupId = "${spring.kafka.consumer.group-id}")
    public void handlePaymentEvent(@Payload BookingCreatedEvent event) {
        log.info("Received payment event for booking: {}", event.getBookingId());

        PaymentDto payment = paymentService.createPayment(
                event.getUserId(), event.getBookingId(), event.getCurrency(), event.getTotalPrice()
        );

        log.info("Created payment for Booking ID: {}, Payment ID: {}", event.getBookingId(), payment.getId());
    }
}
