package com.rentspace.listingservice.service;

import com.rentspace.listingservice.dto.ListingAmenitiesDto;
import com.rentspace.listingservice.enums.AmenityType;

import java.util.List;
import java.util.Set;

public interface ListingAmenitiesService {
    ListingAmenitiesDto getAmenitiesByListing(Long listingId);
    ListingAmenitiesDto addAmenitiesToListing(Long listingId, Set<AmenityType> amenityTypes);
    ListingAmenitiesDto updateAmenitiesForListing(Long listingId, Set<AmenityType> amenityTypes);
    ListingAmenitiesDto removeAmenityFromListing(Long listingId, AmenityType amenityType);
    void removeAllAmenitiesFromListing(Long listingId);
}
