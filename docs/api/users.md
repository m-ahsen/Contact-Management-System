# Users API

These endpoints require a valid JWT in the `Authorization` header.

```http
Authorization: Bearer <jwt>
```

The authenticated user is taken from the token. Clients must not send a user id to identify themselves.

## Current profile

```http
GET /api/v1/users/me
```

**Authentication:** required

### Success

**Status:** `200 OK`

```json
{
  "id": 1,
  "email": "user@example.com",
  "phone": "+15551234567"
}
```

Password is never included.

### Errors

| Status | When |
|--------|------|
| `401 Unauthorized` | Missing, invalid, or expired token |
| `404 Not Found` | Authenticated user no longer exists |

---

## Change password

```http
PUT /api/v1/users/password
```

**Authentication:** required

### Request

```json
{
  "currentPassword": "Password1",
  "newPassword": "NewPass12"
}
```

`newPassword` must satisfy the same rules as registration (8-72 characters, at least one letter and one digit).

### Success

**Status:** `200 OK`

```json
{
  "message": "Password changed successfully"
}
```

### Errors

| Status | When |
|--------|------|
| `400 Bad Request` | Invalid new password |
| `401 Unauthorized` | Missing/invalid token or incorrect current password |
