Feature: Can create and update Users

  Scenario: Create a new user
    Given A new user that wants to register
    And The user has a unique username
    When The user tries to register
    Then the outcome of the operation is "OK"

  Scenario: Create a new user with an already registered account
    Given A new user that wants to register
    And The user has not a unique username
    When The user tries to register
    Then the outcome of the operation is "KO"

  Scenario: Modify data of an existing user 
    Given An already existing user
    When The user tries to change his data
    Then the outcome of the operation is "OK"
    And The modified data have been stored

  Scenario: Delete existing user
    Given An already existing user
    When the user tries to cancel himself
    Then the outcome of the operation is "OK"
    And the user has been canceled

    