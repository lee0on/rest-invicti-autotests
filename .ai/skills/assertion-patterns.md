# Skill: Assertion Patterns

## Purpose
Reference catalog of assertion strategies for API test checks.
All assertions use fluent style (AssertJ or equivalent).

## Status Code
Assert exact status code match. For range checks, assert the code falls
within the expected range (e.g., 2xx for success).

## Single Object Response
Deserialize response body into the corresponding response payload class.
Assert individual fields by exact value, range, or non-null with further
property checks. Prefer specific field assertions over whole-object comparison.

## Full Object Comparison
When comparing entire objects, use recursive comparison and ignore
server-generated fields like ID and timestamps.

## Collection Response
Assert collection size (exact, minimum, or non-empty). Extract specific
fields from collection elements and assert they contain expected values.
Use filtering to verify subsets match conditions.

## Nested Fields via Path Expressions
When full deserialization is unnecessary, extract values using JSON path
expressions and assert them directly.

## Error Response
Assert both status code and error body. Deserialize error response and
verify the error message contains the relevant field name and constraint
description. For multiple validation errors, assert the error list contains
entries for all invalid fields.

## Response Headers
Assert header presence, exact value, or partial content match for
Content-Type, Location, correlation IDs, and custom headers.

## Response Time
Assert response time in milliseconds is below the expected SLA threshold.

## Null and Absence
Assert a field is null when expected. When a field must not be null,
assert non-null and then continue with specific property assertions.

## Soft Assertions
When a test needs to verify multiple independent properties without
short-circuiting on the first failure, group assertions into a soft
assertion block so all failures are reported together.

## Anti-Patterns to Avoid
- Asserting only non-null without checking specific values
- Using string contains on raw body instead of deserialized field comparison
- Comparing boolean fields with assertEquals against true/false instead of
  using isTrue/isFalse
- Checking collection size by extracting size as integer instead of using
  the collection size assertion directly
- Omitting descriptive context on complex assertions