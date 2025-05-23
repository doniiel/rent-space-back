package com.rentspace.listingservice.handler;

import com.rentspace.core.enums.ListingStatus;
import com.rentspace.core.event.*;
import com.rentspace.listingservice.service.ListingAvailabilityService;
import com.rentspace.listingservice.service.ListingsService;
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
public class ListingHandler {
    private final ListingAvailabilityService listingAvailabilityService;
    private final ListingsService listingsService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${event.topic.listing.availability.response}")
    private String responseTopic;

    @Value("${event.topic.notification.send-request}")
    private String notificationSendRequestTopic;

    @KafkaListener(topics = "${event.topic.listing.availability.request}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleListingAvailabilityEvent(@Payload ListingAvailabilityEvent event) {
        log.info("Received listing availability request for booking ID: {}", event.getBookingId());

        boolean isAvailable = listingAvailabilityService.isAvailable(event.getListingId(), event.getStartDate(), event.getEndDate());

        ListingAvailableResponse response = ListingAvailableResponse.builder()
                .bookingId(event.getBookingId())
                .listingId(event.getListingId())
                .status(isAvailable ? ListingStatus.AVAILABLE.name() : ListingStatus.UNAVAILABLE.name())
                .build();
        kafkaTemplate.send(responseTopic, event.getBookingId().toString(), response);
        log.info("Sent listing availability response for booking ID: {}", event.getBookingId());
    }

    @KafkaListener(topics = "${event.topic.listing.availability.block}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleListingBlockEvent(@Payload ListingBlockEvent event) {
        log.info("Received listing block request for booking ID: {}", event.getBookingId());
        listingAvailabilityService.blockAvailability(event.getListingId(), event.getStartDate(), event.getEndDate());
        log.info("Successfully blocked availability for listingId={} from {} to {}",
                event.getListingId(), event.getStartDate(), event.getEndDate());

        Long userId = listingsService.getListingById(event.getListingId()).getUserId();
        ListingNotificationEvent notificationEvent = ListingNotificationEvent.builder()
                .userId(userId)
                .listingId(event.getListingId())
                .message("Listing #" + event.getListingId() + " blocked for booking #" + event.getBookingId())
                .eventType("listing-blocked")
                .build();

        kafkaTemplate.send(notificationSendRequestTopic, userId.toString(), notificationEvent);
    }

    @KafkaListener(topics = "${event.topic.listing.availability.unblock}", groupId = "${spring.kafka.consumer.group-id}")
    public void handlerListingUnblockEvent(@Payload ListingUnblockEvent event) {
        log.info("Received listing unblock request for booking ID: {}", event.getBookingId());
        listingAvailabilityService.unblockAvailability(event.getListingId(), event.getStartDate(), event.getEndDate());
        log.info("Successfully unblocked availability for listingId={} from {} to {}",
                event.getListingId(), event.getStartDate(), event.getEndDate());

        Long userId = listingsService.getListingById(event.getListingId()).getUserId();
        ListingNotificationEvent notificationEvent = ListingNotificationEvent.builder()
                .userId(userId)
                .listingId(event.getListingId())
                .message("Listing #" + event.getListingId() + " unblocked for booking #" + event.getBookingId())
                .eventType("listing-unblocked")
                .build();
        kafkaTemplate.send(notificationSendRequestTopic, userId.toString(), notificationEvent);
    }

    @KafkaListener(topics = "${event.topic.listing.rating.updated}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleReviewUpdated(@Payload ReviewEvent event) {
        log.info("Received review event for rating update: {}", event);
        listingsService.updateListingRating(event.getListingId(), event.getAverageRating());
        log.info("Updated listing rating for listing ID: {}", event.getListingId());

        Long userId = listingsService.getListingById(event.getListingId()).getUserId();
        ListingNotificationEvent notificationEvent = ListingNotificationEvent.builder()
                .userId(userId)
                .listingId(event.getListingId())
                .message("Listing #" + event.getListingId() + " rating updated to " + event.getAverageRating())
                .eventType("listing-rating-updated")
                .build();
        kafkaTemplate.send(notificationSendRequestTopic, userId.toString(), notificationEvent);
    }
}