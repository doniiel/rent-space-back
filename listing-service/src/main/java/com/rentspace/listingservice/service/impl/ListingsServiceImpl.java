package com.rentspace.listingservice.service.impl;

import com.rentspace.listingservice.dto.ListingDto;
import com.rentspace.listingservice.dto.ListingPhotoDto;
import com.rentspace.listingservice.entity.Listing;
import com.rentspace.listingservice.entity.ListingPhoto;
import com.rentspace.listingservice.exception.ListingAlreadyExistsException;
import com.rentspace.listingservice.exception.ListingNotFoundException;
import com.rentspace.listingservice.mapper.ListingMapper;
import com.rentspace.listingservice.repository.ListingsRepository;
import com.rentspace.listingservice.repository.ListingPhotoRepository;
import com.rentspace.listingservice.service.ListingPhotoService;
import com.rentspace.listingservice.service.ListingsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.lang.String.format;
import static java.time.LocalDateTime.now;

@Service
@RequiredArgsConstructor
@Slf4j
public class ListingsServiceImpl implements ListingsService {

    private final ListingsRepository listingsRepository;
    private final ListingMapper listingMapper;
    private final ListingPhotoService listingPhotoService;

    @Override
    @Transactional
    public ListingDto createListing(ListingDto listingDto) {
        log.info("Creating new listing: {}", listingDto);

        if (listingDto.getId() != null && listingsRepository.existsById(listingDto.getId())) { // check have any listing with this id
            throw new ListingAlreadyExistsException("Listing", "listingId", listingDto.getId());
        }

        Listing listing = listingMapper.toEntity(listingDto); // save listing in the database
        Listing savedListing = listingsRepository.save(listing);

        List<ListingPhoto> listingPhotos = null;
        if (listingDto.getPhotos() != null  && !listingDto.getPhotos().isEmpty()) {
            listingPhotos = listingPhotoService.savePhotos(savedListing, listingDto.getPhotos());
        }

        savedListing.setListingPhotos(listingPhotos);
        listingsRepository.save(savedListing);

        ListingDto savedDto = listingMapper.toDto(savedListing);
        savedDto.setPhotos(listingPhotoService.toDtoList(listingPhotos));

        log.info("Listing created successfully with ID: {}", savedListing.getId());
        return savedDto;
    }

    @Override
    @Transactional
    public ListingDto updateListing(Long id, ListingDto listingDto) {
        log.info("Updating listing with ID: {}", listingDto.getId());

        Listing existingListing = listingsRepository.findById(listingDto.getId()).orElseThrow( // check have any listing with this id
                () -> new ListingNotFoundException("Listing", "listingId", id)
        );

        existingListing.setTitle(listingDto.getTitle());
        existingListing.setDescription(listingDto.getDescription());
        existingListing.setAddress(listingDto.getAddress());
        existingListing.setCity(listingDto.getCity());
        existingListing.setCountry(listingDto.getCountry());
        existingListing.setLatitude(listingDto.getLatitude());
        existingListing.setLongitude(listingDto.getLongitude());
        existingListing.setType(listingDto.getType());
        existingListing.setMaxGuests(listingDto.getMaxGuests());
        existingListing.setPricePerNight(listingDto.getPricePerNight());
        existingListing.setUpdatedAt(now());
        existingListing.setUpdatedBy("LISTINGSERVICE");

        List<ListingPhoto> listingPhotos = null;
        if (listingDto.getPhotos() != null && !listingDto.getPhotos().isEmpty()) {
            List<ListingPhoto> oldListingPhotos = existingListing.getListingPhotos();
            listingPhotoService.deletePhotos(oldListingPhotos);
            listingPhotos = listingPhotoService.savePhotos(existingListing, listingDto.getPhotos());
        }

        existingListing.setListingPhotos(listingPhotos);
        listingsRepository.save(existingListing);

        ListingDto updatedDto = listingMapper.toDto(existingListing);
        updatedDto.setPhotos(listingPhotoService.toDtoList(listingPhotos));

        log.info("Listing updated successfully with ID: {}", updatedDto.getId());
        return updatedDto;
    }

    @Override
    @Transactional(readOnly = true)
    public ListingDto getListingById(Long listingId) {
        log.info("Fetching listing with id: {}", listingId);

        Listing listing = listingsRepository.findById(listingId).orElseThrow(
                () -> new ListingNotFoundException("Listing", "listingId", listingId));

        List<ListingPhoto> listingPhotos = listingPhotoService.getAllPhotos(listingId);

        ListingDto listingDto = listingMapper.toDto(listing);
        listingDto.setPhotos(listingPhotoService.toDtoList(listingPhotos));

        log.info("Fetched listing: {}", listingDto);
        return listingDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ListingDto> getAllListings() {
        return null;
    }

    @Override
    @Transactional
    public String deleteListing(Long listingId) {
        log.info("Deleting listing with ID: {}", listingId);

        Listing listing = listingsRepository.findById(listingId).orElseThrow(
                () -> new ListingNotFoundException("Listing", "listingId", listingId)
        );

        List<ListingPhoto> listingPhotos = listing.getListingPhotos();
        listingPhotoService.deletePhotos(listingPhotos);

        listingsRepository.delete(listing);

        log.info("Listing deleted successfully with ID: {}", listingId);
        return format("Listing deleted successfully with ID: %s", listingId);
    }

//    private ListingPhoto uploadPhoto(Listing listing, ListingPhotoDto listingPhotoDto) {
//        String fileName = storageService.uploadFile(listingPhotoDto.getFile(), "listings");
//        return ListingPhoto.builder()
//                .listing(listing)
//                .photoUrl(fileName)
//                .build();
//    }
//    private void deletePhotos(List<ListingPhoto> listingPhotos) {
//        listingPhotos.forEach(listingPhoto -> {
//            try {
//                storageService.deleteFile(listingPhoto.getPhotoUrl(), "listings");
//            } catch (Exception e) {
//                log.error("Failed to delete listingPhotos url: {}", e.getMessage());
//                throw new StorageException("Failed to delete listingPhotos from Minio: " + e.getMessage(), e);
//            }
//        });
//        listingPhotoRepository.deleteAll(listingPhotos);
//    }

}
