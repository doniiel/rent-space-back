package com.renstapce.review.service;

import com.renstapce.review.dto.ReviewCreateRequest;
import com.renstapce.review.dto.ReviewDto;
import com.renstapce.review.dto.ReviewUpdateRequest;

import java.util.List;

public interface ReviewService {
    ReviewDto createReview(ReviewCreateRequest request);
    ReviewDto getReview(Long id);
    List<ReviewDto> getReviewsByListing(Long listingId);
    ReviewDto updateReview(Long id, ReviewUpdateRequest request);
    void deleteReview(Long id);}
