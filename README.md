# Contact Management System

Phase 1 foundation for a modular-monolith Contact Management System (React + Spring Boot + MySQL).

## Requirements

| Component | Notes |
|-----------|--------|
| Java | 21 |
| Spring Boot | 4.1.0 (Maven) |
| Database | MySQL (local) |
| Frontend | React.js + Vite |
| Node.js | 20.19+ recommended |

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

One Git repository with separate `frontend` and `backend` apps. Backend packages use Controller → Service → Repository. No microservices.

See [docs/architecture/overview.md](docs/architecture/overview.md).

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

1. Start MySQL on `localhost:3306`.
2. Create the database:

```sql
CREATE DATABASE contact_management;
```

3. Connection used by the backend (dev profile):

- Host: `localhost`
- Port: `3306`
- Database: `contact_management`
- Username: `root`
- Password: `password` (override with `DB_PASSWORD` if needed)

## Backend

```powershell
cd backend
.\mvnw.cmd spring-boot:run
```

API: `http://localhost:8080`

Health check:

```text
GET http://localhost:8080/api/v1/health
```

Expected:

```json
{ "status": "UP" }
```

Tests:

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

App: `http://localhost:5173`

The home page calls `GET /api/v1/health` and shows the result.

## Documentation

- [Architecture overview](docs/architecture/overview.md)
- [Conceptual data model](docs/database/conceptual-model.md)
- [Health API](docs/api/health.md)

## Phase 1 scope

This phase only sets up structure, MySQL connection, CORS, health endpoint, and frontend ↔ backend communication. Auth, contacts, and other business features come in later phases.
