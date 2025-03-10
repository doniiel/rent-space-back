package com.rentspace.bookingservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class AppConfig {

    @Value("${event.topic.payment}")
    private String paymentTopic;

    private final static Integer TOPIC_REPLICATION_FACTOR = 1;
    private final static Integer TOPIC_PARTITIONS = 3;

    @Bean
    public NewTopic newTopic() {
        return TopicBuilder
                .name(paymentTopic)
                .partitions(TOPIC_PARTITIONS)
                .replicas(TOPIC_REPLICATION_FACTOR)
                .build();
    }
}
