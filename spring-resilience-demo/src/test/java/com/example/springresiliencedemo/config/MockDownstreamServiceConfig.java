package com.example.springresiliencedemo.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@TestConfiguration
public class MockDownstreamServiceConfig {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public WireMockServer wireMockServer() {
        WireMockServer wireMockServer = new WireMockServer(8081);
        
        // Configure the mock response
        wireMockServer.stubFor(get(urlPathMatching("/api/users/1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody("""
                                {
                                    "id": 1,
                                    "name": "John Doe",
                                    "email": "john.doe@example.com"
                                }
                                """)));

        return wireMockServer;
    }
} 