package com.rentspace.listingservice.controller;

import com.rentspace.listingservice.dto.ListingAmenitiesDto;
import com.rentspace.listingservice.enums.AmenityType;
import com.rentspace.listingservice.service.ListingAmenitiesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/listings/{listingId}/amenities")
@RequiredArgsConstructor
public class AmenitiesController {
    private final ListingAmenitiesService listingAmenitiesService;

    @GetMapping
    public ResponseEntity<ListingAmenitiesDto> getAmenitiesByListingId(@PathVariable Long listingId) {
        return ResponseEntity.ok(listingAmenitiesService.getAmenitiesByListing(listingId));
    }

    @PostMapping
    public ResponseEntity<ListingAmenitiesDto> addAmenityToListing(@PathVariable Long listingId, @RequestBody AmenityType amenityType) {
        return ResponseEntity.ok(listingAmenitiesService.addAmenitiesToListing(listingId, Collections.singleton(amenityType)));
    }

    @PutMapping
    public ResponseEntity<ListingAmenitiesDto> updateAmenitiesForListing(@PathVariable Long listingId, @RequestBody Set<AmenityType> amenityTypes) {
         return ResponseEntity.ok(listingAmenitiesService.updateAmenitiesForListing(listingId, amenityTypes));
    }

    @DeleteMapping("/{amenityType}")
    public ResponseEntity<ListingAmenitiesDto> removeAmenityFromListing(@PathVariable Long listingId, @PathVariable AmenityType amenityType) {
        return ResponseEntity.ok(listingAmenitiesService.removeAmenityFromListing(listingId, amenityType));
    }
    @DeleteMapping
    public ResponseEntity<Void> deleteAmenityFromListing(@PathVariable Long listingId) {
        listingAmenitiesService.removeAllAmenitiesFromListing(listingId);
        return ResponseEntity.noContent().build();
    }
}
