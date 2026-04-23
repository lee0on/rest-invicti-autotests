# Skill: OpenAPI Parsing

## Purpose
Extract structured information from OpenAPI 3.x / Swagger 2.0 specifications
to feed into other agents.

## What to Extract

### Per Endpoint
- Operation ID, HTTP method, path, summary, description
- Tags for API grouping
- Deprecation status
- Path parameters with name, type, required flag
- Query parameters with name, type, required flag, enum values if any
- Header parameters with name, type, required flag
- Request body: content type, schema reference or inline definition, required flag
- Responses: status code, description, content type, schema reference or inline

### Per Schema / Model
- Name, type (object, array, enum, primitive)
- Properties: name, type, format, required flag, constraints (min, max, pattern)
- Nested references — resolve recursively to build full object tree
- Discriminator for polymorphic models

### Authentication
- Security scheme type (HTTP, API key, OAuth2)
- Scheme details (bearer, basic)
- Parameter location (header, query, cookie)
- Whether applied globally or per endpoint

## Version Differences
- OpenAPI 3.x stores models in `components.schemas`, request bodies in `requestBody.content`,
  auth in `components.securitySchemes`, server URL in `servers[].url`
- Swagger 2.0 stores models in `definitions`, body in `parameters[in=body]`,
  auth in `securityDefinitions`, server URL as `host` + `basePath`
- Content types in 3.x are per-operation; in 2.0 they are global `consumes`/`produces`

## Output Format
Produce a structured text summary per endpoint listing: operation ID, method, path,
auth requirement, request body schema with field names/types/required flags,
and response schemas per status code. Mark each field's constraints where known.

## When Spec Is Unavailable
Construct equivalent information from example curl commands, sample HTTP
request/response pairs, HTML/PDF documentation, or verbal descriptions.
Flag confidence level: high (from spec), medium (from examples), low (from description).