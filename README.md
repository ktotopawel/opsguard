### **Revised `README.md`**

# OpsGuard

> A cloud-native Incident Response platform simulating high-availability enterprise architecture.

---

## Architecture

```mermaid
graph LR
    User[Client / API] -->|HTTP POST| API[OpsGuard Service]
    API -->|Persist| DB[(PostgreSQL)]
    API -->|Audit Log| DB
    API -->|Async Event| PubSub[Google Pub/Sub]
    PubSub -->|Consume| Listener[Alert Consumer]
````

## Key Features

* **Cloud-Native Messaging:** Decoupled architecture using **Google Cloud Pub/Sub** for asynchronous critical alert propagation.
* **Enterprise Compliance:** Full data auditing with **Hibernate Envers** (tracking *who* changed *what* and *when*).
* **Database Version Control:** Schema evolution managed safely via **Flyway Migrations**.
* **Advanced Search:** Dynamic filtering using **JPA Specifications** (e.g., find incidents by multiple Tags & Severity).
* **Security:** Custom **Interceptor-based Authentication** mimicking an API Gateway (header-based Identity Context).
* **Resilience:** Optimistic concurrency handling to prevent race conditions during high-load tag creation.

## Tech Stack

* **Java 21**
* **Spring Boot 3.3**
* **PostgreSQL 15**
* **Google Cloud Pub/Sub**
* **Docker & Docker Compose**

-----

## Quick Start

The project includes a fully containerized environment (App + DB + Cloud Emulator).

### Option A: Full Stack (Demo Mode)

Run the entire application inside Docker.

```bash
docker-compose --profile prod up --build -d
```

* **API:** http://localhost:8080
* **Swagger UI:** http://localhost:8080/swagger-ui/index.html

### Option B: Developer Mode

Run infrastructure in Docker, but run the Java App in your IDE (IntelliJ) for debugging.

```bash
docker-compose --profile dev up -d
```

-----

## Testing the Loop

**1. Create a User (Public Endpoint)**

```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"username": "sre_lead", "email": "admin@opsguard.com", "password": "secure"}'
```

**2. Report a CRITICAL Incident**
*Requires Authentication Header*

```bash
curl -X POST http://localhost:8080/api/incidents \
  -H "Content-Type: application/json" \
  -H "X-User-Id: 1" \
  -d '{
    "description": "Production DB Latency Spike", 
    "severity": "CRITICAL", 
    "tags": ["Infra", "Database"]
  }'
```

**3. Verify the Flow**
Check the logs (`docker logs opsguard-app`) to see the event lifecycle:

1.  `INFO: Created GCP Pub/Sub topic crit-alert`
2.  `INFO: Published incident with id 1...`
3.  `INFO: [CONSUMER] Received message: {...}`

<!-- end list -->
