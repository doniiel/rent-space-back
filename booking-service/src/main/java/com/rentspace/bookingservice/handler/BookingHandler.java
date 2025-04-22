package com.rentspace.bookingservice.handler;

import com.rentspace.bookingservice.dto.BookingDto;
import com.rentspace.bookingservice.enums.BookingStatus;
import com.rentspace.bookingservice.exception.BookingNotFoundException;
import com.rentspace.bookingservice.service.BookingService;
import com.rentspace.core.enums.ListingStatus;
import com.rentspace.core.event.*;
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

    @Value("${event.topic.listing.availability.block}")
    private String listingBlockTopic;

    @Value("${event.topic.notification.send-request}")
    private String notificationSendRequestTopic;

    @Transactional
    @KafkaListener(topics = "${event.topic.payment.success}", groupId = "${spring.kafka.consumer.group-id}")
    public void handlerSuccessPaymentEvent(@Payload PaymentSuccessEvent event) {
        log.info("Received payment success event for booking ID: {}", event.getBookingId());
        service.updateBookingStatus(event.getBookingId(), BookingStatus.CONFIRMED);

        try {
            BookingDto booking = service.getBookingById(event.getBookingId());
            publishListingBlockEvent(booking);
        }
         catch (BookingNotFoundException e) {
            log.error("Booking not found for payment success, booking ID: {}", event.getBookingId(), e);
         }
    }

    @Transactional
    @KafkaListener(topics = "${event.topic.payment.failure}", groupId = "${spring.kafka.consumer.group-id}")
    public void handlerFailurePaymentEvent(@Payload PaymentFailureEvent event) {
        log.info("Received payment failure event for booking ID: {}", event.getBookingId());
        try {
            service.updateBookingStatus(event.getBookingId(), BookingStatus.CANCELLED);
            BookingDto booking = service.getBookingById(event.getBookingId());
            service.cancelBooking(event.getBookingId());
            publishNotificationEvent(booking, "Payment failed for your booking #" + booking.getId());
         } catch (BookingNotFoundException e) {
            log.error("Booking not found for payment failure, booking ID: {}", event.getBookingId(), e);
        }
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
            try {
                BookingDto booking = service.getBookingById(event.getBookingId());
                service.cancelBooking(event.getBookingId());
                publishNotificationEvent(booking, "Booking #" + booking.getId() + " canceled due to listing unavailability");
            } catch (BookingNotFoundException e) {
                log.error("Booking not found for availability response, booking ID: {}", event.getBookingId(), e);
            }
        }
    }

    private void publishBookingCreatedEvent(BookingDto booking) {
        BookingCreatedEvent event = BookingCreatedEvent.builder()
                .bookingId(booking.getId())
                .userId(booking.getUserId())
                .listingId(booking.getListingId())
                .totalPrice(booking.getTotalPrice())
                .currency("USD")
                .build();
        publisher.publish(paymentTopic, booking.getId(), event, "booking created");
    }

    private void publishListingBlockEvent(BookingDto booking) {
        ListingBlockEvent event = ListingBlockEvent.builder()
                .bookingId(booking.getId())
                .listingId(booking.getListingId())
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .build();
        publisher.publish(listingBlockTopic, booking.getId(), event, "listing ");
    }

    private void publishNotificationEvent(BookingDto booking, String message) {
        NotificationEvent event = NotificationEvent.builder()
                .userId(booking.getUserId())
                .message(message)
                .build();
        publisher.publish(notificationSendRequestTopic, booking.getUserId(), event, "notification send");
    }
}
