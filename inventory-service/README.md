# SmartLogix Inventory Service

Spring Boot microservice responsible for product and stock management.

## Run Locally

By default, the service uses PostgreSQL. Start PostgreSQL with Docker Compose from the backend root:

```bash
docker compose up postgres
```

Then run the service:

```bash
./mvnw spring-boot:run
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

The service runs on:

```text
http://localhost:8081
```

## Database

Default local PostgreSQL values:

```text
SMARTLOGIX_DB_URL=jdbc:postgresql://localhost:5432/smartlogix_inventory
SMARTLOGIX_DB_USERNAME=smartlogix
SMARTLOGIX_DB_PASSWORD=smartlogix123
```

When running with Docker Compose, these values are configured automatically.

H2 is kept only for automated tests through the `test` Spring profile.

## Run Tests

```bash
./mvnw test
```

On Windows:

```bash
mvnw.cmd test
```

## Security

Product endpoints require JWT Bearer authentication.

The service validates HS256 JWT tokens using a shared secret. For local development, the default secret is:

```text
smartlogix-development-secret-key-change-me-2026
```

Override it with:

```text
SMARTLOGIX_JWT_SECRET
```

The BFF should send requests to this service with:

```http
Authorization: Bearer <jwt>
```

Public endpoints:

```http
GET /actuator/health
GET /actuator/info
```

## Product API

Base URL:

```text
http://localhost:8081/products
```

## Docker

Build the image:

```bash
docker build -t smartlogix-inventory-service .
```

Run the container:

```bash
docker run --rm -p 8081:8081 \
  -e SMARTLOGIX_DB_URL=jdbc:postgresql://host.docker.internal:5432/smartlogix_inventory \
  -e SMARTLOGIX_DB_USERNAME=smartlogix \
  -e SMARTLOGIX_DB_PASSWORD=smartlogix123 \
  -e SMARTLOGIX_JWT_SECRET=smartlogix-development-secret-key-change-me-2026 \
  smartlogix-inventory-service
```

### List Products

```http
GET /products
```

### Get Product By ID

```http
GET /products/{id}
```

### Create Product

```http
POST /products
Content-Type: application/json
Authorization: Bearer <jwt>

{
  "sku": "SKU-001",
  "name": "Wireless Mouse",
  "description": "Ergonomic wireless mouse",
  "category": "Accessories",
  "unitPrice": 12990,
  "stock": 25
}
```

### Update Product

```http
PUT /products/{id}
Content-Type: application/json

{
  "sku": "SKU-001",
  "name": "Wireless Mouse",
  "description": "Ergonomic wireless mouse",
  "category": "Accessories",
  "unitPrice": 11990,
  "stock": 30
}
```

### Update Stock

```http
PATCH /products/{id}/stock
Content-Type: application/json

{
  "stock": 40
}
```

### Delete Product

```http
DELETE /products/{id}
```
