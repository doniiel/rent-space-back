package com.rentspace.listingservice.dto;

import com.rentspace.listingservice.enums.ListingType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ListingUpdateRequest {
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

    @NotNull
    private BigDecimal latitude;

    @NotNull
    private BigDecimal longitude;

    @NotNull
    private ListingType type;

    @Positive
    private Integer maxGuests;

    @Positive
    private Double pricePerNight;

//    private List<MultipartFile> newPhotos;
//
//    private List<String> existingPhotoUrls;
}
