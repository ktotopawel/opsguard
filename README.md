# OpsGuard

OpsGuard is a Spring Boot application designed to demonstrate incident management capabilities with a RESTful API, database persistence, and asynchronous messaging integration.

## Features

### Incident Management
*   **REST API:** Full CRUD operations for managing incidents.
*   **Data Model:** Tracks incident details including description, severity, and status.

### User Management
*   **User Profiles:** Create and retrieve user information.
*   **Authentication:** Custom header-based authentication mechanism using `X-User-Id`.
*   **Public Endpoints:** Specific endpoints (like User Creation) are accessible without authentication.

### Asynchronous Messaging
*   **GCP Pub/Sub Integration:** Actively subscribes to a Google Cloud Pub/Sub topic (`CRIT_ALERT_SUB`) to consume and log critical alert messages.

## Technology Stack

*   **Language:** Java 21
*   **Framework:** Spring Boot 3
*   **Database:** PostgreSQL
*   **ORM:** Spring Data JPA / Hibernate
*   **Cloud Integration:** Spring Cloud GCP (Pub/Sub)
*   **Utilities:** Lombok
*   **Build Tool:** Maven

## Getting Started

### Prerequisites
*   Java 21 SDK
*   PostgreSQL database
*   Google Cloud Platform credentials (for Pub/Sub)

### Configuration
Ensure your `application.properties` is configured with:
*   PostgreSQL connection details (`spring.datasource.url`, etc.)
*   GCP credentials and project ID.

### Running the Application
```bash
./mvnw spring-boot:run
```