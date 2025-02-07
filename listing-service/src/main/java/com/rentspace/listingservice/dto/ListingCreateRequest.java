package com.rentspace.listingservice.dto;

import com.rentspace.listingservice.enums.ListingType;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;

@Data
public class ListingCreateRequest {
    @NotNull(message = "User ID cannot be null.")
    @Min(value = 1, message = "User ID must be greater than zero.")
    private Long userId;

    @NotBlank(message = "Title cannot be empty.")
    @Size(min = 5, max = 100, message = "Title should be between 5 and 100 characters.")
    private String title;

    @NotBlank(message = "Description cannot be empty.")
    @Size(min = 5, max = 1000, message = "Description should be between 5 and 1000 characters.")
    private String description;

    @NotBlank
    private String address;

    @NotBlank
    private String city;

    @NotBlank
    private String country;

    @NonNull
    private BigDecimal latitude;

    @NonNull
    private BigDecimal longitude;

    @NonNull
    private ListingType type;

    @Positive
    private Integer maxGuests;

    @NotNull(message = "Price per night cannot be null.")
    @DecimalMin(value = "0.1", message = "Price per night must be greater than zero.")
    private Double pricePerNight;

//    @NotEmpty
//    @Size(min = 1, message = "At least one photo is required.")
//    @Size(max = 10, message = "A listing cannot have more than 10 listingPhotos.")
//    private List<MultipartFile> photos;
}
