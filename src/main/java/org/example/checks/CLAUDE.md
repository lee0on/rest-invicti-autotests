# Checks Package

## Purpose
Automated test checks — the top layer of the framework.
This is where we verify API behavior by combining payloads, requests, and assertions.
The primary goal is **maximum readability**: anyone should understand
what a check does just by reading it.

## Rules
- Naming: `{Feature}Test`
- JUnit 5 annotations: `@Test`, `@DisplayName`, `@Tag`
- `@DisplayName` is **mandatory** on every test — human-readable sentence
- `@Tag` for categorization: `"smoke"`, `"regression"`, `"negative"`, `"boundary"`

## Test Structure (Arrange → Act → Assert)
```java
@Test
@DisplayName("Successfully create a new booking with valid data")
@Tag("smoke")
void testCreateBooking() {
    // Arrange — prepare test data
    EasyRandom generator = EasyRandomFactory.bookingGenerator();
    BookingRequestPayload payload = generator.nextObject(BookingRequestPayload.class);

    // Act — send request via request class
    Response response = BookingApi.createBooking(payload);

    // Assert — verify outcome
    assertThat(response.getStatusCode()).isEqualTo(200);
    BookingResponsePayload booking = response.as(BookingResponsePayload.class);
    assertThat(booking.getFirstname()).isEqualTo(payload.getFirstname());
    assertThat(booking.getTotalprice()).isEqualTo(payload.getTotalprice());
}
```

## Test Coverage Strategy

### For each endpoint, cover these categories:

| Category       | Examples                                          |
|----------------|---------------------------------------------------|
| Happy path     | Valid data → expected success response            |
| Validation     | Missing required fields → 400                     |
| Auth           | No token / invalid token → 401/403                |
| Not found      | Non-existent resource → 404                       |
| Boundary       | Min/max values, empty strings, special characters |
| Business rules | Domain-specific constraints from requirements     |
| Idempotency    | Duplicate POST, double DELETE                     |


## Assertion Style

- Be specific: check exact values, not just non-null
- For collections: use extracting(), containsExactly(), hasSize()
- For error responses: verify both status code AND error message body

## Data Generation

- Use EasyRandomFactory for realistic randomized data
- Override specific fields via Builder only when the test requires controlled values
- NEVER use hardcoded dummy data like "test", "abc123", "John Doe"

## Anti-Patterns

- Do NOT import io.restassured — use request classes instead
- Do NOT construct URLs or make HTTP calls directly
- Do NOT use assertNotNull() as the only assertion
- Do NOT use assertTrue(body.contains(...)) — fragile
- Do NOT share mutable state between tests
- Do NOT depend on test execution order
- Do NOT use @Disabled without a comment explaining why