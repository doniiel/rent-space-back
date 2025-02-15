package com.rentspace.listingservice.service.impl;

import com.rentspace.listingservice.entity.Listing;
import com.rentspace.listingservice.entity.ListingPhoto;
import com.rentspace.listingservice.exception.ListingNotFoundException;
import com.rentspace.listingservice.exception.ListingPhotosNotFoundException;
import com.rentspace.listingservice.exception.PhotosUploadException;
import com.rentspace.listingservice.repository.ListingPhotoRepository;
import com.rentspace.listingservice.repository.ListingsRepository;
import com.rentspace.listingservice.service.ListingPhotoService;
import com.rentspace.listingservice.storage.StorageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ListingPhotoServiceImpl implements ListingPhotoService {

    private final StorageService storageService;
    private final ListingsRepository listingsRepository;
    private final ListingPhotoRepository photoRepository;

    @Override
    @Transactional
    public void savePhotos(Long listingId, List<MultipartFile> photos) {
        Listing listing = listingsRepository.findById(listingId)
                .orElseThrow(() -> new ListingNotFoundException("Listing", "listingId", listingId));
        try {
            savePhotos(listing, photos);
        } catch (Exception ex) {
            throw new PhotosUploadException("Error uploading photos");
        }
    }

    private void savePhotos(Listing listing, List<MultipartFile> photos) {
        if (photos == null || photos.isEmpty()) {
            return;
        }

        List<ListingPhoto> savedPhotos = photos.stream()
                .map(photo -> {
                    String filePath = storageService.uploadFile(photo, "listings/" + listing.getId());
                    String fileUrl = storageService.getFileUrl(filePath, null);
                    return ListingPhoto.builder()
                            .listing(listing)
                            .photoUrl(fileUrl)
                            .build();
                })
                .collect(Collectors.toList());

        photoRepository.saveAll(savedPhotos);
    }

    @Override
    @Transactional
    public void deletePhotos(List<ListingPhoto> listingPhotos) {
        if (listingPhotos == null || listingPhotos.isEmpty()) {
            return;
        }
        listingPhotos.forEach(photo -> {
            String fileUrl = photo.getPhotoUrl();
            String fileName = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);

            log.info("Удаление фото из MinIO: {}", fileName);
            storageService.deleteFile(fileName, "listings/" + photo.getListing().getId());
            log.info("Удалено фото: {}", fileName);
        });

        photoRepository.deleteAll(listingPhotos);
    }

    @Override
    @Transactional
    public void deletePhotos(Long listingId, List<String> deleteUrls) {
        if (deleteUrls == null || deleteUrls.isEmpty()) {
            return;
        }
        List<ListingPhoto> existingPhotos = photoRepository.findByListingId(listingId);
        List<ListingPhoto> photosToDelete = existingPhotos.stream()
                .filter(photo -> deleteUrls.contains(photo.getPhotoUrl()))
                .collect(Collectors.toList());

        if (photosToDelete.isEmpty()) {
            throw new ListingPhotosNotFoundException("Photo for deletion not found");
        }

        Listing listing = photosToDelete.get(0).getListing();
        listing.getPhotos().removeAll(photosToDelete);

        photosToDelete.forEach(photo -> {
            String fileUrl = photo.getPhotoUrl();
            String fileName = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);
            log.info("Удаление фото из хранилища: {}", fileName);
            storageService.deleteFile(fileName, "listings/" + listingId);
            log.info("Фото удалено: {}", fileName);
        });
        photoRepository.deleteAll(photosToDelete);
    }
}
