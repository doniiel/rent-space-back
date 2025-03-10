package com.rentspace.bookingservice.handler;

import com.rentspace.bookingservice.enums.BookingStatus;
import com.rentspace.bookingservice.service.BookingService;
import com.rentspace.core.event.PaymentFailureEvent;
import com.rentspace.core.event.PaymentSuccessEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingConsumer {
    private final BookingService service;

    @KafkaListener(topics = "${event.topic.payment-success}", groupId = "${spring.kafka.consumer.group-id}")
    public void handlerSuccessPaymentEvent(@Payload PaymentSuccessEvent event) {
        log.info("Received payment success event for booking ID: {}", event.getBookingId());
        service.updateBookingStatus(event.getBookingId(), BookingStatus.CONFIRMED);
    }

    @KafkaListener(topics = "${event.topic.payment-failure}", groupId = "${spring.kafka.consumer.group-id}")
    public void handlerFailurePaymentEvent(@Payload PaymentFailureEvent event) {
        log.info("Received payment failure event for booking ID: {}", event.getBookingId());
        service.updateBookingStatus(event.getBookingId(), BookingStatus.CANCELLED);
    }

}
