package com.renstapce.review.dto;

import lombok.Data;

@Data
public class ReviewCreateRequest {
    private Long listingId;
    private Long userId;
    private String comment;
    private Integer rating;
}
