package com.renstapce.review.dto;


import lombok.Data;

@Data
public class ReviewUpdateRequest {
    private String comment;
    private Integer rating;
}
