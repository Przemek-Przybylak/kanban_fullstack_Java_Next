# 🚀 Fullstack Kanban Board 2026

A professional, secure, and highly responsive Kanban application. Integrated with Java Spring Boot 3 and React/Next.js to provide a seamless workflow management experience.

---


https://github.com/user-attachments/assets/c9c2d7f4-dba2-4d15-8650-488b8500b848


## ⚡ Quick Access (Live Version)

**The fastest way to explore the project. No local setup required.**

* **Live Frontend:** https://kanban-fullstack-java-next.vercel.app/
* **API Documentation:** https://kanban-fullstack-java-next.onrender.com/swagger-ui/index.html
* **Test Credentials:** `admin` / `admin123`

---

## 🏗️ System Architecture

### 💻 Frontend (The Experience)

* **Core:** React 19, Next.js 15, TypeScript.
* **Drag & Drop:** Advanced gesture handling via `@dnd-kit`.
* **Mobile UX:** Snap-to-center horizontal scroll with `85vw` card hints.
* **Error Handling:** Global `StatusWrapper` for loaders and API error mapping.

### ⚙️ Backend (The Engine)

* **Core:** Java 21, Spring Boot 3.4.
* **Security:** Spring Security + JWT with strict Data Ownership (ACL).
* **Database:** PostgreSQL managed via Docker.
* **Optimization:** `PATCH` endpoints for efficient partial task updates.

---

## ✨ Key Features

### 🔑 Secure Authentication & Ownership
Full Register/Login flow. Every task and project is strictly linked to its creator using JWT claims, ensuring total data isolation between users.

### 📱 Intelligent Mobile Layout
The desktop board automatically transforms into a fluid horizontal "slider" on mobile devices. This prevents vertical clutter and improves ergonomics.

### 🔄 Real-time UX Synchronization
Optimistic UI updates ensure that moving a task feels instantaneous, while the backend processes the changes in the background.

### ⚠️ Advanced Error Handling
Unauthorized attempts or server errors are caught by the `StatusWrapper`, providing clear feedback like "Access Denied" instead of silent failures.

---

## 🛣️ API Reference (Endpoints)

| Method | Endpoint | Description | Access |
| :--- | :--- | :--- | :--- |
| **POST** | `/auth/login` | Receive JWT Token | Public |
| **GET** | `/projects` | List all user projects | Private |
| **POST** | `/projects/{id}/tasks` | Add task to project | Private |
| **PATCH** | `/tasks/{id}` | Update task (D&D / Edit) | Private |
| **DELETE** | `/tasks/{id}` | Remove task | Private |

---

## 🚀 Local Development (Docker)


# 1. Clone the repository
https://github.com/Przemek-Przybylak/kanban_fullstack_Java_Next/tree/main/kanban_fullstack_Java_Next

```bash

# 2. Start Backend & Database
cd kanban-backend && docker compose up --build

# 3. Start Frontend
cd ../kanban-frontend && npm install && npm run dev

🧪 Roadmap & Progress

[x] Core Backend: Spring Boot 3 + Java 21.

[x] JWT Security: Authentication and ACL Ownership.

[x] Advanced Mobile UX: Snap-scroll and 85vw layouts.

[x] Responsive UI Fix: Clean desktop view (no scrollbars).

[x] Global Error Handling: Integrated StatusWrapper.

[x] Optimistic Updates: PATCH synchronization.

[ ] Advanced Testing: 90%+ coverage (JUnit & Vitest).

[ ] CI/CD: Automated deployment.
```

---

## 📬 Contact
Project developed as a showcase of Fullstack Development capabilities. Feel free to reach out for collaboration!

Dev Tip: If the UI doesn't reflect latest CSS changes during development in IntelliJ, a manual IDE restart is recommended.
