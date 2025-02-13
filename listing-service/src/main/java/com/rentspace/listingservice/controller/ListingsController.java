package com.rentspace.listingservice.controller;

import com.rentspace.listingservice.dto.ListingCreateRequest;
import com.rentspace.listingservice.dto.ListingDto;
import com.rentspace.listingservice.dto.ListingUpdateRequest;
import com.rentspace.listingservice.service.ListingsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/listings")
public class ListingsController {

    private final ListingsService listingsService;

    @PostMapping
    public ResponseEntity<ListingDto> createListing(@Valid @RequestBody ListingCreateRequest request) {
        return ResponseEntity.status(CREATED).body(listingsService.createListing(request));
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

    @PutMapping("/{id}")
    public ResponseEntity<ListingDto> updateListing(@PathVariable Long id, @Valid @RequestBody ListingUpdateRequest request) {
        return ResponseEntity.ok(listingsService.updateListing(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteListing(@PathVariable Long id) {
        listingsService.deleteListing(id);
        return ResponseEntity.noContent().build();
    }
}
