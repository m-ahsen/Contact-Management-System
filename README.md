# Contact Management System

Phase 1 foundation for a modular-monolith Contact Management System (React + Spring Boot + MySQL).

## Requirements

| Component | Notes |
|-----------|--------|
| Java | 21 |
| Spring Boot | 4.1.0 (Maven) |
| Database | MySQL (local) |
| Frontend | React.js + Vite |
| Node.js | 20.19+ recommended (or >=22.12.0) |

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
2. Create the database and a non-root local development user:

```sql
CREATE DATABASE contact_management;
CREATE USER 'cms_dev'@'localhost' IDENTIFIED BY 'cms_dev_local';
GRANT ALL PRIVILEGES ON contact_management.* TO 'cms_dev'@'localhost';
FLUSH PRIVILEGES;
```

3. Connection used by the backend (dev profile):

- Host: `localhost`
- Port: `3306`
- Database: `contact_management`
- Username: `cms_dev` (override with `DB_USERNAME` if needed)
- Password: `cms_dev_local` (override with `DB_PASSWORD` if needed)

Production credentials must be supplied via environment variables (`DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USERNAME`, `DB_PASSWORD`) with no local defaults.

## Backend

Uses the **dev** profile by default for local runs (`.\mvnw.cmd spring-boot:run`).
For production, set `SPRING_PROFILES_ACTIVE=prod` and provide all `DB_*` environment variables.

**Windows (PowerShell):**

```powershell
cd backend
.\mvnw.cmd spring-boot:run
```

**macOS / Linux:**

```bash
cd backend
./mvnw spring-boot:run
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

**Windows (PowerShell):**

```powershell
cd backend
.\mvnw.cmd test
```

**macOS / Linux:**

```bash
cd backend
./mvnw test
```

## Frontend

**Windows (PowerShell):**

```powershell
cd frontend
copy .env.example .env
npm install
npm run dev
```

**macOS / Linux:**

```bash
cd frontend
cp .env.example .env
npm install
npm run dev
```

App: `http://localhost:5173`

The home page calls `GET /api/v1/health` and shows the result.

Client routes such as `/health` rely on SPA history fallback (`vite` `appType: 'spa'`, plus `public/_redirects` / `vercel.json` for static hosts). Ensure your production static host rewrites unknown paths to `index.html`.

## Documentation

- [Architecture overview](docs/architecture/overview.md)
- [Conceptual data model](docs/database/conceptual-model.md)
- [Health API](docs/api/health.md)

## Phase 1 scope

This phase only sets up structure, MySQL connection, CORS, health endpoint, and frontend ↔ backend communication. Auth, contacts, and other business features come in later phases.
