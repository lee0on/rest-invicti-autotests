# Payloads Package

## Purpose
POJO classes used for serializing HTTP request bodies and deserializing HTTP response bodies.
This package is the **data layer** of the framework — no logic, no HTTP calls, no assertions.
Supports multiple content types: JSON, XML, and potentially others via Jackson modules.

## General Rules
- Every class is a pure data container — no business logic, no HTTP logic
- One class per file, one entity per class
- Nested objects get their own separate class
- Required methods: `toString()`, `equals()`, `hashCode()`

## Naming Convention
| Type              | Pattern                          | Example                      |
|-------------------|----------------------------------|------------------------------|
| Request payload   | `{Entity}RequestPayload`         | `BookingRequestPayload`      |
| Response payload  | `{Entity}ResponsePayload`        | `BookingResponsePayload`     |
| Nested object     | `{Parent}{Child}Payload`         | `BookingDatesPayload`        |
| Shared model      | `{Entity}Payload` (no direction) | `ErrorPayload`               |

---

## Content Type Support

### JSON (default)

**Required annotations:**
- `@JsonProperty("field_name")` on every field — explicit mapping
- `@JsonInclude(JsonInclude.Include.NON_NULL)` at class level for request payloads
- `@JsonIgnoreProperties(ignoreUnknown = true)` at class level for response payloads

### XML

##### When the API uses Content-Type: application/xml, use Jackson XML annotations from com.fasterxml.jackson.dataformat.xml.annotation package.

#### Required annotations:

```
@JacksonXmlRootElement(localName = "RootElementName") at class level
@JacksonXmlProperty(localName = "field_name") on every field
@JacksonXmlProperty(isAttribute = true) for XML attributes
@JacksonXmlElementWrapper(localName = "wrapper") for collection wrappers
@JacksonXmlText for text content of an element
```

#### Dual Format Support (JSON + XML)

When the same API supports both content types, you can annotate a single class with both JSON and XML annotations simultaneously:
Jackson will use JSON annotations when serializing with ObjectMapper and XML annotations when serializing with XmlMapper.

## Request Payloads (request/)

### Purpose

Models representing HTTP request bodies sent to APIs.

### Specific Rules

Annotate class with @JsonInclude(JsonInclude.Include.NON_NULL) so null fields are excluded from serialization
Implement Builder pattern for convenient test data construction
Group into sub-packages by API domain: request/booking/, request/auth/

### Construction

Request payloads are constructed via:

Factories (EasyRandomFactory) — default, for randomized realistic data
Builder in checks — only when a test needs specific controlled values

### When Creating a New Request Payload

Check the API spec or example request for field names, types, and content type
Choose annotation set: JSON, XML, or dual
Create class in the appropriate API sub-package
Add serialization annotations on every field
Implement Builder
Verify that a corresponding factory method exists in EasyRandomFactory; create one if missing

## Response Payloads (response/)

### Purpose

Models representing HTTP response bodies returned by APIs.

### Specific Rules

Annotate class with @JsonIgnoreProperties(ignoreUnknown = true) for JSON
For XML responses: @JacksonXmlRootElement + @JacksonXmlProperty
NO Builder needed — Jackson/XmlMapper uses no-arg constructor + setters
Group into sub-packages by API domain: response/booking/, response/auth/

### Design Notes

Response payloads often have MORE fields than request payloads (e.g., id, createdAt, status added by server)
If the API returns a wrapper (e.g., { "data": [...], "meta": {...} } or <Response><Data>...</Data><Meta>...</Meta></Response>), model the wrapper as a separate class

### When Creating a New Response Payload

Send a real request or check the API spec for response schema and content type
Create class in the appropriate API sub-package
Add deserialization annotations on every field (JSON and/or XML)
Add @JsonIgnoreProperties(ignoreUnknown = true) for JSON
Provide getters + setters + toString() + equals() + hashCode()

## Field Type Mapping

| API Type / Format | Java Type           | Notes                                |
|:------------------|:--------------------|:-------------------------------------|
| string            | String              |                                      |
| integer / int32   | Integer             | Wrapper, not primitive               |
| long / int64      | Long                |                                      |
| number / float    | Double              |                                      |
| boolean           | Boolean             | Wrapper, not primitive               |
| array             | List                | Never raw array                      |
| object            | Dedicated class     | Always a separate POJO               |
| date (string)     | String              | Unless date operations needed        |
| date-time         | String              | Or LocalDateTime if parsing needed   |
| enum              | String or Java enum | Enum if values are fixed/known       |
| nullable field    | Wrapper type        | Integer not int, Boolean not boolean |
| XML attribute     | String / primitive  | Mark with isAttribute = true         |

## Anti-Patterns

- Do NOT put HTTP logic in payload classes
- Do NOT put validation logic in payload classes
- Do NOT use Map<String, Object> instead of a typed class
- Do NOT hardcode test values in payload classes — use factories
- Do NOT mix XML and JSON naming in a confusing way — keep consistent naming strategy per content type
- Do NOT forget @JacksonXmlElementWrapper for lists in XML — without it, Jackson produces incorrect XML structure
- Do NOT use @JsonProperty for XML-only APIs — use @JacksonXmlProperty