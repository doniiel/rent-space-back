package com.rentspace.listingservice.service.impl;

import com.rentspace.core.exception.ListingNotAvailableException;
import com.rentspace.listingservice.dto.ListingAvailabilityDto;
import com.rentspace.listingservice.dto.ListingAvailabilityRequest;
import com.rentspace.listingservice.entity.Listing;
import com.rentspace.listingservice.entity.ListingAvailability;
import com.rentspace.listingservice.exception.InvalidDateRangeException;
import com.rentspace.listingservice.mapper.ListingAvailabilityMapper;
import com.rentspace.listingservice.repository.ListingAvailabilityRepository;
import com.rentspace.listingservice.service.ListingAvailabilityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ListingAvailabilityServiceImpl implements ListingAvailabilityService {
    private final ListingAvailabilityRepository availabilityRepository;
    private final ListingAvailabilityMapper mapper;
    private final ListingBaseService listingBaseService;

    @Override
    @Transactional(readOnly = true)
    public List<ListingAvailabilityDto> getAvailabilityByListing(Long listingId) {
        log.debug("Fetching availabilities for listingId: {}", listingId);
        List<ListingAvailability> availabilities = availabilityRepository.findByListingId(listingId);
        return availabilities.stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public ListingAvailabilityDto setAvailability(Long listingId, ListingAvailabilityRequest request) {
        log.debug("Setting availability for listingId: {} with request: {}", listingId, request);
        Listing listing = listingBaseService.getListingById(listingId);
        validateAvailabilityRequest(request);

        if (hasOverlappingRecords(listingId, request.getStartDate(), request.getEndDate(), null)) {
            log.warn("Cannot set availability for listingId: {} - the period from {} to {} overlaps with an existing record",
                    listingId, request.getStartDate(), request.getEndDate());
            throw new ListingNotAvailableException(listingId, request.getStartDate().toString(), request.getEndDate().toString());
        }

        ListingAvailability availability = ListingAvailability.builder()
                .listing(listing)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .available(request.isAvailable())
                .build();
        availability = availabilityRepository.save(availability);
        log.info("Availability set for listingId: {}", listingId);
        return mapper.toDto(availability);
    }

    @Override
    @Transactional
    public ListingAvailabilityDto updateAvailability(Long listingId, Long availabilityId, ListingAvailabilityRequest request) {
        log.debug("Updating availability for listingId: {} with request: {}", listingId, request);
        Listing listing = listingBaseService.getListingById(listingId);
        validateAvailabilityRequest(request);

        if (!listingId.equals(request.getListingId())) {
            log.warn("Listing ID mismatch: path variable listingId={} does not match request listingId={}", listingId, request.getListingId());
            throw new IllegalArgumentException("Listing ID in path must match the listing ID in the request");
        }

        Optional<ListingAvailability> existingAvailability = availabilityRepository.findById(availabilityId);
        if (existingAvailability.isEmpty()) {
            log.warn("Availability record with ID: {} not found for listingId: {}", availabilityId, listingId);
            throw new ListingNotAvailableException(listingId, "Availability record with ID " + availabilityId + " not found", "");
        }

        ListingAvailability availability = existingAvailability.get();
        if (!availability.getListing().getId().equals(listingId)) {
            log.warn("Availability record with ID: {} does not belong to listingId: {}", availabilityId, listingId);
            throw new IllegalArgumentException("Availability record does not belong to the specified listing");
        }

        if (hasOverlappingRecords(listingId, request.getStartDate(), request.getEndDate(), availability.getId())) {
            log.warn("Cannot update availability for listingId: {} - the period from {} to {} overlaps with another existing record",
                    listingId, request.getStartDate(), request.getEndDate());
            throw new ListingNotAvailableException(listingId, request.getStartDate().toString(), request.getEndDate().toString());
        }

        availability.setStartDate(request.getStartDate());
        availability.setEndDate(request.getEndDate());
        availability.setAvailable(request.isAvailable());
        availability = availabilityRepository.save(availability);
        log.info("Updated availability record with ID: {} for listingId: {}", availability.getId(), listingId);
        return mapper.toDto(availability);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isAvailable(Long listingId, LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Checking availability for listingId={} from {} to {}", listingId, startDate, endDate);
        validateDateRange(startDate, endDate);
        return !availabilityRepository.existsByListingIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                listingId, startDate, endDate);
    }

    @Override
    @Transactional
    public void blockAvailability(Long listingId, LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("Blocking availability for listingId={} from {} to {}", listingId, startDate, endDate);
        Listing listing = listingBaseService.getListingById(listingId);
        validateBlockRequest(listingId, startDate, endDate);

        if (hasOverlappingRecords(listingId, startDate, endDate, null)) {
            log.warn("Cannot block availability for listingId: {} - the period from {} to {} overlaps with an existing record",
                    listingId, startDate, endDate);
            throw new ListingNotAvailableException(listingId, startDate.toString(), endDate.toString());
        }

        ListingAvailability availability = ListingAvailability.builder()
                .listing(listing)
                .startDate(startDate)
                .endDate(endDate)
                .available(false)
                .build();
        availabilityRepository.save(availability);
        log.info("Availability blocked for listingId={} from {} to {}", listingId, startDate, endDate);
    }

    @Override
    @Transactional
    public void unblockAvailability(Long listingId, LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("Unblocking availability for listingId={} from {} to {}", listingId, startDate, endDate);
        validateDateRange(startDate, endDate);

        int deletedCount = availabilityRepository.deleteByListingIdAndStartDateAndEndDateAndAvailableFalse(
                listingId, startDate, endDate);
        logAvailabilityUnblocked(deletedCount, listingId, startDate, endDate);
    }

    private void validateAvailabilityExists(List<ListingAvailability> availabilities, Long listingId) {
        if (availabilities.isEmpty()) {
            log.warn("No availabilities found for listingId: {}", listingId);
            throw new ListingNotAvailableException(listingId, "No availability records found", "");
        }
    }

    private void validateAvailabilityRequest(ListingAvailabilityRequest request) {
        if (request == null) {
            throw new InvalidDateRangeException("Availability request cannot be null", "");
        }
        validateDateRange(request.getStartDate(), request.getEndDate());
    }

    private void validateBlockRequest(Long listingId, LocalDateTime startDate, LocalDateTime endDate) {
        validateDateRange(startDate, endDate);
        if (!isAvailable(listingId, startDate, endDate)) {
            throw new ListingNotAvailableException(listingId, startDate.toString(), endDate.toString());
        }
    }

    private void validateDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
            throw new InvalidDateRangeException(startDate != null ? startDate.toString() : "null",
                    endDate != null ? endDate.toString() : "null");
        }
    }

    private void logAvailabilityUnblocked(int deletedCount, Long listingId, LocalDateTime startDate, LocalDateTime endDate) {
        if (deletedCount == 0) {
            log.warn("No availability unblocked for listing ID: {} from {} to {}", listingId, startDate, endDate);
        } else {
            log.info("Unblocked {} availability records for listing ID: {} from {} to {}", deletedCount, listingId, startDate, endDate);
        }
    }

    private boolean hasOverlappingRecords(Long listingId, LocalDateTime startDate, LocalDateTime endDate, Long excludeId) {
        log.debug("Checking for overlapping records for listingId: {} from {} to {}, excluding id={}",
                listingId, startDate, endDate, excludeId);
        boolean hasOverlap;
        if (excludeId != null) {
            hasOverlap = availabilityRepository.existsByListingIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualAndIdNot(
                    listingId, startDate, endDate, excludeId);
        } else {
            hasOverlap = availabilityRepository.existsByListingIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                    listingId, startDate, endDate);
        }
        log.debug("Overlap check result for listingId: {} from {} to {}: {}", listingId, startDate, endDate, hasOverlap);
        return hasOverlap;
    }
}