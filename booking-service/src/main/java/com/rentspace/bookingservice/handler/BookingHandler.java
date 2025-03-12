package com.rentspace.bookingservice.handler;

import com.rentspace.bookingservice.entity.Booking;
import com.rentspace.bookingservice.enums.BookingStatus;
import com.rentspace.bookingservice.exception.BookingNotFoundException;
import com.rentspace.bookingservice.repository.BookingRepository;
import com.rentspace.bookingservice.service.BookingService;
import com.rentspace.core.enums.ListingStatus;
import com.rentspace.core.event.BookingCreatedEvent;
import com.rentspace.core.event.ListingAvailableResponse;
import com.rentspace.core.event.PaymentFailureEvent;
import com.rentspace.core.event.PaymentSuccessEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingHandler {
    private final BookingService service;
    private final BookingRepository repository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    @Value("${event.topic.payment}")
    private String paymentTopic;

    @KafkaListener(topics = "${event.topic.payment-success}", groupId = "${spring.kafka.consumer.group-id}")
    public void handlerSuccessPaymentEvent(@Payload PaymentSuccessEvent event) {
        log.info("Received payment success event for booking ID: {}", event.getBookingId());
        service.updateBookingStatus(event.getBookingId(), BookingStatus.CONFIRMED);
    }

    @KafkaListener(topics = "${event.topic.payment-failure}", groupId = "${spring.kafka.consumer.group-id}")
    public void handlerFailurePaymentEvent(@Payload PaymentFailureEvent event) {
        log.info("Received payment failure event for booking ID: {}", event.getBookingId());
        service.updateBookingStatus(event.getBookingId(), BookingStatus.CANCELLED);
        service.cancelBooking(event.getBookingId());
    }

    @KafkaListener(topics = "${event.topic.listing.availability.response}", groupId = "${spring.kafka.consumer.group-id}")
    public void handlerListingAvailabilityEvent(@Payload ListingAvailableResponse event) {
        log.info("Received listing availability response for booking ID: {}", event.getBookingId());

        if (ListingStatus.AVAILABLE.equals(event.getStatus())) {
            log.info("Listing is available. Proceeding with booking ID: {}", event.getBookingId());

            Booking booking = repository.findById(event.getBookingId())
                    .orElseThrow(() -> new BookingNotFoundException("Booking", "bookingId", event.getBookingId()));

            BookingCreatedEvent bookingCreatedEvent = BookingCreatedEvent.builder()
                    .bookingId(booking.getId())
                    .userId(booking.getUserId())
                    .listingId(booking.getListingId())
                    .totalPrice(booking.getTotalPrice())
                    .build();

            kafkaTemplate.send(paymentTopic, bookingCreatedEvent);
            log.info("Sent payment request for booking ID: {}", event.getBookingId());
        } else {
            log.warn("Listing is not available. Canceling booking ID: {}", event.getBookingId());
            service.cancelBooking(event.getBookingId());
        }
    }
}
