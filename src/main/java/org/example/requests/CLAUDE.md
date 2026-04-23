# Requests Package

## Purpose
HTTP request abstraction layer. Each class = one API domain.
This is the **only** place where REST Assured calls and URL definitions exist
(see root `CLAUDE.md` architecture rules).

## Rules
- All methods are **static**, return `io.restassured.response.Response`
- Methods accept payload objects or primitives — never raw JSON strings
- Common request setup goes into a shared `RequestSpecification`, not repeated per method

## Method Naming
| Action         | Method Name             |
|----------------|-------------------------|
| Get one        | `getBooking(int id)`    |
| Get all        | `getAllBookings()`      |
| Get filtered   | `getBookingsByFilter(String checkin, String checkout)` |
| Create         | `createBooking(BookingRequestPayload payload)` |
| Update         | `updateBooking(int id, BookingRequestPayload payload)` |
| Delete         | `deleteBooking(int id)` |

## Method Signature Pattern
```java
public static Response createBooking(BookingRequestPayload payload) {
    return given()
            .spec(requestSpec)
            .body(payload)
        .when()
            .post("/booking")
        .then()
            .extract().response();
}
```

## RequestSpecification
Define `private static final RequestSpecification requestSpec` at class level:
- Base URI from `System.getenv()` or config file
- Content type, accept header, auth (if applicable)

## Javadoc
Every public method must have Javadoc: purpose, HTTP method, path, params, return.

## When Adding a New Request
1. Check if a class for this API already exists — add method if yes
2. If new class needed — create with shared `RequestSpecification`
3. Ensure required payload classes exist in `payloads/`

## Anti-Patterns
- Assertions in request methods — belongs in `checks/`
- Returning deserialized objects — return raw `Response`
- Duplicated request specs across methods