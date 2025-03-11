package com.rentspace.listingservice.service;

import com.rentspace.listingservice.dto.ListingAvailabilityDto;
import com.rentspace.listingservice.dto.ListingAvailabilityRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface ListingAvailabilityService {
    List<ListingAvailabilityDto> getAvailabilityByListing(Long listingId);
    ListingAvailabilityDto setAvailability(Long listingId, ListingAvailabilityRequest request);
    boolean isAvailable(Long listingId, LocalDateTime startDate, LocalDateTime endDate);
    void blockAvailability(Long listingId, LocalDateTime startDate, LocalDateTime endDate);
    void unblockAvailability(Long listingId, LocalDateTime startDate, LocalDateTime endDate);
}
