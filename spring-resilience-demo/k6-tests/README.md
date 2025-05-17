# Resilience Pattern Tests

This directory contains k6 load tests for testing the resilience patterns implemented in the Spring Boot application.

## Prerequisites

1. Install k6:
   - macOS: `brew install k6`
   - Windows: `choco install k6`
   - Linux: See [k6 installation guide](https://k6.io/docs/getting-started/installation)

2. Make sure the Spring Boot application is running on `http://localhost:8080`

## Running the Tests

To run the tests, execute the following command from this directory:

```bash
k6 run resilience-test.js
```

## Test Scenarios

The test script includes two scenarios:

1. **Circuit Breaker Test**
   - Ramps up to 10 virtual users over 30 seconds
   - Maintains 10 virtual users for 1 minute
   - Ramps down to 0 users over 30 seconds
   - Tests the `/api/users/{id}` endpoint

2. **Rate Limiter Test**
   - Runs 20 concurrent virtual users for 1 minute
   - Starts after the circuit breaker test
   - Tests the `/api/users` endpoint

## Expected Results

- The circuit breaker test should show how the system handles failures and recovers
- The rate limiter test should show how the system handles concurrent requests and enforces rate limits

## Monitoring

You can monitor the test results in real-time in the terminal. The output includes:
- HTTP request statistics
- Response times
- Success/failure rates
- Custom checks results 