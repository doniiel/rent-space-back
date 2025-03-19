package com.rentspace.bookingservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class AppConfig {
    private final static Integer TOPIC_REPLICATION_FACTOR = 1;
    private final static Integer TOPIC_PARTITIONS = 3;

    @Value("${event.topic.payment.success}")
    private String paymentSuccessTopic;

    @Value("${event.topic.payment.failure}")
    private String paymentFailureTopic;

    @Bean
    public NewTopic newTopic() {
        return TopicBuilder
                .name(paymentSuccessTopic)
                .partitions(TOPIC_PARTITIONS)
                .replicas(TOPIC_REPLICATION_FACTOR)
                .build();
    }

    @Bean
    public NewTopic newTopic2() {
        return TopicBuilder
                .name(paymentFailureTopic)
                .partitions(TOPIC_PARTITIONS)
                .replicas(TOPIC_REPLICATION_FACTOR)
                .build();
    }
}
