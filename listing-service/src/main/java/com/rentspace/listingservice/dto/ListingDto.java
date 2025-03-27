package com.rentspace.listingservice.dto;

import com.rentspace.listingservice.enums.ListingType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Schema(description = "Response object representing a listing with its details and photo URLs")
public class ListingDto {

    @Schema(description = "Unique identifier of the listing", example = "1")
    private Long id;

    @Schema(description = "Identifier of the user who owns the listing", example = "1")
    private Long userId;

    @Schema(description = "Title of the listing", example = "Cozy Apartment")
    private String title;

    @Schema(description = "Description of the listing", example = "A cozy apartment.")
    private String description;

    @Schema(description = "Address of the listing", example = "123 Main St")
    private String address;

    @Schema(description = "City where the listing is located", example = "New York")
    private String city;

    @Schema(description = "Country where the listing is located", example = "USA")
    private String country;

    @Schema(description = "Latitude coordinate", example = "40.7128")
    private BigDecimal latitude;

    @Schema(description = "Longitude coordinate", example = "-74.0060")
    private BigDecimal longitude;

    @Schema(description = "Type of the listing", example = "APARTMENT")
    private ListingType type;

    @Schema(description = "Maximum number of guests allowed", example = "4")
    private Integer maxGuests;

    @Schema(description = "Price per night for the listing", example = "100.50")
    private Double pricePerNight;

    @Schema(description = "Average rating of the listing", example = "4.5")
    private Double averageRating;

    @Schema(description = "List of URLs for the listing's photos", example = "[\"http://example.com/photo1.jpg\"]")
    private List<String> photoUrls;
}