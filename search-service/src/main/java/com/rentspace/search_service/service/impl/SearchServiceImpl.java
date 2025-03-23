package com.rentspace.search_service.service.impl;

import com.rentspace.search_service.model.Listing;
import com.rentspace.search_service.repository.ListingRepository;
import com.rentspace.search_service.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
    private final ListingRepository listingRepository;

    public List<Listing> searchListings(String query) {
        return listingRepository.findByTitleContainingOrDescriptionContainingOrCityContaining(query, query, query);
    }

    public Page<Listing> searchListings(String city, String type, Double minPrice, Double maxPrice, Integer minGuests, String amenities, Pageable pageable) {
        List<String> amenitiesList = amenities != null ? List.of(amenities.split(",")) : null;

        return listingRepository.findByFilters(city, type, minPrice != null ? minPrice : 0.0,
                maxPrice != null ? maxPrice : Double.MAX_VALUE,
                minGuests, amenitiesList, pageable);
    }

}