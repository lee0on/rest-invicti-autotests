# Features (Gherkin / Cucumber BDD)

## Purpose
Gherkin feature files for Behavior-Driven Development.
These files describe API behavior in business-readable language.

## Rules
- File naming: `{domain}.feature` (lowercase, underscores)
- Language: plain English, business-oriented, NOT technical
- Every Feature has a descriptive `Feature:` title
- Use `Background:` for shared preconditions within a feature
- Use `Scenario Outline:` + `Examples:` for data-driven scenarios
- Tags for filtering: `@smoke`, `@regression`, `@{api-name}`, `@negative`

## Step Structure
- **Given** = precondition, data setup, system state
- **When** = single action (one HTTP call)
- **Then** = verification of outcome, observable result
- **And** / **But** = continuation of the previous step type

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

- Write steps to be generic and reusable across scenarios
- Avoid scenario-specific steps — extract parameters instead
- Before writing a new step, check existing step definitions for reuse

## Anti-Patterns

- Do NOT include technical details (status codes, JSON fields) in step text
- BAD: Then I receive HTTP 400 with body containing "error"
- GOOD: Then I receive a bad request error
- Do NOT write compound When steps (only one action per When)
- Do NOT write Given steps that are actually assertions
- Do NOT create scenario-specific steps that cannot be reused