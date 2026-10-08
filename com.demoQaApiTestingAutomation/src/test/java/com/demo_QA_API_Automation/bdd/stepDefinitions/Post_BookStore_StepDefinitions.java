package com.demo_QA_API_Automation.bdd.stepDefinitions;

import com.demo_QA_API_Automation.bdd.API.base.ApiTestBase;
import com.demo_QA_API_Automation.bdd.API.base.TestContext;
import com.demo_QA_API_Automation.bdd.Common_Utils.ConfigReader;
import com.demo_QA_API_Automation.bdd.Common_Utils.ResourceURIs;
import com.demo_QA_API_Automation.bdd.Report_utill.ReportManager;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Post_BookStore_StepDefinitions extends ApiTestBase {

	private TestContext testContext;

	public Post_BookStore_StepDefinitions(TestContext testContext) {
		this.testContext = testContext;
	}

	@Given("I set up authenticated request specification")
	public void iSetUpAuthenticatedRequestSpecification() {

		// Generate fresh token
		String token = generateToken();

		RequestSpecification specification = buildRequestSpecification().header("Authorization", "Bearer " + token)
				.header("Accept", "application/json").header("Content-Type", "application/json");

		// Store token and request specification
		testContext.setToken(token);
		testContext.setRequestSpecification(specification);

		ReportManager.logInfoAPI("Authenticated API request specification created.");

		ReportManager.logInfoAPI("Authentication token generated successfully.");
	}

	@When("I send the POST request to {string} endpoint with body")
	public void iSendThePostRequestToEndpointWithBody(String endpointName, String requestBody) {

		String endpoint = ResourceURIs.getResourceURI(endpointName);

		String userId = ConfigReader.getAPIProperty("api.userId");

		String isbn = ConfigReader.getAPIProperty("api.isbn");

		if (userId == null || userId.isBlank()) {
			throw new RuntimeException("api.userId is missing in configAPI.properties.");
		}

		if (isbn == null || isbn.isBlank()) {
			throw new RuntimeException("api.isbn is missing in configAPI.properties.");
		}

		// Replace framework placeholders
		requestBody = requestBody.replace("${USER_ID}", userId).replace("${ISBN}", isbn);

		ReportManager.createAPI_Node("POST Request");

		ReportManager.logInfoAPI("POST Endpoint: " + endpoint);

		ReportManager.logInfoAPI("Request Body: " + requestBody);

		/*
		 * IMPORTANT: Do not execute POST directly on
		 * testContext.getRequestSpecification().
		 *
		 * Use RestAssured.given().spec(...)
		 */
		Response response = RestAssured.given().spec(testContext.getRequestSpecification()).body(requestBody).when()
				.post(endpoint);

		// Store response in scenario context
		testContext.setResponse(response);

		System.out.println("========== POST RESPONSE ==========");
		System.out.println("Status Code: " + response.getStatusCode());
		System.out.println("Response Body: " + response.asPrettyString());
		System.out.println("==================================");

		ReportManager.logInfoAPI("Response Status Code: " + response.getStatusCode());
		ReportManager.logInfoAPI("Response Time: " + response.getTime() + " ms");
		ReportManager.logInfoAPI("Response Body: " + response.asPrettyString());

		ReportManager.logInfoAPI("Response Status Code: " + response.getStatusCode());

		ReportManager.logInfoAPI("Response Time: " + response.getTime() + " ms");

		ReportManager.logInfoAPI("Response Body: " + response.asPrettyString());
	}
}