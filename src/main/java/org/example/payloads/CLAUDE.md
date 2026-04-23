# Payloads Package

## Purpose
POJOs for serializing request bodies and deserializing response bodies.
Pure data layer — no logic, no HTTP, no assertions (see root `CLAUDE.md` architecture rules).

## Naming

| Type            | Pattern                    | Example                 |
|-----------------|----------------------------|-------------------------|
| Request payload | `{Entity}RequestPayload`   | `BookingRequestPayload` |
| Response payload| `{Entity}ResponsePayload`  | `BookingResponsePayload`|
| Nested object   | `{Parent}{Child}Payload`   | `BookingDatesPayload`   |
| Shared model    | `{Entity}Payload`          | `ErrorPayload`          |

## Required Methods
`toString()`, `equals()`, `hashCode()` on every payload class.

---

## Content Type Support

### JSON (default)
- `@JsonProperty("field_name")` on every field
- `@JsonInclude(NON_NULL)` at class level for request payloads
- `@JsonIgnoreProperties(ignoreUnknown = true)` at class level for response payloads

### XML
Use Jackson XML annotations (`com.fasterxml.jackson.dataformat.xml.annotation`):
- `@JacksonXmlRootElement(localName = "...")` at class level
- `@JacksonXmlProperty(localName = "...")` on every field
- `@JacksonXmlProperty(isAttribute = true)` for XML attributes
- `@JacksonXmlElementWrapper(localName = "...")` for collection wrappers
- `@JacksonXmlText` for text content

### Dual Format (JSON + XML)
Annotate with both JSON and XML annotations. Jackson uses JSON annotations
with `ObjectMapper` and XML annotations with `XmlMapper`.

---

## Request Payloads (`request/`)
- `@JsonInclude(NON_NULL)` — exclude null fields from serialization
- Builder pattern for fluent construction in tests
- Group by API domain: `request/booking/`, `request/auth/`
- Constructed via `EasyRandomFactory` (default) or Builder (when test needs controlled values)

## Response Payloads (`response/`)
- `@JsonIgnoreProperties(ignoreUnknown = true)` for JSON
- No Builder — uses no-arg constructor + setters for deserialization
- Group by API domain: `response/booking/`, `response/auth/`
- May have more fields than request payloads (id, createdAt, status)
- Wrapper objects (data + meta) get their own class

## Field Type Mapping

| API Type / Format | Java Type       | Notes                            |
|:------------------|:----------------|:---------------------------------|
| string            | String          |                                  |
| integer / int32   | Integer         | Wrapper, not primitive           |
| long / int64      | Long            |                                  |
| number / float    | Double          |                                  |
| boolean           | Boolean         | Wrapper, not primitive           |
| array             | List            | Never raw array                  |
| object            | Dedicated class | Always a separate POJO           |
| date / date-time  | String          | Or LocalDateTime if parsing needed |
| enum              | String or enum  | Enum if values are fixed/known   |
| XML attribute     | String / prim   | Mark with isAttribute = true     |

## Anti-Patterns
- `Map<String, Object>` instead of a typed class
- Hardcoded test values in payload classes — use factories
- `@JsonProperty` for XML-only APIs — use `@JacksonXmlProperty`
- Missing `@JacksonXmlElementWrapper` for lists in XML
