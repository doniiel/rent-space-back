package com.rentspace.gateway_service.filter;

import com.rentspace.gateway_service.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {
    private final JwtUtil jwtUtil;

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (isAuthEndpoint(exchange)) {
                return chain.filter(exchange);
            }

            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return unauthorizedResponse(exchange, "Missing Authorization header");
            }

            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return unauthorizedResponse(exchange, "Invalid Authorization header");
            }

            String token = authHeader.substring(7);
            if (!jwtUtil.isTokenValid(token)) {
                return unauthorizedResponse(exchange, "Invalid or expired token");
            }

            String username = jwtUtil.extractUsername(token);
            exchange.getRequest().mutate()
                    .header("X-Username", username)
                    .build();

            return chain.filter(exchange);
        };
    }

    private boolean isAuthEndpoint(ServerWebExchange exchange) {
        String path = exchange.getRequest().getURI().getPath();
        return path.startsWith("/rentspace/auth/login") ||
                path.startsWith("/rentspace/auth/register") ||
                path.startsWith("/rentspace/auth/refresh") ||
                path.startsWith("/rentspace/auth/confirm");
    }

    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String message) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().add("Content-Type", "application/json");

        DataBuffer buffer = exchange.getResponse()
                .bufferFactory()
                .wrap(("{\"error\":\"" + message + "\"}").getBytes());

        return exchange.getResponse().writeWith(Mono.just(buffer))
                .doOnError(e -> log.error("Error writing response: {}", e.getMessage()));
    }

    public static class Config {
    }
}