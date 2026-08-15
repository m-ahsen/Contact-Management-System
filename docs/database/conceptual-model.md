# Conceptual Data Model

Phase 2 creates the `users` table. Contact tables are still deferred.

## Entities

```text
User          ← implemented (users table)
  └── Contact
        ├── Email  (multiple, labeled)
        └── Phone  (multiple, labeled)
```

### User

Application account used for registration, login, and profile. Owns contacts in later phases.

See [users.md](users.md) for the table definition.

### Contact

Person/organization entry owned by a user. Not implemented yet.

### Email

One or more labeled email addresses per contact. Not implemented yet.

### Phone

One or more labeled phone numbers per contact. Not implemented yet.

## Notes

- Database name: `contact_management`.
- A user registers with email **or** phone (or both). Unique indexes allow `NULL` for the unused identifier.
- Passwords are stored as BCrypt hashes.
