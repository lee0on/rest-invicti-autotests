---
name: test-data-generation
description: Generate realistic, constraint-aware test data across all categories (valid, controlled, invalid, boundary, edge cases) using Faker provider selection and seed management. Use when building payloads or factory methods for tests.
---

# Skill: Test Data Generation

## Purpose
Generate realistic, constraint-aware test data for all test categories.

> Factory class structure and code patterns: see `factories/CLAUDE.md`.

## Faker Provider Selection
Match field semantics to the most realistic provider:
- Names: `name().firstName()`, `name().lastName()`
- Contact: `internet().emailAddress()`, `phoneNumber().cellPhone()`
- Credentials: `credentials().username()`, custom constrained password
- Address: `address().streetAddress()`, `address().city()`, `address().zipCode()`
- Financial: number providers with ranges (price, quantity)
- Text: `lorem().sentence()`, `lorem().paragraph()`
- Dates: date providers with direction (past/future) and range
- Booleans: `bool().bool()`
- Identifiers: UUID generation

For format-constrained fields (passwords, codes), create private helper methods.

## Data Categories

### Valid (Happy Path)
All fields populated with realistic values within constraints. Default for most tests.

### Controlled (Specific Scenario)
Builder sets exact values on scenario-critical fields. Others factory-generated or null.

### Invalid (Negative Tests)
Per field type:
- Required: absent, null, empty, whitespace
- String: exceeds max, below min, wrong format, special characters
- Numeric: negative, zero, max integer, decimal when integer expected
- Date: invalid format, end before start, far past/future
- Enum: outside allowed set, wrong case

### Boundary Values
- Strings: empty, single char, exactly at max, one over max
- Integers: 0, 1, -1, Integer.MIN_VALUE, Integer.MAX_VALUE
- Dates: today, yesterday, far past, far future, epoch
- Collections: empty, single item, max count

### Edge Cases
Special characters, unicode, emoji, RTL, accented characters.
Names with apostrophes/hyphens. Injection strings (XSS, SQL).

## Seed Management
Fixed seeds for reproducible generation. Different seed per entity.
