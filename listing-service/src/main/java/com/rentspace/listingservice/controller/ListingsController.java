package com.rentspace.listingservice.controller;

import com.rentspace.listingservice.dto.ListingCreateRequest;
import com.rentspace.listingservice.dto.ListingDto;
import com.rentspace.listingservice.dto.ListingUpdateRequest;
import com.rentspace.listingservice.service.ListingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@Tag(name = "Listings REST API in RentSpace", description = "APIs to manage listings")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/listings")
public class ListingsController {
    private final ListingsService listingsService;

    @Operation(summary = "Create a new listing", description = "Creates a new listing with the provided details")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Listing created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN', 'USER')")
    @PostMapping
    public ResponseEntity<ListingDto> createListing(@Valid @RequestBody ListingCreateRequest request) {
        return ResponseEntity.status(CREATED).body(listingsService.createListing(request));
    }

    @Operation(summary = "Get a listing by ID", description = "Retrieves details of a specific listing")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listing retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Listing not found")
    })
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER')")
    @GetMapping("/{id}")
    public ResponseEntity<ListingDto> getListingById(@PathVariable @NotNull Long id) {
        return ResponseEntity.ok(listingsService.getListingById(id));
    }

    @Operation(summary = "Get all listings", description = "Retrieves a list of all listings")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listings retrieved successfully")
    })
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER')")
    @GetMapping
    public ResponseEntity<List<ListingDto>> getAllListings() {
        return ResponseEntity.ok(listingsService.getAllListings());
    }

    @Operation(summary = "Get all listings for a user", description = "Retrieves all listings owned by a specific user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User listings retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ListingDto>> getAllUserListings(@PathVariable @NotNull Long userId) {
        return ResponseEntity.ok(listingsService.getAllUserListings(userId));
    }

    @Operation(summary = "Update a listing", description = "Updates an existing listing with the provided details")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listing updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Listing not found")
    })
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN', 'USER') or #id == authentication.principal.id")
    @PutMapping("/{id}")
    public ResponseEntity<ListingDto> updateListing(@PathVariable @NotNull Long id,
                                                    @Valid @RequestBody ListingUpdateRequest request) {
        return ResponseEntity.ok(listingsService.updateListing(id, request));
    }

    @Operation(summary = "Delete a listing", description = "Deletes a specific listing")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Listing deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Listing not found")
    })
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN', 'USER') or #id == authentication.principal.id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteListing(@PathVariable @NotNull Long id) {
        listingsService.deleteListing(id);
        return ResponseEntity.noContent().build();
    }
}
