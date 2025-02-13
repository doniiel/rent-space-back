    package com.rentspace.listingservice.controller;

    import com.rentspace.listingservice.dto.ListingAvailabilityDto;
    import com.rentspace.listingservice.dto.ListingAvailabilityRequest;
    import com.rentspace.listingservice.service.ListingAvailabilityService;
    import jakarta.validation.Valid;
    import lombok.RequiredArgsConstructor;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @RestController
    @RequiredArgsConstructor
    @RequestMapping("/api/v1/listings/{listingId}/availability")
    public class AvailabilityController {
        private final ListingAvailabilityService service;

        @GetMapping
        public ResponseEntity<List<ListingAvailabilityDto>> getAvailabilityByListingId(@PathVariable Long listingId) {
            return ResponseEntity.ok(service.getAvailabilityByListing(listingId));
        }

        @PostMapping
        public ResponseEntity<ListingAvailabilityDto> createAvailability(@PathVariable Long listingId, @Valid @RequestBody ListingAvailabilityRequest request) {
            return ResponseEntity.ok(service.setAvailability(listingId, request));
        }

        @PutMapping
        public ResponseEntity<ListingAvailabilityDto> updateAvailability(@PathVariable Long listingId, @Valid @RequestBody ListingAvailabilityRequest request) {
            return ResponseEntity.ok(service.setAvailability(listingId, request));
        }
    }
