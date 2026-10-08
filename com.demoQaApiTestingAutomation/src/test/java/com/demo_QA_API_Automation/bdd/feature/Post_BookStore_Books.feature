@BookStoreAPIS
Feature: Validation of DemoQA BookStore POST APIs

  @PostPositive
  Scenario: TC_002_Add book to user's collection - positive scenario
    Given I set up authenticated request specification
    When I send the POST request to "ADD_BOOK" endpoint with body
      """
      {
        "userId": "${USER_ID}",
        "collectionOfIsbns": [
          {
            "isbn": "${ISBN}"
          }
        ]
      }
      """
    Then I should see the response code as "201"
    And verify response time is less than "5000" milliseconds
    And verify response headers
      | HeaderName   | HeaderValue                     |
      | Content-Type | application/json; charset=utf-8 |
