# Guia de Integracion Frontend - SmartLogix BFF

Este documento explica como el frontend debe comunicarse con el Backend For Frontend (BFF) y como funciona el flujo backend por detras.

El frontend debe comunicarse solo con el BFF. No debe llamar directamente a `inventory-service`.

```text
Frontend Svelte
  -> BackendForFrontend
  -> inventory-service
```

## URLs Locales

Servicios locales por defecto:

```text
BFF:               http://localhost:8080
Inventory service: http://localhost:8081
Frontend:          http://localhost:5173
```

El frontend debe usar solo esta URL base:

```text
http://localhost:8080
```

## Flujo De Autenticacion

### 1. Login

El frontend envia las credenciales al BFF:

```http
POST /api/auth/login
Content-Type: application/json
```

Body:

```json
{
  "username": "admin",
  "password": "admin123"
}
```

Respuesta exitosa:

```json
{
  "token": "jwt-token-aqui",
  "tokenType": "Bearer",
  "expiresIn": 3600
}
```

El frontend debe guardar el valor de `token` y enviarlo en las siguientes solicitudes.

Para esta entrega, se puede guardar en `localStorage`:

```ts
localStorage.setItem('smartlogix_token', data.token);
```

### 2. Solicitudes Autenticadas

Todas las solicitudes protegidas deben enviar el token asi:

```http
Authorization: Bearer <token>
```

Ejemplo:

```ts
const token = localStorage.getItem('smartlogix_token');

const response = await fetch('http://localhost:8080/api/products', {
  headers: {
    Authorization: `Bearer ${token}`
  }
});
```

## Ejemplos Para El Frontend

### Login

```ts
export async function login(username: string, password: string) {
  const response = await fetch('http://localhost:8080/api/auth/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ username, password })
  });

  if (!response.ok) {
    throw new Error('Credenciales invalidas');
  }

  const data = await response.json();
  localStorage.setItem('smartlogix_token', data.token);
  return data;
}
```

### Helper Para Headers

```ts
function authHeaders() {
  const token = localStorage.getItem('smartlogix_token');

  return {
    'Content-Type': 'application/json',
    Authorization: `Bearer ${token}`
  };
}
```

### Listar Productos

```ts
export async function getProducts() {
  const response = await fetch('http://localhost:8080/api/products', {
    headers: authHeaders()
  });

  if (!response.ok) {
    throw new Error('No se pudieron cargar los productos');
  }

  return response.json();
}
```

### Crear Producto

```ts
export async function createProduct(product: {
  sku: string;
  name: string;
  description?: string;
  category: string;
  unitPrice: number;
  stock: number;
}) {
  const response = await fetch('http://localhost:8080/api/products', {
    method: 'POST',
    headers: authHeaders(),
    body: JSON.stringify(product)
  });

  if (!response.ok) {
    throw new Error('No se pudo crear el producto');
  }

  return response.json();
}
```

### Actualizar Producto

```ts
export async function updateProduct(id: number, product: {
  sku: string;
  name: string;
  description?: string;
  category: string;
  unitPrice: number;
  stock: number;
}) {
  const response = await fetch(`http://localhost:8080/api/products/${id}`, {
    method: 'PUT',
    headers: authHeaders(),
    body: JSON.stringify(product)
  });

  if (!response.ok) {
    throw new Error('No se pudo actualizar el producto');
  }

  return response.json();
}
```

### Actualizar Stock

```ts
export async function updateStock(id: number, stock: number) {
  const response = await fetch(`http://localhost:8080/api/products/${id}/stock`, {
    method: 'PATCH',
    headers: authHeaders(),
    body: JSON.stringify({ stock })
  });

  if (!response.ok) {
    throw new Error('No se pudo actualizar el stock');
  }

  return response.json();
}
```

### Eliminar Producto

```ts
export async function deleteProduct(id: number) {
  const response = await fetch(`http://localhost:8080/api/products/${id}`, {
    method: 'DELETE',
    headers: authHeaders()
  });

  if (!response.ok) {
    throw new Error('No se pudo eliminar el producto');
  }
}
```

## Endpoints Disponibles Para El Frontend

Todos estos endpoints pertenecen al BFF:

```http
POST   /api/auth/login
GET    /api/products
GET    /api/products/{id}
POST   /api/products
PUT    /api/products/{id}
PATCH  /api/products/{id}/stock
DELETE /api/products/{id}
```

## Contrato De Producto

### Request Para Crear O Actualizar Producto

Se usa en:

```http
POST /api/products
PUT  /api/products/{id}
```

Body:

```json
{
  "sku": "SKU-001",
  "name": "Wireless Mouse",
  "description": "Ergonomic wireless mouse",
  "category": "Accessories",
  "unitPrice": 12990,
  "stock": 25
}
```

Campos requeridos:

```text
sku
name
category
unitPrice
stock
```

Reglas de validacion:

```text
sku: requerido, maximo 80 caracteres
name: requerido, maximo 160 caracteres
description: opcional, maximo 500 caracteres
category: requerido, maximo 100 caracteres
unitPrice: requerido, mayor que 0
stock: requerido, minimo 0
```

### Request Para Actualizar Stock

Se usa en:

```http
PATCH /api/products/{id}/stock
```

Body:

```json
{
  "stock": 40
}
```

### Response De Producto

```json
{
  "id": 1,
  "sku": "SKU-001",
  "name": "Wireless Mouse",
  "description": "Ergonomic wireless mouse",
  "category": "Accessories",
  "unitPrice": 12990,
  "stock": 25,
  "active": true,
  "createdAt": "2026-05-18T19:00:00-04:00",
  "updatedAt": "2026-05-18T19:00:00-04:00"
}
```

## Errores Esperados

### Login Incorrecto

```http
401 Unauthorized
```

Significa que el usuario o la contrasena son incorrectos.

### Token Faltante O Invalido

```http
401 Unauthorized
```

Significa que el frontend no envio token, el token esta mal formado o el token expiro.

En este caso, el frontend deberia cerrar sesion localmente y volver al login.

### Error De Validacion

```http
400 Bad Request
```

Ejemplo:

```json
{
  "timestamp": "2026-05-18T19:00:00-04:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/products",
  "validationErrors": {
    "name": "must not be blank",
    "stock": "must be greater than or equal to 0"
  }
}
```

### SKU Duplicado

```http
409 Conflict
```

Significa que ya existe un producto con ese `sku`.

### Producto No Encontrado

```http
404 Not Found
```

Significa que no existe un producto con el `id` solicitado.

## Como Funciona El Backend

El BFF es el unico backend que debe conocer el frontend.

El BFF cumple dos responsabilidades principales:

- Autenticacion: recibe credenciales y genera tokens JWT.
- Gateway/API frontend: recibe las solicitudes del frontend y las envia al microservicio correspondiente.

En esta etapa, el microservicio disponible es `inventory-service`.

`inventory-service` es responsable de:

- manejar productos;
- manejar stock;
- validar datos de inventario;
- guardar datos en base de datos;
- responder al BFF.

El frontend no necesita saber donde esta corriendo `inventory-service`. Si esa URL cambia, solo se cambia la configuracion del BFF.

## Flujo Actual Del Login

```text
Frontend
  POST /api/auth/login
    -> BFF valida username/password
    -> BFF genera JWT
    -> BFF responde token al frontend
```

## Flujo Actual De Productos

```text
Frontend
  GET /api/products con Bearer token
    -> BFF valida JWT
    -> BFF reenvia la solicitud a inventory-service con el mismo JWT
    -> inventory-service valida JWT
    -> inventory-service responde productos
    -> BFF responde al frontend
```

## Notas Importantes

- El frontend debe llamar siempre al BFF.
- El frontend no debe llamar directamente a `inventory-service`.
- El token se obtiene con `/api/auth/login`.
- El token se envia con `Authorization: Bearer <token>`.
- Si una solicitud responde `401`, probablemente hay que volver al login.
- El BFF permite CORS desde `http://localhost:5173` y `http://localhost:4173`.
- `signup` todavia no esta implementado.
- Por ahora se usa un usuario de prueba configurable:

```text
username: admin
password: admin123
```

