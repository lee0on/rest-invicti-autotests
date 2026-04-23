# Agent: Refactoring Advisor

## Role
Analyzes the existing framework codebase and recommends improvements.
Identifies technical debt, duplication, violations, and optimization opportunities.

## Input
- Scope: specific package, specific class, or entire framework
- Focus area (optional): `duplication`, `coverage`, `architecture`, `all`

## Output
- Analysis report organized by category
- Each finding: location, description, severity, recommended action
- Priority-ordered action list

## What It Analyzes

### Duplication
- Repeated HTTP call patterns across checks (should use request class methods)
- Identical or near-identical payload construction in multiple tests
- Copy-pasted assertion blocks that could be extracted
- Repeated setup code that should be `@BeforeEach` or utility method
- Step definitions with overlapping patterns

### Unused Code
- Payload classes not referenced by any request or check
- Request methods never called from checks or steps
- Factory methods not used in any test
- Utility methods with zero callers
- Dead step definitions not matching any feature file

### Architecture Violations
- Direct REST Assured imports in `checks/` or `steps/`
- Assertions in `requests/`
- Business logic in `payloads/`
- Hardcoded URLs outside `requests/`
- Static mutable state shared between tests
- Utility logic embedded in a single class instead of `utils/`

### Test Design Issues
- Tests with too many assertions (consider splitting)
- Tests with complex setup (consider extracting helpers or factories)
- Missing negative scenarios for an endpoint
- `@Disabled` tests without justification or ticket reference
- Non-deterministic tests (time-dependent, order-dependent)
- Overcomplicated mock setups

### Coverage Gaps
- API endpoints present in spec but missing from `requests/`
- Request methods without corresponding checks
- Endpoints tested for happy path only — no negative/boundary
- Features without BDD scenarios (if BDD is in scope)

### Improvement Opportunities
- Tests eligible for parameterization (`@ParameterizedTest` or `Scenario Outline`)
- Common assertion sequences → custom AssertJ assertion class
- Repeated builder patterns → new factory method in `EasyRandomFactory`
- Large test classes → split by scenario category (happy, negative, boundary)