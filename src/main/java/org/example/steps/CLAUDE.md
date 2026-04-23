# Steps Package (Cucumber Step Definitions)

## Purpose
Glue code connecting Gherkin scenarios to the framework's request and payload layers.
Steps are **thin orchestrators** — they delegate to other layers, not implement logic.

> Step writing rules and reusability: see `gherkin-authoring` skill and `src/test/resources/CLAUDE.md`.

## Rules
- Naming: `{Domain}Steps` — one class per domain/feature
- Step methods are instance methods (not static) — Cucumber manages lifecycle
- Shared state between steps via **TestContext** (dependency injection)
- Common cross-domain steps go in `CommonSteps.java`

## Cucumber Configuration
- Glue package: `org.example.steps`
- Features path: `src/test/resources/features`
- Runner: `@Suite` + `@ConfigurationParameter` (JUnit Platform)

## Relationship to Other Layers
```
.feature files     →  define WHAT to test (business language)
Steps (this pkg)   →  define HOW to connect (glue code)
requests/          →  define HOW to call APIs
payloads/          →  define WHAT data looks like
factories/         →  define HOW to generate test data
```

## Anti-Patterns
- HTTP calls directly in step methods — use `requests/` classes
- Complex assertion logic in steps — extract to utility or custom assertion
- Static fields for shared state — use TestContext via DI
- Duplicate step definitions across classes — causes Cucumber errors
- Catching exceptions in steps to hide failures
