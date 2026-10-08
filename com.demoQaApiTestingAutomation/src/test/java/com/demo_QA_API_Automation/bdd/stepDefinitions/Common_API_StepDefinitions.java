package com.demo_QA_API_Automation.bdd.stepDefinitions;

import static org.hamcrest.Matchers.lessThan;

import java.util.List;
import java.util.Map;

import com.demo_QA_API_Automation.bdd.API.base.TestContext;
import com.demo_QA_API_Automation.bdd.Report_utill.ReportManager;

import io.restassured.response.Response;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.restassured.path.json.JsonPath;

import org.testng.Assert;

public class Common_API_StepDefinitions {

	private TestContext testContext;

	public Common_API_StepDefinitions(TestContext testContext) {
		this.testContext = testContext;
	}

	/*
	 * ============================================================ RESPONSE STATUS
	 * CODE ============================================================
	 */

	@And("I should see the response code as {string}")
	public void iShouldSeeTheResponseCodeAs(String expectedStatusCode) {

		Response response = getResponse();

		int actualStatusCode = response.getStatusCode();
		int expectedCode = Integer.parseInt(expectedStatusCode);

		ReportManager.logInfoAPI("Expected Status Code: " + expectedCode);

		ReportManager.logInfoAPI("Actual Status Code: " + actualStatusCode);

		Assert.assertEquals(actualStatusCode, expectedCode, "Response status code mismatch.");

		ReportManager.logPassAPI("Response status code validation passed: " + actualStatusCode);
	}

	/*
	 * ============================================================ RESPONSE TIME
	 * ============================================================
	 */

	@And("verify response time is less than {string} milliseconds")
	public void verifyResponseTimeIsLessThanMilliseconds(String expectedTime) {

		Response response = getResponse();

		long actualTime = response.getTime();
		long expectedTimeValue = Long.parseLong(expectedTime);

		ReportManager.logInfoAPI("Expected Response Time: < " + expectedTimeValue + " ms");

		ReportManager.logInfoAPI("Actual Response Time: " + actualTime + " ms");

		Assert.assertTrue(actualTime < expectedTimeValue, "Response time exceeded expected limit. " + "Expected < "
				+ expectedTimeValue + " ms but was " + actualTime + " ms.");

		ReportManager.logPassAPI("Response time validation passed: " + actualTime + " ms");
	}

	/*
	 * ============================================================ RESPONSE HEADERS
	 * ============================================================
	 */

	@And("verify response headers")
	public void verifyResponseHeaders(DataTable dataTable) {

		Response response = getResponse();

		List<Map<String, String>> headers = dataTable.asMaps(String.class, String.class);

		for (Map<String, String> header : headers) {

			String headerName = header.get("HeaderName");
			String expectedValue = header.get("HeaderValue");

			String actualValue = response.getHeader(headerName);

			ReportManager.logInfoAPI(
					"Header: " + headerName + " | Expected: " + expectedValue + " | Actual: " + actualValue);

			Assert.assertEquals(actualValue, expectedValue, "Response header mismatch for: " + headerName);

			ReportManager.logPassAPI("Response header validation passed: " + headerName);
		}
	}

	/*
	 * ============================================================ RESPONSE JSON
	 * FIELD VALIDATION ============================================================
	 */

	@And("the fields in response should match with expected values")
	public void verifyResponseFields(DataTable dataTable) {

		Response response = getResponse();

		String responseBody = response.asString();

		JsonPath jsonPath = new JsonPath(responseBody);

		List<Map<String, String>> fields = dataTable.asMaps(String.class, String.class);

		for (Map<String, String> field : fields) {

			String jsonPathExpression = field.get("JPath");
			String expectedValue = field.get("Value");

			Object actualValue = jsonPath.get(jsonPathExpression);

			ReportManager.logInfoAPI(
					"JSON Path: " + jsonPathExpression + " | Expected: " + expectedValue + " | Actual: " + actualValue);

			Assert.assertNotNull(actualValue, "No value found for JSONPath: " + jsonPathExpression);

			Assert.assertEquals(String.valueOf(actualValue), expectedValue,
					"Response value mismatch at " + jsonPathExpression);

			ReportManager.logPassAPI("Response field validation passed: " + jsonPathExpression);
		}
	}

	/*
	 * ============================================================ COMMON RESPONSE
	 * ACCESS ============================================================
	 *
	 * This gets the response from the TestContext.
	 */

	private Response getResponse() {

		Response response = testContext.getResponse();

		if (response == null) {

			throw new IllegalStateException("Response is not available. " + "Please execute the API request first.");
		}

		return response;
	}
}