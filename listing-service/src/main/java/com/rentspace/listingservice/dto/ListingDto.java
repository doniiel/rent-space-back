package com.rentspace.listingservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ListingDto {

    private Long id;

    @NotNull(message = "User ID cannot be null.")
    @Min(value = 1, message = "User ID must be greater than zero.")
    private Long userId;

    @NotBlank(message = "Title cannot be empty.")
    @Size(min = 5, max = 100, message = "Title should be between 5 and 100 characters.")
    private String title;

    @NotBlank(message = "Description cannot be empty.")
    @Size(min = 5, max = 1000, message = "Description should be between 5 and 1000 characters.")
    private String description;

    private String address;
    private String city;
    private String country;

    private BigDecimal latitude;
    private BigDecimal longitude;

    private String type;
    private Integer maxGuests;

    @NotNull(message = "Price per night cannot be null.")
    @DecimalMin(value = "0.1", message = "Price per night must be greater than zero.")
    private Double pricePerNight;

    @Valid
    @Size(min = 1, message = "At least one photo is required.")
    @Size(max = 10, message = "A listing cannot have more than 10 listingPhotos.")
    private List<ListingPhotoDto> photos;
}
