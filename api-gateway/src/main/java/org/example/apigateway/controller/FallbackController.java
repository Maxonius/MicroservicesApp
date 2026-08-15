package org.example.apigateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FallbackController {

    @GetMapping("/fallback/users")
    public Mono<String> userServiceFallback(){
        return Mono.just("User service is down, try again later");
    }

    @GetMapping("/fallback/email")
    public Mono<String> emailServiceFallback(){
        return Mono.just("Email service is down, try again later");
    }
}
