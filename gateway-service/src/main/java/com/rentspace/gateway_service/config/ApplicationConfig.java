package com.rentspace.gateway_service.config;

import com.rentspace.gateway_service.filter.AuthFilter;
import com.rentspace.gateway_service.filter.LoggingFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final AuthFilter authFilter;
    private final LoggingFilter loggingFilter;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service", route -> route
                        .path("/rentspace/users/**", "/rentspace/auth/**")
                        .filters(filter -> filter
                                .rewritePath("/rentspace/(?<segment>.*)", "/${segment}")
                                .filter(authFilter.apply(new AuthFilter.Config()))
                                .filter(loggingFilter.apply(new LoggingFilter.Config())))
                        .uri("lb://USERSERVICE"))

                .route("listing-service", route -> route
                        .path("/rentspace/listings/**")
                        .filters(filter -> filter
                                .rewritePath("/rentspace/listings/(?<segment>.*)", "/${segment}")
                                .filter(authFilter.apply(new AuthFilter.Config()))
                                .filter(loggingFilter.apply(new LoggingFilter.Config())))
                        .uri("lb://LISTINGSERVICE"))

                .route("notification-service", route -> route
                        .path("/rentspace/notifications/**")
                        .filters(filter -> filter
                                .rewritePath("/rentspace/notifications/(?<segment>.*)", "/${segment}")
                                .filter(authFilter.apply(new AuthFilter.Config()))
                                .filter(loggingFilter.apply(new LoggingFilter.Config())))
                        .uri("lb://NOTIFICATIONSERVICE"))

                .build();
    }
}