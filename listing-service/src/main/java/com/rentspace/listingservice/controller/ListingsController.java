package com.rentspace.listingservice.controller;

import com.rentspace.listingservice.dto.ListingDto;
import com.rentspace.listingservice.service.ListingsService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
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

    @GetMapping("/hello")
    public String hello() {
        return "Hello";
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListingDto> getListings(@PathVariable Long id) {
        return ResponseEntity.ok(listingsService.getListingById(id));
    }

    @PostMapping
    public ResponseEntity<ListingDto> createListings(@Valid @RequestPart ListingDto listingDto,
                                                     @RequestPart("photos") @NotEmpty List<MultipartFile> files) {
        listingDto.setPhotos(files);
        return ResponseEntity.status(CREATED).body(listingsService.createListing(listingDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ListingDto> updateListings(@PathVariable Long id,
                                                     @Valid @RequestBody ListingDto listingDto,
                                                     @RequestPart("photos") @NotEmpty List<MultipartFile> files) {
        listingDto.setId(id);
        listingDto.setPhotos(files);
        return ResponseEntity.ok(listingsService.updateListing(id, listingDto));
    }
    @DeleteMapping("/{listingId}")
    public ResponseEntity<String> deleteListings(@PathVariable Long listingId) {
        return ResponseEntity.ok(listingsService.deleteListing(listingId));
    }
}
