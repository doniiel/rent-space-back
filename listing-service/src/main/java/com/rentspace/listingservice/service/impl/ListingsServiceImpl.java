package com.rentspace.listingservice.service.impl;

import com.rentspace.listingservice.dto.ListingCreateRequest;
import com.rentspace.listingservice.dto.ListingDto;
import com.rentspace.listingservice.dto.ListingUpdateRequest;
import com.rentspace.listingservice.entity.Listing;
import com.rentspace.listingservice.exception.InvalidListingDataException;
import com.rentspace.listingservice.mapper.ListingMapper;
import com.rentspace.listingservice.repository.ListingsRepository;
import com.rentspace.listingservice.service.ListingPhotoService;
import com.rentspace.listingservice.service.ListingsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ListingsServiceImpl implements ListingsService {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ListingsRepository listingsRepository;
    private final ListingMapper listingMapper;
    private final ListingPhotoService listingPhotoService;
    private final ListingBaseService listingBaseService;

    @Value("${event.topic.listing.created}")
    private String listingCreatedTopic;

    @Value("${event.topic.listing.updated}")
    private String listingUpdatedTopic;

    @Override
    @Transactional
    public ListingDto createListing(ListingCreateRequest request) {
        log.debug("Creating listing from request: {}", request);
        validateCreateRequest(request);
        Listing listing = createAndSaveListing(request);
        kafkaTemplate.send(listingCreatedTopic, listing.getId().toString(), listing);
        log.info("Listing created with ID: {}", listing.getId());
        return listingMapper.toDto(listing);
    }

    @Override
    @Transactional(readOnly = true)
    public ListingDto getListingById(Long id) {
        log.debug("Fetching listing by ID: {}", id);
        Listing listing = listingBaseService.getListingById(id);
        return listingMapper.toDto(listing);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ListingDto> getAllListings() {
        log.debug("Fetching all listings");
        return listingsRepository.findAll().stream()
                .map(listingMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ListingDto> getAllUserListings(Long userId) {
        log.debug("Fetching listings for user ID: {}", userId);
        validateUserId(userId);
        return listingsRepository.findByUserId(userId).stream()
                .map(listingMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public ListingDto updateListing(Long listingId, ListingUpdateRequest request) {
        log.debug("Updating listing ID: {} with request: {}", listingId, request);
        Listing listing = listingBaseService.getListingById(listingId);
        validateUpdateRequest(request);
        updateAndSaveListing(listing, request);
        kafkaTemplate.send(listingUpdatedTopic, listing.getId().toString(), listing);
        log.info("Listing ID: {} updated", listingId);
        return listingMapper.toDto(listing);
    }

    @Override
    @Transactional
    public void deleteListing(Long listingId) {
        log.debug("Deleting listing ID: {}", listingId);
        Listing listing = listingBaseService.getListingById(listingId);
        listingPhotoService.deletePhotos(listing.getPhotos());
        listingsRepository.delete(listing);
        log.info("Listing ID: {} deleted", listingId);
    }

    private void validateCreateRequest(ListingCreateRequest request) {
        if (request == null) {
            throw new InvalidListingDataException("Listing create request cannot be null");
        }
    }

    private void validateUpdateRequest(ListingUpdateRequest request) {
        if (request == null) {
            throw new InvalidListingDataException("Listing update request cannot be null");
        }
    }

    private void validateUserId(Long userId) {
        if (userId == null || userId <= 0) {
            throw new InvalidListingDataException("User ID must be a positive number");
        }
    }

    private Listing createAndSaveListing(ListingCreateRequest request) {
        Listing listing = listingMapper.toEntity(request);
        return listingsRepository.save(listing);
    }

    private void updateAndSaveListing(Listing listing, ListingUpdateRequest request) {
        listingMapper.updateListingFromRequest(request, listing);
        listingsRepository.save(listing);
    }
}