package com.rentspace.listingservice.service.impl;

import com.rentspace.listingservice.dto.ListingsDto;
import com.rentspace.listingservice.entity.Listings;
import com.rentspace.listingservice.entity.Photos;
import com.rentspace.listingservice.exception.ListingAlreadyExistsException;
import com.rentspace.listingservice.exception.ResourseNotFoundException;
import com.rentspace.listingservice.mapper.ListingsMapper;
import com.rentspace.listingservice.mapper.PhotosMapper;
import com.rentspace.listingservice.repository.ListingsRepository;
import com.rentspace.listingservice.repository.PhotosRepository;
import com.rentspace.listingservice.service.ListingsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.lang.String.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ListingsServiceImpl implements ListingsService {

    private final ListingsRepository listingsRepository;
    private final PhotosRepository photosRepository;
    private final ListingsMapper listingsMapper;
    private final PhotosMapper photosMapper;

    @Override
    @Transactional(readOnly = true)
    public ListingsDto getListingById(Long listingId) {
        log.info("Fetching listing with id: {}", listingId);

        Listings listings = listingsRepository.findById(listingId).orElseThrow(
                () -> new ResourseNotFoundException("Listings", "listingId", listingId)
        );

        List<Photos> photos = photosRepository.findByListings_Id(listings.getId()).orElseThrow(
                () -> new ResourseNotFoundException("Photos", "listingId", listingId)
        );

        ListingsDto listingsDto = listingsMapper.toDto(listings);
        listingsDto.setPhotos(photosMapper.toListDto(photos));

        log.info("Fetched listing: {}", listingsDto);
        return listingsDto;
    }

    @Override
    @Transactional
    public String createListing(ListingsDto listingsDto) {
        log.info("Creating new listing: {}", listingsDto);

        if (listingsRepository.existsById(listingsDto.getId())) {
            throw new ListingAlreadyExistsException("Listings", "listingId", listingsDto.getId());
        }
        Listings updateListings = listingsMapper.toEntity(listingsDto);
        Listings savedListings = listingsRepository.save(updateListings);

        log.info("Listing created successfully with ID: {}", savedListings.getId());
        return format("Listing created successfully with ID: %s", savedListings.getId());
    }

    @Override
    @Transactional
    public ListingsDto updateListing(ListingsDto listingsDto) {
        log.info("Updating listing with ID: {}", listingsDto.getId());

        Listings listings = listingsRepository.findById(listingsDto.getId()).orElseThrow(
                () -> new ResourseNotFoundException("Listings", "listingId", listingsDto.getId())
        );

        // update photos
        List<Photos> updatedPhotos = photosMapper.toListEntity(listingsDto.getPhotos());
        List<Photos> savedPhotos = photosRepository.saveAll(updatedPhotos);

        // update listings
        listings.setPhotos(savedPhotos);
        Listings savedListings = listingsRepository.save(listings);

        ListingsDto updatedListingsDto = listingsMapper.toDto(savedListings);
        log.info("Listing updated successfully with ID: {}", updatedListingsDto.getId());

        return updatedListingsDto;
    }

    @Override
    @Transactional
    public String deleteListing(Long listingId) {
        log.info("Deleting listing with ID: {}", listingId);

        Listings listings = listingsRepository.findById(listingId).orElseThrow(
                () -> new ResourseNotFoundException("Listings", "listingId", listingId)
        );

        listingsRepository.delete(listings);
        log.info("Listing deleted successfully with ID: {}", listingId);
        return format("Listing deleted successfully with ID: %s", listingId);
    }
}
