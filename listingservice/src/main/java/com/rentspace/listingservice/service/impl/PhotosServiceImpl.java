package com.rentspace.listingservice.service.impl;

import com.rentspace.listingservice.repository.PhotosRepository;
import com.rentspace.listingservice.service.PhotosService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PhotosServiceImpl implements PhotosService {

    private final PhotosRepository repository;

}
