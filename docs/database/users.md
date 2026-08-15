# Users table

Phase 2 introduces the `users` application account table. Contact tables are deferred to later phases.

Local development creates this table from the JPA entity (`spring.jpa.hibernate.ddl-auto=update` in the `dev` profile). Tests use `create-drop` against H2. Production keeps `ddl-auto=none` and should apply the SQL below.

## Production DDL

```sql
CREATE TABLE users (
    id BIGINT NOT NULL AUTO_INCREMENT,
    email VARCHAR(255) NULL,
    phone VARCHAR(20) NULL,
    password VARCHAR(100) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_users_email (email),
    UNIQUE KEY uk_users_phone (phone)
);
```

## Notes

- `email` and `phone` are optional individually, but a user must register with at least one.
- Unique indexes allow multiple `NULL` values, so email-only and phone-only accounts can coexist.
- `password` stores a BCrypt hash, never plain text.
