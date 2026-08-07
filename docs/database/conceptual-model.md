# Conceptual Data Model

Phase 1 documents the intended domain model only. **No business tables are created in this phase.** Schema changes will be introduced later via Flyway migrations.

## Entities (conceptual)

```text
User
  └── Contact
        ├── Email  (multiple, labeled)
        └── Phone  (multiple, labeled)
```

### User

Represents an application account. Owns contacts and later authentication credentials / profile data.

### Contact

A person or organization entry belonging to a user. Supports detail views, search/filter, and pagination in later phases.

### Email

One or more labeled email addresses for a contact (for example: personal, work).

### Phone

One or more labeled phone numbers for a contact (for example: mobile, home, work).

## Notes

- Relationships above are conceptual for design alignment.
- Implementation (JPA entities, repositories, DDL) belongs to later phases.
- Database name for the application: `contact_management`.
