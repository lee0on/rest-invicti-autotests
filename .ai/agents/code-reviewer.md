# Agent: Code Reviewer

## Role
Validates generated or modified code against the framework's architecture,
conventions, and quality standards. Acts as the final gate before code is accepted.

## Input
- List of generated/modified files to review
- Context: which agent produced the code, which workflow

## Output
- Review report: per-file list of findings
- Each finding: file, line (if applicable), severity, description, suggested fix
- Severities: `ERROR` (must fix), `WARNING` (should fix), `INFO` (suggestion)
- Final verdict: `PASS`, `PASS_WITH_WARNINGS`, `FAIL`

## Checklist

### Architecture
- [ ] HTTP calls exist ONLY in `requests/` — nowhere else
- [ ] Assertions exist ONLY in `checks/` or `steps/` — not in requests or payloads
- [ ] Payload classes contain NO logic — pure data only
- [ ] No hardcoded URLs, ports, or host names anywhere
- [ ] No code duplication across packages
- [ ] Utility methods used in 2+ places are extracted to `utils/`
- [ ] Factory methods exist for all request payload classes

### Naming
- [ ] Payload classes: `{Entity}{Request|Response}Payload`
- [ ] Request classes: `{ApiName}Api`
- [ ] Check classes: `{Feature}Test`
- [ ] Mock classes: `{ApiName}Mock`
- [ ] Step classes: `{Domain}Steps`
- [ ] Utility classes: `{Concern}Utils`
- [ ] Method names are descriptive and follow project conventions
- [ ] `@DisplayName` present on every test — human-readable sentence

### Test Quality
- [ ] Every test follows Arrange → Act → Assert structure
- [ ] Tests are independent — no shared mutable state
- [ ] No reliance on test execution order
- [ ] Assertions are specific — no bare `assertNotNull` or `assertTrue(contains(...))`
- [ ] Negative scenarios covered: 400, 401, 403, 404
- [ ] Boundary values tested where applicable
- [ ] Data generated via `EasyRandomFactory` — no hardcoded dummy values
- [ ] No `@Disabled` without an explanatory comment
- [ ] `@Tag` present for categorization

### Compatibility
- [ ] Code compiles without errors
- [ ] All imports are correct and present
- [ ] No conflicts with existing classes or method signatures
- [ ] Jackson annotations match the target content type (JSON/XML)
- [ ] REST Assured method chain is syntactically correct
- [ ] Cucumber step expressions don't duplicate existing step definitions

### BDD (if applicable)
- [ ] Feature files use business language — no technical details
- [ ] Steps are atomic — one action per When, one check per Then
- [ ] Step definitions delegate to framework layers — no inline HTTP/logic
- [ ] TestContext used for shared state — no static fields
- [ ] Existing steps reused where possible