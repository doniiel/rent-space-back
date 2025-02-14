package com.rentspace.listingservice.service.impl;

import com.rentspace.listingservice.dto.ListingAmenitiesDto;
import com.rentspace.listingservice.entity.ListingAmenities;
import com.rentspace.listingservice.enums.AmenityType;
import com.rentspace.listingservice.exception.ListingNotFoundException;
import com.rentspace.listingservice.mapper.ListingAmenitiesMapper;
import com.rentspace.listingservice.repository.ListingAmenitiesRepository;
import com.rentspace.listingservice.repository.ListingsRepository;
import com.rentspace.listingservice.service.ListingAmenitiesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ListingAmenitiesServiceImpl implements ListingAmenitiesService {
    private final ListingAmenitiesRepository repository;
    private final ListingsRepository listingsRepository;
    private final ListingAmenitiesMapper mapper;

    @Override
    public ListingAmenitiesDto getAmenitiesByListing(Long listingId) {
        ListingAmenities amenities = repository.findByListingId(listingId).orElseThrow(
                () -> new ListingNotFoundException("Listing", "listingId", listingId));
        return mapper.toDto(amenities);
    }

    @Override
    public ListingAmenitiesDto addAmenitiesToListing(Long listingId, Set<AmenityType> amenityTypes) {
        ListingAmenities amenities = repository.findByListingId(listingId)
                .orElseGet(() -> {
                    var newAmenities = new ListingAmenities();
                    newAmenities.setListing(listingsRepository.findById(listingId)
                            .orElseThrow(() -> new ListingNotFoundException("Listing", "listingId", listingId)));
                    newAmenities.setAmenityTypes(new HashSet<>());
                    return newAmenities;
                });

        Set<AmenityType> currAmenities = amenities.getAmenityTypes();
        if (currAmenities == null) {
            currAmenities = new HashSet<>();
        }
        currAmenities.addAll(amenityTypes);
        amenities.setAmenityTypes(currAmenities);
        repository.save(amenities);
        return mapper.toDto(amenities);
    }

    @Override
    public ListingAmenitiesDto updateAmenitiesForListing(Long listingId, Set<AmenityType> amenityTypes) {
        ListingAmenities amenities = repository.findByListingId(listingId)
                .orElseThrow(() -> new ListingNotFoundException("Listing", "listingId", listingId));
        amenities.setAmenityTypes(amenityTypes);
        repository.save(amenities);
        return mapper.toDto(amenities);
    }

    @Override
    public ListingAmenitiesDto removeAmenityFromListing(Long listingId, AmenityType amenityType) {
        ListingAmenities amenities = repository.findByListingId(listingId)
                .orElseThrow(() -> new ListingNotFoundException("Listing", "listingId", listingId));
        Set<AmenityType> currentAmenities = amenities.getAmenityTypes();
        if (currentAmenities != null) {
            currentAmenities.remove(amenityType);
            amenities.setAmenityTypes(currentAmenities);
            repository.save(amenities);
        }
        return mapper.toDto(amenities);
    }

    @Override
    public void removeAllAmenitiesFromListing(Long listingId) {
        ListingAmenities amenities = repository.findByListingId(listingId)
                .orElseThrow(() -> new ListingNotFoundException("Listing", "listingId", listingId));
        amenities.setAmenityTypes(Collections.emptySet());
        repository.save(amenities);
    }
}
