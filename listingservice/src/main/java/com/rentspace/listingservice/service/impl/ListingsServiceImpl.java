package com.rentspace.listingservice.service.impl;

import com.rentspace.listingservice.repository.ListingsRepository;
import com.rentspace.listingservice.repository.PhotosRepository;
import com.rentspace.listingservice.service.ListingsService;
import com.rentspace.listingservice.service.PhotosService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ListingsServiceImpl implements ListingsService {

    private final ListingsRepository listingsRepository;
    private final PhotosRepository photosRepository;

}
