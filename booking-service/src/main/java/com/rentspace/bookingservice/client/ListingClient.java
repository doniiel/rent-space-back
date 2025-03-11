package com.rentspace.bookingservice.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "listing-service", url = "${listing-service.url}")
public interface ListingClient {
    @GetMapping("/{listingId}/availibility/check")
    Boolean checkAvailability(
            @PathVariable Long listingId,
            @RequestParam String startDate,
            @RequestParam String endDate);
}
