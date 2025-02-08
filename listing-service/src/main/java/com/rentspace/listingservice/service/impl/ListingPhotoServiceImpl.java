package com.rentspace.listingservice.service.impl;

import com.rentspace.listingservice.entity.Listing;
import com.rentspace.listingservice.entity.ListingPhoto;
import com.rentspace.listingservice.exception.ListingNotFoundException;
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
    public List<ListingPhoto> savePhotos(Long listingId, List<MultipartFile> photos) {
        Listing listing = listingsRepository.findById(listingId)
                .orElseThrow(() -> new ListingNotFoundException("Listing", "listingId", listingId));

        return savePhotos(listing, photos);
    }


    private List<ListingPhoto> savePhotos(Listing listing, List<MultipartFile> photos) {
        if (photos == null || photos.isEmpty()) {
            return List.of();
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

        return photoRepository.saveAll(savedPhotos);
    }

    @Override
    @Transactional
    public void deletePhotos(List<ListingPhoto> listingPhotos) {
        if (listingPhotos == null || listingPhotos.isEmpty()) {
            return;
        }

        listingPhotos.forEach(photo -> {
            // 1. Парсим URL, оставляем только имя файла
            String fileUrl = photo.getPhotoUrl();
            String fileName = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);

            log.info("Удаление фото из MinIO: {}", fileName);

            // 2. Передаем только имя файла и директорию
            storageService.deleteFile(fileName, "listings/" + photo.getListing().getId());

            log.info("Удалено фото: {}", fileName);
        });

        photoRepository.deleteAll(listingPhotos);
    }

    @Override
    @Transactional
    public void updatePhotos(Long listingId, List<MultipartFile> newPhotos, List<String> deleteUrls) {
        List<ListingPhoto> existingPhotos = photoRepository.findByListingId(listingId);

        // Загружаем listing, если фото отсутствуют
        Listing listing = existingPhotos.isEmpty()
                ? listingsRepository.findById(listingId)
                .orElseThrow(() -> new ListingNotFoundException("Listing", "listingId", listingId))
                : existingPhotos.get(0).getListing();

        List<ListingPhoto> photosToDelete = existingPhotos.stream()
                .filter(photo -> deleteUrls.contains(photo.getPhotoUrl()))
                .collect(Collectors.toList());

        deletePhotos(photosToDelete);

        List<ListingPhoto> addedPhotos = savePhotos(listing, newPhotos);

        listing.getPhotos().clear();
        listing.getPhotos().addAll(addedPhotos);
    }
}
