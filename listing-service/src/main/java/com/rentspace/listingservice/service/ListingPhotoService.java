package com.rentspace.listingservice.service;

import com.rentspace.listingservice.entity.Listing;
import com.rentspace.listingservice.entity.ListingPhoto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ListingPhotoService {
    public List<ListingPhoto> savePhotos(Long listingId, List<MultipartFile> photos);
    public void deletePhotos(List<ListingPhoto> listingPhotos);
    public void updatePhotos(Long listingId, List<MultipartFile> newPhotos, List<String> deleteUrls);
}
