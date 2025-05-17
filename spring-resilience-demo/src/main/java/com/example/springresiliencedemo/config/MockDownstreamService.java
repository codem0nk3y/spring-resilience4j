package com.example.springresiliencedemo.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

@TestConfiguration
@Profile("mock")
public class MockDownstreamService {

    @Bean
    @Primary
    public RestTemplate mockRestTemplate() {
        return new RestTemplate();
    }
} 