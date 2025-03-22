package com.rentspace.search_service.handler;

import com.rentspace.core.event.ListingEvent;
import com.rentspace.search_service.mapper.ListingMapper;
import com.rentspace.search_service.model.Listing;
import com.rentspace.search_service.repository.ListingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchHandler {
    private final ListingRepository listingRepository;
    private final ListingMapper mapper;

    @KafkaListener(topics = "${event.topic.listing.created}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleListingCreated(ListingEvent event) {
        log.info("Received listing created event: {}", event);
        Listing listing = mapper.toEntity(event);
        listingRepository.save(listing);
        log.info("Indexed listing with ID: {}", listing.getId());
    }

    @KafkaListener(topics = "${event.topic.listing.updated}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleListingUpdated(ListingEvent event) {
        log.info("Received listing updated event: {}", event);
        Listing listing = mapper.toEntity(event);
        listingRepository.save(listing);
        log.info("Updated listing with ID: {} in Elasticsearch", listing.getId());
    }

    @KafkaListener(topics = "${event.topic.listing.deleted}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleListingDeleted(ListingEvent event) {
        log.info("Received listing deleted event for listing ID: {}", event.getId());
        listingRepository.deleteById(event.getId());
        log.info("Deleted listing with ID: {} from Elasticsearch", event.getId());
    }
}