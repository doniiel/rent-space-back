package com.rentspace.listingservice.service.impl;

import com.rentspace.core.exception.ListingNotAvailableException;
import com.rentspace.listingservice.dto.ListingAvailabilityDto;
import com.rentspace.listingservice.dto.ListingAvailabilityRequest;
import com.rentspace.listingservice.entity.Listing;
import com.rentspace.listingservice.entity.ListingAvailability;
import com.rentspace.listingservice.exception.InvalidDateRangeException;
import com.rentspace.listingservice.mapper.ListingAvailabilityMapper;
import com.rentspace.listingservice.repository.ListingAvailabilityRepository;
import com.rentspace.listingservice.repository.ListingsRepository;
import com.rentspace.listingservice.service.ListingAvailabilityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ListingAvailabilityServiceImpl implements ListingAvailabilityService {
    private final ListingAvailabilityRepository availabilityRepository;
    private final ListingsRepository listingsRepository;
    private final ListingAvailabilityMapper mapper;
    private final ListingBaseService listingBaseService;

    @Override
    @Transactional(readOnly = true)
    public List<ListingAvailabilityDto> getAvailabilityByListing(Long listingId) {
        log.debug("Fetching availabilities for listingId: {}", listingId);
        List<ListingAvailability> availabilities = availabilityRepository.findByListingId(listingId);
        validateAvailabilityExists(availabilities, listingId);
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
        ListingAvailability availability = createAndSaveAvailability(listing, request);
        log.info("Availability set for listingId: {}", listingId);
        return mapper.toDto(availability);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isAvailable(Long listingId, LocalDateTime startDate , LocalDateTime endDate) {
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
        saveBlockedAvailability(listing, startDate, endDate);
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

    private ListingAvailability createAndSaveAvailability(Listing listing, ListingAvailabilityRequest request) {
        ListingAvailability availability = ListingAvailability.builder()
                .listing(listing)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .available(request.isAvailable())
                .build();
        return availabilityRepository.save(availability);
    }

    private void saveBlockedAvailability(Listing listing, LocalDateTime startDate, LocalDateTime endDate) {
        ListingAvailability availability = ListingAvailability.builder()
                .listing(listing)
                .startDate(startDate)
                .endDate(endDate)
                .available(false)
                .build();
        availabilityRepository.save(availability);
    }

    private void logAvailabilityUnblocked(int deletedCount, Long listingId, LocalDateTime startDate, LocalDateTime endDate) {
        if (deletedCount == 0) {
            log.warn("No availability unblocked for listing ID: {} from {} to {}", listingId, startDate, endDate);
        } else {
            log.info("Unblocked {} availability records for listing ID: {} from {} to {}", deletedCount, listingId, startDate, endDate);
        }
    }
}
