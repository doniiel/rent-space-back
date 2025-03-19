package com.rentspace.listingservice.service.impl;

import com.rentspace.listingservice.entity.Listing;
import com.rentspace.listingservice.exception.ListingNotFoundException;
import com.rentspace.listingservice.repository.ListingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ListingBaseService {
    private final ListingsRepository listingsRepository;

    public Listing getListingById(Long id) {
        return listingsRepository.findById(id).orElseThrow(
                () -> new ListingNotFoundException("Listing", "listingId", id));
    }
}