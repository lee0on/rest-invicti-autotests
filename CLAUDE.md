# API Test Automation Framework

## Architecture Overview
A modular framework for REST API test automation built on a strict three-layer separation principle:
Payloads → Requests → Checks

Each layer has a single responsibility and communicates only with its direct neighbors.

## Project Structure

```
    src/
    ├── main/java/org/example/
    │   ├── payloads/              # POJOs for request/response bodies
    │   │   ├── request/                # Request body models (grouped by API)
    │   │   └── response/               # Response body models (grouped by API)
    │   ├── requests/              # HTTP request abstractions (one class per API)
    │   ├── factories/             # Data generation factories (EasyRandom + Faker)
    │   ├── steps/                 # BDD Cucumber step definitions
    │   └── utils/                 # Shared utility classes
    ├── test/
    │   ├── java/
    │   │   └── mocks/             # mock server stubs for test isolation
    │   └── resources/
    │       └── features/          # Gherkin feature files (Cucumber BDD)
    │
    └── specs/                     # OpenAPI/Swagger specifications
```

## Tech Stack
- **Language:** Java 17
- **Build:** Maven
- **HTTP Client:** REST Assured
- **Test Runner:** JUnit 5
- **Assertions:** JUnit 5
- **Serialization:** Jackson
- **Mocking:** WireMock
- **BDD:** Cucumber 7 + Gherkin
- **Data Generation:** EasyRandom + JavaFaker

## Build & Run Commands
```bash
mvn clean test                                    # run all tests
mvn test -Dtest=BookingTest                       # run specific test class
mvn test -Dtest=BookingTest#testCreateBooking     # run specific test method
mvn test -Dcucumber.filter.tags="@smoke"          # run BDD smoke tests
mvn compile -q                                    # quick compilation check
mvn spotless:apply                                # format code
```

---

## Critical Architecture Rules

### 1. Layer Separation (NEVER violate)
- **checks/** must NEVER contain direct HTTP calls or REST Assured imports
- **checks/** must ONLY call methods from **requests/** classes
- **requests/** must NEVER contain assertions or test logic
- **requests/** must ALWAYS accept payload objects, NEVER raw JSON strings
- **payloads/** are pure data classes — no business logic, no HTTP logic

### 2. No Hardcoded URLs
- Base URLs live ONLY in request classes, read from configuration/environment
- Endpoint paths are defined ONLY in request class methods

### 3. Payload Construction
- Use **EasyRandom + Faker factories** from `factories/` for generating test data
- NEVER construct payloads with hardcoded dummy values like "test123" or "aaa"
- Each API domain has its own factory method that produces realistic randomized data
- Payload classes use Jackson annotations and Builder pattern

### 4. Utility Extraction
- Common reusable methods MUST be extracted into utility classes under `utils/`
- Examples: date formatting, auth token generation, response parsing helpers,
  common assertion helpers, configuration readers
- Each utility class should be focused on a single concern
- Utility classes are `final` with a `private` constructor (static methods only)

### 5. Test Independence
- Every test must be independent and self-contained
- No shared mutable state between tests
- No reliance on test execution order

---

## Package Conventions

### Payloads (`org.example.payloads`)
- Class naming: `{Entity}{Request|Response}Payload`
    - Examples: `BookingRequestPayload`, `UserResponsePayload`
- Jackson annotations required: `@JsonProperty`, `@JsonInclude(NON_NULL)`
- Use `@JsonIgnoreProperties(ignoreUnknown = true)` on response payloads
- Implement Builder pattern for request payloads
- Include `toString()`, `equals()`, `hashCode()`

### Requests (`org.example.requests`)
- Class naming: `{ApiName}Api`
    - Examples: `BookingApi`, `AuthApi`
- All methods are **static**
- Every method returns REST Assured `Response` object
- Methods accept payload objects or primitive parameters (ids, filters)

### Checks (`org.example.checks`)
- Class naming: `{Feature}Test`
    - Examples: `BookingTest`, `AuthTest`
- `@DisplayName` is **mandatory** on every test — must be human-readable
- `@Tag` for categorization: `"smoke"`, `"regression"`, `"negative"`
- Test structure: **Arrange** (build data) → **Act** (call request) → **Assert** (verify)

### Factories (`org.example.factories`)
- Class naming: `EasyRandomFactory` (or `{Domain}DataFactory` for complex cases)
- Classes are `final` with `private` constructor
- Use EasyRandom + JavaFaker for realistic data generation
- Each factory method returns a configured `EasyRandom` instance for a specific entity
- Field randomization is explicitly configured via `EasyRandomParameters`
  using `FieldPredicates.named()` and corresponding Faker providers

### Utils (`org.example.utils`)
- Class naming: `{Concern}Utils`
    - Examples: `DateUtils`, `AuthUtils`, `ResponseUtils`, `ConfigUtils`
- Classes are `final` with `private` constructor (non-instantiable)
- All methods are **static**
- Single responsibility: one utility class per concern
- Javadoc on every public method
- Extract here any helper logic used across 2+ classes in the framework

### Mocks (`org.example.mocks`)
- WireMock stubs for isolating tests from external API dependencies
- Cover scenarios: success, client error (4xx), server error (5xx), timeout

### BDD Features (`src/test/resources/features`)
- Given = precondition / data setup
- When = single action (one HTTP call)
- Then = verification of outcome
- Reuse existing step definitions wherever possible
- Tags: `@smoke`, `@regression`, `@{api-name}`

---

## Before Generating Code
1. Read `.ai/config.yaml` for current conventions and settings
2. Read the `CLAUDE.md` in the target directory for package-specific rules
3. Analyze existing files in the target package for consistency
4. Check if related payloads/requests already exist before creating new ones
5. Verify that factory methods exist for the relevant entity; create if missing

## Anti-Patterns (AVOID)
- `assertNotNull(response)` without further specific assertions
- `assertTrue(body.contains("..."))` — fragile string matching
- HTTP calls directly in test methods
- Hardcoded URLs, ports, or environment-specific values
- Magic numbers or strings without named constants
- Duplicated utility logic across multiple classes
- Constructing payloads with meaningless test data instead of using factories