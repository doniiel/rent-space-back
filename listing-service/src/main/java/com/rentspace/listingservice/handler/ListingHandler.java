package com.rentspace.listingservice.handler;

import com.rentspace.core.enums.ListingStatus;
import com.rentspace.core.event.ListingAvailabilityEvent;
import com.rentspace.core.event.ListingAvailableResponse;
import com.rentspace.core.event.ListingUnblockEvent;
import com.rentspace.listingservice.service.ListingAvailabilityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ListingHandler {
    private final ListingAvailabilityService service;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    @Value("${event.topic.listing.availability.response}")
    private String responseTopic;

    @KafkaListener(topics = "${event.topic.listing.availability.request}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleListingAvailabilityEvent(@Payload ListingAvailabilityEvent event) {
        log.info("Received listing availability event for booking ID: {}", event.getBookingId());

        boolean isAvailable = service.isAvailable(event.getListingId(), event.getStartDate(), event.getEndDate());

        ListingAvailableResponse response;
        if (isAvailable) {
            service.blockAvailability(event.getListingId(), event.getStartDate(), event.getEndDate());

            response = ListingAvailableResponse.builder()
                    .bookingId(event.getBookingId())
                    .listingId(event.getListingId())
                    .status(ListingStatus.AVAILABLE.name())
                    .build();

            kafkaTemplate.send(responseTopic, response);
            log.info("Sent listing available response for booking ID: {}", event.getBookingId());
        } else {
            response = ListingAvailableResponse.builder()
                    .bookingId(event.getBookingId())
                    .listingId(event.getListingId())
                    .status(ListingStatus.UNAVAILABLE.name())
                    .build();

            kafkaTemplate.send(responseTopic, event.getListingId().toString(), response);
            log.info("Sent listing unavailable response for booking ID: {}", event.getBookingId());
        }
    }

    @KafkaListener(topics = "${event.topic.listing.availability.unblock}", groupId = "${spring.kafka.consumer.group-id}")
    public void handlerListingUnblock(@Payload ListingUnblockEvent event) {
        log.info("Received listing unblock event for booking ID: {}", event.getBookingId());
        try {
            service.unblockAvailability(event.getListingId(), LocalDateTime.parse(event.getStartDate()), LocalDateTime.parse(event.getEndDate()));
            log.info("Successfully unblocked availability for listingId={} from {} to {}", event.getListingId(), event.getStartDate(), event.getEndDate());
        } catch (DateTimeParseException e) {
            log.error("Failed to parse dates for unblock event: bookingId: {}, startDate: {}, endDate: {}",
                    event.getBookingId(), event.getStartDate(), event.getEndDate());
            throw new IllegalArgumentException("Invalid date format in unblock event", e);
        }
    }
}
