package com.demo_QA_API_Automation.bdd.API.base;

import java.util.HashMap;
import java.util.Map;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class TestContext {

	private RequestSpecification requestSpecification;

	private Response response;

	private String userId;

	private String token;

	public RequestSpecification getRequestSpecification() {
		return requestSpecification;
	}

	public void setRequestSpecification(RequestSpecification requestSpecification) {

		this.requestSpecification = requestSpecification;
	}

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {

		this.response = response;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {

		this.userId = userId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {

		this.token = token;
	}
}
