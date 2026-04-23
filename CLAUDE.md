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
- Reusable methods used in 2+ classes MUST go into `utils/`
- Each utility class: `final`, private constructor, static methods only, single concern

### 5. Test Independence
- Every test must be independent and self-contained
- No shared mutable state between tests
- No reliance on test execution order

---

## Package Naming Quick Reference

| Package      | Class Pattern                        | Example                  |
|--------------|--------------------------------------|--------------------------|
| `payloads/`  | `{Entity}{Request\|Response}Payload` | `BookingRequestPayload`  |
| `requests/`  | `{ApiName}Api`                       | `BookingApi`             |
| `checks/`    | `{Feature}Test`                      | `BookingTest`            |
| `factories/` | `EasyRandomFactory`                  | `EasyRandomFactory`      |
| `utils/`     | `{Concern}Utils`                     | `DateUtils`              |
| `mocks/`     | `{ApiName}Mock`                      | `BookingMock`            |
| `steps/`     | `{Domain}Steps`                      | `BookingSteps`           |

> Full rules for each package: see the `CLAUDE.md` in that package's directory.

---

## Before Generating Code
1. Read `.ai/config.yaml` for conventions
2. Read the `CLAUDE.md` in the target package directory
3. Analyze existing files in the target package for consistency
4. Check if related payloads/requests already exist — avoid duplicates
5. Verify factory methods exist for the entity; create if missing

## Anti-Patterns (AVOID)
- `assertNotNull(response)` without specific field assertions
- `assertTrue(body.contains("..."))` — use deserialized field comparison
- HTTP calls directly in test methods (use `requests/` classes)
- Hardcoded URLs, ports, or environment-specific values
- Constructing payloads with dummy values — use factories