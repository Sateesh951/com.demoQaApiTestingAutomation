package com.demo_QA_API_Automation.bdd.stepDefinitions;

import com.demo_QA_API_Automation.bdd.API.base.ApiTestBase;
import com.demo_QA_API_Automation.bdd.API.base.TestContext;
import com.demo_QA_API_Automation.bdd.Common_Utils.ConfigReader;
import com.demo_QA_API_Automation.bdd.Common_Utils.ResourceURIs;
import com.demo_QA_API_Automation.bdd.Report_utill.ReportManager;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.List;
import java.util.Map;

public class Get_BookStore_StepDefinitions extends ApiTestBase{

	private TestContext testContext;

	public Get_BookStore_StepDefinitions(TestContext testContext) {
		this.testContext = testContext;
	}

	@Given("I set up request specification")
	public void iSetUpRequestSpecification(DataTable dataTable) {

		RequestSpecification specification = buildRequestSpecification();

		List<Map<String, String>> headers = dataTable.asMaps(String.class, String.class);

		for (Map<String, String> header : headers) {

			specification.header(header.get("HeaderName"), header.get("HeaderValue"));
		}

		testContext.setRequestSpecification(specification);

		ReportManager.logInfoAPI("GET request specification created.");
	}

	@When("I send the GET request to {string} endpoint")
	public void iSendTheGetRequestToEndpoint(String endpointName) {

		String endpoint = ResourceURIs.getResourceURI(endpointName);

		ReportManager.createAPI_Node("GET Request");

		ReportManager.logInfoAPI("GET Endpoint: " + endpoint);

		Response response = testContext.getRequestSpecification().when().get(endpoint);

		testContext.setResponse(response);

		ReportManager.logInfoAPI("Response Status Code: " + response.getStatusCode());

		ReportManager.logInfoAPI("Response Time: " + response.getTime() + " ms");

		ReportManager.logInfoAPI("Response Body: " + response.asPrettyString());
	}
}