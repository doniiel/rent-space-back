package com.rentspace.listingservice.service.impl;

import com.rentspace.listingservice.dto.ListingPhotoDto;
import com.rentspace.listingservice.entity.Listing;
import com.rentspace.listingservice.entity.ListingPhoto;
import com.rentspace.listingservice.mapper.ListingPhotoMapper;
import com.rentspace.listingservice.repository.ListingPhotoRepository;
import com.rentspace.listingservice.service.ListingPhotoService;
import com.rentspace.listingservice.storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListingPhotoServiceImpl implements ListingPhotoService {

    private final StorageService storageService;
    private final ListingPhotoRepository repository;
    private final ListingPhotoMapper photoMapper;

    @Override
    public List<ListingPhoto> savePhotos(Listing listing, List<MultipartFile> photos) {
        List<String> photoUrls = storageService.uploadFiles(photos, "listings/" + listing.getId());
        for (String photoUrl : photoUrls) {
            ListingPhoto photo = new ListingPhoto();
            photo.setListing(listing);
            photo.setPhotoUrl(photoUrl);
            repository.save(photo);
        }
        return repository.findByListing(listing);
    }

    @Override
    public void deletePhotos(List<ListingPhoto> listingPhotos) {
        listingPhotos.forEach(listingPhoto -> {
            storageService.deleteFile(listingPhoto.getPhotoUrl(), "listings");
        });
        repository.deleteAll(listingPhotos);
    }

    @Override
    public List<ListingPhotoDto> toDtoList(List<ListingPhoto> listingPhotos) {
        return photoMapper.toDtoList(listingPhotos);
    }

    @Override
    public List<ListingPhotoDto> getAllPhotos(Long listingId) {
        List<ListingPhoto> listingPhotos = repository.findByListingId(listingId);
        return toDtoList(listingPhotos);
    }
}
