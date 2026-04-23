# Mocks Package

## Purpose
WireMock stub configurations for isolating tests from external API dependencies.

> See `wiremock-stubs` skill for detailed stub patterns and scenario types.

## Rules
- Naming: `{ApiName}Mock`
- Static methods that register WireMock stubs
- Method names describe the scenario: `stubCreateBookingSuccess()`,
  `stubCreateBookingServerError()`, `stubGetBookingTimeout()`

## Integration with Tests
- `@WireMockTest` or `WireMockExtension` in JUnit 5
- Stub setup in `@BeforeEach` or Arrange phase
- Verify expected calls with `WireMock.verify()`

## When to Use Mocks
- External third-party APIs not under your control
- APIs with rate limits or costs (payment, SMS)
- Testing error handling and timeout resilience

## When NOT to Use Mocks
- The actual API under test
- A real sandbox/staging environment is available and stable

## Anti-Patterns
- Mock setup inside check classes — keep it here
- Mocking the API you are actually testing
- Covering only success scenarios — include errors and timeouts
