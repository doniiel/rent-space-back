package com.rentspace.listingservice.controller;

import com.rentspace.listingservice.dto.ListingAmenitiesDto;
import com.rentspace.listingservice.enums.AmenityType;
import com.rentspace.listingservice.service.ListingAmenitiesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Set;

import static org.springframework.http.HttpStatus.CREATED;

@Tag(name = "Amenities REST API in RentSpace", description = "APIs to manage amenities of a listing")
@RestController
@RequestMapping("/api/v1/listings/{listingId}/amenities")
@RequiredArgsConstructor
public class AmenitiesController {
    private final ListingAmenitiesService listingAmenitiesService;

    @Operation(summary = "Get amenities for a listing", description = "Retrieves all amenities associated with the specified listing")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Amenities retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Listing not found")
    })
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    @GetMapping
    public ResponseEntity<ListingAmenitiesDto> getAmenitiesByListingId(@PathVariable @NotNull Long listingId) {
        return ResponseEntity.ok(listingAmenitiesService.getAmenitiesByListing(listingId));
    }

    @Operation(summary = "Add an amenity to a listing", description = "Adds a single amenity to the specified listing")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Amenity added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid amenity type"),
            @ApiResponse(responseCode = "404", description = "Listing not found")
    })
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN', 'USER')")
    @PostMapping
    public ResponseEntity<ListingAmenitiesDto> addAmenityToListing(@PathVariable @NotNull Long listingId,
                                                                   @RequestBody @NotNull AmenityType amenityType) {
        return ResponseEntity.status(CREATED)
                .body(listingAmenitiesService.addAmenitiesToListing(listingId, Collections.singleton(amenityType)));
    }

    @Operation(summary = "Update amenities for a listing", description = "Replaces all amenities for the specified listing with the provided set")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Amenities updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid amenity types"),
            @ApiResponse(responseCode = "404", description = "Listing not found")
    })
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    @PutMapping
    public ResponseEntity<ListingAmenitiesDto> updateAmenitiesForListing(@PathVariable @NotNull Long listingId,
                                                                         @RequestBody @NotNull Set<AmenityType> amenityTypes) {
         return ResponseEntity.ok(listingAmenitiesService.updateAmenitiesForListing(listingId, amenityTypes));
    }

    @Operation(summary = "Remove an amenity from a listing", description = "Removes a specific amenity from the specified listing")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Amenity removed successfully"),
            @ApiResponse(responseCode = "404", description = "Listing or amenity not found")
    })
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN', 'USER')")
    @DeleteMapping("/{amenityType}")
    public ResponseEntity<ListingAmenitiesDto> removeAmenityFromListing(@PathVariable @NotNull Long listingId,
                                                                        @PathVariable @NotNull AmenityType amenityType) {
        return ResponseEntity.ok(listingAmenitiesService.removeAmenityFromListing(listingId, amenityType));
    }

    @Operation(summary = "Remove all amenities from a listing", description = "Deletes all amenities associated with the specified listing")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "All amenities removed successfully"),
            @ApiResponse(responseCode = "404", description = "Listing not found")
    })
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN', 'USER')")
    @DeleteMapping
    public ResponseEntity<Void> deleteAmenityFromListing(@PathVariable @NotNull Long listingId) {
        listingAmenitiesService.removeAllAmenitiesFromListing(listingId);
        return ResponseEntity.noContent().build();
    }
}
