# Spring Resilience Demo

This project demonstrates the implementation of resilience patterns in a Spring Boot application, including circuit breakers and rate limiters.

## Features

- Circuit Breaker pattern implementation
- Rate Limiter implementation
- Load testing with k6
- Mock downstream service for testing

## Prerequisites

- Java 17 or higher
- Maven
- k6 (for load testing)

## Getting Started

1. Clone the repository
2. Start the downstream service:
   ```bash
   cd downstream-service
   mvn spring-boot:run
   ```
3. Start the main application:
   ```bash
   cd spring-resilience-demo
   mvn spring-boot:run
   ```

## Running Tests

See the [k6-tests/README.md](k6-tests/README.md) for instructions on running load tests.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details. 