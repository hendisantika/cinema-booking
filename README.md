# Cinema Booking System

A microservices-based cinema booking system built with Spring Boot 4.0.1, Spring Cloud 2025.1.0, and modern cloud-native
technologies.

## Overview

The Cinema Booking System is a comprehensive platform for managing cinema reservations, payments, and notifications. The
system follows a microservices architecture with event-driven communication, API Gateway routing, and OAuth2/JWT
authentication.

## Architecture

```
┌─────────────┐
│   Client    │
└──────┬──────┘
       │
       ▼
┌─────────────────────────────────────────┐
│     API Gateway (Port 8081)             │
│  - Routing                              │
│  - Rate Limiting                        │
│  - Request Tracking                     │
└──────┬──────────────────────────────────┘
       │
       ├──────────────┬──────────────┬────────────────┐
       ▼              ▼              ▼                ▼
┌──────────────┐ ┌──────────┐ ┌──────────┐  ┌──────────────┐
│ Reservation  │ │ Payment  │ │Notification│  │   Keycloak   │
│   Service    │ │ Service  │ │  Service  │  │   (Auth)     │
│  (Port 8084) │ │(Port 8082)│ │(Port 8083)│  │  (Port 8080) │
└──────┬───────┘ └────┬─────┘ └─────┬────┘  └──────────────┘
       │              │             │
       ▼              ▼             ▼
┌──────────────────────────────────────────┐
│         Event Bus (Kafka)                │
│         Port 9092-9093                   │
└──────────────────────────────────────────┘
       │              │             │
       ▼              ▼             ▼
┌──────────────┐ ┌──────────┐ ┌──────────┐
│  PostgreSQL  │ │  Redis   │ │Zookeeper │
│  (Port 5431) │ │(Port 6379)│ │(Port 2181)│
└──────────────┘ └──────────┘ └──────────┘
```

## Services

### API Gateway

- **Port**: 8081
- **Technology**: Spring Cloud Gateway (WebFlux)
- **Features**:
  - Centralized routing to all microservices
  - Redis-based rate limiting
  - Request tracking with custom headers
  - Actuator health endpoints
- **Documentation**: [api-gateway/README.md](api-gateway/README.md)

### Reservation Service

- **Port**: 8084
- **Technology**: Spring Boot, JPA, WebSocket
- **Features**:
  - Cinema, branch, and seat management
  - Movie and showtime scheduling
  - Reservation management with JWT authentication
  - Real-time seat availability via WebSocket
  - Kafka event publishing
- **Documentation**: [reservation-service/README.md](reservation-service/README.md)

### Payment Service

- **Port**: 8082
- **Status**: To be implemented
- **Planned Features**:
  - Payment processing
  - Transaction management
  - Integration with payment gateways

### Notification Service

- **Port**: 8083
- **Status**: To be implemented
- **Planned Features**:
  - Email notifications
  - SMS notifications
  - Push notifications

## Technology Stack

### Core Technologies

- **Java**: 25 (Java 17+ supported)
- **Spring Boot**: 4.0.1
- **Spring Cloud**: 2025.1.0 (Oakwood)
- **Spring Framework**: 7.0.2
- **Spring Security**: 7.0.2 with OAuth2 Resource Server

### Infrastructure

- **Database**: PostgreSQL 18
- **Authentication**: Keycloak 23.0
- **Message Broker**: Apache Kafka 7.6.0
- **Caching/Rate Limiting**: Redis (Alpine)
- **API Gateway**: Spring Cloud Gateway Server WebFlux 5.0.x
- **Container Platform**: Docker

### Additional Tools

- **Build Tool**: Maven 3.6+
- **ORM**: Hibernate 7.2.0
- **WebSocket**: Spring WebSocket with STOMP
- **Reactive Programming**: Project Reactor

## Prerequisites

Before running the application, ensure you have:

- **Java 25** (or Java 17+) installed
- **Maven 3.6+** installed
- **Docker** and **Docker Compose** installed
- At least 4GB of available RAM
- Ports available: 5431, 6379, 8080, 8081, 8082, 8083, 8084, 9092, 9093, 2181

## Quick Start

### 1. Start Infrastructure Services

Start all required Docker containers:

```bash
# Start PostgreSQL
docker run -d --name cinema-postgres \
  -e POSTGRES_DB=cinema_booking \
  -e POSTGRES_USER=yu71 \
  -e POSTGRES_PASSWORD=53cret \
  -p 5431:5432 \
  postgres:18

# Start Keycloak
docker run -d --name cinema-keycloak \
  -e KEYCLOAK_ADMIN=admin \
  -e KEYCLOAK_ADMIN_PASSWORD=admin \
  -e KC_HEALTH_ENABLED=true \
  -p 8080:8080 \
  quay.io/keycloak/keycloak:23.0 \
  start-dev

# Start Zookeeper
docker run -d --name cinema-zookeeper \
  -e ZOOKEEPER_CLIENT_PORT=2181 \
  -e ZOOKEEPER_TICK_TIME=2000 \
  -p 2181:2181 \
  confluentinc/cp-zookeeper:7.6.0

# Start Kafka
docker run -d --name cinema-kafka \
  -e KAFKA_BROKER_ID=1 \
  -e KAFKA_ZOOKEEPER_CONNECT=host.docker.internal:2181 \
  -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 \
  -e KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1 \
  -p 9092:9092 \
  -p 9093:9093 \
  confluentinc/cp-kafka:7.6.0

# Start Redis
docker run -d --name cinema-redis \
  -p 6379:6379 \
  redis:alpine
```

### 2. Verify Infrastructure

Check all containers are running and healthy:

```bash
docker ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"
```

Expected output:

```
NAMES              STATUS                  PORTS
cinema-redis       Up X minutes            0.0.0.0:6379->6379/tcp
cinema-kafka       Up X minutes (healthy)  0.0.0.0:9092-9093->9092-9093/tcp
cinema-keycloak    Up X minutes (healthy)  0.0.0.0:8080->8080/tcp
cinema-postgres    Up X minutes (healthy)  0.0.0.0:5431->5432/tcp
cinema-zookeeper   Up X minutes (healthy)  0.0.0.0:2181->2181/tcp
```

Test connectivity:

```bash
# Test PostgreSQL
docker exec cinema-postgres pg_isready -U yu71

# Test Redis
docker exec cinema-redis redis-cli ping

# Test Keycloak
curl http://localhost:8080/health/ready
```

### 3. Start API Gateway

```bash
cd api-gateway
mvn clean install
mvn spring-boot:run
```

API Gateway will start on `http://localhost:8081`

### 4. Start Reservation Service

```bash
cd reservation-service
mvn clean install
mvn spring-boot:run
```

Reservation Service will start on `http://localhost:8084`

### 5. Verify Services

```bash
# Check API Gateway health
curl http://localhost:8081/actuator/health

# Check Reservation Service health (direct)
curl http://localhost:8084/actuator/health

# Check Reservation Service through API Gateway
curl http://localhost:8081/api/reservation/health
```

## Port Reference

| Service              | Port      | Protocol | Description                     |
|----------------------|-----------|----------|---------------------------------|
| API Gateway          | 8081      | HTTP     | Gateway to all microservices    |
| Keycloak             | 8080      | HTTP     | Authentication & Authorization  |
| Payment Service      | 8082      | HTTP     | Payment processing              |
| Notification Service | 8083      | HTTP     | Notification management         |
| Reservation Service  | 8084      | HTTP     | Reservation & cinema management |
| PostgreSQL           | 5431      | TCP      | Database                        |
| Redis                | 6379      | TCP      | Cache & rate limiting           |
| Zookeeper            | 2181      | TCP      | Kafka coordination              |
| Kafka                | 9092-9093 | TCP      | Event streaming                 |

## Configuration

### Database Connection

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5431/cinema_booking
    username: yu71
    password: 53cret
```

### Keycloak Configuration

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          jwk-set-uri: http://127.0.0.1:8080/realms/CineBook/protocol/openid-connect/certs
```

### Kafka Configuration

```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
```

## Development Workflow

### Build All Services

```bash
# From project root
mvn clean install -DskipTests
```

### Run Tests

```bash
# Test individual service
cd reservation-service
mvn test

# Test API Gateway
cd api-gateway
mvn test
```

### View Logs

```bash
# Docker container logs
docker logs -f cinema-postgres
docker logs -f cinema-kafka
docker logs -f cinema-keycloak

# Application logs (when running with mvn spring-boot:run)
# Logs appear in the terminal
```

## API Documentation

### Reservation Service Endpoints

Base URL: `http://localhost:8081/api/reservation` (via Gateway)

#### Cinema Management

```bash
GET    /api/cinema           # List all cinemas
POST   /api/cinema           # Create cinema
GET    /api/cinema/{id}      # Get cinema by ID
PUT    /api/cinema/{id}      # Update cinema
DELETE /api/cinema/{id}      # Delete cinema
```

#### Branch Management

```bash
GET    /api/branch           # List all branches
POST   /api/branch           # Create branch
GET    /api/branch/{id}      # Get branch by ID
PUT    /api/branch/{id}      # Update branch
DELETE /api/branch/{id}      # Delete branch
```

#### Seat Management

```bash
GET    /api/seats            # List all seats
POST   /api/seats            # Add seat
PUT    /api/seats/{id}       # Update seat
DELETE /api/seats/{id}       # Delete seat
```

For complete API documentation, see:

- [Reservation Service API](reservation-service/README.md#api-endpoints)
- [API Gateway Routes](api-gateway/README.md#api-routes)

## Authentication

The system uses Keycloak for OAuth2/JWT authentication.

### Setup Keycloak Realm

1. Access Keycloak: http://localhost:8080
2. Login with admin credentials (admin/admin)
3. Create a new realm: `CineBook`
4. Configure clients and users as needed

### Obtain Access Token

```bash
# Example: Get token from Keycloak
curl -X POST http://localhost:8080/realms/CineBook/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "username=YOUR_USERNAME" \
  -d "password=YOUR_PASSWORD" \
  -d "grant_type=password" \
  -d "client_id=YOUR_CLIENT_ID"
```

### Use Token in API Calls

```bash
curl -H "Authorization: Bearer YOUR_ACCESS_TOKEN" \
  http://localhost:8081/api/reservation/cinema
```

## Monitoring & Health Checks

### Service Health Status

```bash
# API Gateway
curl http://localhost:8081/actuator/health

# Reservation Service
curl http://localhost:8084/actuator/health

# Infrastructure
curl http://localhost:8080/health/ready  # Keycloak
docker exec cinema-postgres pg_isready -U yu71
docker exec cinema-redis redis-cli ping
```

### Gateway Routes

```bash
# View all configured routes
curl http://localhost:8081/actuator/gateway/routes | jq
```

### Database Status

```bash
# Check database
docker exec -it cinema-postgres psql -U yu71 -d cinema_booking -c "\dt"
```

## Troubleshooting

### Port Already in Use

```bash
# Find process using port
lsof -i :8080

# Kill process
kill -9 <PID>
```

### Docker Container Issues

```bash
# Stop all containers
docker stop cinema-postgres cinema-keycloak cinema-kafka cinema-zookeeper cinema-redis

# Remove all containers
docker rm cinema-postgres cinema-keycloak cinema-kafka cinema-zookeeper cinema-redis

# Restart containers (follow Quick Start section)
```

### Database Connection Failed

```bash
# Verify PostgreSQL is running
docker ps | grep cinema-postgres

# Check PostgreSQL logs
docker logs cinema-postgres

# Test connection
docker exec cinema-postgres pg_isready -U yu71
```

### Kafka Connection Issues

```bash
# Check Kafka logs
docker logs cinema-kafka

# List Kafka topics
docker exec cinema-kafka kafka-topics --bootstrap-server localhost:9092 --list
```

## Known Issues

### Spring Boot 4.0.1 / Spring Cloud 2025.1.0 Migration

1. **Spring Cloud Gateway**: Migrated from `spring-cloud-starter-gateway` to `spring-cloud-gateway-server-webflux`
2. **WebFlux Required**: Must explicitly add `spring-boot-starter-webflux` dependency
3. **LoadBalancer**: Requires `spring-cloud-starter-loadbalancer` dependency

### Reservation Service

- **Spring Security 7.x Compatibility**: Some deprecated classes (e.g., `ChannelDecisionManager`) need migration
- Database and Kafka functionality working correctly

## Project Structure

```
cinema-booking/
├── api-gateway/              # API Gateway service
│   ├── src/
│   ├── pom.xml
│   └── README.md
├── reservation-service/      # Reservation management service
│   ├── src/
│   ├── pom.xml
│   └── README.md
├── payment-service/          # Payment processing service (planned)
├── notification-service/     # Notification service (planned)
├── pom.xml                   # Parent POM (if applicable)
└── README.md                 # This file
```

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'feat: add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For issues and questions:

- Create an issue in the repository
- Contact: support@cinemabooking.com

## Changelog

### Version 1.0.1 (2026-01-14)

**Infrastructure:**

- Configured Docker infrastructure with PostgreSQL 18, Kafka 7.6.0, Keycloak 23.0, Zookeeper, and Redis
- All containers running and healthy

**API Gateway:**

- Upgraded to Spring Boot 4.0.1 and Spring Cloud 2025.1.0 (Oakwood)
- Migrated to `spring-cloud-gateway-server-webflux` architecture
- Added Spring Cloud LoadBalancer
- Changed port to 8081 (avoid Keycloak conflict)
- Redis-based rate limiting active

**Reservation Service:**

- Successfully built with Spring Boot 4.0.1
- Database schema created automatically
- Kafka producer configured
- WebSocket support for real-time updates
- JWT authentication integrated

**Documentation:**

- Comprehensive README for all services
- API documentation with examples
- Setup and troubleshooting guides

### Version 1.0.0 (2026-01-04)

- Initial project structure
- Basic microservices architecture
- Core service implementations
