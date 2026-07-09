# Eclipse Hotel: Spring Boot REST API

Eclipse Hotel is a RESTful backend application developed as a technical challenge during a backend development process.

The project focuses on building a well-structured hotel management system using Spring Boot, applying good software engineering practices such as domain separation, validation, testing, API documentation and containerized environments.

Although the project does not aim to replicate a complete enterprise hotel platform, it represents an important step in understanding how real backend systems organize business rules, data relationships and application architecture.

The entire environment can be executed using Docker Compose, providing a consistent and reproducible development setup.

---

# ✨ Features

## 👥 Customer Management

Complete customer management with CRUD operations.

Features:

- Customer creation
- Customer information update
- Customer lookup
- Customer deletion with reservation validation
- Automatic address retrieval through ViaCEP integration


## 🏨 Room Management

Responsible for managing hotel rooms.

Features:

- Room creation
- Room update
- Room lookup
- Room availability search
- Current occupied rooms visualization
- Room deletion validation


## 📅 Reservation System

Core business domain of the application.

Features:

- Reservation creation
- Availability validation before booking
- Reservation cancellation
- Reservation completion
- Reservation filtering by date range
- Current room occupancy management


## ⚡ Performance and Reliability

The project also explores backend improvements beyond basic CRUD operations.

Features:

- Query caching with TTL configuration
- Detailed application logging
- Unit testing for business logic
- API documentation through Swagger/OpenAPI

---

# 🏗️ Architecture and Development Focus

The main challenge of Eclipse Hotel was organizing the application properly while dealing with real business rules.

The project focuses on:

- Clear separation between controllers, services and repositories
- Proper domain modeling
- Business validation
- Entity relationships
- Maintainable REST API design
- Automated testing of service layers

The goal was not only to make the API work, but to understand how backend applications should be structured to remain maintainable.

---

# 🚀 Technologies Used

## Backend

- Java 21
- Spring Boot 3
- Spring MVC
- Spring Data JPA
- Lombok


## Databases

- PostgreSQL 13 (Docker environment)
- H2 Database (local development)


## API Documentation

- SpringDoc OpenAPI
- Swagger UI


## Infrastructure

- Docker
- Docker Compose


## Build and Dependencies

- Maven


## Testing

- JUnit 5
- Mockito

---

# 📡 API Overview

The API exposes REST endpoints for managing customers, rooms and reservations.

Main resources:

## Customers

    GET     /customers
    GET     /customers/{id}
    POST    /customers
    PUT     /customers/{id}
    DELETE  /customers/{id}


## Rooms

    GET     /rooms
    GET     /rooms/{id}
    GET     /rooms/available
    GET     /rooms/occupied
    POST    /rooms
    PUT     /rooms/{id}
    DELETE  /rooms/{id}


## Reservations

    GET     /reservations
    GET     /reservations/{id}
    POST    /reservations
    PATCH   /reservations/{id}/cancel


Full API documentation is available through Swagger.

---

# 🐳 Running Locally

## Requirements

- Java 21+
- Docker
- Docker Compose
- Maven


---

## Running with Docker (Recommended)

Clone the repository:

    git clone https://github.com/gaelcoder/EclipseHotel


Navigate to the project:

    cd EclipseHotel


Start the application:

    docker-compose up --build


This will:

- Build the application container
- Start PostgreSQL
- Configure the database environment
- Persist database data through Docker volumes


Access:

API:

    http://localhost:8080/


Swagger Documentation:

    http://localhost:8080/swagger-ui.html


---

## Running Locally with Maven

Clone the repository:

    git clone https://github.com/gaelcoder/EclipseHotel

Navigate to the folder:

    cd EclipseHotel


Run:

    mvn spring-boot:run


The application will use H2 as an in-memory database.

Access:

API:

    http://localhost:8080/


H2 Console:

    http://localhost:8080/h2-console


JDBC URL:

    jdbc:h2:mem:testdb


---

# 🧪 Running Tests

Execute:

    mvn clean test


The project includes unit tests focused on service-layer business rules.

---

# 💡 Development Experience

Building Eclipse Hotel was an important step in my backend journey.

Although I had already worked with Spring Boot before, this project introduced new challenges: a shorter development deadline, stricter evaluation requirements and the need to implement concepts I had not deeply explored before, such as caching and more extensive unit testing.

It provided a more realistic view of how production-oriented APIs need to be designed, especially regarding organization, maintainability and reliability.

---

# 🏁 Main Concepts Explored

Through Eclipse Hotel, the main concepts explored were:

- RESTful API design
- Backend architecture organization
- Business rule modeling
- Database relationships
- External API integration
- Data validation
- Caching strategies
- Automated testing
- Containerized development environments


---

# 👨‍💻 Author

Gabriel Azevedo

GitHub:
https://github.com/gaelcoder

LinkedIn:
https://www.linkedin.com/in/gabrielsaz/
