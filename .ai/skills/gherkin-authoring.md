# Skill: Gherkin Authoring

## Purpose
Write well-structured Gherkin feature files in business-readable language.

> Feature file conventions (naming, tags, examples): see `src/test/resources/CLAUDE.md`.

## Step Types

### Given — Preconditions
State before the action. Past tense or present state.
Covers: auth status, existing data, system config, external service availability.

### When — Action
Exactly one action per scenario. Present tense.
This is where the API call happens conceptually.

### Then — Outcome
Observable result. Present or future tense.
Covers: response status category, content, side effects, error messages.
Multiple Then/And steps fine for different aspects.

## Parameterization

### Inline Parameters
Quoted strings, integers, or `{word}` for simple variation.

### Scenario Outline + Examples
Same flow with multiple data sets. Examples table drives execution.

### DataTable
Complex structured input as map or list. Parsed in step definitions.

## Tags
- `@smoke` — critical happy paths
- `@regression` — full suite
- `@negative` — error/validation
- `@boundary` — edge cases
- `@wip` — skip in CI
- `@{api-name}` — filter by API

## Language Rules
- Plain English, no jargon
- No status codes — say "bad request error"
- No JSON/XML/class names — say "booking details"
- No URLs — say "I create a booking"
- Scenario title describes what is tested, not how

## Step Reusability
- Parameterized steps over duplicate fixed-text
- One step = one precondition, action, or verification
- Check existing definitions before creating new ones
