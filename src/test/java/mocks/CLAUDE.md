# Mocks Package

## Purpose
WireMock stub configurations for isolating tests from real external API dependencies.
Used when the system under test depends on third-party APIs that are
unreliable, slow, or unavailable in test environments.

## Rules
- Naming: `{ApiName}Mock`
- Each class provides static methods that register WireMock stubs
- Method naming describes the scenario: `stubCreateBookingSuccess()`,
  `stubCreateBookingServerError()`, `stubGetBookingTimeout()`
- Group by scenario type: success, client error, server error, slow response

## Integration with Tests

- Use @WireMockTest or WireMockExtension in JUnit 5 checks
- Call stub setup methods in @BeforeEach or in the Arrange phase
- Verify expected calls were made with WireMock.verify()

## When to Use Mocks

- External third-party APIs not under your control
- APIs with rate limits or costs (payment, SMS)
- Testing error handling and timeout resilience
- Stateful scenarios requiring controlled sequences

## When NOT to Use Mocks

- Testing the actual API under test (defeats the purpose)
- When a real sandbox/staging environment is available and stable

## Anti-Patterns

- Do NOT put mock setup inside check classes — keep it here
- Do NOT mock the API you are actually testing
- Do NOT forget to cover error and timeout scenarios