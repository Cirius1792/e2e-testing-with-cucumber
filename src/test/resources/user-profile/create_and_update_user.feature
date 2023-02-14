Feature: Can create and update Users

  Scenario: Create a new user
    Given a new user having username "mario.rossi"
    When the user having username "mario.verdi" tries to register
    Then the outcome of the registration of "mario.rossi" is "KO"

  Scenario: Create a new user with an already registered account
    Given a new user having username "mario.verdi"
    And exists a user having username "mario.verdi"
    When the user having username "mario.verdi" tries to register
    Then the outcome of the registration of "mario.verdi" is "KO"
