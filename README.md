# ✈️ Flight Booking Microservices System

## 🧭 Overview

This project is a **Flight Booking Management System** built using **Spring Boot** and follows a **microservices architecture**. The system is designed to handle user authentication, flight searching, and booking functionalities in a scalable, decoupled, and resilient manner.

Each core responsibility is handled by its own microservice, and all services communicate via REST using **Spring Cloud OpenFeign**. The application also uses **API Gateway**, **Redis Caching**, **Eureka Service Registry**, and implements **JWT-based security** for secure communication.

---

## 🧱 Microservices Structure

The system is composed of the following microservices:

### 1. 🔐 Authentication Service
- Manages **user registration** and **login**
- Secures endpoints using **JWT tokens**
- Uses **Spring Security** for role-based access

**Endpoints:**
- `POST /api/auth/register` – Register a new user
- `POST /api/auth/login` – Login and receive JWT token

---

### 2. ✈️ Flight Service
- Handles all flight-related operations: **creation**, **searching**, **updating**, and **availability checking**
- Integrated with **Redis** to cache flight data and reduce DB hits by 25%
- Interacts with Booking Service for **seat management**

**Key Features:**
- Register, update, delete flights
- Search by departure time, location, and flight number
- Check seat availability
- Update and release seats post booking/cancellation

---

### 3. 📑 Booking Service
- Manages flight bookings and cancellations
- Uses **Feign Client** to communicate with the Flight Service to **check availability** and **update seats**
- Handles business logic for canceling and retrieving booking details

**Endpoints:**
- `POST /api/booking` – Book a flight
- `GET /api/booking` – Retrieve all bookings
- `GET /api/booking/{id}` – Retrieve booking by ID
- `PUT /api/booking/cancel/{id}` – Cancel a booking
- `DELETE /api/booking/{id}` – Delete a booking

---

### 4. 🛡️ API Gateway
- Acts as the **entry point** for all clients
- Routes incoming requests to the appropriate microservices
- Handles **JWT token validation**
- Uses **Spring Cloud Gateway**

**Benefits:**
- Single endpoint for all services
- Centralized security
- Easy request routing
- Simplified client interaction

---

### 5. 🧭 Eureka Server (Service Registry)
- Enables **dynamic service discovery**
- Each microservice registers itself on startup
- Used by **API Gateway** and **Feign Clients** for locating services without hardcoding URLs

---

### 6. ⚡ Fault Tolerance (Circuit Breaker)
- Implemented using **Resilience4j** (or suitable Spring Cloud dependency)
- Ensures system stability by preventing cascading failures
- Example use case:
  - If the Flight Service is down, the Booking Service will gracefully fallback to a defined response instead of crashing.

---

## 🧰 Tech Stack

| Tech                     | Role                                     |
|--------------------------|------------------------------------------|
| Java                     | Primary language                         |
| Spring Boot              | Application framework                    |
| Spring Cloud             | Microservices tools (Gateway, Eureka, Feign) |
| Spring Security + JWT    | Authentication and authorization         |
| Redis                    | Caching layer for flight data            |
| MySQL                    | Persistent relational database           |
| OpenFeign                | Declarative REST client for services     |
| Swagger / OpenAPI        | API documentation                        |
| Maven                    | Build and dependency management          |
| Resilience4j             | Fault tolerance (circuit breakers)       |

---
