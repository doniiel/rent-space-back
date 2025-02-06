package com.rentspace.listingservice.service;

import com.rentspace.listingservice.dto.ListingAvailabilityDto;
import com.rentspace.listingservice.entity.ListingAvailability;

import java.time.LocalDate;
import java.util.List;

public interface ListingAvailabilityService {
    List<ListingAvailabilityDto> getAvailabilityByListing(Long listingId);
    ListingAvailabilityDto setAvailability(Long listingId, LocalDate startDate, LocalDate endDate, boolean available);
    void updateAvailability(Long listingId, boolean available);
}
