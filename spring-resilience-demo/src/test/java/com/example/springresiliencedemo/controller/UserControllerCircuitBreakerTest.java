package com.example.springresiliencedemo.controller;

import com.example.springresiliencedemo.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerCircuitBreakerTest {

    @RegisterExtension
    static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getUserById_WhenCircuitBreakerOpen_ShouldReturnFallbackResponse() throws Exception {
        // Configure WireMock to return 500 error
        wireMockServer.stubFor(WireMock.get("/api/users/1")
                .willReturn(aResponse()
                        .withStatus(500)
                        .withHeader("Content-Type", "application/json")));

        // Make multiple calls to trigger circuit breaker
        for (int i = 0; i < 5; i++) {
            mockMvc.perform(get("/api/users/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.name").value("Fallback User"))
                    .andExpect(jsonPath("$.email").value("fallback@example.com"));
        }

        // Verify circuit breaker is open
        wireMockServer.verify(5, WireMock.getRequestedFor(WireMock.urlEqualTo("/api/users/1")));
    }

    @Test
    void getUserById_WhenCircuitBreakerHalfOpen_ShouldAllowSomeRequests() throws Exception {
        // First trigger circuit breaker to open
        wireMockServer.stubFor(WireMock.get("/api/users/1")
                .willReturn(aResponse()
                        .withStatus(500)
                        .withHeader("Content-Type", "application/json")));

        // Make calls to open circuit breaker
        for (int i = 0; i < 5; i++) {
            mockMvc.perform(get("/api/users/1"));
        }

        // Wait for circuit breaker to transition to half-open
        Thread.sleep(5000);

        // Configure WireMock to return success
        wireMockServer.stubFor(WireMock.get("/api/users/1")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\":1,\"name\":\"John Doe\",\"email\":\"john@example.com\"}")));

        // Make a request when circuit breaker is half-open
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));

        wireMockServer.verify(6, WireMock.getRequestedFor(WireMock.urlEqualTo("/api/users/1")));
    }

    @Test
    void getUserById_WhenCircuitBreakerClosed_ShouldReturnNormalResponse() throws Exception {
        // Configure WireMock to return success
        wireMockServer.stubFor(WireMock.get("/api/users/1")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\":1,\"name\":\"John Doe\",\"email\":\"john@example.com\"}")));

        // Make request when circuit breaker is closed
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));

        wireMockServer.verify(1, WireMock.getRequestedFor(WireMock.urlEqualTo("/api/users/1")));
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("downstream.base-url", () -> wireMockServer.getRuntimeInfo().getHttpBaseUrl() + "/api/users");
    }
} 