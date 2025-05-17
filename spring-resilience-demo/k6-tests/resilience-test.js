import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  scenarios: {
    circuitBreakerTest: {
      executor: 'ramping-vus',
      startVUs: 0,
      stages: [
        { duration: '30s', target: 10 }, // Ramp up to 10 users
        { duration: '1m', target: 10 },  // Stay at 10 users
        { duration: '30s', target: 0 },  // Ramp down to 0 users
      ],
      gracefulRampDown: '30s',
    },
    rateLimiterTest: {
      executor: 'constant-vus',
      vus: 20,
      duration: '1m',
      startTime: '2m', // Start after circuit breaker test
    },
  },
};

const BASE_URL = 'http://localhost:8080';

export default function () {
  // Test circuit breaker pattern
  const userId = Math.floor(Math.random() * 100) + 1;
  const response = http.get(`${BASE_URL}/api/users/${userId}`);
  
  check(response, {
    'status is 200': (r) => r.status === 200,
    'has user data': (r) => r.json().id !== undefined,
  });

  // Test rate limiter pattern
  const usersResponse = http.get(`${BASE_URL}/api/users`);
  
  check(usersResponse, {
    'status is 200': (r) => r.status === 200,
    'has users array': (r) => Array.isArray(r.json()),
  });

  sleep(1);
} 