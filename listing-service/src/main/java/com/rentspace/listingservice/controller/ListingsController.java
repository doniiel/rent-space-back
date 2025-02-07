package com.rentspace.listingservice.controller;

import com.rentspace.listingservice.dto.ListingCreateRequest;
import com.rentspace.listingservice.dto.ListingDto;
import com.rentspace.listingservice.dto.ListingUpdateRequest;
import com.rentspace.listingservice.service.ListingPhotoService;
import com.rentspace.listingservice.service.ListingsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/listings")
public class ListingsController {

    private final ListingsService listingsService;
    private final ListingPhotoService listingPhotoService;

    @PostMapping
    public ResponseEntity<ListingDto> createListing(@Valid @RequestBody ListingCreateRequest request) {
        return ResponseEntity.status(CREATED).body(listingsService.createListing(request));
    }

    @PostMapping("/{id}/photos")
    public ResponseEntity<String> uploadPhotos(@PathVariable Long id, @RequestParam("photos") List<MultipartFile> files) {
        listingPhotoService.savePhotos(id, files);
        return ResponseEntity.ok("Photos uploaded successfully.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ListingDto> updateListing(@PathVariable Long id, @Valid @RequestBody ListingUpdateRequest request) {
        return ResponseEntity.ok(listingsService.updateListing(id, request));
    }

    @PutMapping("/{id}/photos")
    public ResponseEntity<String> updatePhotos(@PathVariable Long id,
                                               @RequestParam(value = "newPhotos", required = false) List<MultipartFile> newPhotos,
                                               @RequestParam(value = "deleteUrls", required = false) List<String> deleteUrls) {
        listingPhotoService.updatePhotos(id, newPhotos != null ? newPhotos : List.of(), deleteUrls != null ? deleteUrls : List.of());
        return ResponseEntity.ok("Photos updated successfully.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListingDto> getListingById(@PathVariable Long id) {
        return ResponseEntity.ok(listingsService.getListingById(id));
    }

    @GetMapping
    public ResponseEntity<List<ListingDto>> getAllListings() {
        return ResponseEntity.ok(listingsService.getAllListings());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ListingDto>> getAllUserListings(@PathVariable Long userId) {
        return ResponseEntity.ok(listingsService.getAllUserListings(userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteListing(@PathVariable Long id) {
        listingsService.deleteListing(id);
        return ResponseEntity.noContent().build();
    }
}
