# Features (Gherkin / Cucumber BDD)

## Purpose
Gherkin feature files describing API behavior in business-readable language.

> See `gherkin-authoring` skill for detailed step writing rules, parameterization, and language guidelines.

## Rules
- File naming: `{domain}.feature` (lowercase, underscores)
- Every feature has a descriptive `Feature:` title
- Use `Background:` for shared preconditions within a feature
- Use `Scenario Outline:` + `Examples:` for data-driven scenarios
- Tags: `@smoke`, `@regression`, `@negative`, `@{api-name}`

## Writing Style
```gherkin
Feature: Booking management

  Background:
    Given the booking API is available
    And I am authenticated as an admin user

  @smoke
  Scenario: Successfully create a new booking
    Given I have valid booking details
    When I create a new booking
    Then the booking is created successfully
    And the response contains the booking id

  @negative
  Scenario: Reject booking with missing required fields
    Given I have booking details without a firstname
    When I create a new booking
    Then I receive a bad request error
    And the error message indicates firstname is required

  @regression
  Scenario Outline: Create booking with different room types
    Given I have booking details for a "<room_type>" room
    When I create a new booking
    Then the booking is created with room type "<room_type>"

    Examples:
      | room_type |
      | single    |
      | double    |
      | suite     |
```

## Step Reusability
- Write generic, parameterized steps — avoid scenario-specific ones
- Before writing a new step, check existing step definitions for reuse

## Anti-Patterns
- Technical details in step text (status codes, JSON fields, URLs)
- Compound `When` steps performing multiple actions
- `Given` steps that are actually assertions
