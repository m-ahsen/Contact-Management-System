# Contact Management System

Modular-monolith Contact Management System (React + Spring Boot + MySQL).

Phase 2 adds authentication and user management on the Phase 1 foundation.

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
    ↓ REST API + JWT
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

The `dev` profile uses `spring.jpa.hibernate.ddl-auto=update`, so the `users` table is created from the JPA entity on startup. Production keeps `ddl-auto=none`; apply [docs/database/users.md](docs/database/users.md).

Production credentials must be supplied via environment variables (`DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USERNAME`, `DB_PASSWORD`) with no local defaults.

## Backend

Uses the **dev** profile by default for local runs (`./mvnw spring-boot:run`).
For production, set `SPRING_PROFILES_ACTIVE=prod` and provide all `DB_*` environment variables plus `JWT_SECRET`.

Optional JWT settings:

| Variable | Default (dev) | Notes |
|----------|---------------|--------|
| `JWT_SECRET` | local-only fallback in `dev` | **Required in production.** At least 32 bytes. |
| `JWT_EXPIRATION_MS` | `86400000` (24h) | Token lifetime |

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

Health check (public):

```text
GET http://localhost:8080/api/v1/health
```

Expected:

```json
{ "status": "UP" }
```

Auth:

```text
POST /api/v1/auth/register
POST /api/v1/auth/login
GET  /api/v1/users/me
PUT  /api/v1/users/password
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

Public routes: `/login`, `/register`, `/health`. Protected routes: `/dashboard`, `/profile`. `/` redirects based on authentication state.

Client routes rely on SPA history fallback (`vite` `appType: 'spa'`, plus `public/_redirects` / `vercel.json` for static hosts). Ensure your production static host rewrites unknown paths to `index.html`.

## Documentation

- [Architecture overview](docs/architecture/overview.md)
- [Conceptual data model](docs/database/conceptual-model.md)
- [Users table](docs/database/users.md)
- [Health API](docs/api/health.md)
- [Auth API](docs/api/auth.md)
- [Users API](docs/api/users.md)

## Phase 2 scope

Registration, login, JWT-protected APIs, profile, and password change. Contact management is deferred to a later phase.
