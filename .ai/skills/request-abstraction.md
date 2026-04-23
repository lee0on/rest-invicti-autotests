# Skill: Request Abstraction

## Purpose
Build HTTP request methods that encapsulate endpoint details so that URL,
method, headers, and auth are defined in exactly one place.

## Class Design
One class per API domain. Class contains a shared request specification
configured once at class level with base URI from environment or config,
content type, accept header, and auth setup. All methods are static and
return the raw response object.

## Method Design per HTTP Verb

### GET by ID
Accepts resource identifier as parameter. Uses path parameter substitution.
Returns response.

### GET with filters
Accepts filter values as method parameters. Applies them as query parameters.
Returns response.

### GET all
No parameters beyond the shared specification. Returns response.

### POST
Accepts request payload object as parameter. Serializes it as request body.
Returns response.

### PUT
Accepts resource identifier and full payload object. Uses path parameter
for ID, payload as body. Returns response.

### PATCH
Accepts resource identifier and partial payload object. Same structure as PUT
but semantically a partial update. Returns response.

### DELETE
Accepts resource identifier. Uses path parameter. Returns response.

## Method Naming Convention
- Get one: getEntity
- Get all: getAllEntities
- Get filtered: getEntitiesbyFilter
- Create: createEntity
- Full update: updateEntity
- Partial update: partialUpdateEntity
- Delete: deleteEntity
- Custom actions: verbEntityDetail

## Key Principles
- Always return raw response — never deserialize inside request methods
- Never assert inside request methods
- Use path parameter substitution — never string concatenation for URLs
- One method equals one endpoint call
- Every public method has documentation describing the endpoint, HTTP method,
  path, parameters, and return value
- Base URL is never hardcoded — always read from environment or configuration

## Dependency Requirement
All payload classes referenced in method signatures must already exist.
If missing, report to the orchestrator rather than creating placeholder types.