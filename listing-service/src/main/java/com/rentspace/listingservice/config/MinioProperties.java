package com.rentspace.listingservice.config;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "minio")
@AllArgsConstructor
@NoArgsConstructor
public class MinioProperties {

    private String bucket;
    private String url;
    private String accessKey;
    private String secretKey;
}
