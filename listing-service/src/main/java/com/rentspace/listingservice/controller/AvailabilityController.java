    package com.rentspace.listingservice.controller;

    import com.rentspace.listingservice.dto.ListingAvailabilityDto;
    import com.rentspace.listingservice.dto.ListingAvailabilityRequest;
    import com.rentspace.listingservice.service.ListingAvailabilityService;
    import lombok.RequiredArgsConstructor;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @RestController
    @RequiredArgsConstructor
    @RequestMapping("/api/v1/availability")
    public class AvailabilityController {
        private final ListingAvailabilityService service;

        @GetMapping("/{listingId}")
        public ResponseEntity<List<ListingAvailabilityDto>> getAvailabilityByListingId(@PathVariable Long listingId) {
            return ResponseEntity.ok(service.getAvailabilityByListing(listingId));
        }

        @PostMapping("create/{listingId}")
        public ResponseEntity<ListingAvailabilityDto> createAvailability(@PathVariable Long listingId, @RequestBody ListingAvailabilityRequest request) {
            return ResponseEntity.ok(service.setAvailability(listingId, request));
        }

        @PutMapping("update/{listingId}")
        public ResponseEntity<ListingAvailabilityDto> updateAvailability(@PathVariable Long listingId, @RequestBody ListingAvailabilityRequest request) {
            return ResponseEntity.ok(service.setAvailability(listingId, request));
        }
    }
