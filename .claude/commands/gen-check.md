## Parameters
$ARGUMENTS = what to test. Accepts either:
- endpoint: "POST /booking happy path and negative scenarios"
- request class: "all methods in BookingApi"
- specific scenarios: "test that GET /booking/{id} returns 404 for non-existent id"

## Actions
1. Read `checks/CLAUDE.md` for conventions
2. Find the corresponding request class and its methods. If not found — abort with suggestion to run `/gen-request` first
3. Find the corresponding payload classes. If not found — abort with suggestion to run `/gen-payload` first
4. Apply Error Scenario Mining skill to identify test scenarios per endpoint
5. Generate test class or add methods to existing test class:
    - Each test follows Arrange → Act → Assert
    - Arrange: data via `EasyRandomFactory` or Builder for scenario-specific values
    - Act: call request class method
    - Assert: AssertJ fluent assertions following Assertion Patterns skill
    - Every test has `@Test`, `@DisplayName`, `@Tag`
6. Cover at minimum: happy path, one validation error (400), one auth error (401), one not found (404)
7. Verify the file compiles