package com.rentspace.search_service.controller;

import com.rentspace.search_service.model.Listing;
import com.rentspace.search_service.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/search")
public class SearchController {
    private final SearchService searchService;

    @GetMapping
    public List<Listing> search(@RequestParam String query) {
        return searchService.searchListings(query);
    }

    @GetMapping("/listings")
    public Page<Listing> searchListings(@RequestParam(required = false) String city,
                                        @RequestParam(required = false) String type,
                                        @RequestParam(required = false) Double minPrice,
                                        @RequestParam(required = false) Double maxPrice,
                                        @RequestParam(required = false) Integer minGuests,
                                        @RequestParam(required = false) String amenities, @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return searchService.searchListings(city, type, minPrice, maxPrice, minGuests, amenities, pageable);
    }
}