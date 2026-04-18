# Utils Package

## Purpose
Shared utility classes containing reusable static helper methods
used across multiple parts of the framework.

## Rules
- Naming: `{Concern}Utils`
- Every class is `final` with a `private` constructor
- All methods are `static`
- **Single responsibility**: one utility class per concern
- Javadoc on every public method (purpose, params, return, example)
- Extract here only when a helper is used in **2 or more** classes

## What Belongs Here

- Date/time formatting and calculation
- Auth token generation and header construction
- Common response body extraction shortcuts
- Configuration/property file reading
- String manipulation helpers specific to the project
- Retry or wait utilities

## What Does NOT Belong Here

- HTTP request logic → goes in requests/
- Test data generation → goes in factories/
- Business logic or domain rules
- Anything used by only one class (keep it in that class)

## Anti-Patterns

- Do NOT create a generic "Helper" or "Common" god-class
- Do NOT put stateful logic here — utils are stateless
- Do NOT duplicate what the standard library already provides