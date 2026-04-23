# Skill: POJO Generation

## Purpose
Generate typed data classes from API schemas, examples, or descriptions.

> Naming conventions, annotation rules, and type mapping: see `payloads/CLAUDE.md`.

## Class Structure Order
1. Package declaration
2. Imports
3. Class-level annotations
4. Private fields with field-level annotations
5. No-arg constructor
6. Getters and setters
7. `toString()`, `equals()`, `hashCode()`
8. Builder inner class (request payloads only)

## Request vs Response Differences
- **Request**: Builder pattern, `@JsonInclude(NON_NULL)`, exclude null fields
- **Response**: No-arg constructor + setters, `@JsonIgnoreProperties(ignoreUnknown = true)`

## Key Principles
- Wrapper types (Integer, Boolean) not primitives — supports nullability
- Strings for date/time unless operations needed
- Nested objects → separate class in same sub-package
- Arrays → `List<T>`, never raw arrays
- Generic objects → dedicated class, never `Map`
