package com.rentspace.listingservice.service;

import com.rentspace.listingservice.entity.ListingPhoto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ListingPhotoService {
    void savePhotos(Long listingId, List<MultipartFile> photos);
    void deletePhotos(List<ListingPhoto> listingPhotos);
    void deletePhotos(Long listingId, List<String> deleteUrls);

}
