package com.rentspace.core.event;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListingEvent {
    private Long id;
    private Long userId;
    private String title;
    private String description;
    private String address;
    private String city;
    private String country;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String type;
    private Integer maxGuests;
    private Double pricePerNight;
    private String eventType;
}
