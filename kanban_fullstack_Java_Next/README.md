# Kanban Board API 🚀

A professional, secure, and scalable Kanban board backend built with **Java 21** and **Spring Boot 3**. This project demonstrates modern backend practices, including JWT authentication, Docker containerization, and clean architecture.

---

## ✨ Features
````
✔ Full CRUD for Projects & Tasks** – Manage your workflow with ease.
✔ JWT Authentication** – Secure access with JSON Web Tokens.
✔ User-Task Association** – Automatic User ID extraction from tokens for data ownership.
✔ Advanced Validation** – Using Validation Groups (`OnCreate`, `OnUpdate`) for data integrity.
✔ Partial Updates** – Efficient `PATCH` endpoints for modifying specific task/project fields.
✔ Global Exception Handling** – Consistent API error responses.
✔ Interactive API Docs** – Fully documented with **Swagger UI (OpenAPI 3.0)**.
✔ Automated Test User** – Auto-initializes an `admin` user on startup for easy testing.
✔ Ownership Verification (ACL): Implementing security checks to ensure users can only access their own projects and tasks.
````
---

## 📦 Tech Stack

| Layer | Technology |
|-------|------------|
| **Language** | Java 21 |
| **Framework** | Spring Boot 3.4.x |
| **Security** | Spring Security + JWT |
| **Persistence** | Spring Data JPA |
| **Database** | PostgreSQL |
| **Documentation** | Swagger / OpenAPI |
| **Containerization** | Docker & Docker Compose |
| **Testing** | JUnit 5 & Mockito |

---

## 🔑 Getting Started (API Usage)

### Default Test User
The application automatically creates a test user on startup:
- **Username:** `admin`
- **Password:** `admin123`

### Authentication & Access
- **Public Access:** Unauthenticated users can only access `/auth/**` and `GET /projects`.
- **Protected Access:** All other endpoints (Tasks, Project modifications) require a valid JWT token.
- **Login:** `POST /auth/login` to receive your JWT Token.
- **Register:** `POST /auth/register` to create a new account.
- **Authorization:** Add the token to your headers: `Authorization: Bearer <your_token>`.

### Key Endpoints
| Resource | Method      | Endpoint               | Description                                               |
|----------|-------------|------------------------|-----------------------------------------------------------|
| **Tasks** | `GET`       | `/tasks`               | List all tasks                                            |
| | `GET`       | `/tasks/{id}`          | Get specific task by ID (requires ID in URL)              |
| | `PATCH/PUT` | `/tasks/{id}`          | Edit task (requires ID in URL)                            |
| | `DELETE`    | `/tasks/{id}`          | Remove task (requires ID in URL)                          |
| **Projects** | `GET`       | `/projects`            | List all projects (Public)                                |
| | `GET`       | `/projects/{id}`       | Get specific project by ID (requires ID in URL)           |
| | `GET`       | `/projects/{id}/tasks` | Get all tasks for a specific project (requires ID in URL) |
| | `Post`      | `/projects`            | Add a new Project                                         |
| | `POST`      | `/projects/{id}/tasks` | Add a new task to a specific project (requires ID in URL) |
| | `PATCH/PUT` | `/projects/{id}`       | Edit project (requires ID in URL)                         |
| | `DELETE`    | `/projects/{id}`       | Remove project (requires ID in URL)                       |

---
## 📁 Project Structure
````
src/ 
├── main/ 
│ ├── java/com/example/kanban/ 
│ │ ├── config # Security & Global configurations 
│ │ ├── controller # REST Endpoints 
│ │ ├── service # Business Logic & Interfaces 
│ │ ├── repository # JPA Data Access 
│ │ ├── model # JPA Entities 
│ │ ├── DTO # Request/Response objects (Records) 
│ │ ├── util # Helper classes (Mappers, Update tools) 
│ │ ├── user # User management & Authentication logic 
│ │ └── exception # Global Exception Handler 
│ └── resources/ 
│ └── application.properties 
└── test/ # Unit & Logic tests
````

---

## 🐳 Running with Docker

This is the recommended way to run the project. It handles both the App and the PostgreSQL database.

1. **Build and start:**
   ```bash
   docker compose up --build
Access the API: http://localhost:8080

API Documentation: http://localhost:8080/swagger-ui/index.html

## 🧪 Testing
Current tests focus on the service layer using JUnit 5 and Mockito. To run them:


./mvnw test
## 🗺️ Roadmap (Upcoming Features)
[NOW] React + Next.js Integration: Connecting a modern frontend (migrating from a legacy Express.js setup).

[NEXT] Advanced Testing: * Expanding Unit Tests to cover 90%+ of the codebase.

Implementing Integration Tests using Testcontainers for real PostgreSQL environment simulation.

[PLAN] Deployment: Automated CI/CD pipeline for cloud deployment.

## 📬 Contact
Project developed as part of a Backend Developer portfolio. Feel free to contact me for feedback or collaboration!edback, feel free to open an issue or contact me.