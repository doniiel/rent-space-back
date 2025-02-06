package com.rentspace.listingservice.service.impl;

import com.rentspace.listingservice.dto.ListingAvailabilityDto;
import com.rentspace.listingservice.service.ListingAvailabilityService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ListingAvailabilityServiceImpl implements ListingAvailabilityService {
    @Override
    public List<ListingAvailabilityDto> getAvailabilityByListing(Long listingId) {
        return null;
    }

    @Override
    public ListingAvailabilityDto setAvailability(Long listingId, LocalDate startDate, LocalDate endDate, boolean available) {
        return null;
    }

    @Override
    public void updateAvailability(Long listingId, boolean available) {

    }
}
