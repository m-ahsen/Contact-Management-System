# Health API

## Endpoint

```http
GET /api/v1/health
```

## Description

Simple health check to confirm the Spring Boot app is running and the frontend can call the backend.

Public. No authentication required.

## Response

**Status:** `200 OK`

```json
{
  "status": "UP"
}
```

## Local check

```bash
curl http://localhost:8080/api/v1/health
```

The React app (`/` and `/health`) calls this endpoint via the shared API client.
