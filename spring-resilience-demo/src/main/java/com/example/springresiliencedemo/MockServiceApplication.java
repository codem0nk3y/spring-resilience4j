package com.example.springresiliencedemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
@Profile("mock")
public class MockServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MockServiceApplication.class, args);
    }
} 