# Library Management System Ecosystem

A distributed, enterprise-grade event-driven microservices ecosystem engineered with **Spring Boot**, **Java 21**, and **Gradle**. This system is completely containerized utilizing **Docker Compose** and orchestrates high-performance relational storage, isolated **Redis caching grids**, and asynchronous data pipelines powered by an **Apache Kafka** event streaming broker.

---

## 🚀 Advanced Architectural Evolution

* **Distributed Event-Driven Topologies:** Fully decoupled application architecture. Core actions inside the primary backend broadcast transactional data asynchronously across **Apache Kafka** event message streams.
* **Independent Microservices:** Features a completely detached, lightweight `notification-service` running in its own runtime wrapper container. It monitors the network broker to catch and process live library audit events without blocking main API responses.
* **High-Performance Caching Layer:** Features a standalone distributed **Redis cache instance** intercepting heavy read pipelines (such as filtered queries and inventory lookups) to slash database lookup overhead. Built with safe Java type serialization structures and precise **Dynamic SpEL `@Caching` eviction algorithms** to prevent stale state leaks.
* **Containerized Infrastructure Abstraction:** Zero local machine installation dependencies. The entire network topology—including MySQL 8, Redis, Zookeeper, the Kafka Message Broker, and the Spring Boot application binaries—boots up uniformly using a single orchestration script.
* **Transactional Reliability Guardrails:** Core business services execute mutating actions within atomic `@Transactional` boundary frames, ensuring zero state corruption, while event broadcasting operations are decoupled at the controller boundary to avoid dirty data pollution.

---

## 🛠️ Multi-App Tech Stack

* **Core Framework:** Spring Boot 3.x / Java 21 (LTS)
* **Build Automation Engine:** Gradle (Groovy)
* **Event Streaming & Message Broker:** Apache Kafka & Apache Zookeeper (Confluent Infrastructure Images)
* **In-Memory Storage & Cache:** Redis 7.x (Lightweight Alpine Core)
* **Primary Relational Database:** MySQL 8.x (Backed by dedicated Docker Named Volumes)
* **Documentation Engine:** Swagger UI / OpenAPI 3

---

## 📂 System Architecture Layout

The ecosystem is split into two isolated code repositories running side-by-side on a shared virtual network bridge:

```text
📁 My-Backend-Workspace/
   ├── 📁 library-management-system-backend/   # Primary Core Engine (Monolith Platform Core)
   │     ├── 📄 docker-compose.yml              # Central Multi-Container Orchestrator
   │     ├── 📄 Dockerfile                      # Compilation image layout for Core App
   │     └── src/main/java/com/irons/...
   │           ├── config/                      # Redis Caching & Kafka Topic Definitions
   │           └── service/                     # Event Producers & Core Database Workflows
   │
   └── 📁 notification-service/                # Independent Secondary Microservice
         ├── 📄 Dockerfile                      # Compilation image layout for Notification App
         └── src/main/java/com/irons/...       # Dedicated Event Consumers & Listeners

```

---

## 🚦 Getting Started (Universal Docker Boot Sequence)

Thanks to Docker Compose, you do not need to manually configure schemas, local MySQL installations, or Redis servers.

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

## 📖 API Boundaries & Real-Time Stream Verification

Once the system output prints a successful startup validation signature, you can verify your infrastructure components:

* **Interactive Swagger Gateway API:** `http://localhost:8080/swagger-ui/index.html`
* **Core API Endpoint Port:** `8080` (Direct Access Container)
* **Isolated Notification Microservice Port:** `8081` (Dedicated Consumer Container)

### 🧪 Witnessing the Event Stream in Real-Time

1. Open your terminal console running your active Docker container log views.
2. Direct your browser to the Swagger UI page and fire an execution request against the `/api/books/borrow` endpoint.
3. Observe your consolidated container log output streams. You will see the asynchronous data packet flow seamlessly cross process boundaries:

```text
library-backend-app  | INFO --- : 🚀 Broadcasting Event to Kafka Topic [library-transactions]: TRANSACTION_EVENT: User ID 1 successfully borrowed Book ID 5
library-notification-app | INFO --- : 🔔 [NOTIFICATION SERVICE] Processing Live Event Alert: TRANSACTION_EVENT: User ID 1 successfully borrowed Book ID 5
library-notification-app | INFO --- : 📩 Simulated Email/SMS notification sent successfully to the patron context.

```

---

## 📝 API Endpoint Blueprint Matrix

### 📚 Books Routing Abstractions (`/api/books`)

| Method | Endpoint | Description | Cache Layer Impact / Event Streams |
| :--- | :--- | :--- | :--- |
| **GET** | `/all` | Retrieves complete library catalog inventory | Reads `allBooksCache` |
| **GET** | `/available` | Filters and discovers books currently available to borrow | Reads `availableBooksCache` |
| **GET** | `/unavailable` | Filters and tracks active book loans | Reads `unavailableBooksCache` |
| **GET** | `/id/{id}` | Looks up specific book parameters by database primary key | Reads `bookDetailsCache` |
| **GET** | `/name/{name}` | Searches for a book by its matching textual string name | Reads `bookDetailsByNameCache` |
| **GET** | `/author/{author}` | Filters catalog list to fetch books by a specific writer | Reads `booksByAuthorCache` |
| **GET** | `/genre/{genre}` | Isolates catalog elements by their structural category tags | Reads `booksByGenreCache` |
| **POST** | `/add` | Appends a new book record payload into the system inventory | Evicts List Caches \| Streams `BOOK_EVENT` |
| **DELETE** | `/{id}` | Purges a book element permanently from the application rows | Evicts All Book Caches \| Streams `BOOK_EVENT` |
| **PATCH** | `/borrow` | Commits an atomic lease to issue a book to a library patron | Evicts State Caches \| Streams `TRANSACTION_EVENT` |
| **PATCH** | `/return` | Releases an active book lease allocation back to open shelf | Evicts State Caches \| Streams `TRANSACTION_EVENT` |

### 👥 Users Routing Abstractions (`/api/users`)

| Method | Endpoint | Description | Cache Layer Impact / Event Streams |
| :--- | :--- | :--- | :--- |
| **GET** | `/all` | Fetches profiling records of all registered system entities | Reads `allUsersCache` |
| **GET** | `/id/{id}` | Locates explicit member metrics matching database primary key | Reads `userDetailsByIdCache` |
| **GET** | `/name/{name}` | Inquires user profile records using alphanumeric string keys | Reads `userDetailsByNameCache` |
| **GET** | `/books/{id}` | Extracts a tracked array mapping all active leases held by user | Reads `booksBorrowedByUserCache` |
| **GET** | `/admins` | Filter view parsing users with full administrative access limits | Reads `usersByRoleCache` (true) |
| **GET** | `/members` | Filter view parsing core standard library patrons | Reads `usersByRoleCache` (false) |
| **POST** | `/add` | Registers a fresh user profile payload to database infrastructure | Evicts List Caches \| Streams `USER_EVENT` |
| **DELETE** | `/{id}` | Wipes a specific user entirely out of the application databases | Evicts All User Caches \| Streams `USER_EVENT` |
| **PATCH** | `/{id}` | Escalates standard patron access rights up to Admin security tier | Evicts Target Key & List Caches \| Streams `USER_EVENT` |