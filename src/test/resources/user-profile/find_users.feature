Feature: Can find Existing Users

  Scenario: Look for an existing user
    Given An existing user having id "0"
    When I look for the user "0"
    Then I should receive receive 200
    
  Scenario: Look for a not existing user
    Given A not existing user having id "1"
    When I look for the user "1"
    Then I should receive receive 404


