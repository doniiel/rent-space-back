package com.rentspace.listingservice.service;

import com.rentspace.listingservice.dto.ListingAvailabilityDto;
import com.rentspace.listingservice.dto.ListingAvailabilityRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ListingAvailabilityService {
    List<ListingAvailabilityDto> getAvailabilityByListing(Long listingId); // Getting the listingId availability
    ListingAvailabilityDto setAvailability(Long listingId, ListingAvailabilityRequest request); // Availability Setup (owner of the ad)
    boolean isAvailable(Long listingId, LocalDateTime startDate, LocalDateTime endDate); // Check availability for booking
    ListingAvailabilityDto bookAvailability(Long listingId, LocalDateTime startDate, LocalDateTime endDate); // Reservation (setting available = false)
    void cancelBooking(Long listingId, LocalDateTime startDate, LocalDateTime endDate); // Cancel Booking
    void blockAvailability(Long listingId, LocalDateTime startDate, LocalDateTime endDate);
    void unblockAvailability(Long listingId, LocalDateTime startDate, LocalDateTime endDate);
}
