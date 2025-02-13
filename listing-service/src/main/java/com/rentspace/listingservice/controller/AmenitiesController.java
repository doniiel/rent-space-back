package com.rentspace.listingservice.controller;

import com.rentspace.listingservice.dto.ListingAmenitiesDto;
import com.rentspace.listingservice.enums.AmenityType;
import com.rentspace.listingservice.service.ListingAmenitiesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/listings/{listingId}/amenities")
@RequiredArgsConstructor
public class AmenitiesController {
    private final ListingAmenitiesService listingAmenitiesService;

    @GetMapping
    public ResponseEntity<List<ListingAmenitiesDto>> getAmenitiesByListingId(@PathVariable Long listingId) {
        return ResponseEntity.ok(listingAmenitiesService.getAmenitiesByListing(listingId));
    }

    @PostMapping
    public ResponseEntity<ListingAmenitiesDto> addAmenityToListing(@PathVariable Long listingId, @RequestBody AmenityType amenityType) {
        return ResponseEntity.ok(listingAmenitiesService.addAmenityToListing(listingId, amenityType));
    }

    @DeleteMapping("/{amenityId}")
    public ResponseEntity<Void> deleteAmenityFromListing(@PathVariable Long listingId, @PathVariable Long amenityId) {
        listingAmenitiesService.removeAmenityFromListing(listingId, amenityId);
        return ResponseEntity.noContent().build();
    }
}
