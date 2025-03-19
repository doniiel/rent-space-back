package com.rentspace.listingservice.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MinioBucketConfig {
    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucket;

    @Bean
    public CommandLineRunner createBucketIfNotExists() {
        return args -> {
            try {
                boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
                if (!exists) {
                    minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
                    log.info("Created MinIO bucket: {}", bucket);
                } else {
                    log.debug("MinIO bucket {} already exists", bucket);
                }
            } catch (Exception e) {
                log.error("Failed to create MinIO bucket: {}", e.getMessage(), e);
                throw new RuntimeException("MinIO bucket initialization failed", e);
            }
        };
    }
}