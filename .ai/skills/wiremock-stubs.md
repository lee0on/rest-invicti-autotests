# Skill: WireMock Stubs

## Purpose
Create mock configurations for isolating tests from external API dependencies.
Supports programmatic and declarative approaches.

## Programmatic Stubs
Define stubs as static methods in mock classes. Each method registers a stub
mapping with a request matcher and a response definition. Method name describes
the scenario: stubActionSuccess, stubActionNotFound, stubActionTimeout.

## Declarative Stubs
JSON mapping files in the wiremock mappings directory. Each file defines one
request-to-response mapping. Response bodies stored as separate files in the
__files directory, referenced by file name from the mapping.

## Scenario Types to Cover Per Endpoint

### Success
Return expected status (200 or 201) with a valid response body matching
the response payload class structure. Include correct Content-Type header.

### Client Errors
Bad request (400) with error body describing validation failure.
Unauthorized (401) with no body or minimal error.
Forbidden (403) for insufficient permissions.
Not found (404) with error body.

### Server Errors
Internal server error (500) with generic error body.

### Timeout and Slow Response
Success status but with fixed delay exceeding client timeout threshold.
Log-normal random delay for simulating realistic latency variance.

### Stateful Scenarios
Use WireMock scenario state machine for flows where the same endpoint
returns different responses based on prior interactions (e.g., resource
exists on first call, returns 404 after deletion).

## Request Matching Strategies
- Exact URL match for specific resources
- Path-only match ignoring query parameters
- Regex path matching for dynamic segments
- Header matching for content negotiation
- Body content matching for conditional responses
- Query parameter matching for filtered endpoints

## XML Response Stubs
Set Content-Type to application/xml. Construct XML body matching the
XML payload class annotations. Use Accept header matching to differentiate
JSON and XML stubs for the same endpoint.

## When to Use Which Approach
Programmatic stubs are better for dynamic responses, complex matching,
stateful scenarios, and test-specific one-off configurations. Declarative
JSON stubs are better for static predictable responses, simple matching,
stubs shared across many tests, and easy version control review.

## Integration Notes
Stubs are registered in test setup phase (BeforeEach or Arrange).
Verification of expected calls is done in the assertion phase when needed.
Tests use the WireMock JUnit 5 extension for lifecycle management.