package com.rentspace.listingservice.service;

import com.rentspace.listingservice.dto.ListingDto;
import com.rentspace.listingservice.entity.Listing;

public interface ListingsService {


    ListingDto getListingById(Long listingId);

    ListingDto createListing(ListingDto listingDto);

    ListingDto updateListing(ListingDto listingDto);

    String deleteListing(Long listingId);
}
