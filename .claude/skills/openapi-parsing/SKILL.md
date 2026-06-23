---
name: openapi-parsing
description: Extract structured information (endpoints, schemas, authentication) from OpenAPI 3.x and Swagger 2.0 specifications, accounting for version differences. Use when analyzing an API spec to drive payload, request, or test generation.
---

# Skill: OpenAPI Parsing

## Purpose
Extract structured information from OpenAPI 3.x / Swagger 2.0 specifications.

## What to Extract

### Per Endpoint
- Operation ID, HTTP method, path, summary, tags, deprecation status
- Path/query/header parameters: name, type, required, enum values
- Request body: content type, schema (reference or inline), required flag
- Responses: status code, description, content type, schema

### Per Schema
- Name, type, properties with name/type/format/required/constraints
- Nested references — resolve recursively
- Discriminator for polymorphic models

### Authentication
- Scheme type (HTTP, API key, OAuth2), details (bearer, basic)
- Parameter location (header, query, cookie)
- Global vs per-endpoint scope

## Version Differences
| Aspect        | OpenAPI 3.x                  | Swagger 2.0               |
|---------------|------------------------------|----------------------------|
| Models        | `components.schemas`         | `definitions`              |
| Request body  | `requestBody.content`        | `parameters[in=body]`      |
| Auth          | `components.securitySchemes` | `securityDefinitions`      |
| Server URL    | `servers[].url`              | `host` + `basePath`        |
| Content types | Per-operation                | Global `consumes`/`produces`|

## Output Format
Structured text per endpoint: operation ID, method, path, auth, request body
schema (fields/types/required), response schemas per status code, constraints.

## When Spec Is Unavailable
Construct from curl examples, HTTP samples, or documentation.
Flag confidence: high (spec), medium (examples), low (description).
