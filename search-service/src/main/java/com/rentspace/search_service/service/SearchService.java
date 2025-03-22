package com.rentspace.search_service.service;

import com.rentspace.search_service.model.Listing;

import java.util.List;

public interface SearchService {
    List<Listing> searchListings(String query);
}
