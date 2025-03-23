package com.rentspace.search_service.service;

import com.rentspace.search_service.model.Listing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SearchService {
    List<Listing> searchListings(String query);
    Page<Listing> searchListings(String city, String type, Double minPrice, Double maxPrice, Integer minGuests, String amenities, Pageable pageable);
}
