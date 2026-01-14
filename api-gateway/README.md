# Cinema Booking System - API Gateway

A centralized API Gateway service built with Spring Cloud Gateway for routing and managing requests to microservices in
the Cinema Booking System.

## Overview

The API Gateway serves as a single entry point for all client requests, providing routing, rate limiting, and
centralized request handling for the cinema booking microservices architecture.

## Features

- 🚀 **Centralized Routing** - Single entry point for all microservices
- 🛡️ **Rate Limiting** - Redis-based request rate limiting per service
- 🔍 **Request Tracking** - Custom headers for gateway identification
- 📊 **Monitoring** - Actuator endpoints for health checks and route inspection
- 🔒 **Host Preservation** - Maintains original host headers

## Architecture

```
Client Request
     ↓
API Gateway (Port 8081)
     ↓
├── Reservation Service (Port 8084) - /api/reservation/**
├── Payment Service (Port 8082) - /api/payments/**
└── Notification Service (Port 8083) - /api/notifications/**
```

**Note:** Port changed to 8081 to avoid conflict with Keycloak (port 8080)

## Prerequisites

- Java 25 (or Java 17+)
- Maven 3.6+
- **Redis Server** (required for rate limiting)
- Running microservices:
    - Reservation Service on port 8084
    - Payment Service on port 8082
    - Notification Service on port 8083
- Keycloak on port 8080 (for authentication)
- PostgreSQL on port 5431 (for reservation-service database)

## Technology Stack

- **Spring Boot** 4.0.1
- **Spring Cloud** 2025.1.0 (Oakwood)
- **Spring Cloud Gateway Server WebFlux** 5.0.x - Reactive gateway implementation
- **Spring Cloud LoadBalancer** - Client-side load balancing
- **Spring WebFlux** - Reactive web framework with Netty
- **Redis** - Rate limiting storage (reactive)
- **Spring Boot Actuator** - Monitoring and management

## Getting Started

### 1. Clone the Repository

```bash
git clone <repository-url>
cd api-gateway
```

### 2. Start Redis

Redis is **required** for rate limiting functionality.

```bash
# Using Docker (recommended)
docker run -d --name cinema-redis -p 6379:6379 redis:alpine

# Or using local Redis
redis-server
```

Verify Redis is running:

```bash
docker ps | grep cinema-redis
# Or
redis-cli ping  # Should return PONG
```

### 3. Build the Project

```bash
mvn clean install
```

### 4. Run the Application

```bash
mvn spring-boot:run
```

The API Gateway will start on `http://localhost:8081`

Expected startup log:

```
Netty started on port 8081 (http)
Started ApiGatewayApplication in X.XXX seconds
```

## Configuration

### Application Configuration (`application.yml`)

```yaml
server:
  port: 8081  # Changed to avoid conflict with Keycloak (8080)

spring:
  application:
    name: api-gateway

  cloud:
    gateway:
      default-filters:
        - PreserveHostHeader
        - AddRequestHeader=X-Gateway, Cinema-API-Gateway

      routes:
        - id: reservation-service
          uri: http://localhost:8084
          predicates:
            - Path=/api/reservation/**
          filters:
            - name: RequestRateLimiter
              args:
                redis-rate-limiter.replenishRate: 10
                redis-rate-limiter.burstCapacity: 20
```

### Rate Limiting Configuration

| Service      | Replenish Rate  | Burst Capacity |
|--------------|-----------------|----------------|
| Reservation  | 10 requests/sec | 20 requests    |
| Payment      | 5 requests/sec  | 10 requests    |
| Notification | No limit        | No limit       |

## API Routes

### Reservation Service

- **Base URL**: `http://localhost:8081/api/reservation`
- **Examples**:

```bash
  GET    http://localhost:8081/api/reservation
  POST   http://localhost:8081/api/reservation
  GET    http://localhost:8081/api/reservation/{id}
```

### Payment Service

- **Base URL**: `http://localhost:8081/api/payments`
- **Examples**:

```bash
  POST   http://localhost:8081/api/payments/create-session
  GET    http://localhost:8081/api/payments/{id}
```

### Notification Service

- **Base URL**: `http://localhost:8081/api/notifications`
- **Examples**:

```bash
  POST   http://localhost:8081/api/notifications/send
  GET    http://localhost:8081/api/notifications
```

## Usage Examples

### Direct Service Call (Without Gateway)

```bash
curl http://localhost:8084/api/reservation
```

### Through API Gateway

```bash
curl http://localhost:8081/api/reservation
```

### With Authentication

```bash
curl -H "Authorization: Bearer <token>" \
     http://localhost:8081/api/reservation
```

### POST Request

```bash
curl -X POST http://localhost:8081/api/reservation \
  -H "Content-Type: application/json" \
  -d '{
    "movieId": 1,
    "showTimeId": 5,
    "seats": ["A1", "A2"]
  }'
```

## Monitoring & Management

### Actuator Endpoints

The gateway exposes the following actuator endpoints:

| Endpoint       | URL                                             | Description                |
|----------------|-------------------------------------------------|----------------------------|
| Health         | `http://localhost:8081/actuator/health`         | Service health status      |
| Info           | `http://localhost:8081/actuator/info`           | Application information    |
| Gateway Routes | `http://localhost:8081/actuator/gateway/routes` | List all configured routes |

### Check Gateway Routes

```bash
# View all routes
curl http://localhost:8081/actuator/gateway/routes | jq

# View specific route
curl http://localhost:8081/actuator/gateway/routes/reservation-service | jq
```

### Health Check

```bash
curl http://localhost:8081/actuator/health
```

Expected response:

```json
{
  "groups": [
    "liveness",
    "readiness"
  ],
  "status": "UP"
}
```

## Rate Limiting

The gateway implements Redis-based rate limiting:

- **Replenish Rate**: Number of requests allowed per second
- **Burst Capacity**: Maximum requests allowed in a burst

When rate limit is exceeded, the gateway returns:

```
HTTP 429 Too Many Requests
```

### Testing Rate Limiting

```bash
# Send multiple requests quickly
for i in {1..25}; do
  curl -w "\nStatus: %{http_code}\n" http://localhost:8081/api/reservation
done
```

## Environment Variables

You can override default configurations using environment variables:

```bash
# Service URLs
export RESERVATION_SERVICE_URL=http://reservation-service:8084
export PAYMENT_SERVICE_URL=http://payment-service:8082
export NOTIFICATION_SERVICE_URL=http://notification-service:8083

# Redis configuration
export SPRING_DATA_REDIS_HOST=redis-server
export SPRING_DATA_REDIS_PORT=6379

# Server port
export SERVER_PORT=8081
```

## Docker Support

### Dockerfile

```dockerfile
FROM eclipse-temurin:25-jdk-alpine
WORKDIR /app
COPY target/api-gateway-*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Build Docker Image

```bash
mvn clean package
docker build -t cinema-api-gateway .
```

### Run with Docker

```bash
docker run -d \
  -p 8081:8081 \
  -e SPRING_DATA_REDIS_HOST=redis \
  --name api-gateway \
  cinema-api-gateway
```

### Docker Compose

```yaml
services:
  redis:
    image: redis:alpine
    ports:
      - "6379:6379"

  api-gateway:
    build: .
    ports:
      - "8081:8081"
    environment:
      SPRING_DATA_REDIS_HOST: redis
      RESERVATION_SERVICE_URL: http://reservation-service:8084
      PAYMENT_SERVICE_URL: http://payment-service:8082
      NOTIFICATION_SERVICE_URL: http://notification-service:8083
    depends_on:
      - redis
```

## Troubleshooting

### 404 Not Found Error

**Problem**: Gateway returns 404 when calling an endpoint

**Solutions**:

1. Verify the service is running:

```bash
   curl http://localhost:8084/api/reservation
```

2. Check gateway routes:

```bash
   curl http://localhost:8081/actuator/gateway/routes
```

3. Enable debug logging:

```yaml
   logging:
     level:
       org.springframework.cloud.gateway: DEBUG
```

### 503 Service Unavailable

**Problem**: Gateway cannot reach the downstream service

**Solutions**:

1. Check if the service is running on the correct port
2. Verify the URI in gateway configuration
3. Check network connectivity

### Rate Limit Issues

**Problem**: Requests failing with 429 error

**Solutions**:

1. Verify Redis is running:

```bash
   redis-cli ping
```

2. Check Redis connection in gateway logs

3. Adjust rate limits in configuration if needed

### Redis Connection Failed

**Problem**: Gateway cannot connect to Redis

**Solutions**:

1. Start Redis server
2. Check Redis host/port configuration
3. Temporarily disable rate limiting for testing:

```yaml
   # Comment out RequestRateLimiter filters
```

## Performance Tuning

### Increase Rate Limits

```yaml
filters:
  - name: RequestRateLimiter
    args:
      redis-rate-limiter.replenishRate: 100  # Increased
      redis-rate-limiter.burstCapacity: 200   # Increased
```

### Connection Pool Configuration

```yaml
spring:
  cloud:
    gateway:
      httpclient:
        pool:
          max-connections: 500
          max-idle-time: 30s
```

## Security Considerations

1. **JWT Token Forwarding**: The gateway preserves Authorization headers
2. **CORS**: Configure if accessing from web applications
3. **API Key**: Consider adding API key validation
4. **SSL/TLS**: Use HTTPS in production

## Development

### Enable Debug Logging

```yaml
logging:
  level:
    org.springframework.cloud.gateway: DEBUG
    reactor.netty: DEBUG
```

### Hot Reload

Use Spring Boot DevTools for automatic restart during development:

```xml

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <scope>runtime</scope>
</dependency>
```

## Testing

### Integration Tests

```bash
mvn test
```

### Load Testing

```bash
# Using Apache Bench
ab -n 1000 -c 10 http://localhost:8081/api/reservation

# Using wrk
wrk -t12 -c400 -d30s http://localhost:8081/api/reservation
```

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'feat: add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Related Services

- [Reservation Service](../reservation-service/README.md)
- [Payment Service](../payment-service/README.md)
- [Notification Service](../notification-service/README.md)

## Support

For issues and questions:

- Create an issue in the repository
- Contact: support@cinemabooking.com

## Known Issues & Notes

### Spring Cloud 2025.1.0 (Oakwood) Changes

The upgrade to Spring Cloud 2025.1.0 introduces several breaking changes:

1. **New Gateway Artifact Names**: The old `spring-cloud-starter-gateway` has been replaced with:
    - `spring-cloud-gateway-server-webflux` (for reactive/WebFlux)
    - `spring-cloud-gateway-server-webmvc` (for traditional MVC)

2. **Explicit WebFlux Dependency**: You must explicitly add `spring-boot-starter-webflux` to your dependencies.

3. **LoadBalancer Dependency**: `spring-cloud-starter-loadbalancer` is required for client-side load balancing.

### Warnings You May See

- **Caffeine Cache Warning**: The LoadBalancer uses a default cache. For production, consider adding Caffeine cache
  dependency.
- **MacOS DNS Warning**: Netty DNS resolver warning on macOS can be safely ignored or fixed by adding
  `netty-resolver-dns-native-macos` dependency.

## Changelog

### Version 1.0.1 (2026-01-14)

- Upgraded to Spring Boot 4.0.1 and Spring Cloud 2025.1.0 (Oakwood)
- Migrated from `spring-cloud-starter-gateway` to `spring-cloud-gateway-server-webflux`
- Added Spring Cloud LoadBalancer for client-side load balancing
- Changed default port from 8080 to 8081 (avoid Keycloak conflict)
- Added explicit WebFlux dependency for reactive support
- Updated all dependencies to Spring Framework 7.0.2
- Enhanced documentation with infrastructure setup details

### Version 1.0.0 (2026-01-04)

- Initial release
- Basic routing configuration
- Redis-based rate limiting
- Actuator endpoints for monitoring