package com.rentspace.listingservice.service;

import com.rentspace.listingservice.dto.ListingPhotoDto;
import com.rentspace.listingservice.entity.Listing;
import com.rentspace.listingservice.entity.ListingPhoto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ListingPhotoService {
    public List<ListingPhoto> savePhotos(Listing listing, List<MultipartFile> photos);
    public void deletePhotos(List<ListingPhoto> listingPhotos);
    public List<ListingPhotoDto> toDtoList(List<ListingPhoto> listingPhotos);
    public List<ListingPhotoDto> getAllPhotos(Long listingId);
}
