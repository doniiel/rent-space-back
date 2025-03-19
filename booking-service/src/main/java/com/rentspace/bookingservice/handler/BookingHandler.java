package com.rentspace.bookingservice.handler;

import com.rentspace.bookingservice.dto.BookingDto;
import com.rentspace.bookingservice.enums.BookingStatus;
import com.rentspace.bookingservice.exception.BookingNotFoundException;
import com.rentspace.bookingservice.service.BookingService;
import com.rentspace.core.enums.ListingStatus;
import com.rentspace.core.event.BookingCreatedEvent;
import com.rentspace.core.event.ListingAvailableResponse;
import com.rentspace.core.event.PaymentFailureEvent;
import com.rentspace.core.event.PaymentSuccessEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingHandler {
    private final BookingService service;
    private final BookingPublisher publisher;

    @Value("${event.topic.payment.send}")
    private String paymentTopic;

    @Transactional
    @KafkaListener(topics = "${event.topic.payment.success}", groupId = "${spring.kafka.consumer.group-id}")
    public void handlerSuccessPaymentEvent(@Payload PaymentSuccessEvent event) {
        log.info("Received payment success event for booking ID: {}", event.getBookingId());
        service.updateBookingStatus(event.getBookingId(), BookingStatus.CONFIRMED);
    }

    @Transactional
    @KafkaListener(topics = "${event.topic.payment.failure}", groupId = "${spring.kafka.consumer.group-id}")
    public void handlerFailurePaymentEvent(@Payload PaymentFailureEvent event) {
        log.info("Received payment failure event for booking ID: {}", event.getBookingId());
        service.updateBookingStatus(event.getBookingId(), BookingStatus.CANCELLED);
        service.cancelBooking(event.getBookingId());
    }

    @Transactional
    @KafkaListener(topics = "${event.topic.listing.availability.response}", groupId = "${spring.kafka.consumer.group-id}")
    public void handlerListingAvailabilityEvent(@Payload ListingAvailableResponse event) {
        log.info("Received listing availability response for booking ID: {}", event.getBookingId());

        if (ListingStatus.AVAILABLE.name().equals(event.getStatus())) {
            log.info("Listing is available. Proceeding with booking ID: {}", event.getBookingId());
            try {
                BookingDto booking = service.getBookingById(event.getBookingId());
                publishBookingCreatedEvent(booking);
            } catch (BookingNotFoundException e) {
                log.error("Booking not found for availability response, booking ID: {}", event.getBookingId(), e);
            }
        } else {
            log.warn("Listing is not available. Canceling booking ID: {}", event.getBookingId());
            service.cancelBooking(event.getBookingId());
        }
    }

    private void publishBookingCreatedEvent(BookingDto booking) {
        BookingCreatedEvent event = BookingCreatedEvent.builder()
                .bookingId(booking.getId())
                .userId(booking.getUserId())
                .listingId(booking.getListingId())
                .totalPrice(booking.getTotalPrice())
                .build();
        publisher.publish(paymentTopic, booking.getId(), event, "booking created");
    }
}
