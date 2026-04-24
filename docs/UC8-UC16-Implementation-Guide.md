# Fundoo Notes UC8-UC16 Guide

This guide explains what was implemented from UC8 to UC16 with request examples.

## Base URL
- `http://localhost:8080`

## Common Header (Protected APIs)
- `Authorization: Bearer <JWT_TOKEN>`
- `Content-Type: application/json`

---

## UC8 - Note Creation (Safe User Mapping)

### Endpoint
- `POST /api/notes`

### Body
```json
{
  "title": "My First Note",
  "content": "Hello world"
}
```

### What happens
- Email is extracted from JWT `Principal`.
- Server resolves current user from DB.
- Note is saved with that user's `userId`.
- Client cannot create notes for another user.

---

## UC9 - Note Dashboard Fetch (IDOR-safe)

### Endpoint
- `GET /api/notes`

### What happens
- User identity is read from security context.
- Only notes where `userId == currentUserId` and not deleted are returned.
- No user id is accepted from request, preventing IDOR.

---

## UC10 - Toggle APIs (Pin, Archive, Trash)

### Endpoints
- `PUT /api/notes/{id}/pin`
- `PUT /api/notes/{id}/archive`
- `PUT /api/notes/{id}/trash`

### What happens
- Server fetches note using `noteId + currentUserId`.
- If note is not owned by user, operation fails.
- Boolean state is toggled safely.

---

## UC11 - AOP (Cross-cutting Concerns)

### Implemented
- `LoggingAspect` with around advice.
- Logs method entry, execution time, and failures for controller/service layers.

### Benefit
- Centralized logging and timing without duplicate code.

---

## UC12 - RabbitMQ Messaging

### Implemented
- Queue config.
- Producer publishes note-created event.
- Consumer listens and logs event.

### Trigger point
- On successful note creation in `NoteService`.

---

## UC13 - Redis Caching (JWT + OTP + TTL)

### Implemented
- JWT cached at login with 1-hour TTL.
- JWT filter validates signature + token cache presence.
- OTP cache APIs with TTL 5 minutes.

### OTP APIs
- `POST /api/cache/otp/send`
```json
{ "otp": "123456" }
```
- `POST /api/cache/otp/verify`
```json
{ "otp": "123456" }
```

---

## UC14 - Spring Cache for Notes

### Implemented
- `@Cacheable` on user notes fetch.
- `@CacheEvict` on create and toggle operations.

### Result
- Faster repeated dashboard fetch for same user.

---

## UC15 - JMS (ActiveMQ)

### Implemented
- `JmsMessagingService` producer + listener.
- API to send JMS message:

### Endpoint
- `POST /api/jms/send`
```json
{ "message": "hello from jms" }
```

---

## UC16 - Batch Excel Import

### Implemented
- Excel parser service using Apache POI.
- Protected API to import notes from Excel file.
- Reads first sheet, expects:
  - Column A: title
  - Column B: content

### Endpoint
- `POST /api/batch/notes/import` (multipart form-data)
- Form key: `file`

---

## Security fix for 403 on note POST

### Done
- Stateless security session enabled.
- CORS support added.
- `OPTIONS` preflight permitted.
- JWT filter now accepts both:
  - `Authorization: Bearer <token>`
  - `Authorization: <token>`

### Correct note create request
```http
POST /api/notes
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json

{
  "title": "My First Note",
  "content": "Hello world"
}
```

---

## Quick Test Flow
1. Register: `POST /api/users/register`
2. Login: `POST /api/users/login` -> copy token
3. Create note: `POST /api/notes`
4. Fetch notes: `GET /api/notes`
5. Toggle pin/archive/trash
6. Test OTP cache
7. Test JMS send
8. Test Excel import

