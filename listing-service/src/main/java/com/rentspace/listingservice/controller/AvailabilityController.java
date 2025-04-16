package com.rentspace.listingservice.controller;

import com.rentspace.listingservice.dto.DateRangeRequest;
import com.rentspace.listingservice.dto.ListingAvailabilityDto;
import com.rentspace.listingservice.dto.ListingAvailabilityRequest;
import com.rentspace.listingservice.service.ListingAvailabilityService;
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

@Tag(name = "Availability REST API in RentSpace", description = "APIs to manage listing availability")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/listings/{listingId}/availability")
public class AvailabilityController {

    private final ListingAvailabilityService service;

    @Operation(summary = "Get availability for a listing", description = "Retrieves all availability periods for the specified listing. Returns an empty list if no availability records exist.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Availability retrieved successfully (or empty list if none exist)"),
            @ApiResponse(responseCode = "404", description = "Listing not found")
    })
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER')")
    @GetMapping
    public ResponseEntity<List<ListingAvailabilityDto>> getAvailabilityByListingId(@PathVariable @NotNull Long listingId) {
        return ResponseEntity.ok(service.getAvailabilityByListing(listingId));
    }

    @Operation(summary = "Create availability for a listing", description = "Sets a new availability period for the specified listing. Fails if the period overlaps with an existing one. Dates must be in 'yyyy-MM-dd'T'HH:mm:ss' format (e.g., '2025-04-01T10:00:00').")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Availability created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid date range, format, or period overlaps with an existing one"),
            @ApiResponse(responseCode = "404", description = "Listing not found")
    })
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN', 'USER')")
    @PostMapping
    public ResponseEntity<ListingAvailabilityDto> createAvailability(
            @PathVariable @NotNull Long listingId,
            @Valid @RequestBody ListingAvailabilityRequest request) {
        return ResponseEntity.status(CREATED).body(service.setAvailability(listingId, request));
    }

    @Operation(summary = "Update availability for a listing", description = "Updates an existing availability period for the specified listing. Creates a new one if no matching record exists, but fails if the period overlaps with another existing one. Dates must be in 'yyyy-MM-dd'T'HH:mm:ss' format (e.g., '2025-04-01T10:00:00').")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Availability updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid date range, format, or period overlaps with an existing one"),
            @ApiResponse(responseCode = "404", description = "Listing not found")
    })
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN', 'USER')")
    @PutMapping("/{availabilityId}")
    public ResponseEntity<ListingAvailabilityDto> updateAvailability(
            @PathVariable @NotNull Long listingId,
            @PathVariable @NotNull Long availabilityId,
            @Valid @RequestBody ListingAvailabilityRequest request) {
        return ResponseEntity.ok(service.updateAvailability(listingId, availabilityId, request));
    }

    @Operation(summary = "Delete availability", description = "Deletes an availability period for the specified listing by its availability ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Availability deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Listing or availability record not found")
    })
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN', 'USER')")
    @DeleteMapping("/{availabilityId}")
    public ResponseEntity<Void> deleteAvailability(
            @PathVariable @NotNull Long listingId,
            @PathVariable @NotNull Long availabilityId) {
        service.deleteAvailability(listingId, availabilityId);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Check availability", description = "Checks if the listing is available for the specified date range. Dates must be in 'yyyy-MM-dd'T'HH:mm:ss' format (e.g., '2025-04-01T00:00:00').")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Availability status returned"),
            @ApiResponse(responseCode = "400", description = "Invalid date range or format"),
            @ApiResponse(responseCode = "404", description = "Listing not found")
    })
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER')")
    @GetMapping("/check")
    public ResponseEntity<Boolean> checkAvailability(
            @PathVariable @NotNull Long listingId,
            @Valid @RequestBody DateRangeRequest dateRange) {
        return ResponseEntity.ok(service.isAvailable(listingId, dateRange.getStartDate(), dateRange.getEndDate()));
    }

    @Operation(summary = "Block availability", description = "Blocks the specified date range for the listing. Dates must be in 'yyyy-MM-dd'T'HH:mm:ss' format (e.g., '2025-04-01T00:00:00').")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Availability blocked successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid date range or format"),
            @ApiResponse(responseCode = "404", description = "Listing not found")
    })
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN', 'USER')")
    @PostMapping("/block")
    public ResponseEntity<Void> blockAvailability(
            @PathVariable @NotNull Long listingId,
            @Valid @RequestBody DateRangeRequest dateRange) {
        service.blockAvailability(listingId, dateRange.getStartDate(), dateRange.getEndDate());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Unblock availability", description = "Unblocks the specified date range for the listing. Dates must be in 'yyyy-MM-dd'T'HH:mm:ss' format (e.g., '2025-04-01T00:00:00').")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Availability unblocked successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid date range or format"),
            @ApiResponse(responseCode = "404", description = "Listing not found")
    })
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN', 'USER')")
    @DeleteMapping("/unblock")
    public ResponseEntity<Void> unblockAvailability(
            @PathVariable @NotNull Long listingId,
            @Valid @RequestBody DateRangeRequest dateRange) {
        service.unblockAvailability(listingId, dateRange.getStartDate(), dateRange.getEndDate());
        return ResponseEntity.noContent().build();
    }
}