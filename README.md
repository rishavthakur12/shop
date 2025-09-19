### Microshop 🛒

A microservices-based e-commerce backend built with **Spring Boot**, **Kafka**, **Eureka**, **MongoDB**, **Postgres** and **Docker**.  
This project includes product, order, inventory, notification services, and an API Gateway, with metrics and monitoring via **Prometheus** and **Grafana**.


---

## Features 

- Microservices architecture
- API Gateway for routing requests
- MongoDB and Postgres database for storage
- Security implementation using KeyCloak
- Kafka for asynchronous communication between services
- Resilience4j for circuit breaker and fault tolerance
- Eureka service discovery
- Prometheus metrics and Grafana dashboards
- Dockerized for easy deployment

---

## Technologies

- Spring Boot
- Spring Cloud (Eureka, Gateway)
- Apache Kafka
- KeyCloak
- Resilience4j
- Postgres, MongoDB  
- Prometheus & Grafana  
- Docker

---

## Docker Images 🐳

All services are available on Docker Hub under my account.

| Service               | Docker Image                                         |
|-----------------------|-----------------------------------------------------|
| API Gateway           | `rishavthakur/api-gateway:latest`     |
| DiscoveryServer       | `rishavthakur/discovery-server:latest`        |
| Product Service       | `rishavthakur/product-service:latest` |
| Order Service         | `rishavthakur/order-service:latest`   |
| Inventory Service     | `rishavthakur/inventory-service:latest` |
| Notification Service  | `rishavthakur/notification-service:latest` |

---

## Architecture

![Ach drawio](https://github.com/user-attachments/assets/1c3834e7-1d20-417c-8662-bd21d4f56439)
