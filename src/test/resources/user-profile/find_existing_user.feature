Feature: Can find Existing Users

  Scenario: Look for an existing user
    Given A user having id "<id>"
    When I lookfor the user
    Then I should receive receive "<outcome>"

  Examples:
    | id         | outcome  |
    | 0L         | 200      |
    | 1L         | 200      |
    | 2L         | 404      |