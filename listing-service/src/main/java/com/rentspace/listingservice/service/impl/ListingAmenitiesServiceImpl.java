package com.rentspace.listingservice.service.impl;

import com.rentspace.listingservice.dto.ListingAmenitiesDto;
import com.rentspace.listingservice.entity.Listing;
import com.rentspace.listingservice.entity.ListingAmenities;
import com.rentspace.listingservice.enums.AmenityType;
import com.rentspace.listingservice.exception.ListingNotFoundException;
import com.rentspace.listingservice.mapper.ListingAmenitiesMapper;
import com.rentspace.listingservice.repository.ListingAmenitiesRepository;
import com.rentspace.listingservice.service.ListingAmenitiesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class ListingAmenitiesServiceImpl implements ListingAmenitiesService {

    private final ListingAmenitiesRepository repository;
    private final ListingBaseService listingBaseService;
    private final ListingAmenitiesMapper mapper;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "listingAmenities", key = "#listingId", unless = "#result == null")
    public ListingAmenitiesDto getAmenitiesByListing(Long listingId) {
        log.debug("Fetching amenities for listing ID: {}", listingId);
        ListingAmenities amenities = repository.findByListingId(listingId)
                .orElseThrow(() -> new ListingNotFoundException("Listing", "listingId", listingId));
        return mapper.toDto(amenities);
    }

    @Override
    @Transactional
    @CachePut(value = "listingAmenities", key = "#listingId")
    public ListingAmenitiesDto addAmenitiesToListing(Long listingId, Set<AmenityType> amenityTypes) {
        log.debug("Adding amenities to listing ID: {}", listingId);
        ListingAmenities amenities = getOrCreateAmenities(listingId);
        addAmenities(amenities, validateAmenityTypes(amenityTypes));
        repository.save(amenities);
        log.info("Added amenities to listing ID: {}", listingId);
        return mapper.toDto(amenities);
    }

    @Override
    @Transactional
    @CachePut(value = "listingAmenities", key = "#listingId")
    public ListingAmenitiesDto updateAmenitiesForListing(Long listingId, Set<AmenityType> amenityTypes) {
        log.debug("Updating amenities for listing ID: {}", listingId);
        ListingAmenities amenities = repository.findByListingId(listingId)
                .orElseThrow(() -> new ListingNotFoundException("Listing", "listingId", listingId));
        amenities.setAmenityTypes(validateAmenityTypes(amenityTypes));
        repository.save(amenities);
        log.info("Updated amenities for listing ID: {}", listingId);
        return mapper.toDto(amenities);
    }

    @Override
    @Transactional
    @CachePut(value = "listingAmenities", key = "#listingId")
    public ListingAmenitiesDto removeAmenityFromListing(Long listingId, AmenityType amenityType) {
        log.debug("Removing amenity {} from listing ID: {}", amenityType, listingId);
        ListingAmenities amenities = repository.findByListingId(listingId)
                .orElseThrow(() -> new ListingNotFoundException("Listing", "listingId", listingId));
        removeAmenity(amenities, validateAmenityType(amenityType));
        repository.save(amenities);
        log.info("Removed amenity {} from listing ID: {}", amenityType, listingId);
        return mapper.toDto(amenities);
    }

    @Override
    @Transactional
    @CachePut(value = "listingAmenities", key = "#listingId")
    public void removeAllAmenitiesFromListing(Long listingId) {
        log.debug("Removing all amenities from listing ID: {}", listingId);
        ListingAmenities amenities = repository.findByListingId(listingId)
                .orElseThrow(() -> new ListingNotFoundException("Listing", "listingId", listingId));
        amenities.setAmenityTypes(Collections.emptySet());
        repository.save(amenities);
        log.info("All amenities removed from listing ID: {}", listingId);
    }

    private ListingAmenities getOrCreateAmenities(Long listingId) {
        return repository.findByListingId(listingId)
                .orElseGet(() -> {
                    Listing listing = listingBaseService.getListingById(listingId);
                    return ListingAmenities.builder()
                            .listing(listing)
                            .amenityTypes(new HashSet<>())
                            .build();
                });
    }

    private Set<AmenityType> validateAmenityTypes(Set<AmenityType> amenityTypes) {
        if (CollectionUtils.isEmpty(amenityTypes)) {
            throw new IllegalArgumentException("Amenity types cannot be empty");
        }
        return amenityTypes;
    }

    private AmenityType validateAmenityType(AmenityType amenityType) {
        if (amenityType == null) {
            throw new IllegalArgumentException("Amenity type cannot be null");
        }
        return amenityType;
    }

    private void addAmenities(ListingAmenities amenities, Set<AmenityType> amenityTypes) {
        Set<AmenityType> currentAmenities = Optional.ofNullable(amenities.getAmenityTypes())
                .orElseGet(HashSet::new);
        currentAmenities.addAll(amenityTypes);
        amenities.setAmenityTypes(currentAmenities);
    }

    private void removeAmenity(ListingAmenities amenities, AmenityType amenityType) {
        Set<AmenityType> currentAmenities = amenities.getAmenityTypes();
        if (!CollectionUtils.isEmpty(currentAmenities)) {
            currentAmenities.remove(amenityType);
            amenities.setAmenityTypes(currentAmenities);
        }
    }
}