Feature: Can create and update Users

  Scenario: Create a new user
    Given A new user that wants to register
    And The user has a unique username
    When The user tries to register
    Then the outcome of the registration is "OK"

  Scenario: Create a new user with an already registered account
    Given A new user that wants to register
    And The user has not a unique username
    When The user tries to register
    Then the outcome of the registration is "KO"
