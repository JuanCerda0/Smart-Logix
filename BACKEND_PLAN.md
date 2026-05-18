# SmartLogix Backend Plan

## Context

SmartLogix is a logistics management platform for small and medium eCommerce businesses. The backend must support inventory, orders, shipping/logistics, and basic user management over time.

For this delivery, the backend work will focus on a practical microservice-based structure using Java and Spring Boot. The frontend is counted as one microservice for the assignment, so the backend will start with one backend microservice plus a required Backend For Frontend.

## Repository Structure

All backend components will live inside the same backend repository, separated by folders:

```text
Smart-Logix-Back/
  smartlogix-bff/
  inventory-service/
  docker-compose.yml        # optional later
  README.md
  BACKEND_PLAN.md
```

## Initial Components

### 1. smartlogix-bff

The BFF is the public backend entry point for the frontend.

Responsibilities:

- Expose frontend-friendly REST endpoints.
- Forward product and inventory requests to `inventory-service`.
- Hide internal service URLs from the frontend.
- Prepare the project for centralized authentication with JWT.
- Simplify future integration with more backend services.

Initial API:

```http
GET    /api/products
GET    /api/products/{id}
POST   /api/products
PUT    /api/products/{id}
PATCH  /api/products/{id}/stock
DELETE /api/products/{id}
```

### 2. inventory-service

The inventory service owns product and stock management.

Responsibilities:

- Manage products.
- Manage product stock.
- Persist inventory data.
- Validate product and stock data.
- Expose internal REST endpoints consumed by the BFF.

Initial API:

```http
GET    /products
GET    /products/{id}
POST   /products
PUT    /products/{id}
PATCH  /products/{id}/stock
DELETE /products/{id}
```

## Suggested Technology Stack

Backend:

- Java 17 or Java 21
- Spring Boot 3.x
- Spring Web
- Spring Data JPA
- Bean Validation
- Maven

Database:

- H2 for quick local development and testing.
- PostgreSQL later when Docker Compose is added.

Infrastructure:

- Dockerfile per backend component.
- Docker Compose later to run the BFF, inventory service, and database together.

Security:

- JWT authentication planned for the BFF.
- Internal services should not be consumed directly by the frontend.

## Design Patterns

The backend should use these patterns:

- Repository Pattern: isolate persistence logic with Spring Data repositories.
- DTO Pattern: avoid exposing entity classes directly through APIs.
- Service Layer Pattern: keep business logic outside controllers.
- Backend For Frontend: adapt backend responses and routing for the frontend.

## Development Phases

### Phase 1: Backend Base

- Create `smartlogix-bff` Spring Boot project.
- Create `inventory-service` Spring Boot project.
- Add basic configuration and health endpoints.
- Add README instructions for running each service.

### Phase 2: Inventory Service

- Create product entity.
- Create DTOs for product requests and responses.
- Create repository, service, and controller layers.
- Add CRUD endpoints.
- Add stock update endpoint.
- Add validation.

### Phase 3: BFF Integration

- Add product endpoints under `/api/products`.
- Configure the inventory service base URL.
- Use Spring HTTP client support to call `inventory-service`.
- Return frontend-friendly responses.

### Phase 4: Docker

- Add Dockerfile for `smartlogix-bff`.
- Add Dockerfile for `inventory-service`.
- Verify each service can run as a container.

### Phase 5: Optional Docker Compose

- Add `docker-compose.yml`.
- Run BFF and inventory service together.
- Add PostgreSQL if time allows.

### Phase 6: Optional Extra Microservice

If there is enough time, add `orders-service`.

Potential responsibilities:

- Create customer orders.
- Track order status.
- Coordinate stock reservation with inventory.
- Prepare future shipping/logistics integration.

## Recommended First Goal

The first complete backend milestone should be:

- `inventory-service` has working product CRUD.
- `smartlogix-bff` exposes `/api/products`.
- The frontend only calls the BFF.
- Both backend components include Dockerfiles.
- The repository includes clear run instructions.

This keeps the scope realistic while still matching the assignment requirements and the SmartLogix architecture.
