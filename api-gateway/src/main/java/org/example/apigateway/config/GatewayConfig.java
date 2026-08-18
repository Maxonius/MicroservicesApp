package org.example.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions.circuitBreaker;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;

//@Configuration
public class GatewayConfig {
/*
    @Bean
    public RouterFunction<ServerResponse> userServiceRoute() {
        return route("user-service-route")
                .GET("/api/users/**", http())
                .filter(circuitBreaker("userServiceBreaker", "forward:/fallback/users"))
                .build();
    }


 */
}