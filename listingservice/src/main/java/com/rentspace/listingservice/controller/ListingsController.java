package com.rentspace.listingservice.controller;

import com.rentspace.listingservice.service.ListingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/listings")
@RequiredArgsConstructor
public class ListingsController {

    private final ListingsService listingsService;

}
