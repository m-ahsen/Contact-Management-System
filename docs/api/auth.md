# Auth API

Authentication endpoints are public. Passwords are never returned.

## Register

```http
POST /api/v1/auth/register
```

**Authentication:** none

### Request

```json
{
  "email": "user@example.com",
  "phone": "+15551234567",
  "password": "Password1"
}
```

Provide **email or phone** (or both). Password must be 8-72 characters and include at least one letter and one digit. Phone must be 10-15 digits and may start with `+`.

### Success

**Status:** `201 Created`

```json
{
  "message": "User registered successfully"
}
```

### Errors

| Status | When |
|--------|------|
| `400 Bad Request` | Missing email and phone, invalid email/phone/password |
| `409 Conflict` | Email or phone already registered |

---

## Login

```http
POST /api/v1/auth/login
```

**Authentication:** none

### Request

Login with the same identifier used at registration:

```json
{
  "email": "user@example.com",
  "password": "Password1"
}
```

or

```json
{
  "phone": "+15551234567",
  "password": "Password1"
}
```

### Success

**Status:** `200 OK`

```json
{
  "token": "<jwt>",
  "tokenType": "Bearer",
  "user": {
    "id": 1,
    "email": "user@example.com",
    "phone": null
  }
}
```

Send the token as `Authorization: Bearer <jwt>` on protected requests.

### Errors

| Status | When |
|--------|------|
| `400 Bad Request` | Missing identifier or password, invalid format |
| `401 Unauthorized` | Invalid credentials (generic message) |
