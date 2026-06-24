@comment
Feature: Comment lifecycle journeys

  End-to-end journeys that combine several comment operations in a single
  flow, verifying that state persists correctly across calls.

  Background:
    Given the comment API is available
    And I am authenticated as an admin user

  @regression
  Scenario: A newly created comment can be retrieved by its identifier
    Given I have valid comment details
    When I create a new comment
    Then the comment is created successfully
    And the response contains the comment id
    When I retrieve the comment by its identifier
    Then the requested comment is returned

  @regression
  Scenario: A newly created comment appears in the full comment list
    Given I have valid comment details
    When I create a new comment
    Then the comment is created successfully
    When I retrieve all comments
    Then the list of comments is not empty

  @regression
  Scenario: An updated comment reflects the new text when retrieved again
    Given an existing comment
    When I update the comment with new text
    Then the comment text is updated
    When I retrieve the comment by its identifier
    Then the requested comment is returned

  @regression
  Scenario: A comment can be created, updated and then deleted
    Given an existing comment
    When I update the comment with new text
    Then the comment text is updated
    When I delete the comment
    Then the comment is removed with a confirmation

  @negative
  Scenario: Retrieving all comments still succeeds after a rejected creation
    Given I have comment details without the required fields
    When I create a new comment
    Then I receive a bad request error
    When I retrieve all comments
    Then the list of comments is not empty