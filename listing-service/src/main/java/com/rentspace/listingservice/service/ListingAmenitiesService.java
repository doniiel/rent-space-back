package com.rentspace.listingservice.service;

import com.rentspace.listingservice.dto.ListingAmenitiesDto;
import com.rentspace.listingservice.entity.ListingAmenities;
import com.rentspace.listingservice.enums.AmenityType;

import java.util.List;

public interface ListingAmenitiesService {
    ListingAmenitiesDto addAmenityToListing(Long listingId, AmenityType amenityType);
    List<ListingAmenitiesDto> getAmenitiesByListing(Long listingId);
    void removeAmenityFromListing(Long listingId, Long amenityId);
}
