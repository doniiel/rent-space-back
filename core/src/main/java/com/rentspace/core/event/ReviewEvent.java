package com.rentspace.core.event;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ReviewEvent {
    private Long id;
    private Long userId;
    private Long listingId;
    private Integer rating;
    private String comment;
    private String eventType;
    private Double averageRating;
}
