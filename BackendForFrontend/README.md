# SmartLogix Backend For Frontend

Spring Boot BFF used as the public backend entry point for the Svelte frontend.

## Responsibilities

- Expose frontend-friendly endpoints under `/api`.
- Generate JWT tokens from a basic login endpoint.
- Validate JWT tokens on protected endpoints.
- Forward product requests to `inventory-service`.
- Hide internal microservice URLs from the frontend.

## Run Locally

```bash
./mvnw spring-boot:run
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

The BFF runs on:

```text
http://localhost:8080
```

## Configuration

Default local values:

```text
SMARTLOGIX_AUTH_USERNAME=admin
SMARTLOGIX_AUTH_PASSWORD=admin123
SMARTLOGIX_JWT_SECRET=smartlogix-development-secret-key-change-me-2026
SMARTLOGIX_JWT_EXPIRATION_SECONDS=3600
INVENTORY_SERVICE_URL=http://localhost:8081
```

The JWT secret must match the value configured in `inventory-service`.

## Login

```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

Response:

```json
{
  "token": "<jwt>",
  "tokenType": "Bearer",
  "expiresIn": 3600
}
```

## Product API

The frontend should call these endpoints using the token returned by login:

```http
Authorization: Bearer <jwt>
```

Available endpoints:

```http
GET    /api/products
GET    /api/products/{id}
POST   /api/products
PUT    /api/products/{id}
PATCH  /api/products/{id}/stock
DELETE /api/products/{id}
```

The BFF forwards these calls to `inventory-service` using the same JWT.

## Docker

Recommended option from the backend root:

```bash
docker compose up --build
```

This starts the BFF, `inventory-service`, and PostgreSQL together.

Manual container build:

Build the image:

```bash
docker build -t smartlogix-bff .
```

Run the container:

```bash
docker run --rm -p 8080:8080 \
  -e SMARTLOGIX_AUTH_USERNAME=admin \
  -e SMARTLOGIX_AUTH_PASSWORD=admin123 \
  -e SMARTLOGIX_JWT_SECRET=smartlogix-development-secret-key-change-me-2026 \
  -e INVENTORY_SERVICE_URL=http://host.docker.internal:8081 \
  smartlogix-bff
```
