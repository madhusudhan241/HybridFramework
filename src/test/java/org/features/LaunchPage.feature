Feature: Login Page validations

  Scenario Outline: Verify the login Functionality
    Given user is on login page
    And User login with valid credentials "<UserName>" "<Password>"
    And user gets the title of the page
    Then page title should be "Account Login"
    Examples:
      | UserName                       | Password  |
      | madhusudhan.guniputi@gmail.com | Madhu@241 |


  Scenario: Login page title
    Given user is on login page
    When user gets the title of the page
    Then page title should be "Account Login"

  Scenario: Forgot Password link
    Given user is on login page
    Then forgot your password link should be displayed
