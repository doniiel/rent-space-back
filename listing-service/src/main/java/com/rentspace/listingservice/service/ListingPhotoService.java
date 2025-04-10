package com.rentspace.listingservice.service;

import com.rentspace.listingservice.dto.ListingDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ListingPhotoService {
    ListingDto getListingPhotos(Long listingId);
    ListingDto savePhotos(Long listingId, List<MultipartFile> photos);
    ListingDto deletePhotos(Long listingId, List<String> deleteUrls);
}
