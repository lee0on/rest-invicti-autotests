# Requests Package

## Purpose
Abstraction layer for all HTTP requests. Each class represents one API
(a group of related endpoints). This is the **only** place where
REST Assured calls and URL definitions exist.

## Rules
- Naming: `{ApiName}Api`
- All methods are **static**
- Every method returns `io.restassured.response.Response`
- Methods accept payload objects or primitive parameters — NEVER raw JSON strings
- Base URL is read from configuration or environment variables — NEVER hardcoded
- Common request setup (headers, content-type, auth) goes into a shared
  `RequestSpecification`, not repeated in every method

## Method Naming
- Use descriptive verb-based names matching the endpoint action:
    - `getBooking(int id)`
    - `getAllBookings()`
    - `createBooking(BookingRequestPayload payload)`
    - `updateBooking(int id, BookingRequestPayload payload)`
    - `deleteBooking(int id)`
    - `getBookingsByFilter(String checkin, String checkout)`

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

Define a private static final RequestSpecification requestSpec at class level
Include: base URI, base path, content type, accept header, auth (if applicable)
Read base URL from environment: System.getenv("BOOKING_API_URL") or a properties/config file
Javadoc


## Every public method must have Javadoc:
```java
/**
 * Creates a new booking.
 * POST /booking
 *
 * @param payload booking details
 * @return Response containing created booking with id
 */
 ```

## When Adding a New Request

Determine if a class for this API already exists
If yes — add a new method to the existing class
If no — create a new {ApiName}Api class with shared RequestSpecification
Ensure required payload classes exist in payloads/
Add Javadoc describing the endpoint

## Anti-Patterns

- Do NOT put assertions here — that belongs in checks/
- Do NOT return deserialized objects — return raw Response (let checks decide how to handle it)
- Do NOT hardcode base URLs or ports
- Do NOT duplicate request specs across methods