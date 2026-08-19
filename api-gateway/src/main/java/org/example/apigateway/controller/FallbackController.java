package org.example.apigateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FallbackController {

    @GetMapping("/fallback/users")
    public Mono<String> fallbackUsers() {
        return Mono.just("User service is down, try again later");
    }

    @GetMapping("/fallback/notifications")
    public Mono<String> fallbackNotifications() {
        return Mono.just("User service is down, try again later");
    }
}
