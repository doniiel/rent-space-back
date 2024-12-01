package com.rentspace.listingservice.service.impl;

import com.rentspace.listingservice.entity.Listings;
import com.rentspace.listingservice.entity.Photos;
import com.rentspace.listingservice.repository.ListingsRepository;
import com.rentspace.listingservice.repository.PhotosRepository;
import com.rentspace.listingservice.service.PhotosService;
import com.rentspace.listingservice.util.MinioProperties;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PhotosServiceImpl implements PhotosService {

    private final MinioProperties minioProperties;
    private final MinioClient minioClient;
    private final ListingsRepository listingsRepository;
    private final PhotosRepository photosRepository;

    @Override
    public List<String> uploadPhotos(Long listingId, List<MultipartFile> files) {
        List<String> uploadedFileUrls = new ArrayList<>();

        Listings listings = listingsRepository.findById(listingId)
                .orElseThrow(() -> new RuntimeException("Объявление не найдено"));

        List<Photos> photoList = new ArrayList<>();

        for (MultipartFile file : files) {
            try (InputStream inputStream = file.getInputStream()) {
                String objectName = UUID.randomUUID() + "_" + file.getOriginalFilename();

                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(minioProperties.getBucket())
                                .object(objectName)
                                .stream(inputStream, file.getSize(), -1)
                                .contentType(file.getContentType())
                                .build()
                );

                Photos photo = new Photos();
                photo.setListings(listings);
                photo.setUrl(objectName);
                photoList.add(photo);
                uploadedFileUrls.add(objectName);

            } catch (Exception e) {
                throw new RuntimeException("Failed to upload photo in Minio: " + e.getMessage(), e);
            }
        }

        listings.setPhotos(photoList);
        listingsRepository.save(listings);
        photosRepository.saveAll(photoList);
        return uploadedFileUrls;
    }

    @Override
    public List<String> getPhotos(Long listingId) {
        return photosRepository.findByListings_Id(listingId)
                .stream()
                .map(Photos::getUrl)
                .toList();
    }

//    public void deletePhoto(Long listingId, String fileName) {
//        try {
//            // Find the listing by ID
//            Listings listings = listingsRepository.findById(listingId)
//                    .orElseThrow(() -> new RuntimeException("Listing not found"));
//
//            // Find the photo in the database by its URL (fileName)
//            Photos photo = photosRepository.findByListings_IdAndUrl(listingId, fileName)
//                    .orElseThrow(() -> new RuntimeException("Photo not found"));
//
//            // Remove the photo from the listing
//            listings.getPhotos().remove(photo);
//            listingsRepository.save(listings);
//
//            // Delete the photo from Minio
//            minioClient.removeObject(
//                    RemoveObjectArgs.builder()
//                            .bucket(minioProperties.getBucket())
//                            .object(fileName)
//                            .build()
//            );
//
//            // Delete the photo record from the database
//            photosRepository.delete(photo);
//
//        } catch (MinioException | IOException e) {
//            throw new RuntimeException("Failed to delete photo: " + e.getMessage(), e);
//        }
//    }

}
