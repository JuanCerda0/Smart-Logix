# SmartLogix - Grupo 9
**Plataforma de Gestión Logística para eCommerce**

---

## Descripción

SmartLogix es un proyecto enfocado en el desarrollo de una plataforma para mejorar la gestión logística de pequeñas y medianas empresas de comercio electrónico.

Surge como respuesta a las limitaciones actuales de muchos negocios, que aún dependen de procesos manuales o sistemas poco flexibles para manejar inventarios, pedidos y envíos.

---

## Problema

Las PYMEs de eCommerce suelen enfrentar:

- Desorden en la gestión de inventario  
- Errores en el procesamiento de pedidos  
- Retrasos en envíos  
- Dificultad para escalar operaciones  

Esto impacta directamente en la eficiencia y en la experiencia del cliente.

---

## Objetivo

Desarrollar una solución que permita:

- Centralizar la operación logística  
- Reducir errores manuales  
- Mejorar la organización y seguimiento de procesos  
- Facilitar el crecimiento del negocio  

---

## Alcance Inicial

El proyecto considera abordar:

- Inventario  
- Pedidos  
- Envíos  
- Gestión básica de usuarios  

---

## Estado del Proyecto

Backend implementado con:

- Backend For Frontend en `BackendForFrontend`
- Microservicio de inventario en `inventory-service`
- PostgreSQL como base de datos local mediante Docker Compose
- H2 solo para tests automatizados del microservicio de inventario

---

## Ejecutar Backend Completo

Desde esta carpeta:

```bash
docker compose up --build
```

Esto levanta:

- PostgreSQL en `localhost:5432`
- `inventory-service` en `http://localhost:8081`
- `BackendForFrontend` en `http://localhost:8080`

El frontend debe consumir el BFF:

```text
http://localhost:8080
```

No debe consumir directamente `inventory-service`.
