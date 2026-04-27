## Parameters
$ARGUMENTS = feature description. Accepts either:
- user story: "as a user I want to create a booking so that I can reserve a room"
- endpoint list: "CRUD operations for /booking"
- plain description: "booking cancellation scenarios including deadline validation"

## Actions
1. Read `features/CLAUDE.md` for Gherkin conventions
2. Read `steps/CLAUDE.md` for step definition conventions
3. Use Project Analyzer MCP (or scan `steps/` directory) to collect all existing step definitions
4. Generate `.feature` file in `features/`:
    - Feature title, Background if needed
    - Scenarios with Given/When/Then in business language
    - Scenario Outlines with Examples for data-driven cases
    - Tags: `@smoke`, `@negative`, `@regression`, `@{api-name}`
5. Generate or update `{Domain}Steps.java` in `steps/`:
    - Reuse existing step definitions wherever a matching pattern exists
    - New steps delegate to request classes and use payload factories
    - Shared state via `TestContext`
6. List reused vs newly created steps in the output summary
7. Verify step definitions compile