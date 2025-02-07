package com.rentspace.listingservice.service;

import com.rentspace.listingservice.dto.ListingCreateRequest;
import com.rentspace.listingservice.dto.ListingDto;
import com.rentspace.listingservice.dto.ListingUpdateRequest;

import java.util.List;

public interface ListingsService {
    ListingDto createListing(ListingCreateRequest request);
    ListingDto getListingById(Long listingId);
    List<ListingDto> getAllListings();
    List<ListingDto> getAllUserListings(Long userId);
    ListingDto updateListing(Long listingId, ListingUpdateRequest request);
    void deleteListing(Long listingId);
}
