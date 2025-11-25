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
*   **Docker & Docker Compose**

### Running the Application
The project is fully containerized, including the application, PostgreSQL database, and Google Pub/Sub emulator.

1.  **Build and Start:**
    ```bash
    docker-compose up --build
    ```

2.  **Access the Application:**
    The API will be available at `http://localhost:8080`.

    *   **PostgreSQL:** Port `5432` (User: `admin`, Pass: `password`, DB: `opsguard`)
    *   **Pub/Sub Emulator:** Port `8085`