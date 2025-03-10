package com.rentspace.paymentservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class AppConfig {

    @Value("${event.topic.payment.success}")
    private String paymentSuccessTopic;
    @Value("${event.topic.payment.failure}")
    private String paymentFailureTopic;

    private final static Integer TOPIC_REPLICATION_FACTOR = 1;
    private final static Integer TOPIC_PARTITIONS = 3;

    @Bean
    public NewTopic paymentSuccessTopic() {
        return TopicBuilder
                .name(paymentSuccessTopic)
                .partitions(TOPIC_PARTITIONS)
                .replicas(TOPIC_REPLICATION_FACTOR)
                .build();
    }

    @Bean
    public NewTopic paymentFailureTopic() {
        return TopicBuilder
                .name(paymentFailureTopic)
                .partitions(TOPIC_PARTITIONS)
                .replicas(TOPIC_REPLICATION_FACTOR)
                .build();
    }
}
