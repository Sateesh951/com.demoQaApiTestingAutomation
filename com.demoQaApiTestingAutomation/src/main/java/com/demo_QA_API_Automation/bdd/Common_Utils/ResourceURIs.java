package com.demo_QA_API_Automation.bdd.Common_Utils;

public class ResourceURIs {

	private static final String GET_ALL_BOOKS = "/BookStore/v1/Books";

	private static final String GET_BOOK_BY_ISBN = "/BookStore/v1/Book";

	public static final String ADD_BOOK = "/BookStore/v1/Books";

	public static String getResourceURI(String requestName) {

		if (requestName == null || requestName.trim().isEmpty()) {

			throw new IllegalArgumentException("Request URI cannot be null or empty.");
		}

		switch (requestName.trim()) {

		case "GET_ALL_BOOKS":
			return ResourceURIs.GET_ALL_BOOKS;

		case "GET_BOOK_BY_ISBN":
			return ResourceURIs.GET_BOOK_BY_ISBN;

		case "ADD_BOOK":
			return ResourceURIs.ADD_BOOK;

		default:

			// Allow dynamic endpoints
			return requestName;
		}
	}
}