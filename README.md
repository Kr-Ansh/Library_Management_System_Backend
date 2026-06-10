# Library Management System Ecosystem

A distributed, enterprise-grade event-driven microservices ecosystem engineered with **Spring Boot 3.x**, **Java 21 (LTS)**, and **Gradle**. This system is completely containerized utilizing **Docker Compose** and orchestrates high-performance relational storage, isolated **Redis caching grids**, stateless **JWT Security Barriers**, and asynchronous data pipelines powered by an **Apache Kafka** event streaming broker.

---

## 🚀 Advanced Architectural Evolution

* **Distributed Event-Driven Topologies:** Fully decoupled application architecture. Core actions inside the primary backend broadcast transactional data asynchronously across **Apache Kafka** event message streams.
* **Independent Microservices:** Features a completely detached, lightweight `notification-service` running in its own runtime wrapper container. It monitors the network broker to catch and process live library audit events without blocking main API responses.
* **High-Performance Caching Layer:** Features a standalone distributed **Redis cache instance** intercepting heavy read pipelines (such as filtered catalog queries and inventory lookups) to slash database lookup overhead. Built with safe Java type serialization structures and precise **Dynamic SpEL `@CacheEvict` routing algorithms** to prevent stale state leaks.
* **Stateless Security Gateway Infrastructure:** Secure API routing abstractions managed via a unified custom **Spring Security 6.x filter chain** enforcing stateless **JWT (JSON Web Token) authentication** loops, granular role validations (`USER`, `ADMIN`), and explicit CSRF disabling for optimized stateless token parsing.
* **Robust Enterprise Integration Test Suite:** Production-hardened automation coverage powered by **Testcontainers Framework**. Spins up real, isolated Docker instances of **MySQL 8.0** and **Apache Kafka** dynamically on randomized high-availability network ports during test execution execution tasks, replacing fragile mocks with true-to-life component behaviors.
* **Transactional Reliability Guardrails:** Core business services execute mutating actions within atomic `@Transactional` boundary frames, ensuring zero state corruption, while event broadcasting operations are decoupled at the controller boundary to avoid dirty data pollution.

---

## 🛠️ Multi-App Tech Stack

* **Core Framework:** Spring Boot 3.x / Java 21 (LTS)
* **Build Automation Engine:** Gradle (Groovy DSL)
* **Security Framework:** Spring Security 6.x & JWT Stateless Filter Processing
* **Event Streaming & Message Broker:** Apache Kafka & Apache Zookeeper (Confluent Infrastructure Infrastructure Core Images)
* **In-Memory Storage & Cache:** Redis 7.x (Lightweight Alpine Core Node)
* **Primary Relational Database:** MySQL 8.x (Backed by dedicated Docker Named Volumes)
* **Automated Component Verification:** Testcontainers Platform (Dynamic MySQL & Kafka Engines) & JUnit 5
* **Documentation Engine:** Swagger UI / OpenAPI 3

---

## 📂 System Architecture Layout

The ecosystem is split into two isolated code repositories running side-by-side on a shared virtual network bridge:

```text
📁 My-Backend-Workspace/
   ├── 📁 library-management-system-backend/   # Primary Core Engine (Monolith Platform Core)
   │     ├── 📄 docker-compose.yml              # Central Multi-Container Orchestrator
   │     ├── 📄 Dockerfile                      # Compilation image layout for Core App
   │     └── src/
   │           ├── main/java/com/irons/...
   │           │     ├── config/                # Redis Caching, JWT Filters & Kafka Topic Specs
   │           │     └── service/               # Event Producers & Core Database Workflows
   │           └── test/java/com/irons/...      # 🚀 HARDENED INTEGRATION TEST SUITE
   │                 └── integration/
   │                       ├── BaseIntegrationTest.java  # Testcontainers Orchestrator Core
   │                       ├── BooksControllerIT.java    # Full Slice Endpoint Testing
   │                       └── BookKafkaIntegrationIT.java # Real Pipeline Stream Interceptors
   │
   └── 📁 notification-service/                # Independent Secondary Microservice
         ├── 📄 Dockerfile                      # Compilation image layout for Notification App
         └── src/main/java/com/irons/...       # Dedicated Event Consumers & Listeners

```

---

## 🚦 Getting Started (Universal Docker Boot Sequence)

Thanks to Docker Compose, you do not need to manually configure schemas, local MySQL installations, or Redis servers on your machine.

### 1. Build Project Binaries

Open separate terminals for both directories and execute the Gradle compilation wrappers to bake your localized `.jar` artifacts:

```bash
# Inside library-management-system-backend/
./gradlew clean build -x test

# Inside notification-service/
./gradlew clean build -x test

```

### 2. Boot Up the Distributed Topology

Navigate to the root directory containing your primary project (`library-management-system-backend/`) where the global `docker-compose.yml` file sits, and spin up the multi-container grid:

```bash
docker-compose up --build

```

Docker will automatically pull down the official lightweight open-source images, construct an isolated virtual network bridge (`library-network`), link runtime variables, and boot all 5 major nodes side-by-side.

---

## 🏎️💨 Executing the Enterprise Integration Test Suite

The project features a full-slice test pipeline that runs completely isolated from local server states. It uses real Docker environments running right inside your test runtime cycle.

To execute the suite, make sure **Docker Desktop** is active, navigate to `library-management-system-backend/` and fire the specialized Gradle test target hooks:

```bash
# Run specific controller endpoints validation tests
./gradlew test --tests "com.irons.library_management_system_backend.integration.BooksControllerIT"

# Run event-driven message broker stream transmission verification tests
./gradlew test --tests "com.irons.library_management_system_backend.integration.BookKafkaIntegrationIT"

# Run complete system automated testing sweeps (Unit + Integration Profiles)
./gradlew test

```

### 🧠 Test Architecture Deep Dive

During test execution initialization, `BaseIntegrationTest` triggers dynamic configuration mechanics:

* **Dynamic Ports & DB Handshakes:** Automatically boots a private `mysql:8.0` and `cp-kafka:7.4.0` instance, intercepting runtime connection properties dynamically using `@DynamicPropertySource`.
* **Isolated Caching Neutralization:** Leverages a custom `@TestConfiguration` injecting a local memory-bound `ConcurrentMapCacheManager` combined with `spring.main.allow-bean-definition-overriding=true` properties to bypass remote Redis dependencies seamlessly during unit validations.
* **Security Interception Filters:** Configures test slices via `@AutoConfigureMockMvc(addFilters = false)` to permit raw controller handler execution checks, ensuring deep underlying database queries and serialization parameters are targeted with zero mocking pollution.

---

## 📖 API Boundaries & Real-Time Stream Verification

Once the system output prints a successful startup validation signature, you can verify your infrastructure components:

* **Interactive Swagger Gateway API:** `http://localhost:8080/swagger-ui/index.html`
* **Core API Endpoint Port:** `8080` (Direct Access Container Access Node)
* **Isolated Notification Microservice Port:** `8081` (Dedicated Consumer Container Container Node)

### 🧪 Witnessing the Event Stream in Real-Time

1. Open your terminal console running your active Docker container log views.
2. Direct your browser to the Swagger UI page and fire an authenticated execution request against the `/api/books/borrow` endpoint.
3. Observe your consolidated container log output streams. You will see the asynchronous data packet flow seamlessly cross process boundaries:

```text
library-backend-app  | INFO --- : 🚀 Broadcasting Event to Kafka Topic [library-transactions]: TRANSACTION_EVENT: User ID 1 successfully borrowed Book ID 5
library-notification-app | INFO --- : 🔔 [NOTIFICATION SERVICE] Processing Live Event Alert: TRANSACTION_EVENT: User ID 1 successfully borrowed Book ID 5
library-notification-app | INFO --- : 📩 Simulated Email/SMS notification sent successfully to the patron context.

```

---

## 📝 API Endpoint Blueprint Matrix

### 📚 Books Routing Abstractions (`/api/books`)

| Method | Endpoint | Description | Access Tier Restrictions | Cache Layer Impact / Event Streams |
| --- | --- | --- | --- | --- |
| **GET** | `/all` | Retrieves complete library catalog inventory | Authenticated (`USER`, `ADMIN`) | Reads `allBooksCache` |
| **GET** | `/available` | Filters and discovers books currently available to borrow | Authenticated (`USER`, `ADMIN`) | Reads `availableBooksCache` |
| **GET** | `/unavailable` | Filters and tracks active book loans | Authenticated (`USER`, `ADMIN`) | Reads `unavailableBooksCache` |
| **GET** | `/id/{id}` | Looks up specific book parameters by database primary key | Authenticated (`USER`, `ADMIN`) | Reads `bookDetailsCache` |
| **GET** | `/name/{name}` | Searches for a book by its matching textual string name | Authenticated (`USER`, `ADMIN`) | Reads `bookDetailsByNameCache` |
| **GET** | `/author/{author}` | Filters catalog list to fetch books by a specific writer | Authenticated (`USER`, `ADMIN`) | Reads `booksByAuthorCache` |
| **GET** | `/genre/{genre}` | Isolates catalog elements by their structural category tags | Authenticated (`USER`, `ADMIN`) | Reads `booksByGenreCache` |
| **POST** | `/add` | Appends a new book record payload into the system inventory | **ADMIN ONLY** | Evicts List Caches | Streams `BOOK_EVENT` |
| **DELETE** | `/{id}` | Purges a book element permanently from the application rows | **ADMIN ONLY** | Evicts All Book Caches | Streams `BOOK_EVENT` |
| **PATCH** | `/borrow` | Commits an atomic lease to issue a book to a library patron | Authenticated (`USER`, `ADMIN`) | Evicts State Caches | Streams `TRANSACTION_EVENT` |
| **PATCH** | `/return` | Releases an active book lease allocation back to open shelf | Authenticated (`USER`, `ADMIN`) | Evicts State Caches | Streams `TRANSACTION_EVENT` |

### 👥 Users Routing Abstractions (`/api/users`)

| Method | Endpoint | Description | Access Tier Restrictions | Cache Layer Impact / Event Streams |
| --- | --- | --- | --- | --- |
| **GET** | `/all` | Fetches profiling records of all registered system entities | **ADMIN ONLY** | Reads `allUsersCache` |
| **GET** | `/id/{id}` | Locates explicit member metrics matching database primary key | **ADMIN ONLY** | Reads `userDetailsByIdCache` |
| **GET** | `/name/{name}` | Inquires user profile records using alphanumeric string keys | **ADMIN ONLY** | Reads `userDetailsByNameCache` |
| **GET** | `/books/{id}` | Extracts a tracked array mapping all active leases held by user | **ADMIN ONLY** | Reads `booksBorrowedByUserCache` |
| **GET** | `/admins` | Filter view parsing users with full administrative access limits | **ADMIN ONLY** | Reads `usersByRoleCache` (true) |
| **GET** | `/members` | Filter view parsing core standard library patrons | **ADMIN ONLY** | Reads `usersByRoleCache` (false) |
| **POST** | `/add` | Registers a fresh user profile payload to database infrastructure | **Public Authorization Access** | Evicts List Caches | Streams `USER_EVENT` |
| **DELETE** | `/{id}` | Wipes a specific user entirely out of the application databases | **ADMIN ONLY** | Evicts All User Caches | Streams `USER_EVENT` |
| **PATCH** | `/{id}` | Escalates standard patron access rights up to Admin security tier | **ADMIN ONLY** | Evicts Target Key & List Caches | Streams `USER_EVENT` |
