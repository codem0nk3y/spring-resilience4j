# Spring Resilience Demo

A demonstration of Spring Boot microservices with resilience patterns, containerization, and Kubernetes deployment.

## Project Structure

The project consists of two main services:

1. **Downstream Service** (`downstream-service/`)
   - Port: 8081
   - Provides user data API endpoints
   - Implements basic CRUD operations

2. **Spring Resilience Demo** (`spring-resilience-demo/`)
   - Port: 8080
   - Main application with resilience patterns
   - Communicates with downstream service
   - Implements circuit breaker, retry, and fallback patterns

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- Docker and Docker Compose
- Kubernetes cluster (for production deployment)
- Google Cloud Platform account (for GKE deployment)

## Local Development

### Running Services Locally

1. Start the downstream service:
   ```bash
   cd downstream-service
   mvn spring-boot:run
   ```

2. Start the spring-resilience-demo service:
   ```bash
   cd spring-resilience-demo
   mvn spring-boot:run
   ```

### Using Docker Compose

Run both services using Docker Compose:
   ```bash
   docker-compose up
   ```

## Containerization

### Docker Images

Both services are containerized using multi-stage builds for optimal security and image size:

- **Downstream Service**: `ghcr.io/${GITHUB_REPOSITORY}/downstream-service:${TAG}`
- **Spring Resilience Demo**: `ghcr.io/${GITHUB_REPOSITORY}/spring-resilience-demo:${TAG}`

### Security Features

- Non-root user in containers
- Resource limits and requests
- Health checks
- Network isolation
- Multi-stage builds to minimize attack surface

## Kubernetes Deployment

### Prerequisites

1. GKE cluster with:
   - At least 2 nodes
   - Machine type: e2-standard-2 or better
   - Workload Identity enabled
   - Cloud Monitoring and Logging enabled

2. Required secrets in GitHub repository:
   - `GCP_SA_KEY`: Service account key for GKE deployment
   - `SNYK_TOKEN`: Snyk API token for security scanning

3. Environment variables in GitHub Actions:
   - `GKE_PROJECT`: Your GCP project ID
   - `GKE_CLUSTER`: Your GKE cluster name
   - `GKE_ZONE`: Your GKE cluster zone

### Deployment Configuration

The Kubernetes configuration is managed using Kustomize:

```bash
k8s/base/
├── deployment.yaml    # Deployment configurations
├── service.yaml      # Service definitions
├── ingress.yaml      # Ingress configuration
└── kustomization.yaml # Kustomize configuration
```

### Ingress Setup

1. Reserve a static IP in GCP
2. Update the ingress configuration with your domain
3. Set up managed SSL certificate

## CI/CD Pipeline

The GitHub Actions workflow includes:

1. **Security Scanning**
   - OWASP Dependency Check
   - Snyk vulnerability scanning
   - Container image scanning

2. **Build and Test**
   - Maven build
   - Unit tests
   - Integration tests

3. **Build and Push**
   - Docker image builds
   - Push to GitHub Container Registry

4. **Deploy**
   - GKE authentication
   - Kustomize deployment
   - Rolling updates

## Monitoring and Health Checks

Both services expose actuator endpoints for monitoring:

- Health checks: `/actuator/health`
- Metrics: `/actuator/metrics`
- Info: `/actuator/info`

## Resilience Patterns

The application implements several resilience patterns:

1. **Circuit Breaker**
   - Prevents cascading failures
   - Configurable thresholds and timeouts

2. **Retry**
   - Automatic retry for transient failures
   - Configurable retry attempts and backoff

3. **Fallback**
   - Graceful degradation
   - Default responses when services are unavailable

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details. 