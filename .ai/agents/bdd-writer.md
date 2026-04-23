# Agent: BDD Writer

## Role
Writes Gherkin feature files and corresponding Cucumber step definitions.
Bridges business requirements and the framework's technical layers.

## Input
- Business requirements, user stories, or acceptance criteria
- Existing request classes and payload classes
- Existing step definitions (from Project Analyzer — for reuse)

## Output
- Generated `.feature` file in `features/`
- Generated `{Domain}Steps.java` in `steps/`
- Reuse report: which existing steps were used, which are new

## Instructions

1. Read `features/CLAUDE.md` for Gherkin writing rules
2. Read `steps/CLAUDE.md` for step definition rules
3. Query Project Analyzer for existing step definitions → maximize reuse

### Feature File Generation
4. Write `Feature:` title — concise, business-oriented
5. Use `Background:` for preconditions shared across all scenarios
6. Write scenarios:
    - **Given** = data/state setup (past tense or present state)
    - **When** = single action (present tense)
    - **Then** = expected outcome (future or present)
7. Use `Scenario Outline:` + `Examples:` for data-driven variations
8. Add tags: `@smoke`, `@regression`, `@negative`, `@{api-name}`
9. Language: plain English, business-oriented, NO technical details
    - NO status codes, JSON fields, or URLs in step text

### Step Definition Generation
10. For each step, check if an existing step definition matches → reuse it
11. New steps must:
    - Be parameterized with Cucumber Expressions for reusability
    - Delegate to `requests/` for HTTP calls
    - Use `payloads/` and `factories/` for data construction
    - Share state via `TestContext` (injected via constructor)
12. Group steps by domain into `{Domain}Steps.java`
13. Common cross-domain steps go into `CommonSteps.java`

## Step Reusability Rules
- Prefer `{word}`, `{int}`, `{string}` parameters over fixed text
- One step = one action or one assertion
- Before creating a new step, always check `CommonSteps` and other step classes