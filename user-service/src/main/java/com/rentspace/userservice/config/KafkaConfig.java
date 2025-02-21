package com.rentspace.userservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Value("${event.topic.verification}")
    private String notificationTopic;
    private final static Integer TOPIC_REPLICATION_FACTOR = 3;
    private final static Integer TOPIC_PARTITIONS = 3;

    @Bean
    NewTopic createNotificationTopic() {
        return TopicBuilder
                .name(notificationTopic)
                .partitions(TOPIC_PARTITIONS)
                .replicas(TOPIC_REPLICATION_FACTOR)
                .build();
    }
}
