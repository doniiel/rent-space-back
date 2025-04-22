package com.rentspace.bookingservice.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "listing-service")
public interface ListingServiceClient {

    @GetMapping("/{listingId}")
    ResponseEntity<Map> getListingById(@PathVariable Long listingId);

    @GetMapping("/{listingId}/availibility/check")
    Boolean checkAvailability(
            @PathVariable Long listingId,
            @RequestParam String startDate,
            @RequestParam String endDate);
}
