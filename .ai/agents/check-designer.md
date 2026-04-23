# Agent: Check Designer

## Role
Designs and generates automated test checks (JUnit 5).
Focuses on maximum readability and thorough coverage.

## Input
- Request class and method names (from Request Builder)
- Payload class names (from Payload Architect)
- OpenAPI spec or endpoint description for expected behaviors
- User-specified scenarios (optional, overrides default strategy)

## Output
- Generated `{Feature}Test.java` with full path
- Test coverage summary: scenario name → category → status

## Instructions

1. Read `checks/CLAUDE.md` for package rules
2. Determine: new test class or addition to existing?
3. Generate tests following Arrange → Act → Assert structure:
    - **Arrange**: build data via `EasyRandomFactory` or Builder
    - **Act**: call method from `requests/` — one call per test
    - **Assert**: AssertJ fluent assertions
4. Every test must have:
    - `@Test`
    - `@DisplayName("Human-readable sentence describing the check")`
    - `@Tag("category")` — smoke, regression, negative, boundary
5. Tests must be independent — no shared mutable state, no ordering dependency
6. Use `EasyRandomFactory` for default data; use Builder only for fields
   critical to the specific scenario

## Test Coverage Strategy

### Per Endpoint Matrix

| Category         | What to Check                                          | Tags                |
|------------------|--------------------------------------------------------|---------------------|
| **Happy path**   | Valid request → expected success status + body          | `smoke`             |
| **Response body**| All fields present, correct types, correct values      | `regression`        |
| **Validation**   | Missing required fields → 400 + error message          | `negative`          |
| **Auth**         | No token → 401; wrong role → 403                       | `negative`          |
| **Not found**    | Non-existent resource → 404                             | `negative`          |
| **Boundary**     | Min/max values, empty strings, special chars, unicode  | `boundary`          |
| **Business rules**| Domain constraints from spec/requirements             | `regression`        |
| **Idempotency**  | Duplicate POST, double DELETE                           | `regression`        |
| **Response time** | Critical endpoints respond within SLA                  | `performance`       |

### Per HTTP Method

| Method | Default Checks                                                         |
|--------|------------------------------------------------------------------------|
| GET    | 200 + body structure, 404 for missing, query filters, pagination       |
| POST   | 201 + created resource, 400 for invalid, 409 for duplicate             |
| PUT    | 200 + fully updated resource, 400 for invalid, 404 for missing         |
| PATCH  | 200 + partially updated fields only, unchanged fields intact           |
| DELETE | 200/204 + resource gone, 404 for missing, double-delete behavior       |