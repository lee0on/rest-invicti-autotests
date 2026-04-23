# Skill: Gherkin Authoring

## Purpose
Write well-structured Gherkin feature files that describe API behavior
in business-readable language.

## Feature File Structure
Start with a feature-level tag for the API domain. Feature title is short
and descriptive. Optional narrative block in As-a / I-want / So-that format.
Background section for preconditions shared across all scenarios in the file.
Individual scenarios or scenario outlines with examples tables.

## Step Types

### Given — Preconditions
Describes the state before the action. Uses past tense or present state.
Covers: authentication status, existing data, system configuration,
external service availability.

### When — Action
Describes exactly one action. Uses present tense. There should be only one
When per scenario (additional actions use And under When). This is where
the API call happens conceptually.

### Then — Outcome
Describes the observable result. Uses present or future tense. Covers:
response status category, response content, side effects, error messages.
Multiple Then/And steps are fine for verifying different aspects.

## Parameterization

### Inline Parameters
Use quoted strings, integers, or word parameters directly in step text
for simple variation.

### Scenario Outline with Examples
Use when the same flow applies to multiple data sets. The examples table
drives multiple executions of the same step sequence with different values.

### DataTable
Use for complex structured input that represents an object with multiple
fields. Parsed in step definitions as a map or list.

## Tags
- smoke: critical happy path scenarios
- regression: full regression suite
- negative: error and validation scenarios
- boundary: edge cases and limits
- wip: work in progress, skip in CI
- API domain name: filter by API
- requiresAuth: triggers authentication hook

## Language Rules
Write in plain English with no technical jargon. Never mention status codes —
say "bad request error" instead. Never mention JSON, XML, or payload class
names — say "booking details" instead. Never mention URLs — say "I create
a booking" instead. Use domain vocabulary consistently. Scenario title
describes what is being tested, not how.

## Step Reusability
Prefer parameterized steps over duplicate fixed-text steps. One step
performs one precondition, one action, or one verification. Before creating
a new step, check existing step definitions for possible reuse.

## Anti-Patterns
- Technical language in steps (URLs, status codes, JSON paths)
- Compound When steps that perform multiple actions
- Scenario titles like "Test 1" or "Verify booking" without specifics
- Given steps that are actually assertions
- Steps that cannot be reused by any other scenario