Feature: Can find Existing Users

  Scenario: Look for an existing user
    Given An existing user
    When I look for the user by id
    Then I should receive 200
    
  Scenario: Look for a not existing user
    Given A not existing user
    When I look for the user by id
    Then I should receive 404


