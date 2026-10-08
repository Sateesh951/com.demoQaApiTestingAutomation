@BookStoreAPIS
Feature: Validation of DemoQA BookStore GET APIs

  @GetPositive
  Scenario: TC_001_Get all books - positive scenario
    Given I set up request specification
      | HeaderName | HeaderValue      |
      | Accept     | application/json |
    When I send the GET request to "GET_ALL_BOOKS" endpoint
    Then I should see the response code as "200"
    And verify response time is less than "5000" milliseconds
    And verify response headers
      | HeaderName   | HeaderValue                     |
      | Content-Type | application/json; charset=utf-8 |
    And the fields in response should match with expected values
      | JPath            | Value            |
      | $.books[0].isbn  | 9781449325862    |
      | $.books[0].title | Git Pocket Guide |
