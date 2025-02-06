package com.rentspace.listingservice.service;

import com.rentspace.listingservice.dto.ListingDto;

import java.util.List;

public interface ListingsService {
    ListingDto createListing(ListingDto listingDto);
    ListingDto getListingById(Long listingId);
    List<ListingDto> getAllListings();
    ListingDto updateListing(Long id, ListingDto listingDto);
    String deleteListing(Long listingId);
}
