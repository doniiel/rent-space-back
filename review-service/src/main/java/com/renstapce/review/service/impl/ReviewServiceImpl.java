package com.renstapce.review.service.impl;

import com.renstapce.review.dto.ReviewCreateRequest;
import com.renstapce.review.dto.ReviewDto;
import com.renstapce.review.dto.ReviewUpdateRequest;
import com.renstapce.review.entity.Review;
import com.renstapce.review.exception.ReviewNotFoundException;
import com.renstapce.review.handler.ReviewHandler;
import com.renstapce.review.mapper.ReviewMapper;
import com.renstapce.review.repository.ReviewRepository;
import com.renstapce.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository repository;
    private final ReviewHandler reviewHandler;
    private final ReviewMapper mapper;

    @Override
    public ReviewDto createReview(ReviewCreateRequest request) {
        Review review = mapper.toEntity(request);
        Review savedReview = repository.save(review);
        reviewHandler.publishReviewEvent(savedReview, "CREATED");
        return mapper.toDto(savedReview);
    }

    @Override
    public ReviewDto getReview(Long id) {
        Review review = repository.findById(id).orElseThrow(
                () -> new ReviewNotFoundException("Review with id " + id + " not found"));
        return mapper.toDto(review);
    }

    @Override
    public List<ReviewDto> getReviewsByListing(Long listingId) {
        return repository.findByListingId(listingId).stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public ReviewDto updateReview(Long id, ReviewUpdateRequest request) {
        Review review = repository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("Review with id " + id + " not found"));
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        Review updatedReview = repository.save(review);
        reviewHandler.publishReviewEvent(updatedReview, "UPDATED");
        return mapper.toDto(updatedReview);
    }

    @Override
    public void deleteReview(Long id) {
        Review review = repository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("Review with id " + id + " not found"));
        repository.delete(review);
        reviewHandler.publishReviewEvent(review, "DELETED");
    }
}
