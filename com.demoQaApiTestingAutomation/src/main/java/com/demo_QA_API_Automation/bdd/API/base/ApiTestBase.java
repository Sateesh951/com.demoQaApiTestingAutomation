package com.demo_QA_API_Automation.bdd.API.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.demo_QA_API_Automation.bdd.Common_Utils.ConfigReader;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ApiTestBase {

	public RequestSpecification specification;
	public RequestSpecBuilder specBuilder;
	public String stringRequestObject;
	public Response response;

	public List<Map<String, String>> requestData;
	public Map<String, String> requestMap;
	public Map<String, Object> contextMap;

	public ApiTestBase() {

		requestData = new ArrayList<>();
		requestMap = new HashMap<>();
		contextMap = new HashMap<>();
	}

	/**
	 * Builds the common API request specification.
	 */
	public RequestSpecification buildRequestSpecification() {

		String baseUri = ConfigReader.getAPIProperty("base.url");

		if (baseUri == null || baseUri.trim().isEmpty()) {
			throw new IllegalArgumentException("base.url is missing in configAPI.properties.");
		}

		specBuilder = new RequestSpecBuilder();

		specBuilder.setBaseUri(baseUri).setContentType(ContentType.JSON).setAccept(ContentType.JSON);

		specification = specBuilder.build();

		return specification;
	}

	/**
	 * Generates a fresh DemoQA authentication token.
	 */
	public String generateToken() {

		String baseUri = ConfigReader.getAPIProperty("base.url");

		String userName = ConfigReader.getAPIProperty("api.userName");

		String password = ConfigReader.getAPIProperty("api.password");

		if (baseUri == null || baseUri.trim().isEmpty()) {
			throw new IllegalArgumentException("base.url is missing in configAPI.properties.");
		}

		if (userName == null || userName.trim().isEmpty()) {
			throw new IllegalArgumentException("api.userName is missing in configAPI.properties.");
		}

		if (password == null || password.trim().isEmpty()) {
			throw new IllegalArgumentException("api.password is missing in configAPI.properties.");
		}

		Map<String, String> requestBody = new HashMap<>();

		requestBody.put("userName", userName);
		requestBody.put("password", password);

		Response tokenResponse = RestAssured.given().baseUri(baseUri).contentType(ContentType.JSON)
				.accept(ContentType.JSON).body(requestBody).when().post("/Account/v1/GenerateToken");

		if (tokenResponse.getStatusCode() != 200) {

			throw new RuntimeException("Token generation failed. Status Code: " + tokenResponse.getStatusCode()
					+ " Response: " + tokenResponse.asString());
		}

		String token = tokenResponse.jsonPath().getString("token");

		if (token == null || token.trim().isEmpty()) {

			throw new RuntimeException("Token generation succeeded but token is null or empty.");
		}

		return token;
	}
}