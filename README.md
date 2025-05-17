# Spring Resilience Demo

This project demonstrates the implementation of resilience patterns in Spring Boot applications. It consists of two services:

1. **spring-resilience-demo**: The main service that implements resilience patterns
2. **downstream-service**: A simple service that the main service depends on

## Features

- Circuit Breaker Pattern
- Fallback Mechanism
- Error Handling
- Service Communication

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

## Running the Services

1. Start the downstream service:
```bash
cd downstream-service
mvn spring-boot:run
```

2. Start the main service:
```bash
cd spring-resilience-demo
mvn spring-boot:run
```

The downstream service runs on port 8081, and the main service runs on port 8080.

## Testing the Resilience

1. When both services are running, the main service will communicate with the downstream service
2. If the downstream service is unavailable, the circuit breaker will trigger and the fallback mechanism will provide a default response
3. The system demonstrates graceful degradation when the downstream service is not available

## Technologies Used

- Spring Boot 3.2.3
- Spring Cloud Circuit Breaker
- Spring Web
- Spring Actuator 