package com.rentspace.listingservice.service.impl;

import com.rentspace.listingservice.dto.ListingDto;
import com.rentspace.listingservice.dto.PhotoDto;
import com.rentspace.listingservice.entity.Listing;
import com.rentspace.listingservice.entity.Photo;
import com.rentspace.listingservice.exception.ListingAlreadyExistsException;
import com.rentspace.listingservice.exception.ResourceNotFoundException;
import com.rentspace.listingservice.mapper.ListingMapper;
import com.rentspace.listingservice.mapper.PhotoMapper;
import com.rentspace.listingservice.repository.ListingsRepository;
import com.rentspace.listingservice.repository.PhotosRepository;
import com.rentspace.listingservice.service.ListingsService;
import com.rentspace.listingservice.storage.StorageException;
import com.rentspace.listingservice.storage.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.time.LocalDateTime.now;

@Service
@RequiredArgsConstructor
@Slf4j
public class ListingsServiceImpl implements ListingsService {

    private final ListingsRepository listingsRepository;
    private final PhotosRepository photosRepository;
    private final ListingMapper listingMapper;
    private final PhotoMapper photoMapper;
    private final StorageService storageService;

    @Override
    @Transactional(readOnly = true)
    public ListingDto getListingById(Long listingId) {
        log.info("Fetching listing with id: {}", listingId);

        Listing listing = listingsRepository.findById(listingId).orElseThrow(
                () -> new ResourceNotFoundException("Listing", "listingId", listingId));

        List<Photo> photos = photosRepository.findByListings_Id(listing.getId());

        if (photos.isEmpty()) {
            log.warn("No photos found for listing with ID: {}", listingId);
        }

        ListingDto listingDto = listingMapper.toDto(listing);
        List<PhotoDto> photoDtos = photoMapper.toDtoList(photos);
        listingDto.setPhotos(photoDtos);

        log.info("Fetched listing: {}", listingDto);
        return listingDto;
    }

    @Override
    @Transactional
    public ListingDto createListing(ListingDto listingDto) {
        log.info("Creating new listing: {}", listingDto);

        if (listingDto.getId() != null && listingsRepository.existsById(listingDto.getId())) {
            throw new ListingAlreadyExistsException("Listing", "listingId", listingDto.getId());
        }

        Listing listing = listingMapper.toEntity(listingDto);
        Listing savedListing = listingsRepository.save(listing);

       List<Photo> photos = listingDto.getPhotos().stream()
                       .filter(photoDto -> photoDto.getFile() != null && !photoDto.getFile().isEmpty())
                       .map(photoDto -> uploadPhoto(savedListing, photoDto))
                       .collect(Collectors.toList());

       photosRepository.saveAll(photos);
       savedListing.setPhotos(photos);
       listingsRepository.save(savedListing);

       ListingDto savedDto = listingMapper.toDto(savedListing);
       List<PhotoDto> photoDtos = photos.stream()
                       .map(photoMapper::toDto)
                       .collect(Collectors.toList());
       savedDto.setPhotos(photoDtos);

        log.info("Listing created successfully with ID: {}", savedListing.getId());
        return savedDto;
    }

    @Override
    @Transactional
    public ListingDto updateListing(ListingDto listingDto) {
        log.info("Updating listing with ID: {}", listingDto.getId());

        Listing existingListing = listingsRepository.findById(listingDto.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Listing", "listingId", listingDto.getId())
        );

        listingMapper.updateEntityFromDto(listingDto, existingListing);
        existingListing.setUpdatedAt(now());
        existingListing.setUpdatedBy("LISTINGSERVICE");

        List<Photo> oldPhotos = existingListing.getPhotos();
        deletePhotos(oldPhotos);

        List<Photo> newPhotos = listingDto.getPhotos().stream()
                .filter(photoDto -> photoDto.getFile() != null && !photoDto.getFile().isEmpty())
                .map(photoDto -> uploadPhoto(existingListing, photoDto))
                .collect(Collectors.toList());

        photosRepository.saveAll(newPhotos);
        existingListing.setPhotos(newPhotos);
        listingsRepository.save(existingListing);

        ListingDto updatedDto = listingMapper.toDto(existingListing);
        List<PhotoDto> photoDtos = newPhotos.stream()
                .map(photoMapper::toDto)
                .collect(Collectors.toList());
        updatedDto.setPhotos(photoDtos);

        log.info("Listing updated successfully with ID: {}", updatedDto.getId());
        return updatedDto;
    }

    @Override
    @Transactional
    public String deleteListing(Long listingId) {
        log.info("Deleting listing with ID: {}", listingId);

        Listing listing = listingsRepository.findById(listingId).orElseThrow(
                () -> new ResourceNotFoundException("Listing", "listingId", listingId));

        List<Photo> photos = listing.getPhotos();
        deletePhotos(photos);

        listingsRepository.delete(listing);

        log.info("Listing deleted successfully with ID: {}", listingId);
        return format("Listing deleted successfully with ID: %s", listingId);
    }

    private Photo uploadPhoto(Listing listing, PhotoDto photoDto) {
        String fileName = storageService.uploadFile(photoDto.getFile(), "listings");
        return Photo.builder()
                .listing(listing)
                .url(fileName)
                .build();
    }
    private void deletePhotos(List<Photo> photos) {
        photos.forEach(photo -> {
            try {
                storageService.deleteFile(photo.getUrl(), "listings");
            } catch (Exception e) {
                log.error("Failed to delete photos url: {}", e.getMessage());
                throw new StorageException("Failed to delete photos from Minio: " + e.getMessage(), e);
            }
        });
        photosRepository.deleteAll(photos);
    }

}
