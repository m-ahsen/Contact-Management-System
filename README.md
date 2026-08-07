# Contact Management System

Phase 1 foundation for a modular-monolith Contact Management System (React + Spring Boot + MySQL).

## Requirements

| Component | Version / notes |
|-----------|-----------------|
| Java | 21 (LTS) |
| Spring Boot | 4.1.0 |
| Build tool | Maven (wrapper included) |
| Database | MySQL 8.x (local install) |
| Frontend | React.js + Vite |
| Node.js | 20+ recommended |

## Architecture

```text
React.js (frontend/)
    ↓ REST API
Spring Boot (backend/)
    ↓
Spring Data JPA
    ↓
MySQL (contact_management)
```

Single Git repository with separate `frontend` and `backend` applications. Domain packages follow Controller → Service → Repository. No microservices, Docker Compose, or API gateway in this phase.

See [docs/architecture/overview.md](docs/architecture/overview.md) for details.

## Project structure

```text
contact-management-system/
├── backend/          Spring Boot API
├── frontend/         React (Vite) app
├── docs/
│   ├── architecture/
│   ├── database/
│   └── api/
├── .gitignore
└── README.md
```

## MySQL setup

1. Ensure MySQL is running on `localhost:3306`.
2. Create the database (schema migrations are managed by Flyway; no business tables in Phase 1):

```sql
CREATE DATABASE contact_management;
```

3. Set the database password as an environment variable (never commit secrets):

**Windows (PowerShell):**

```powershell
$env:DB_PASSWORD = "your-mysql-root-password"
```

**macOS / Linux:**

```bash
export DB_PASSWORD=your-mysql-root-password
```

Connection defaults (dev profile):

- Host: `localhost`
- Port: `3306`
- Database: `contact_management`
- Username: `root`
- Password: from `DB_PASSWORD`

## Backend

```powershell
cd backend
.\mvnw.cmd spring-boot:run
```

Default profile is `dev`. API base: `http://localhost:8080`.

Health check:

```text
GET http://localhost:8080/api/v1/health
```

Expected:

```json
{ "status": "UP" }
```

Run tests:

```powershell
cd backend
.\mvnw.cmd test
```

## Frontend

```powershell
cd frontend
copy .env.example .env
npm install
npm run dev
```

App: `http://localhost:5173`. The home page calls the backend health endpoint and displays the result.

Environment variable (see `.env.example`):

```text
VITE_API_BASE_URL=http://localhost:8080
```

## Documentation

- [Architecture overview](docs/architecture/overview.md)
- [Conceptual data model](docs/database/conceptual-model.md)
- [Health API](docs/api/health.md)

## Phase 1 scope

This phase establishes project structure, MySQL connectivity, Flyway baseline, CORS for local Vite, a health endpoint, and frontend ↔ backend communication only. Authentication, contact CRUD, and related business features belong to later phases.
