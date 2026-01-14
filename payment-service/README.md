# Payment Service

## Overview

Payment Service handles secure payment processing through Stripe API. It uses Stripe webhooks for asynchronous payment
confirmation in the Cinema Booking System.

## Features

- 💳 Stripe API payment processing
- 🔔 Webhook handling for payment confirmation
- 📨 Kafka event-driven communication
- 🔐 Webhook signature verification
- 🔄 Idempotency for duplicate prevention

## Tech Stack

- Spring Boot 4.0
- Java 21
- PostgreSQL
- Apache Kafka
- Stripe API (with Webhooks)

## How It Works

1. Consumes `reservation-created` events from Kafka
2. Creates Stripe Payment Intent
3. Customer completes payment
4. Stripe sends webhook to confirm payment
5. Publishes `payment-completed` event to Kafka

## API Endpoints

**Stripe Webhook** (Critical)

```http
POST /api/stripe/webhook
```

## Configuration

```yaml
stripe:
  api-key: ${STRIPE_API_KEY}
  webhook-secret: ${STRIPE_WEBHOOK_SECRET}

spring:
  kafka:
    bootstrap-servers: kafka:9092
  datasource:
    url: jdbc:postgresql://payment-db:5432/payment_db
```

## Environment Variables

```env
STRIPE_API_KEY=sk_test_xxx
STRIPE_WEBHOOK_SECRET=whsec_xxx
```

## Setup

**Local Development**

```bash
# Build
mvn clean install

# Run
mvn spring-boot:run

# Test webhooks locally
stripe listen --forward-to localhost:8082/api/payments/webhook
```

**Docker**

```bash
docker-compose up payment-service
```

## Webhook Setup

**Development:**

```bash
stripe listen --forward-to localhost:8082/api/payments/webhook
```

**Production:**

1. Go to Stripe Dashboard → Webhooks
2. Add endpoint: `https://your-domain.com/api/payments/webhook`
3. Select events: `payment_intent.succeeded`, `payment_intent.payment_failed`
4. Copy webhook secret to environment

## Test Cards

| Card                | Result   |
|---------------------|----------|
| 4242 4242 4242 4242 | Success  |
| 4000 0000 0000 9995 | Declined |

## Kafka Topics

- **Consumer**: `reservation-created`
- **Producer**: `payment-completed`, `payment-failed`

## Database

```sql
CREATE TABLE payments
(
    id                       BIGSERIAL PRIMARY KEY,
    reservation_id           VARCHAR(255),
    stripe_payment_intent_id VARCHAR(255),
    amount                   DECIMAL(10, 2),
    status                   VARCHAR(50),
    webhook_received         BOOLEAN DEFAULT FALSE,
    created_at               TIMESTAMP
);
```

## Security

✅ Webhook signature verification  
✅ Environment variable API keys  
✅ Idempotency support  
✅ HTTPS required for production webhooks

## Troubleshooting

**Webhook signature fails:**

```bash
# Check webhook secret matches
echo $STRIPE_WEBHOOK_SECRET
```

**Local webhooks not working:**

```bash
# Use Stripe CLI
stripe listen --forward-to localhost:8082/api/payments/webhook
```

## Links

- [Stripe Webhooks Docs](https://stripe.com/docs/webhooks)
- [GitHub Repo](https://github.com/Garuka404/CineBook)

---

**Port:** 8082  
**Uses:** Stripe webhooks for payment confirmation