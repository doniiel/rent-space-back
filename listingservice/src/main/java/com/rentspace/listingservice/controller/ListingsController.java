package com.rentspace.listingservice.controller;

import com.rentspace.listingservice.dto.ListingsDto;
import com.rentspace.listingservice.service.ListingsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/v1/listings")
@RequiredArgsConstructor
@Validated
public class ListingsController {

    private final ListingsService listingsService;

    @GetMapping("/fetch/{id}")
    public ResponseEntity<ListingsDto> getListings(@PathVariable("id") Long listingId) {
        return ResponseEntity
                .status(OK)
                .body(listingsService.getListingById(listingId));
    }
    @PostMapping("/create")
    public ResponseEntity<String> createListings(@Valid @RequestBody ListingsDto listingsDto) {
        return ResponseEntity
                .status(CREATED)
                .body(listingsService.createListing(listingsDto));
    }

    @PutMapping("/update")
    public ResponseEntity<ListingsDto> updateListings(@Valid @RequestBody ListingsDto listingsDto) {
        return ResponseEntity
                .status(OK)
                .body(listingsService.updateListing(listingsDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteListings(@PathVariable("id") Long listingId) {
        return ResponseEntity
                .status(OK)
                .body(listingsService.deleteListing(listingId));
    }
}
