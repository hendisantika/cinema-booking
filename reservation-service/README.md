# 🎬 Cinema Reservation Service

A comprehensive cinema booking management system built with Spring Boot 4.0.1, providing complete functionality for
movie reservations, seat management, and payment processing with Keycloak authentication and Kafka event streaming.

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Prerequisites](#prerequisites)
- [Infrastructure Setup](#infrastructure-setup)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [API Documentation](#api-documentation)
- [Known Issues](#known-issues)
- [Project Structure](#project-structure)
- [Contributing](#contributing)
- [License](#license)

## 🎯 Overview

This Cinema Reservation Service is a full-featured backend application that manages cinema operations including customer
registration, movie scheduling, seat booking, and payment processing across multiple branches. The system implements
proper separation of concerns using DTOs, comprehensive validation, robust error handling, and integration with Keycloak
for authentication and Kafka for event streaming.

## ✨ Features

### Core Functionality

- **Customer Management**: User registration, authentication via Keycloak, and profile management
- **Movie Management**: Add, update, and manage movie information with scheduling
- **Multi-Branch Support**: Manage multiple cinema branches with individual halls
- **Seat Booking System**:
    - 7 seat types (Standard, Premium, VIP, Luxury, Recliner, Couple, Wheelchair)
    - Real-time seat availability via WebSocket
    - Dynamic pricing based on seat type
- **Reservation Management**:
    - Create, view, and cancel reservations
    - Automatic seat allocation
    - Total price calculation
    - Payment session integration
- **Payment Processing**: Integration with payment service via Kafka events
- **Real-time Updates**: WebSocket support for live reservation status updates

### Technical Features

- RESTful API architecture
- OAuth2/JWT authentication with Keycloak
- Kafka event-driven messaging
- DTO pattern for request/response separation
- Comprehensive input validation
- Proper error handling with meaningful HTTP status codes
- Transaction management for data consistency
- WebSocket support for real-time notifications
- Logging with SLF4J
- Enum-based type safety (SeatType, ReservationStatus)
- PostgreSQL 18 database with Hibernate ORM

## 🛠 Technology Stack

### Backend

- **Java 25**
- **Spring Boot 4.0.1**
- **Spring Data JPA** - Database operations
- **Spring Web** - REST API
- **Spring Security** - OAuth2 Resource Server
- **Spring Kafka** - Event streaming
- **Spring WebSocket** - Real-time communication
- **Lombok** - Reduce boilerplate code
- **ModelMapper** - Entity-DTO mapping
- **SLF4J** - Logging
- **Springdoc OpenAPI** - API documentation

### Database

- **PostgreSQL 18** - Primary database
- **Hibernate 7.2.0** - ORM

### Authentication & Authorization

- **Keycloak 23.0** - Identity and Access Management

### Messaging

- **Apache Kafka 7.6.0** - Event streaming
- **Zookeeper 7.6.0** - Kafka coordination

### Build Tool

- **Maven 3.6+**

## 📦 Prerequisites

Before you begin, ensure you have the following installed:

- **Java 25** or higher
- **Maven 3.6+**
- **Docker & Docker Compose** - For running PostgreSQL, Kafka, and Keycloak
- **IDE** (IntelliJ IDEA, Eclipse, or VS Code)

## 🐳 Infrastructure Setup

### 1. Start Docker Containers

The project includes a `docker-compose.yml` file in the root cinema-booking directory that sets up all required
infrastructure:

```bash
# From the cinema-booking root directory
cd /path/to/cinema-booking
docker compose up -d
```

This will start the following services:

#### PostgreSQL 18

- **Port**: 5431 (mapped from container port 5432)
- **Username**: yu71
- **Password**: 53cret
- **Database**: cinema_booking
- **Health Check**: Enabled

#### Zookeeper

- **Port**: 2181
- **Health Check**: Enabled

#### Kafka

- **Bootstrap Server**: localhost:9092
- **Internal Port**: 29092
- **Additional Port**: 9093
- **Health Check**: Enabled

#### Keycloak

- **Port**: 8080
- **Admin Username**: admin
- **Admin Password**: admin
- **Admin Console**: http://localhost:8080
- **Database**: Uses PostgreSQL (cinema_booking)
- **Mode**: Development mode (start-dev)
- **Health Check**: Enabled

### 2. Verify All Services are Running

```bash
docker compose ps
```

All services should show status as "Up" and "healthy" after a minute.

### 3. Check Service Connectivity

**PostgreSQL**:

```bash
docker exec cinema-postgres psql -U yu71 -d cinema_booking -c "SELECT version();"
```

**Kafka**:

```bash
docker exec cinema-kafka kafka-topics --list --bootstrap-server localhost:9092
```

**Keycloak**:

```bash
curl http://localhost:8080/health/ready
```

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/hendisantika/cinema-booking.git
cd cinema-booking/reservation-service
```

### 2. Configure Environment Variables (Optional)

The application is pre-configured to work with the Docker containers. You can override settings using environment
variables:

```properties
# Database Configuration
DATABASE_HOST=localhost
DATABASE_PORT=5431
DATABASE_NAME=cinema_booking
DATABASE_USERNAME=yu71
DATABASE_PASSWORD=53cret
# Kafka Configuration
KAFKA_BOOTSTRAP_SERVERS=localhost:9092
# Keycloak Configuration
JWT_SET_URL=http://127.0.0.1:8080/realms/CineBook/protocol/openid-connect/certs
# Application Configuration
SPRING_APP_NAME=reservation-service
HIBERNATE_DDL_AUTO=update
JPA_SHOW_SQL=true
SPRING_SECURITY_LOG_LEVEL=TRACE
SESSION_TIMEOUT=20m
```

### 3. Build the Application

```bash
mvn clean package -DskipTests
```

### 4. Run the Application

```bash
java -jar target/reservation-service-0.0.1-SNAPSHOT.jar
```

Or using Maven:

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8081` (Note: Port 8080 is used by Keycloak)

### 5. Database Schema Creation

On first run, Hibernate will automatically create all required tables:

- `branch` - Cinema branches
- `cinema` - Cinema halls
- `customer` - User accounts
- `movie` - Movie information
- `seat` - Seat inventory
- `reservation` - Booking records
- `seat_reservation` - Many-to-many mapping
- `movie_branch` - Many-to-many mapping

## ⚙ Configuration

### Application Properties

Key configuration properties (`src/main/resources/application.properties`):

```properties
# Application Name
spring.application.name=${SPRING_APP_NAME:reservation-service}
# Database
spring.datasource.url=jdbc:postgresql://${DATABASE_HOST:localhost}:${DATABASE_PORT:5431}/${DATABASE_NAME:cinema_booking}
spring.datasource.username=${DATABASE_USERNAME:yu71}
spring.datasource.password=${DATABASE_PASSWORD:53cret}
# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=${HIBERNATE_DDL_AUTO:update}
spring.jpa.show-sql=${JPA_SHOW_SQL:true}
# OAuth2/Keycloak
spring.security.oauth2.resourceserver.jwt.jwk-set-uri=${JWT_SET_URL:http://127.0.0.1:8080/realms/CineBook/protocol/openid-connect/certs}
# Kafka
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.consumer.group-id=reservation-service-group
```

### Seat Types and Pricing

| Seat Type  | Base Price (LKR) | Description                        |
|------------|------------------|------------------------------------|
| STANDARD   | 500.00           | Regular seating                    |
| PREMIUM    | 800.00           | Enhanced comfort seating           |
| VIP        | 1200.00          | Premium seating with extra legroom |
| LUXURY     | 1500.00          | Luxury reclining seats             |
| RECLINER   | 1800.00          | Full reclining seats with footrest |
| COUPLE     | 2000.00          | Spacious double seats for couples  |
| WHEELCHAIR | 500.00           | Wheelchair accessible seating      |

## 📚 API Documentation

### Base URL

```
http://localhost:8081/api
```

### Swagger/OpenAPI Documentation

```
http://localhost:8081/swagger-ui.html
```

### Authentication

All endpoints (except health checks) require JWT authentication via Keycloak:

```http
Authorization: Bearer <jwt_token>
```

### Main Endpoints

#### Customer Endpoints

```http
GET    /api/customer              # Get all customers (ADMIN)
POST   /api/customer              # Create customer (ADMIN)
PUT    /api/customer              # Update customer (ADMIN)
GET    /api/customer/{email}      # Get customer by email (USER/ADMIN)
DELETE /api/customer/{email}      # Delete customer (ADMIN)
POST   /api/customer/sync         # Sync user from Keycloak (USER/ADMIN)
```

#### Reservation Endpoints

```http
POST   /api/reservation           # Create reservation (USER/ADMIN)
GET    /api/reservation           # Get all reservations (ADMIN)
GET    /api/reservation/{id}      # Get reservation by ID (USER/ADMIN)
PUT    /api/reservation/{id}      # Update reservation (USER/ADMIN)
DELETE /api/reservation/{id}      # Cancel reservation (USER/ADMIN)
```

#### Movie, Seat, Cinema, and Branch Endpoints

Refer to the Swagger documentation for complete endpoint listings.

### WebSocket Endpoint

For real-time reservation status updates:

```
ws://localhost:8081/status
```

## 🐛 Known Issues

### Spring Security Configuration (Spring Boot 4.x Compatibility)

The application currently has a compatibility issue with Spring Boot 4.0.1 and Spring Security 7.x regarding the
`ChannelDecisionManager` class which has been removed in the newer version.

**Status**:

- ✅ Database connection working
- ✅ Kafka integration configured
- ✅ Core business logic functional
- ⚠️ Security configuration needs updating for Spring Boot 4.x

**Error**:

```
java.lang.NoClassDefFoundError: org/springframework/security/web/access/channel/ChannelDecisionManager
```

**Workaround**: Update security configuration or temporarily disable `requiresChannel()` until resolved.

## 📁 Project Structure

```
reservation-service/
├── src/main/java/id/my/hendisantika/reservationservice/
│   ├── config/              # Configuration classes
│   ├── controller/          # REST Controllers
│   ├── dto/                 # Data Transfer Objects
│   ├── entity/              # JPA Entities
│   ├── enums/               # Enumerations
│   ├── event/               # Kafka Event Producers
│   ├── listener/            # Kafka Event Listeners
│   ├── repository/          # JPA Repositories
│   ├── service/             # Business Logic Services
│   ├── socket/              # WebSocket Handlers
│   └── ReservationServiceApplication.java
├── src/main/resources/
│   ├── application.properties
│   └── .env.example
├── pom.xml
└── README.md
```

## 🔧 Useful Commands

### Docker Management

```bash
# Start all services
docker compose up -d

# Stop all services
docker compose down

# View logs
docker compose logs -f cinema-postgres

# Check status
docker compose ps
```

### Application Management

```bash
# Build
mvn clean package -DskipTests

# Run
mvn spring-boot:run
```

### Database Operations

```bash
# Connect to PostgreSQL
docker exec -it cinema-postgres psql -U yu71 -d cinema_booking
```

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'feat: add AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License.

## 👥 Authors

- **Hendi Santika** - [hendisantika](https://github.com/hendisantika)

## 📞 Contact

- Email: hendisantika@yahoo.co.id
- Telegram: @hendisantika34
- Link: s.id/hendisantika

---

**Made with ❤️ for cinema enthusiasts**

**Last Updated**: 2026-01-14
