Feature: Home Page validations

  Scenario Outline: Verify the My Account Page
    Given user is on login page
    And User login with valid credentials "<UserName>" "<Password>"
    Then Change Password link should present
    Examples:
      | UserName                       | Password  |
      | madhusudhan.guniputi@gmail.com | Madhu@241 |
