```markdown
# Library Management System Backend

A robust, enterprise-grade RESTful API engineered with Spring Boot, Java 21, and MySQL to manage a library ecosystem. This system orchestrates complex data flows, including real-time inventory tracking, bidirectional book-to-patron borrowing transactions, strict payload input validation, and centralized exception handling pipelines.

---

## 🚀 Key Architectural Features

* **Data Engineering & Layer Separation:** Completely decoupled architecture utilizing Entity-to-DTO conversion mapping layers (`BookMapper` and `UserMapper`) to guarantee structural payload safety and prevent infinite serialization loops.
* **Transactional Operations:** Atomic patching mechanisms for issuing (`/borrow`) and processing (`/return`) book checkouts, ensuring zero data inconsistency or relationship corruption.
* **Centralized Error Handling Advisor:** Integrated an application-wide `@RestControllerAdvice` interceptor to gracefully catch field validation exceptions (`MethodArgumentNotValidException`) and custom operational errors (`LibraryException`), converting them into clean JSON response contracts for clients.
* **OpenAPI Integration:** Embedded live, interactive documentation utilizing Swagger UI (Springdoc) to allow instant testing and exploration of API boundaries out of the box.
* **Comprehensive Test Coverage:** Backed by isolated Unit Testing configurations using JUnit 5 and Mockito to simulate service layer logic flows under both successful and volatile failure profiles.

---

## 🛠️ Tech Stack & Prerequisites

* **Language:** Java 21 (Long-Term Support)
* **Framework:** Spring Boot 3.x
* **Data Access:** Spring Data JPA / Hibernate
* **Database:** MySQL 8.x (In-Memory H2 configured for unit testing profiles)
* **Build Tool:** Maven
* **Documentation:** Swagger UI / OpenAPI 3
* **Boilerplate Reduction:** Lombok

---

## 📂 Project Architecture Layout

```text
src/main/java/com/irons/library_management_system_backend/
 ├── config/       # OpenAPI/Swagger Documentation Configurations
 ├── controller/   # REST Controllers exposing API gateways and routing definitions
 ├── dto/          # Request and Response Data Transfer Objects with validation constraints
 ├── entities/     # JPA Database Models mapped with bidirectional @OneToMany relationships
 ├── exception/    # Custom Business Exception wrappers and Centralized Global Error Handler
 ├── mapper/       # Custom mapping pipelines converting Entities safely into DTO definitions
 ├── repository/   # Database Access Abstraction Interfaces extending JpaRepository
 └── service/      # Business logic processing layer orchestrating system constraints

```

---

## 🚦 Getting Started

### 1. Database Setup

Log into your MySQL terminal or GUI manager and create a fresh database matching your application profile coordinates:

```sql
CREATE DATABASE librarydb;

```

### 2. Configure Environment Properties

Navigate to `src/main/resources/application.properties` and synchronize your database connectivity profiles:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/librarydb?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.datasource.username=YOUR_MYSQL_USERNAME
spring.datasource.password=YOUR_MYSQL_PASSWORD

# Hibernate Lifecycle Strategy
spring.jpa.hibernate.ddl-auto=update

# Enable SQL Query Formatting inside Logging Terminal
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

```

### 3. Build and Run the Application

Compile the project, execute all internal verification unit tests, and boot up the server engine locally:

```bash
mvn clean package
mvn spring-boot:run

```

The application will boot up on port `8080` by default.

---

## 📖 API Documentation & Testing

Once the backend service instance is fully operational, you can explore, test, and trigger live data requests through the interactive Swagger UI panel:

* **Swagger UI URL:** `http://localhost:8080/swagger-ui/index.html`

### Endpoint Blueprint Summary

#### 📚 Books Routing Matrix

| Method | Endpoint | Description | Payload Constraints |
| --- | --- | --- | --- |
| **GET** | `/api/books/all` | Retrieves entire library catalog inventory | None |
| **GET** | `/api/books/available` | Filter and discover books current marked available | None |
| **GET** | `/api/books/unavailable` | Filter and track currently borrowed library inventory | None |
| **GET** | `/api/books/id/{id}` | Look up explicit book properties by Database ID | Path Variable |
| **GET** | `/api/books/name/{name}` | Search book record using matching textual string name | Path Variable |
| **POST** | `/api/books/add` | Append new book payload to inventory registry | `@Valid BookRequestDTO` |
| **DELETE** | `/api/books/{id}` | Purge book item completely from inventory rows | Path Variable |
| **PATCH** | `/api/books/borrow` | Commit transaction to checkout book to specific patron | Query Params (`bookId`, `userId`) |
| **PATCH** | `/api/books/return` | Release borrowing user link and set book to available | Query Params (`bookId`, `userId`) |

#### 👥 Users Routing Matrix

| Method | Endpoint | Description | Payload Constraints |
| --- | --- | --- | --- |
| **GET** | `/api/users/all` | Fetch catalog profiles of all registered users | None |
| **GET** | `/api/users/id/{id}` | Locate specific member record by Database ID | Path Variable |
| **GET** | `/api/users/name/{name}` | Search user entity using alphanumeric string key | Path Variable |
| **GET** | `/api/users/books/{id}` | Fetch list tracking all active book elements held by user | Path Variable |
| **GET** | `/api/users/admins` | Filter group containing only authorized administrative members | None |
| **GET** | `/api/users/members` | Filter view extracting core non-administrative library patrons | None |
| **POST** | `/api/users/add` | Registry gateway to introduce a new user profile payload | `@Valid UserRequestDTO` |
| **DELETE** | `/api/users/{id}` | Remove user completely from application databases | Path Variable |
| **PATCH** | `/api/users/{id}` | Escalate a normal member's security role group to Admin | Path Variable |

---

## 🧪 Testing Profiles

To isolate and secure data processing rules, all core service behaviors contain complete verification test workflows executing logic validations without spinning up a live MySQL engine instance.

Run the automated validation test suites using the following Maven execution flag:

```bash
mvn test

```

```text
Results:
Tests run: 32, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS

```
