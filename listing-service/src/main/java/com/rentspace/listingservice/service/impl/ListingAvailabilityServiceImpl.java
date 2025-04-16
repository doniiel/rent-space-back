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
        Listing listing = fetchListing(listingId);
        validateRequest(request);

        ensureNoOverlappingPeriods(listingId, request.getStartDate(), request.getEndDate(), null);

        ListingAvailability newAvailability = buildNewAvailability(listing, request);
        ListingAvailability savedAvailability = availabilityRepository.save(newAvailability);
        log.info("Successfully set availability for listingId: {}", listingId);
        return mapper.toDto(savedAvailability);
    }

    @Override
    @Transactional
    public ListingAvailabilityDto updateAvailability(Long listingId, Long availabilityId, ListingAvailabilityRequest request) {
        log.debug("Updating availability for listingId: {} with request: {}", listingId, request);
        Listing listing = fetchListing(listingId);
        validateRequest(request);
        ensureListingIdMatches(request.getListingId(), listingId);

        ListingAvailability availability = findAvailabilityOrThrow(availabilityId, listingId);
        ensureListingOwnership(availability, listingId);

        ensureNoOverlappingPeriods(listingId, request.getStartDate(), request.getEndDate(), availabilityId);

        updateAvailabilityFields(availability, request);
        ListingAvailability updatedAvailability = availabilityRepository.save(availability);
        log.info("Successfully updated availability record with ID: {} for listingId: {}", availabilityId, listingId);
        return mapper.toDto(updatedAvailability);
    }

    @Override
    @Transactional
    public void deleteAvailability(Long listingId, Long availabilityId) {
        Listing listing = fetchListing(listingId);

        ListingAvailability availability = findAvailabilityOrThrow(availabilityId, listingId);
        ensureListingOwnership(availability, listingId);

        availabilityRepository.delete(availability);
        log.info("Successfully deleted availability record with ID: {} for listingId: {}", availabilityId, listingId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isAvailable(Long listingId, LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Checking availability for listingId={} from {} to {}", listingId, startDate, endDate);
        validateDateRange(startDate, endDate);
        return !hasAvailabilityInPeriod(listingId, startDate, endDate);
    }

    @Override
    @Transactional
    public void blockAvailability(Long listingId, LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("Blocking availability for listingId={} from {} to {}", listingId, startDate, endDate);
        Listing listing = fetchListing(listingId);
        validateBlockRequest(listingId, startDate, endDate);

        ensureNoOverlappingPeriods(listingId, startDate, endDate, null);

        ListingAvailability availability = createBlockedAvailability(listing, startDate, endDate);
        availabilityRepository.save(availability);
        log.info("Successfully blocked availability for listingId={} from {} to {}", listingId, startDate, endDate);
    }

    @Override
    @Transactional
    public void unblockAvailability(Long listingId, LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("Unblocking availability for listingId={} from {} to {}", listingId, startDate, endDate);
        validateDateRange(startDate, endDate);

        int deletedCount = availabilityRepository.deleteByListingIdAndStartDateAndEndDateAndAvailableFalse(listingId, startDate, endDate);
        logUnblockResult(deletedCount, listingId, startDate, endDate);
    }

    private Listing fetchListing(Long listingId) {
        return listingBaseService.getListingById(listingId);
    }

    private void validateRequest(ListingAvailabilityRequest request) {
        if (request == null) {
            throw new InvalidDateRangeException("Availability request cannot be null", "");
        }
        validateDateRange(request.getStartDate(), request.getEndDate());
    }

    private void validateDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
            throw new InvalidDateRangeException(startDate != null ? startDate.toString() : "null",
                    endDate != null ? endDate.toString() : "null");
        }
    }

    private void validateBlockRequest(Long listingId, LocalDateTime startDate, LocalDateTime endDate) {
        validateDateRange(startDate, endDate);
        if (!isAvailable(listingId, startDate, endDate)) {
            throw new ListingNotAvailableException(listingId, startDate.toString(), endDate.toString());
        }
    }

    private void ensureListingIdMatches(Long requestListingId, Long listingId) {
        if (!listingId.equals(requestListingId)) {
            log.warn("Listing ID mismatch: path variable listingId={} does not match request listingId={}", listingId, requestListingId);
            throw new IllegalArgumentException("Listing ID in path must match the listing ID in the request");
        }
    }

    private ListingAvailability findAvailabilityOrThrow(Long availabilityId, Long listingId) {
        Optional<ListingAvailability> availability = availabilityRepository.findById(availabilityId);
        if (availability.isEmpty()) {
            throw new ListingNotAvailableException(listingId, "Availability record with ID " + availabilityId + " not found", "");
        }
        return availability.get();
    }

    private void ensureListingOwnership(ListingAvailability availability, Long listingId) {
        if (!availability.getListing().getId().equals(listingId)) {
            log.warn("Availability record with ID: {} does not belong to listing ID: {}", availability.getId(), listingId);
            throw new IllegalArgumentException("Availability record does not belong to the specified listing");
        }
    }

    private void ensureNoOverlappingPeriods(Long listingId, LocalDateTime startDate, LocalDateTime endDate, Long excludeId) {
        if (hasOverlappingPeriods(listingId, startDate, endDate, excludeId)) {
            log.warn("Period from {} to {} overlaps with an existing record for listing ID: {}", startDate, endDate, listingId);
            throw new ListingNotAvailableException(listingId, startDate.toString(), endDate.toString());
        }
    }

    private boolean hasOverlappingPeriods(Long listingId, LocalDateTime startDate, LocalDateTime endDate, Long excludeId) {
        log.debug("Checking for overlapping periods for listing ID: {} from {} to {}, excluding ID={}", listingId, startDate, endDate, excludeId);
        boolean hasOverlap = excludeId != null
                ? availabilityRepository.existsByListingIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualAndIdNot(listingId, startDate, endDate, excludeId)
                : availabilityRepository.existsByListingIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(listingId, startDate, endDate);
        log.debug("Overlap check result for listing ID: {} from {} to {}: {}", listingId, startDate, endDate, hasOverlap);
        return hasOverlap;
    }

    private boolean hasAvailabilityInPeriod(Long listingId, LocalDateTime startDate, LocalDateTime endDate) {
        return availabilityRepository.existsByListingIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(listingId, startDate, endDate);
    }

    private ListingAvailability buildNewAvailability(Listing listing, ListingAvailabilityRequest request) {
        return ListingAvailability.builder()
                .listing(listing)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .available(request.isAvailable())
                .build();
    }

    private ListingAvailability createBlockedAvailability(Listing listing, LocalDateTime startDate, LocalDateTime endDate) {
        return ListingAvailability.builder()
                .listing(listing)
                .startDate(startDate)
                .endDate(endDate)
                .available(false)
                .build();
    }

    private void updateAvailabilityFields(ListingAvailability availability, ListingAvailabilityRequest request) {
        availability.setStartDate(request.getStartDate());
        availability.setEndDate(request.getEndDate());
        availability.setAvailable(request.isAvailable());
    }

    private void logUnblockResult(int deletedCount, Long listingId, LocalDateTime startDate, LocalDateTime endDate) {
        if (deletedCount == 0) {
            log.warn("No availability unblocked for listing ID: {} from {} to {}", listingId, startDate, endDate);
        } else {
            log.info("Unblocked {} availability records for listing ID: {} from {} to {}", deletedCount, listingId, startDate, endDate);
        }
    }
}