# Skill: WireMock Stubs

## Purpose
Create mock configurations for isolating tests from external API dependencies.

> Integration with tests (lifecycle, setup, verification): see `mocks/CLAUDE.md`.

## Approaches

### Programmatic (Java)
Static methods in mock classes. Each method registers a stub with request matcher
and response definition. Best for: dynamic responses, complex matching, stateful
scenarios, test-specific configs.

### Declarative (JSON)
Mapping files in a `mappings/` directory, response bodies in `__files/`.
Naming: `{method}-{entity}-{status}.json`. Best for: static responses, simple
matching, shared stubs, easy review.

## Scenario Types Per Endpoint

| Scenario        | Response                    |
|-----------------|-----------------------------|
| Success         | 200/201 + valid body        |
| Bad request     | 400 + error body            |
| Unauthorized    | 401                         |
| Forbidden       | 403                         |
| Not found       | 404 + error body            |
| Server error    | 500 + error body            |
| Timeout         | 200 + `withFixedDelay()`    |

## Request Matching Strategies
- Exact URL match for specific resources
- Path-only match ignoring query parameters
- Regex for dynamic path segments
- Header matching for content negotiation
- Body content matching for conditional responses
- Query parameter matching for filtered endpoints

## Stateful Scenarios
Use WireMock scenario state machine for flows where the same endpoint returns
different responses based on prior interactions.

## XML Response Stubs
Content-Type `application/xml`. Body matches XML payload annotations.
Use Accept header matching to differentiate JSON/XML stubs for same endpoint.
