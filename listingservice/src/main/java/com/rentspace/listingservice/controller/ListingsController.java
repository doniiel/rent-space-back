package com.rentspace.listingservice.controller;

import com.rentspace.listingservice.dto.ListingsDto;
import com.rentspace.listingservice.repository.ListingsRepository;
import com.rentspace.listingservice.repository.PhotosRepository;
import com.rentspace.listingservice.service.ListingsService;
import com.rentspace.listingservice.service.impl.PhotosServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/listings")
public class ListingsController {

    private final ListingsService listingsService;
    private final PhotosServiceImpl photoService;
    private final PhotosRepository photosRepository;
    private final ListingsRepository listingsRepository;

    @GetMapping("/{listingId}")
    public ResponseEntity<ListingsDto> getListings(@PathVariable Long listingId) {
        return ResponseEntity
                .status(OK)
                .body(listingsService.getListingById(listingId));
    }

    @PostMapping()
    public ResponseEntity<String> createListings(@Valid @RequestBody ListingsDto listingsDto) {
        return ResponseEntity
                .status(CREATED)
                .body(listingsService.createListing(listingsDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ListingsDto> updateListings(@PathVariable Long id,
                                                      @Valid @RequestBody ListingsDto listingsDto) {
        listingsDto.setId(id);
        return ResponseEntity
                .status(OK)
                .body(listingsService.updateListing(listingsDto));
    }
    @DeleteMapping("/{listingId}")
    public ResponseEntity<String> deleteListings(@PathVariable Long listingId) {
        return ResponseEntity
                .status(OK)
                .body(listingsService.deleteListing(listingId));
    }

    @PostMapping("/{listingId}/photos")
    public ResponseEntity<List<String>> uploadPhoto(@PathVariable Long listingId, @RequestParam("file") List<MultipartFile> file) {
        return ResponseEntity
                .status(CREATED)
                .body(photoService.uploadPhotos(listingId, file));
    }

    @GetMapping("/{listingId}/photos")
    public ResponseEntity<List<String>> getPhotos(@PathVariable Long listingId) {
        return ResponseEntity
                .status(OK)
                .body(photoService.getPhotos(listingId));
    }

//    @DeleteMapping("/{listingId}/photos/{photoId}")
//    public ResponseEntity<String> deletePhoto(@PathVariable Long listingId, @PathVariable Long photoId) {
//        return ResponseEntity
//                .status(OK)
//                .body(photoService.deletePhoto(listingId, photoId));
//    }
}
