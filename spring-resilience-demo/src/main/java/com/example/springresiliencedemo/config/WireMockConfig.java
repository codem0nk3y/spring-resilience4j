package com.example.springresiliencedemo.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@Configuration
@Profile("mock")
public class WireMockConfig {

    private WireMockServer wireMockServer;

    @PostConstruct
    public void startWireMock() {
        wireMockServer = new WireMockServer(8081);
        wireMockServer.start();
        
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
    }

    @PreDestroy
    public void stopWireMock() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    @Bean
    public WireMockServer wireMockServer() {
        WireMockServer wireMockServer = new WireMockServer(8081);
        wireMockServer.start();
        return wireMockServer;
    }
} 