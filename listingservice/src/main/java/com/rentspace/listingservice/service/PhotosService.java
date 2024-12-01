package com.rentspace.listingservice.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PhotosService {
    List<String> uploadPhotos(Long listingId, List<MultipartFile> file);
    List<String> getPhotos(Long listingId);
//    void deletePhoto(Long listingId, Long photoId);
}
