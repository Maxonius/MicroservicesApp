package org.example.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.path;

@Configuration
public class GatewayConfig {

    @Bean
    public RouterFunction<ServerResponse> userServiceRoute() {
        return route("user-service-route")
                .route(path("/api/users/**"), http())
                .before(uri("http://localhost:8083"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> demoCircuitBreakerRoute() {
        return route("demo-circuit-breaker-route")
                .route(path("/api/email/**"), http())
                .before(uri("http://localhost:8085"))  // Порт demo-circuit-breaker
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> swaggerRoute() {
        return route("swagger-route")
                .route(path("/swagger-ui/**"), http())
                .before(uri("http://localhost:8083"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> apiDocsRoute() {
        return route("api-docs-route")
                .route(path("/api-docs/**"), http())
                .before(uri("http://localhost:8083"))
                .build();
    }
}