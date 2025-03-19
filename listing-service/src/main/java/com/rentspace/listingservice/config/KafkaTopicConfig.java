package com.rentspace.listingservice.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class KafkaTopicConfig {

    private static final int DEFAULT_PARTITIONS = 3;
    private static final int DEFAULT_REPLICATION_FACTOR = 1;

    @Value("${event.topic.listing.availability.request}")
    private String requestTopic;

    @Value("${event.topic.listing.availability.response}")
    private String responseTopic;

    @Value("${event.topic.listing.availability.block}")
    private String blockTopic;

    @Value("${event.topic.listing.availability.unblock}")
    private String unblockTopic;

    @Bean
    public List<NewTopic> kafkaTopics() {
        List<NewTopic> topics = List.of(
                createTopic(requestTopic),
                createTopic(responseTopic),
                createTopic(blockTopic),
                createTopic(unblockTopic)
        );
        log.info("Configured Kafka topics: {}", topics.stream().map(NewTopic::name).toList());
        return topics;
    }

    private NewTopic createTopic(String name) {
        return TopicBuilder.name(name)
                .partitions(DEFAULT_PARTITIONS)
                .replicas(DEFAULT_REPLICATION_FACTOR)
                .build();
    }
}