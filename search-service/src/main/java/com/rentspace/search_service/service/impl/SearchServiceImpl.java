package com.rentspace.search_service.service.impl;

import com.rentspace.search_service.model.Listing;
import com.rentspace.search_service.repository.ListingRepository;
import com.rentspace.search_service.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
    private final ListingRepository listingRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public List<Listing> searchListings(String query) {
        return listingRepository.findByTitleContainingOrDescriptionContainingOrCityContaining(query, query, query);
    }

    @Override
    public Page<Listing> searchListings(String city, String type, Double priceMin, Double priceMax, Integer maxGuests, Pageable pageable) {
        return listingRepository.findByFilters(city, type, priceMin, priceMax, maxGuests, pageable);
    }
}