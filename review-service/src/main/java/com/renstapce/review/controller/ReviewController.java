package com.renstapce.review.controller;

import com.renstapce.review.dto.ReviewCreateRequest;
import com.renstapce.review.dto.ReviewDto;
import com.renstapce.review.dto.ReviewUpdateRequest;
import com.renstapce.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@Tag(name = "Reviews REST API in RentSpace", description = "APIs to manage reviews")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @Operation(summary = "Create a new review", description = "Create a new review for a listing")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Review created successfully",
                    content = @Content(schema = @Schema(implementation = ReviewDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping
    public ResponseEntity<ReviewDto> createReview(@RequestBody @Valid ReviewCreateRequest request) {
        return ResponseEntity.status(CREATED).body(reviewService.createReview(request));
    }

    @Operation(summary = "Get a review by ID", description = "Get a review by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review found",
                    content = @Content(schema = @Schema(implementation = ReviewDto.class))),
            @ApiResponse(responseCode = "404", description = "Review not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ReviewDto> getReview(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getReview(id));
    }

    @Operation(summary = "Get reviews by listing ID", description = "Retrieves all reviews for a specific listing")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reviews retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ReviewDto.class)))
    })
    @GetMapping("/listings/{listingId}")
    public ResponseEntity<List<ReviewDto>> getReviewsByListing(@PathVariable Long listingId) {
        return ResponseEntity.ok(reviewService.getReviewsByListing(listingId));
    }

    @Operation(summary = "Update a review", description = "Update an existing review by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review updated successfully",
                    content = @Content(schema = @Schema(implementation = ReviewDto.class))),
            @ApiResponse(responseCode = "404", description = "Review not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ReviewDto> updateReview(@PathVariable Long id, @RequestBody @Valid ReviewUpdateRequest request) {
        return ResponseEntity.ok(reviewService.updateReview(id, request));
    }

    @Operation(summary = "Delete a review", description = "Deletes a review by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Review deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Review not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
