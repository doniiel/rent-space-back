package com.rentspace.listingservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class AppConfig {
    private final static Integer TOPIC_REPLICATION_FACTOR = 1;
    private final static Integer TOPIC_PARTITIONS = 3;

    @Value("${event.topic.listing.availability.request}")
    private String requestTopic;

    @Value("${event.topic.listing.availability.response}")
    private String responseTopic;

    @Value("${event.topic.listing.availability.block}")
    private String blockTopic;

    @Value("${event.topic.listing.availability.unblock}")
    private String unblockTopic;

    @Bean
    public NewTopic requestTopic() {
        return TopicBuilder.name(requestTopic)
                .partitions(TOPIC_PARTITIONS)
                .replicas(TOPIC_REPLICATION_FACTOR)
                .build();
    }

    @Bean
    public NewTopic responseTopic() {
        return TopicBuilder.name(responseTopic)
                .partitions(TOPIC_PARTITIONS)
                .replicas(TOPIC_REPLICATION_FACTOR)
                .build();
    }

    @Bean
    public NewTopic blockTopic() {
        return TopicBuilder.name(blockTopic)
                .partitions(TOPIC_PARTITIONS)
                .replicas(TOPIC_REPLICATION_FACTOR)
                .build();
    }

    @Bean
    public NewTopic unblockTopic() {
        return TopicBuilder.name(unblockTopic)
                .partitions(TOPIC_PARTITIONS)
                .replicas(TOPIC_REPLICATION_FACTOR)
                .build();
    }

}
