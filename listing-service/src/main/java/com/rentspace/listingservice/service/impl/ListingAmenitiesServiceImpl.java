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

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ListingAmenitiesServiceImpl implements ListingAmenitiesService {
    private final ListingAmenitiesRepository repository;
    private final ListingsRepository listingsRepository;
    private final ListingAmenitiesMapper mapper;

    @Override
    public List<ListingAmenitiesDto> getAmenitiesByListing(Long listingId) {
        return repository.findByListingId(listingId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ListingAmenitiesDto addAmenityToListing(Long listingId, AmenityType amenityType) {
        var listing  = listingsRepository.findById(listingId).orElseThrow(
                () -> new ListingNotFoundException("Listing", "listingId", listingId));
        ListingAmenities amenities = new ListingAmenities();
        amenities.setListing(listing);
        amenities.setAmenityType(amenityType);
        repository.save(amenities);
        return mapper.toDto(amenities);
    }

    @Override
    public void removeAmenityFromListing(Long listingId, Long amenityId) {
        var listing = listingsRepository.findById(listingId).orElseThrow(
                () -> new ListingNotFoundException("Listing", "listingId", listingId));
        List<ListingAmenities> amenitiesList = repository.findByListingId(listingId);
        amenitiesList.removeIf(amenity -> amenity.getId().equals(amenityId));
        repository.saveAll(amenitiesList);
    }
}
