package com.renstapce.review.controller;

import com.renstapce.review.dto.ReviewCreateRequest;
import com.renstapce.review.dto.ReviewDto;
import com.renstapce.review.dto.ReviewUpdateRequest;
import com.renstapce.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewDto> createReview(@RequestBody @Valid ReviewCreateRequest request) {
        return ResponseEntity.status(CREATED).body(reviewService.createReview(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDto> getReview(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getReview(id));
    }

    @GetMapping("/listings/{listingId}")
    public ResponseEntity<List<ReviewDto>> getReviewsByListing(@PathVariable Long listingId) {
        return ResponseEntity.ok(reviewService.getReviewsByListing(listingId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewDto> updateReview(@PathVariable Long id, @RequestBody @Valid ReviewUpdateRequest request) {
        return ResponseEntity.ok(reviewService.updateReview(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
