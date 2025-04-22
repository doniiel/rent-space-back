package com.rentspace.paymentservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient
public interface ListingServiceClient {

    @GetMapping("/{listingId}")
    ResponseEntity<Map> getListingById(@PathVariable("listingId") Long ListingId);
}
