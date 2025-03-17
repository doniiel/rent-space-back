package com.rentspace.listingservice.dto;

import com.rentspace.listingservice.enums.ListingType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Request object for creating a new listing.")
public class ListingCreateRequest {

    @NotNull(message = "User ID cannot be null.")
    @Min(value = 1, message = "User ID must be greater than zero.")
    @Schema(description = "Identifier of the user creating the listing", example = "1")
    private Long userId;

    @NotBlank(message = "Title cannot be empty.")
    @Size(min = 5, max = 100, message = "Title should be between 5 and 100 characters.")
    @Schema(description = "Title of the listing", example = "Cozy Apartment in Downtown")
    private String title;

    @NotBlank(message = "Description cannot be empty.")
    @Size(min = 5, max = 1000, message = "Description should be between 5 and 1000 characters.")
    @Schema(description = "Description of the listing", example = "A cozy apartment with a great view.")
    private String description;

    @NotBlank(message = "Address cannot be empty.")
    @Size(max = 255, message = "Address cannot exceed 255 characters.")
    @Schema(description = "Address of the listing", example = "123 Main Str")
    private String address;

    @NotBlank(message = "City cannot be empty.")
    @Size(max = 100, message = "City cannot exceed 100 characters.")
    @Schema(description = "City where the listing is located", example = "New York")
    private String city;

    @NotBlank(message = "Country cannot be empty.")
    @Size(max = 100, message = "Country cannot exceed 100 characters.")
    @Schema(description = "Country where the listing is located", example = "USA")
    private String country;

    @NotNull(message = "Latitude cannot be null.")
    @Schema(description = "Latitude coordinate of the listing location", example = "40.7128")
    private BigDecimal latitude;

    @NotNull(message = "Longitude cannot be null.")
    @Schema(description = "Longitude coordinate of the listing location", example = "-74.0060")
    private BigDecimal longitude;

    @NotNull(message = "Listing type cannot be null.")
    @Schema(description = "Type of the listing", example = "APARTMENT")
    private ListingType type;

    @NotNull(message = "Max guests cannot be null.")
    @Positive(message = "Max guests must be greater than zero.")
    @Min(value = 1, message = "Max guests must be at least 1.")
    @Schema(description = "Maximum number of guests allowed", example = "4")
    private Integer maxGuests;

    @NotNull(message = "Price per night cannot be null.")
    @DecimalMin(value = "0.1", message = "Price per night must be greater than 0.1.")
    @Schema(description = "Price per night for the listing", example = "100.50")
    private Double pricePerNight;

//    @NotEmpty
//    @Size(min = 1, message = "At least one photo is required.")
//    @Size(max = 10, message = "A listing cannot have more than 10 listingPhotos.")
//    private List<MultipartFile> photos;
}
