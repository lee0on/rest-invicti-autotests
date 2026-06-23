@comment
Feature: Comment management

  Background:
    Given the comment API is available
    And I am authenticated as an admin user

  @smoke
  Scenario: Successfully create a new comment
    Given I have valid comment details
    When I create a new comment
    Then the comment is created successfully
    And the response contains the comment id

  @smoke
  Scenario: Retrieve an existing comment by its identifier
    Given an existing comment
    When I retrieve the comment by its identifier
    Then the requested comment is returned

  @smoke
  Scenario: Retrieve all comments
    When I retrieve all comments
    Then the list of comments is not empty

  @negative
  Scenario: Reject a comment with missing required details
    Given I have comment details without the required fields
    When I create a new comment
    Then I receive a bad request error

  @regression
  Scenario: Update an existing comment
    Given an existing comment
    When I update the comment with new text
    Then the comment text is updated

  @smoke
  Scenario: Delete an existing comment
    Given an existing comment
    When I delete the comment
    Then the comment is removed with a confirmation

  @negative
  Scenario: Retrieve a comment that does not exist
    When I retrieve a comment that does not exist
    Then I receive a not found error
