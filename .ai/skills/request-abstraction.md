# Skill: Request Abstraction

## Purpose
Build HTTP request methods encapsulating endpoint details in one place.

> Class design, naming, and method signature pattern: see `requests/CLAUDE.md`.

## Method Design per HTTP Verb

### GET by ID
Accepts resource identifier. Uses `pathParam()` substitution. Returns `Response`.

### GET with filters
Accepts filter values as parameters. Applies via `queryParam()`. Returns `Response`.

### GET all
No parameters beyond shared spec. Returns `Response`.

### POST
Accepts request payload object. Serializes as body. Returns `Response`.

### PUT
Accepts resource ID and full payload. Path param for ID, payload as body.

### PATCH
Accepts resource ID and partial payload. Same structure as PUT.

### DELETE
Accepts resource identifier. Uses path param. Returns `Response`.

## Key Principles
- Always return raw `Response` — never deserialize in request methods
- Never assert in request methods
- Use `pathParam()` — never string concatenation for URLs
- One method = one endpoint call
- Base URL from environment or config, never hardcoded
