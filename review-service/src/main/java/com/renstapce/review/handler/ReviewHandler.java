package com.renstapce.review.handler;

import com.renstapce.review.entity.Review;
import com.rentspace.core.event.ReviewEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewHandler {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${event.topic.review.updated}")
    private String reviewUpdatedTopic;

    @Value("${event.topic.listing.rating.updated}")
    private String listingRatingUpdatedTopic;

    public void publishReviewEvent(Review review, String eventType, double averageRating) {
        ReviewEvent event = ReviewEvent.builder()
                .id(review.getId())
                .userId(review.getUserId())
                .listingId(review.getListingId())
                .comment(review.getComment())
                .rating(review.getRating())
                .eventType(eventType)
                .averageRating(averageRating)
                .build();
        kafkaTemplate.send(reviewUpdatedTopic, event);
        kafkaTemplate.send(listingRatingUpdatedTopic, event);
        log.info("Published review event: {}", event);
    }
}
