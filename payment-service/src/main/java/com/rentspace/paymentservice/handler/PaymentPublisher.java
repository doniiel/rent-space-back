package com.rentspace.paymentservice.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publish(String topic, Long key, Object event, String eventType) {
        try {
            kafkaTemplate.send(topic, key.toString(), event);
            log.info("Published {} event with key: {}", eventType, key);
        } catch (Exception e) {
            log.error("Failed to publish {} event with key: {}", eventType, key, e);
            throw new RuntimeException("Failed to publish " + eventType + " event", e);
        }
    }
}
