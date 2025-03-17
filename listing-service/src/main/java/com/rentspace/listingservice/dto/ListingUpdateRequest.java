package com.rentspace.listingservice.dto;

import com.rentspace.listingservice.enums.ListingType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Request object for updating an existing listing")
public class ListingUpdateRequest {

    @NotBlank(message = "Title cannot be empty.")
    @Size(min = 5, max = 100, message = "Title should be between 5 and 100 characters.")
    @Schema(description = "Updated title of the listing", example = "Updated Cozy Apartment")
    private String title;

    @NotBlank(message = "Description cannot be empty.")
    @Size(min = 5, max = 1000, message = "Description should be between 5 and 1000 characters.")
    @Schema(description = "Updated description of the listing", example = "Updated cozy apartment with a view.", required = true)
    private String description;

    @NotBlank(message = "Address cannot be empty.")
    @Size(max = 255, message = "Address cannot exceed 255 characters.")
    @Schema(description = "Updated address of the listing", example = "456 Oak St")
    private String address;

    @NotBlank(message = "City cannot be empty.")
    @Size(max = 100, message = "City cannot exceed 100 characters.")
    @Schema(description = "Updated city where the listing is located", example = "Boston")
    private String city;

    @NotBlank(message = "Country cannot be empty.")
    @Size(max = 100, message = "Country cannot exceed 100 characters.")
    @Schema(description = "Updated country where the listing is located", example = "USA")
    private String country;

    @NotNull(message = "Latitude cannot be null.")
    @Schema(description = "Updated latitude coordinate of the listing location", example = "42.3601")
    private BigDecimal latitude;

    @NotNull(message = "Longitude cannot be null.")
    @Schema(description = "Updated longitude coordinate of the listing location", example = "-71.0589")
    private BigDecimal longitude;

    @NotNull(message = "Listing type cannot be null.")
    @Schema(description = "Updated type of the listing", example = "HOUSE", required = true)
    private ListingType type;

    @NotNull(message = "Max guests cannot be null.")
    @Positive(message = "Max guests must be greater than zero.")
    @Min(value = 1, message = "Max guests must be at least 1.")
    @Schema(description = "Updated maximum number of guests allowed", example = "6", required = true)
    private Integer maxGuests;

    @NotNull(message = "Price per night cannot be null.")
    @Positive(message = "Price per night must be greater than zero.")
    @DecimalMin(value = "0.1", message = "Price per night must be at least 0.1.")
    @Schema(description = "Updated price per night for the listing", example = "150.75", required = true)
    private Double pricePerNight;

//    @Size(max = 10, message = "Cannot add more than 10 new photos.")
//    @Schema(description = "New photos to be added to the listing", required = false)
//    private List<MultipartFile> newPhotos;
//
//    @Schema(description = "URLs of existing photos to retain", example = "[\"http://example.com/photo1.jpg\"]", required = false)
//    private List<String> existingPhotoUrls;
}
