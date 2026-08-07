# Architecture Overview

## Style

This project is a **modular monolith**: one Spring Boot application and one React application in a single Git repository. It is not a microservices system.

## Runtime flow

```text
React.js (Vite)
    ↓ REST / JSON
Spring Boot controllers
    ↓
Application services
    ↓
Spring Data JPA repositories
    ↓
MySQL (contact_management)
```

## Repository layout

| Path | Role |
|------|------|
| `backend/` | Spring Boot API |
| `frontend/` | React UI |
| `docs/` | Architecture, database, and API notes |

## Backend packaging

Feature / domain packages under `com.ahsen.contactmanagement`:

- `config` — cross-cutting configuration (e.g. CORS)
- `security` — reserved for later authentication/authorization
- `auth` — reserved for login/registration
- `user` — reserved for user profile
- `contact` — reserved for contact features
- `common` — shared foundation (e.g. health)
- `exception` — reserved for global exception handling

Dependency direction inside features:

```text
Controller → Service → Repository → Database
```

Controllers must not contain business logic that belongs in services, and must not call repositories directly when a service layer is required.

## Frontend packaging

Feature-based structure under `frontend/src`:

- `app/` — application shell and routing
- `pages/` — route-level screens
- `features/` — feature modules (e.g. health)
- `shared/api/` — centralized HTTP client

## Phase 1 foundation

Phase 1 provides:

- Runnable backend and frontend
- MySQL connectivity
- Flyway baseline (no business schema yet)
- `GET /api/v1/health`
- CORS for local Vite development
- Frontend display of backend health

Business features (auth, contacts, profile, etc.) are intentionally deferred.
