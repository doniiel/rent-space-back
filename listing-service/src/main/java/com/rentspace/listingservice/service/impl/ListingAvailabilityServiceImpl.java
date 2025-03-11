package com.rentspace.listingservice.service.impl;

import com.rentspace.core.exception.ListingNotAvailableException;
import com.rentspace.listingservice.dto.ListingAvailabilityDto;
import com.rentspace.listingservice.dto.ListingAvailabilityRequest;
import com.rentspace.listingservice.entity.Listing;
import com.rentspace.listingservice.entity.ListingAvailability;
import com.rentspace.listingservice.exception.InvalidDateRangeException;
import com.rentspace.listingservice.exception.ListingNotFoundException;
import com.rentspace.listingservice.mapper.ListingAvailabilityMapper;
import com.rentspace.listingservice.repository.ListingAvailabilityRepository;
import com.rentspace.listingservice.repository.ListingsRepository;
import com.rentspace.listingservice.service.ListingAvailabilityService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ListingAvailabilityServiceImpl implements ListingAvailabilityService {
    private final ListingAvailabilityRepository availabilityRepository;
    private final ListingsRepository listingsRepository;
    private final ListingAvailabilityMapper mapper;

    @Override
    public List<ListingAvailabilityDto> getAvailabilityByListing(Long listingId) {
        log.info("Getting availability for listingId: {}", listingId);

        List<ListingAvailability> availabilities = availabilityRepository.findByListingId(listingId);
        if (availabilities.isEmpty()) {
            log.warn("No availability found for listingId: {}", listingId);
            throw new ListingNotFoundException("Listing", "listingId", listingId);
        }

        return availabilities.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ListingAvailabilityDto setAvailability(Long listingId, ListingAvailabilityRequest request) {
        log.info("Set availability for listingId={} from {} to {} (available={})",
                listingId, request.getStartDate(), request.getEndDate(), request.isAvailable());

        var listing = getListingById(listingId);

        ListingAvailability availability = createAvailability(
                listing, request.getStartDate(), request.getEndDate(), request.isAvailable());
        availability = availabilityRepository.save(availability);

        return mapper.toDto(availability);
    }
    @Override
    public boolean isAvailable(Long listingId, LocalDateTime startDate , LocalDateTime endDate) {
        log.info("Checking availability for listingId={} from {} to {}", listingId, startDate, endDate);
        return !availabilityRepository.existsByListingIdAndAvailableTrueAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                listingId, endDate, startDate);
    }

    @Override
    public void blockAvailability(Long listingId, LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Blocking availability for listingId={} from {} to {}", listingId, startDate, endDate);

        validateDateRange(startDate, endDate);
        checkOverlap(listingId, startDate, endDate);
        if (!isAvailable(listingId, startDate, endDate)) {
            throw new ListingNotAvailableException(listingId, startDate.toString(), endDate.toString());
        }

        var listingAvailable = getListingById(listingId);
        ListingAvailability availability = createAvailability(listingAvailable, startDate, endDate, false);
        availabilityRepository.save(availability);

        log.info("Successfully blocked availability for listingId={} from {} to {}", listingId, startDate, endDate);
    }

    @Override
    public void unblockAvailability(Long listingId, LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Unblocking availability for listingId={} from {} to {}", listingId, startDate, endDate);

        int deletedCount = availabilityRepository.deleteByListingIdAndStartDateAndEndDateAndAvailableFalse(
                listingId, startDate, endDate);
        if (deletedCount == 0) {
            log.warn("No booking found to unblock for listingId={} from {} to {}", listingId, startDate, endDate);
        } else {
            log.info("Successfully unblocked availability for listingId={} from {} to {}", listingId, startDate, endDate);
        }
    }

    private ListingAvailability createAvailability(Listing listing, LocalDateTime startDate, LocalDateTime endDate, boolean available) {
        return ListingAvailability.builder()
                .listing(listing)
                .startDate(startDate)
                .endDate(endDate)
                .available(available)
                .build();
    }

    private Listing getListingById(Long listingId) {
        return listingsRepository.findById(listingId).orElseThrow(
                () -> new ListingNotFoundException("Listing", "listingId", listingId));
    }

    private void validateDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate.isAfter(endDate)) {
            throw new InvalidDateRangeException(startDate.toString(), endDate.toString());
        }
    }

    private void checkOverlap(Long listingId, LocalDateTime startDate, LocalDateTime endDate) {
        if (availabilityRepository.existsByListingIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                listingId, startDate, endDate)) {
            throw new ListingNotAvailableException(listingId, startDate.toString(), endDate.toString());
        }
    }
}
