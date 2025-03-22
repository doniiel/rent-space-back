package com.rentspace.search_service.controller;

import com.rentspace.search_service.model.Listing;
import com.rentspace.search_service.service.SearchService;
import lombok.RequiredArgsConstructor;
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
}
