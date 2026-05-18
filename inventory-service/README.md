# SmartLogix Inventory Service

Spring Boot microservice responsible for product and stock management.

## Run Locally

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

## Run Tests

```bash
./mvnw test
```

On Windows:

```bash
mvnw.cmd test
```

## Security

Product endpoints require HTTP Basic authentication.

Default local credentials:

```text
username: inventory
password: inventory123
```

These can be changed with environment variables:

```text
INVENTORY_SERVICE_USER
INVENTORY_SERVICE_PASSWORD
```

Public endpoints:

```http
GET /actuator/health
GET /actuator/info
```

## H2 Console

The local in-memory database console is available at:

```text
http://localhost:8081/h2-console
```

Connection values:

```text
JDBC URL: jdbc:h2:mem:smartlogix_inventory
User: sa
Password:
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
  -e INVENTORY_SERVICE_USER=inventory \
  -e INVENTORY_SERVICE_PASSWORD=inventory123 \
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
Authorization: Basic inventory inventory123

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
