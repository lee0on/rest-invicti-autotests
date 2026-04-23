# Skill: Test Data Generation

## Purpose
Generate realistic, constraint-aware test data for all test categories.

## Primary Tool
EasyRandom with JavaFaker. Factory classes provide pre-configured generators
per entity. Each generator explicitly maps every field to an appropriate
Faker provider using field name predicates.

## Faker Provider Selection
Match field semantics to the most realistic provider:
- Name fields use name providers (firstName, lastName, fullName)
- Contact fields use internet/phone providers (email, cellPhone, url)
- Credential fields use credentials provider (username) or custom constrained generation (password)
- Address fields use address providers (streetAddress, city, zipCode, country)
- Financial fields use number providers with appropriate ranges (price, quantity)
- Text fields use lorem providers (sentence, paragraph)
- Date fields use date providers with direction (past, future) and range
- Boolean fields use bool provider
- Identifiers use UUID generation

For fields with specific format constraints (passwords with character
requirements, codes with fixed patterns), create private helper methods
with explicit constraint application.

## Data Categories

### Valid Data (Happy Path)
Generated via factory methods. All fields populated with realistic values
within all known constraints. This is the default for most tests.

### Controlled Data (Specific Scenario)
Use Builder to set exact values on fields that are critical to the test
scenario. All other fields can remain factory-generated or null depending
on the test purpose.

### Invalid Data (Negative Tests)
Build payloads with deliberately problematic values per field:
- Required fields: absent, null, empty string, whitespace only
- String fields: exceeding max length, below min length, wrong format
- Numeric fields: negative when positive expected, zero, maximum integer value,
  decimal when integer expected
- Date fields: invalid format, end before start, far past or future
- Enum fields: value outside allowed set, empty, wrong case

### Boundary Values
Test at the edges of each constraint:
- Strings: empty, single character, exactly at max length, one over max
- Integers: zero, one, negative one, minimum value, maximum value
- Dates: today, yesterday, far past, far future, epoch start
- Collections: empty list, single item, maximum allowed count
- Booleans: true, false, null if nullable

### Edge Cases
Special characters, unicode, emoji, RTL text, accented characters.
Names with apostrophes, hyphens, spaces. Injection strings for XSS
and SQL injection. Extremely long strings.

## Seed Management
Use fixed seeds for reproducible generation. Assign different seeds per
entity to avoid cross-entity field collisions. Document seed values
in factory class documentation.

## Anti-Patterns
- Leaving fields without explicit randomize rules (EasyRandom defaults are unrealistic)
- Using random strings for structured fields like emails or phone numbers
- Creating a new Faker instance per method call instead of sharing a static one
- Hardcoding dummy values like "test" or "abc123" instead of using factories