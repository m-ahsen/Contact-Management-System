# Architecture Overview

## Style

This project is a **modular monolith**: one Spring Boot app and one React app in a single Git repository.

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

Under `com.ahsen.contactmanagement`:

- `config` — shared config (CORS)
- `security` — reserved (later phases)
- `auth` — reserved (later phases)
- `user` — reserved (later phases)
- `contact` — reserved (later phases)
- `common` — shared foundation (health)
- `exception` — reserved (later phases)

```text
Controller → Service → Repository → Database
```

## Frontend packaging

- `app/` — routing
- `pages/` — screens
- `features/` — feature modules (e.g. health)
- `shared/api/` — API client

## Phase 1

- Runnable backend and frontend
- MySQL connectivity
- `GET /api/v1/health`
- CORS for local Vite
- Frontend shows backend health

Business features are deferred.
