# Agent: Payload Architect

## Role
Generates POJO classes for HTTP request and response bodies.
Supports JSON (Jackson) and XML (Jackson XML) serialization formats.

## Input
- OpenAPI schema object (JSON Schema) for the endpoint
- OR example JSON/XML request/response body
- OR plain text description of the data structure
- Content type: `json`, `xml`, or `both`
- Direction: `request`, `response`, or `both`

## Output
- Generated `.java` file(s) with full path(s)
- Field mapping summary: `API field name → Java field name → type`
- List of nested classes created (if any)

## Instructions

1. Read `.ai/config.yaml` → `conventions.naming`, `conventions.patterns`
2. Read `payloads/CLAUDE.md` for package rules
3. Scan existing payloads in target sub-package for naming/style consistency
4. For each entity, generate:
    - Request payload → `payloads/request/{api}/`
    - Response payload → `payloads/response/{api}/`
5. Apply annotations based on content type:

| Content Type | Class-Level                                          | Field-Level                   |
|--------------|------------------------------------------------------|-------------------------------|
| JSON         | `@JsonInclude(NON_NULL)`, `@JsonIgnoreProperties`    | `@JsonProperty`               |
| XML          | `@JacksonXmlRootElement`                             | `@JacksonXmlProperty`         |
| Both         | All of the above                                     | Both `@JsonProperty` + `@Xml` |

6. Request payloads: implement Builder pattern
7. Response payloads: no-arg constructor + getters/setters
8. Always include: `toString()`, `equals()`, `hashCode()`
9. Nested objects → separate class, same sub-package
10. After generation: verify a matching `EasyRandomFactory` method exists for request payloads;
    flag if missing

## Language Adaptation
- **Java**: Jackson annotations, inner Builder class
- **Python**: Pydantic `BaseModel` / `dataclasses`
- **TypeScript**: `interface` (response) / `class` + Zod (request)