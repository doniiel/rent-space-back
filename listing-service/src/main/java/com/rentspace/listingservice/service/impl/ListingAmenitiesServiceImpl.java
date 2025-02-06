package com.rentspace.listingservice.service.impl;

import com.rentspace.listingservice.dto.ListingAmenitiesDto;
import com.rentspace.listingservice.enums.AmenityType;
import com.rentspace.listingservice.service.ListingAmenitiesService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListingAmenitiesServiceImpl implements ListingAmenitiesService {
    @Override
    public ListingAmenitiesDto addAmenityToListing(Long listingId, AmenityType amenityType) {
        return null;
    }

    @Override
    public List<ListingAmenitiesDto> getAmenitiesByListing(Long listingId) {
        return List.of();
    }

    @Override
    public void removeAmenityFromListing(Long listingId, Long amenityId) {

    }
}
