# OpsGuard - Simple Incident Management

OpsGuard is a basic Spring Boot application developed as a learning project. It demonstrates a foundational REST API for managing incidents.

## Current Features

*   **REST API:** Provides endpoints for basic Create, Read, Update, and Delete (CRUD) operations on incidents.
*   **Data Persistence:** Incidents are stored in a PostgreSQL database.
*   **Incident Structure:** Each incident includes properties for description, severity, and status.

## Technologies Used

*   **Java 21:** The core programming language.
*   **Spring Boot 3:** Framework for building the application.
*   **PostgreSQL:** Relational database for storing incident data.
*   **Spring Data JPA/Hibernate:** Used for interacting with the database.
*   **Lombok:** Reduces boilerplate code (e.g., getters, setters).
*   **Maven:** Project build automation tool.
*   **GCP Pub/Sub:** Configured as a project dependency (though not actively used in the current demo logic).
