# Notification Service

## Overview

Notification Service handles automated email notifications for booking confirmations. It consumes payment events from
Kafka and sends emails via SMTP.

## Features

- 📧 Automated email notifications
- 📨 Kafka event consumption
- ✅ Booking confirmation emails
- 📊 Notification history tracking
- 🔄 Retry mechanism for failed emails

## Tech Stack

- Spring Boot 4.0
- Java 21
- PostgreSQL
- Apache Kafka
- SMTP (Email Service)

## How It Works

1. Consumes `payment-completed` events from Kafka
2. Retrieves booking details
3. Sends confirmation email via SMTP
4. Logs notification status in database