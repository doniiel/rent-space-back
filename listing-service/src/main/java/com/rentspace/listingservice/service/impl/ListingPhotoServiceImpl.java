package com.rentspace.listingservice.service.impl;

import com.rentspace.listingservice.dto.ListingDto;
import com.rentspace.listingservice.entity.Listing;
import com.rentspace.listingservice.entity.ListingPhoto;
import com.rentspace.listingservice.exception.PhotosUploadException;
import com.rentspace.listingservice.mapper.ListingMapper;
import com.rentspace.listingservice.repository.ListingPhotoRepository;
import com.rentspace.listingservice.service.ListingPhotoService;
import com.rentspace.listingservice.storage.StorageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ListingPhotoServiceImpl implements ListingPhotoService {
    private final StorageService storageService;
    private final ListingBaseService listingBaseService;
    private final ListingPhotoRepository photoRepository;
    private final ListingMapper listingMapper;
    private static final int MAX_PHOTOS_COUNT = 10;

    @Override
    @Transactional(readOnly = true)
    public ListingDto getListingPhotos(Long listingId) {
        log.debug("Getting photos for listing ID: {}", listingId);
        Listing listing = listingBaseService.getListingById(listingId);
        log.debug("Retrieved listing: {}", listing);
        ListingDto listingDto = listingMapper.toDto(listing);
        log.debug("Converted into DTO: {}", listingDto);
        return listingDto;
    }

    @Override
    @Transactional
    @Caching(put = { @CachePut(value = "listing", key = "#listingId") },
            evict = { @CacheEvict(value = "listings", key = "'allListings'"),
                      @CacheEvict(value = "userListings", key = "#listing.userId") })
    public ListingDto savePhotos(Long listingId, List<MultipartFile> photos) {
        log.debug("Saving photos for listing ID: {}", listingId);
        Listing listing = listingBaseService.getListingById(listingId);
        List<MultipartFile> validPhotos = validatePhotos(photos);
        if (!validPhotos.isEmpty()) {
            savePhotosForListing(listing, validPhotos);
        }
        return listingMapper.toDto(listing);
    }

    @Override
    @Transactional
    @Caching(put = { @CachePut(value = "listing", key = "#listingId") },
            evict = { @CacheEvict(value = "listings", key = "'allListings'"),
                      @CacheEvict(value = "userListings", key = "#listing.userId") })
    public ListingDto deletePhotos(Long listingId, List<String> deleteUrls) {
        log.debug("Deleting photos for listing ID: {} with deleteUrls: {}", listingId, deleteUrls);
        if (CollectionUtils.isEmpty(deleteUrls)) {
            Listing listing = listingBaseService.getListingById(listingId);
            return listingMapper.toDto(listing);
        }
        Listing listing = listingBaseService.getListingById(listingId);
        List<ListingPhoto> photosToDelete =  findPhotosToDelete(listing, deleteUrls);
        deletePhotosFromStorage(photosToDelete);
        listing.getPhotos().removeAll(photosToDelete);
        photoRepository.deleteAll(photosToDelete);
        log.info("Deleted {} photos for listing ID: {}", photosToDelete.size(), listingId);
        return listingMapper.toDto(listing);
    }

    private List<MultipartFile> validatePhotos(List<MultipartFile> photos) {
        if (CollectionUtils.isEmpty(photos)) {
            log.debug("No photos provided for upload");
            return List.of();
        }
        if (photos.size() > MAX_PHOTOS_COUNT) {
            throw new PhotosUploadException("Too many photos: max allowed is " + MAX_PHOTOS_COUNT);
        }
        return photos;
    }

    private void savePhotosForListing(Listing listing, List<MultipartFile> photos) {
        List<ListingPhoto> savedPhotos = photos.stream()
                .map(photo -> uploadAndBuildPhoto(listing, photo))
                .toList();
        photoRepository.saveAll(savedPhotos);
        log.info("Saved {} photos for listing ID: {}", savedPhotos.size(), listing.getId());
    }

    private ListingPhoto uploadAndBuildPhoto(Listing listing, MultipartFile photo) {
        try {
            String filePath = storageService.uploadFile(photo, "listings/" + listing.getId());
            String fileUrl = storageService.getFileUrl(filePath, null);
            return ListingPhoto.builder()
                    .listing(listing)
                    .photoUrl(fileUrl)
                    .build();
        } catch (Exception ex) {
            log.error("Failed to upload photo for listing ID: {}", listing.getId(), ex);
            throw new PhotosUploadException("Error uploading photo: " + ex.getMessage());
        }
    }

    private void deletePhotosFromStorage(List<ListingPhoto> photos) {
        photos.forEach(photo -> {
            String fileUrl = photo.getPhotoUrl();
            String fileName = extractFileName(fileUrl);
            log.debug("Deleting photo from storage: {}", fileName);
            storageService.deleteFile(fileName, "listings/" + photo.getListing().getId());
        });
    }

    private List<ListingPhoto> findPhotosToDelete(Listing listing, List<String> deleteUrls) {
        List<ListingPhoto> existingPhotos = listing.getPhotos();
        List<ListingPhoto> photosToDelete = existingPhotos.stream()
                .filter(photo -> deleteUrls.contains(photo.getPhotoUrl()))
                .toList();
        if (photosToDelete.isEmpty()) {
            throw new PhotosUploadException("No matching photos found for deletion");
        }
        return photosToDelete;
    }

    private String extractFileName(String fileUrl) {
        return fileUrl.substring(fileUrl.lastIndexOf('/') + 1);
    }
}
