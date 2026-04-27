## Parameters
$ARGUMENTS = endpoint description or spec reference. Accepts either:
- "POST /booking with BookingRequestPayload body"
- "all endpoints from specs/booking-api.yaml"
- "GET /users/{id}, DELETE /users/{id}"

## Actions
1. Read `requests/CLAUDE.md` for conventions
2. Parse the input — extract HTTP method(s), path(s), parameters, payload references
3. If spec file referenced — use OpenAPI Parsing skill to extract all endpoints
4. Determine API name from the path. Check if `{ApiName}Api.java` already exists
    - If exists — add new methods without modifying existing ones
    - If not — create new class with shared `RequestSpecification`
5. For each endpoint generate a static method:
    - Descriptive name following naming convention
    - Accept payload objects or primitives as parameters
    - Return `Response`
    - Include Javadoc with HTTP method, path, parameter descriptions
6. Verify that all referenced payload classes exist. If any are missing — list them and ask whether to generate
7. Verify the file compiles