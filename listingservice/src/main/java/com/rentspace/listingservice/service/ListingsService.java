package com.rentspace.listingservice.service;

import com.rentspace.listingservice.dto.ListingsDto;

public interface ListingsService {


    ListingsDto getListingById(Long listingId);

    String createListing(ListingsDto listingsDto);

    ListingsDto updateListing(ListingsDto listingsDto);

    String deleteListing(Long listingId);
}
