# Architecture Overview

## Style

This project is a **modular monolith**: one Spring Boot app and one React app in a single Git repository.

## Runtime flow

```text
React.js (Vite)
    ↓ REST / JSON + Bearer JWT
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

- `config` — shared config (CORS, JPA auditing)
- `security` — JWT filter, `SecurityConfig`, current-user identity
- `auth` — registration and login
- `user` — profile and password change
- `contact` — reserved (later phases)
- `common` — shared foundation (health, API constants)
- `exception` — API error format and global handler

```text
Controller → Service → Repository → Database
```

Authenticated identity comes from the JWT / security context, not from a client-supplied user id.

## Frontend packaging

- `app/` — routing, protected/public route guards
- `pages/` — screens
- `features/` — feature modules (`auth`, `profile`, `health`)
- `shared/api/` — API client (attaches the Bearer token)

## Phase 1

- Runnable backend and frontend
- MySQL connectivity
- `GET /api/v1/health`
- CORS for local Vite

## Phase 2

- User registration (email or phone)
- Login with JWT
- Protected APIs and routes
- Profile and password change
