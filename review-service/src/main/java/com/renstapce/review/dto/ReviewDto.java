package com.renstapce.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@Schema(description = "Response containing details for a review")
public class ReviewDto {

    @Schema(description = "Unique identifier for the review", example = "1")
    private Long id;

    @Schema(description = "Unique identifier for the listing", example = "1")
    private Long listingId;

    @Schema(description = "Unique identifier for the user", example = "1")
    private Long userId;

    @Schema(description = "Rating given by the user (1 - 5)", example = "4", minimum = "1", maximum = "5")
    private Integer rating;

    @Schema(description = "Comment provided by the user", example = "Great place!")
    private String comment;

    @Schema(description = "Date and time when the review was created", example = "2023-08-01T10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "Date and time when the review was last updated", example = "2023-08-01T10:00:00")
    private LocalDateTime updatedAt;
}
