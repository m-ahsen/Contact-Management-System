# Conceptual Data Model

Phase 1 documents the intended domain model only. **No business tables are created yet.**

## Entities (conceptual)

```text
User
  └── Contact
        ├── Email  (multiple, labeled)
        └── Phone  (multiple, labeled)
```

### User

Application account. Owns contacts; auth/profile come later.

### Contact

Person/organization entry owned by a user.

### Email

One or more labeled email addresses per contact.

### Phone

One or more labeled phone numbers per contact.

## Notes

- This is design only for Phase 1.
- Database name: `contact_management`.
