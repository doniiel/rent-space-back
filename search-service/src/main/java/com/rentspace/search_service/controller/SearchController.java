package com.rentspace.search_service.controller;

import com.rentspace.search_service.model.Listing;
import com.rentspace.search_service.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/quick")
    public List<Listing> quickSearch(@RequestParam String query) {
        return searchService.searchListings(query);
    }

    @GetMapping("/listings")
    public ResponseEntity<Page<Listing>> searchListings(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Double priceMin,
            @RequestParam(required = false) Double priceMax,
            @RequestParam(required = false) Integer maxGuests,
            Pageable pageable) {
        Page<Listing> listings = searchService.searchListings(city, type, priceMin, priceMax, maxGuests, pageable);
        return ResponseEntity.ok(listings);
    }
}