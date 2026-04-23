# Utils Package

## Purpose
Shared static helper methods used across 2+ classes in the framework.

> Class conventions (final, private constructor, static methods, Javadoc): see root `CLAUDE.md`.

## What Belongs Here
- Date/time formatting and calculation
- Auth token generation and header construction
- Response body extraction shortcuts
- Configuration/property file reading
- Project-specific string manipulation
- Retry or wait utilities

## What Does NOT Belong Here
- HTTP request logic → `requests/`
- Test data generation → `factories/`
- Business logic or domain rules
- Anything used by only one class (keep it local)

## Anti-Patterns
- Generic "Helper" or "Common" god-class
- Stateful logic — utils are stateless
- Duplicating what the standard library provides
