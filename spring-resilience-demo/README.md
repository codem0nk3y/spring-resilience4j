# Spring Resilience Demo

This is a Spring Boot application that demonstrates the integration of Resilience4j patterns with a REST API. The application includes circuit breaker and rate limiter patterns, and provides API documentation using Swagger.

## Features

- Spring Boot 3.2.3
- Java 17
- Resilience4j integration (Circuit Breaker and Rate Limiter)
- REST API with Swagger documentation
- Unit tests
- External API integration using RestClient

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

## Getting Started

1. Clone the repository
2. Build the project:
   ```bash
   mvn clean install
   ```
3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

## API Documentation

Once the application is running, you can access the Swagger UI at:
```
http://localhost:8080/swagger-ui.html
```

The OpenAPI documentation is available at:
```
http://localhost:8080/api-docs
```

## Available Endpoints

- `GET /api/users/{id}` - Get user by ID
  - Circuit breaker and rate limiter patterns are applied
  - Fallback mechanism is implemented

## Resilience4j Configuration

The application includes the following resilience patterns:

### Circuit Breaker
- Sliding window size: 10
- Minimum number of calls: 5
- Failure rate threshold: 50%
- Wait duration in open state: 5 seconds

### Rate Limiter
- Limit for period: 10 requests
- Limit refresh period: 1 second
- Timeout duration: 0 seconds (no wait)

## Monitoring

The application exposes the following actuator endpoints:
- `/actuator/health` - Application health information
- `/actuator/metrics` - Application metrics
- `/actuator/circuitbreakers` - Circuit breaker states
- `/actuator/circuitbreakerevents` - Circuit breaker events
- `/actuator/ratelimiters` - Rate limiter states
- `/actuator/ratelimiterevents` - Rate limiter events

## Testing

Run the tests using:
```bash
mvn test
```

## License

This project is licensed under the MIT License. 