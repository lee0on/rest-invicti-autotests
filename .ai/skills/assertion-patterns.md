# Skill: Assertion Patterns

## Purpose
Reference catalog of assertion strategies for API test checks.
All assertions use fluent style (AssertJ or equivalent).

## Status Code
Assert exact status code match. For range checks, assert within expected range.

## Single Object Response
Deserialize into response payload class. Assert individual fields by exact value,
range, or non-null with further property checks. Prefer field-level over whole-object.

## Full Object Comparison
Use recursive comparison ignoring server-generated fields (id, timestamps).

## Collection Response
Assert size (exact, minimum, non-empty). Extract fields and assert expected values.
Use filtering for subset verification.

## Nested Fields via Path Expressions
When full deserialization is unnecessary, extract via JSON path and assert directly.

## Error Response
Assert both status code and error body. Verify error message contains relevant
field name and constraint. For multiple validation errors, assert entries for all fields.

## Response Headers
Assert presence, exact value, or partial match for Content-Type, Location,
correlation IDs, custom headers.

## Response Time
Assert milliseconds below SLA threshold.

## Soft Assertions
Group independent assertions in a soft assertion block so all failures are
reported together without short-circuiting.
