# Checks Package

## Purpose
Test classes — the top layer of the framework.
Verify API behavior by combining payloads, requests, and assertions.
Primary goal: **maximum readability**.

> Architecture constraints (no direct HTTP calls, no REST Assured imports): see root `CLAUDE.md`.

## Rules
- `@DisplayName` **mandatory** on every test — human-readable sentence
- `@Tag` for categorization: `"smoke"`, `"regression"`, `"negative"`, `"boundary"`
- No `@Disabled` without an explanatory comment

## Test Structure (Arrange → Act → Assert)
```java
@Test
@DisplayName("Successfully create a new booking with valid data")
@Tag("smoke")
void testCreateBooking() {
    // Arrange
    EasyRandom generator = EasyRandomFactory.bookingGenerator();
    BookingRequestPayload payload = generator.nextObject(BookingRequestPayload.class);

    // Act
    Response response = BookingApi.createBooking(payload);

    // Assert
    assertThat(response.getStatusCode()).isEqualTo(200);
    BookingResponsePayload booking = response.as(BookingResponsePayload.class);
    assertThat(booking.getFirstname()).isEqualTo(payload.getFirstname());
    assertThat(booking.getTotalprice()).isEqualTo(payload.getTotalprice());
}
```

## Test Coverage Strategy

| Category       | Examples                                         |
|----------------|--------------------------------------------------|
| Happy path     | Valid data → expected success response           |
| Validation     | Missing required fields → 400                    |
| Auth           | No token / invalid token → 401/403               |
| Not found      | Non-existent resource → 404                      |
| Boundary       | Min/max values, empty strings, special characters|
| Business rules | Domain-specific constraints                      |
| Idempotency    | Duplicate POST, double DELETE                    |

## Assertion Style
- Check exact values, not just non-null
- Collections: `extracting()`, `containsExactly()`, `hasSize()`
- Error responses: verify both status code AND error message body
- See `assertion-patterns` skill for full catalog

## Data Generation
- `EasyRandomFactory` for default data
- Builder only when the test requires controlled values