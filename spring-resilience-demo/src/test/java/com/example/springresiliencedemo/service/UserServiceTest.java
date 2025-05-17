package com.example.springresiliencedemo.service;

import com.example.springresiliencedemo.model.User;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserServiceTest {

    @RegisterExtension
    static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("api.base-url", wireMockServer::baseUrl);
    }

    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
        wireMockServer.resetAll();
    }

    @Test
    void getUserById_Success() {
        // Arrange
        User expectedUser = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .build();

        wireMockServer.stubFor(get(urlPathMatching("/api/users/1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody("{\"id\":1,\"name\":\"Test User\",\"email\":\"test@example.com\"}")));

        // Act
        User result = userService.getUserById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(expectedUser.getId(), result.getId());
        assertEquals(expectedUser.getName(), result.getName());
        assertEquals(expectedUser.getEmail(), result.getEmail());

        wireMockServer.verify(getRequestedFor(urlPathMatching("/api/users/1")));
    }

    @Test
    void getUserFallback_Direct() {
        // Act
        User result = userService.getUserFallback(1L, new RuntimeException("API Error"));

        // Assert
        assertNotNull(result);
        assertEquals(new User(1L, "Fallback User", "fallback@example.com"), result);
    }
} 