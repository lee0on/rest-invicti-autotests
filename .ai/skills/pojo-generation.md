# Skill: POJO Generation

## Purpose
Generate typed data classes from API schemas, examples, or descriptions.
Supports JSON and XML serialization formats.

## Class Structure Order
Package declaration, imports, class-level annotations, private fields with
field-level annotations, no-arg constructor, getters and setters, toString,
equals and hashCode, Builder inner class (request payloads only).

## Annotation Rules
- JSON request payloads: class-level non-null inclusion, field-level explicit
  property name mapping
- JSON response payloads: class-level ignore-unknown-properties, field-level
  explicit property name mapping
- XML payloads: class-level root element with local name, field-level property
  with local name. Lists require element wrapper annotation. Attributes use
  the attribute flag. Text content uses text annotation.
- Dual format: apply both JSON and XML annotations on the same class and fields

## Type Mapping Principles
- Use wrapper types (Integer, Boolean, Long) instead of primitives to support nullability
- Strings for date/time fields unless date operations are explicitly needed
- Every nested object becomes a separate class in the same sub-package
- Arrays map to List of typed elements, never raw arrays
- Generic objects map to dedicated classes, never to Map

## Request vs Response Differences
- Request payloads include Builder pattern for fluent construction in tests
- Response payloads use no-arg constructor plus setters for deserialization
- Response payloads are more permissive with unknown fields
- Request payloads exclude null fields from serialization

## Naming
- Request: EntityNameRequestPayload
- Response: EntityNameResponsePayload
- Nested: ParentChildPayload or standalone ChildPayload if reused independently
- One class per file, one entity per class

## Mandatory Methods
Every payload class must have toString (for readable test output), equals and
hashCode (for assertion comparisons). Builder inner class with static factory
method for request payloads.

## Language Adaptation
- Java: Jackson annotations, inner Builder class
- Python: Pydantic BaseModel with Field descriptors
- TypeScript: interface for responses, class with Zod schema for requests