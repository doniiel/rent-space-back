package com.rentspace.listingservice.controller;

import com.rentspace.listingservice.dto.ListingDto;
import com.rentspace.listingservice.repository.ListingsRepository;
import com.rentspace.listingservice.repository.PhotosRepository;
import com.rentspace.listingservice.service.ListingsService;
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

    @GetMapping("/{id}")
    public ResponseEntity<ListingDto> getListings(@PathVariable Long id) {
        return ResponseEntity
                .status(OK)
                .body(listingsService.getListingById(id));
    }

    @PostMapping()
    public ResponseEntity<ListingDto> createListings(@Valid @RequestBody ListingDto listingDto) {
        return ResponseEntity
                .status(CREATED)
                .body(listingsService.createListing(listingDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ListingDto> updateListings(@PathVariable Long id,
                                                     @Valid @RequestBody ListingDto listingDto) {
        listingDto.setId(id);
        return ResponseEntity
                .status(OK)
                .body(listingsService.updateListing(listingDto));
    }
    @DeleteMapping("/{listingId}")
    public ResponseEntity<String> deleteListings(@PathVariable Long listingId) {
        return ResponseEntity
                .status(OK)
                .body(listingsService.deleteListing(listingId));
    }
}
