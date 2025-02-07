package com.rentspace.listingservice.dto;

import com.rentspace.listingservice.enums.ListingType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ListingDto {
    private Long id;
    private Long userId;
    private String title;
    private String description;
    private String address;
    private String city;
    private String country;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private ListingType type;
    private Integer maxGuests;
    private Double pricePerNight;
    private List<String> photoUrls;
}
