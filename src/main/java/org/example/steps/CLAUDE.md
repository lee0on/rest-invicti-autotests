# Steps Package (Cucumber Step Definitions)

## Purpose
Java implementation of Gherkin steps defined in `.feature` files.
This is the **glue code** that connects business-readable BDD scenarios
to the framework's request and payload layers.

## Rules
- Naming: `{Domain}Steps`
- One-step definition class per business domain / feature file
- Step methods are instance methods (not static) — Cucumber manages lifecycle
- Each step method is small: delegate to `requests/` and `payloads/`
- Shared state between steps via **TestContext** (dependency injection)
- NEVER duplicate step definitions — reuse across feature files

## Cucumber Configuration
- Glue package: `org.example.steps`
- Features path: `src/test/resources/features`
- Runner class uses `@Suite` + `@ConfigurationParameter` (JUnit Platform)
  or `@CucumberOptions` (JUnit 4 vintage)


## Step Reusability Guidelines

### DO reuse steps across features:
````
@Given("I am authenticated as an admin user") — shared auth step
@Then("I receive HTTP status {int}") — generic status assertion
@Given("the {word} API is available") — health check step
DO parameterize instead of creating duplicates:

GOOD: @When("I create a {word} booking") → handles "valid", "invalid", etc.
BAD: @When("I create a valid booking") + @When("I create an invalid booking")
````
### DO keep steps atomic:
````
One Given = one precondition
One When = one action
One Then = one verification
Runner Configuration
````

## Relationship to Other Layers
````
.feature files     →  define WHAT to test (business language)
Steps (this pkg)   →  define HOW to connect (glue code)
requests/          →  define HOW to call APIs
payloads/          →  define WHAT data looks like
factories/         →  define HOW to generate test data
Steps should be thin: they orchestrate calls to other layers, not implement logic.
````
## Anti-Patterns

- Do NOT put HTTP calls directly in step methods — use requests/ classes
- Do NOT put complex assertion logic in steps — extract to utility or custom assertion
- Do NOT use static fields for shared state — use TestContext via DI
- Do NOT create step definitions that are too specific to one scenario
- Do NOT catch exceptions in steps to hide failures
- Do NOT create steps that combine multiple actions in one When
- Do NOT duplicate step definitions across step classes — will cause Cucumber errors
- Do NOT ignore @Before/@After hooks for setup/cleanup — tests must be independent