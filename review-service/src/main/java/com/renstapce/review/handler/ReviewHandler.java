package com.renstapce.review.handler;

import com.renstapce.review.entity.Review;
import com.rentspace.core.event.ReviewEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewHandler {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishReviewEvent(Review review, String eventType) {
        ReviewEvent event = ReviewEvent.builder()
                .id(review.getId())
                .userId(review.getUserId())
                .listingId(review.getListingId())
                .comment(review.getComment())
                .rating(review.getRating())
                .eventType(eventType)
                .build();
        kafkaTemplate.send("${event.topic.review.updated}", event);
        log.info("Published review event: {}", event);
    }
}
