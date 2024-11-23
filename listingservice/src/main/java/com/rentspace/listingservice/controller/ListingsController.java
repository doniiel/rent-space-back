package com.rentspace.listingservice.controller;

import com.rentspace.listingservice.dto.ListingsDto;
import com.rentspace.listingservice.service.ListingsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/listings")
@RequiredArgsConstructor
@Validated
public class ListingsController {

    private final ListingsService listingsService;

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
}
