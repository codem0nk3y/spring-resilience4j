package com.example.springresiliencedemo.service;

import com.example.springresiliencedemo.model.User;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.beans.factory.annotation.Value;

@Service
@Slf4j
public class UserService {

    private final RestClient restClient;
    private final String baseUrl;

    public UserService(RestClient restClient, @Value("${api.base-url}") String baseUrl) {
        this.restClient = restClient;
        this.baseUrl = baseUrl;
    }

    @CircuitBreaker(name = "userService", fallbackMethod = "getUserFallback")
    @RateLimiter(name = "userService")
    public User getUserById(Long id) {
        log.info("Fetching user with id: {}", id);
        try {
            return restClient.get()
                    .uri(baseUrl + "/api/users/{id}", id)
                    .retrieve()
                    .body(User.class);
        } catch (Exception e) {
            log.error("Error fetching user: {}", e.getMessage());
            throw e;
        }
    }

    public User getUserFallback(Long id, Exception ex) {
        log.error("Fallback executed for user id: {}, error: {}", id, ex.getMessage());
        return new User(id, "Fallback User", "fallback@example.com");
    }
} 