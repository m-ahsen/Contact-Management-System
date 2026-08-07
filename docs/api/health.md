# Health API

## Endpoint

```http
GET /api/v1/health
```

## Description

Foundation health check used to verify that the Spring Boot application is running and that the frontend can call the backend.

This endpoint does not require authentication (Phase 1 has no auth).

## Response

**Status:** `200 OK`

```json
{
  "status": "UP"
}
```

## Local verification

With the backend running on port 8080:

```bash
curl http://localhost:8080/api/v1/health
```

The React app (`/`, `/health`) calls the same endpoint through the shared API client and displays the result.
