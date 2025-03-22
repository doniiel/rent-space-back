package com.rentspace.search_service.service.impl;

import com.rentspace.search_service.model.Listing;
import com.rentspace.search_service.service.SearchService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {
    @Override
    public List<Listing> searchListings(String query) {
        return List.of();
    }
}
