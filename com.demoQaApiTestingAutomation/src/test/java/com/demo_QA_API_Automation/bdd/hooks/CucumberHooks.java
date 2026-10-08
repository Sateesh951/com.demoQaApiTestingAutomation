package com.demo_QA_API_Automation.bdd.hooks;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.demo_QA_API_Automation.bdd.Common_Utils.BrowserFactory;
import com.demo_QA_API_Automation.bdd.Report_utill.ReportManager;
import com.microsoft.playwright.Tracing;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class CucumberHooks {

	public static ArrayList<String> passedTests = new ArrayList<>();

	public static ArrayList<String> failedTests = new ArrayList<>();

	public static ArrayList<String> totalTestCases = new ArrayList<>();

	private static String str_Execution_TYPE;

	public String str_BrowserType;

	BrowserFactory browserfactory = BrowserFactory.getInstance();

	public static String featureFileName;

	// =========================================================
	// BEFORE HOOK
	// =========================================================

	@Before(order = 1)
	public void before(Scenario scenario) throws Exception {

		System.out.println();
		System.out.println("+++++++++++++++++++ BEFORE HOOKS +++++++++++++++++++");

		// =====================================================
		// DETERMINE EXECUTION TYPE
		// =====================================================

		if (scenario.getSourceTagNames().contains("@API")) {

			str_Execution_TYPE = "API";

		} else {

			str_Execution_TYPE = System.getProperty("execType", "Web_UI");
		}

		// =====================================================
		// BROWSER
		// =====================================================

		str_BrowserType = System.getProperty("Browser", "chrome");

		System.out.println("Scenario       : " + scenario.getName());

		System.out.println("Execution Type : " + str_Execution_TYPE);

		System.out.println("Browser        : " + str_BrowserType);

		System.out.println("Tags           : " + scenario.getSourceTagNames());

		// =====================================================
		// API EXECUTION
		// =====================================================

		if ("API".equalsIgnoreCase(str_Execution_TYPE)) {

			System.out.println("Starting API scenario: " + scenario.getName());

			ReportManager.startTestAPI(scenario.getName(), "API");

			System.out.println("API reporting initialized.");

			return;
		}

		// =====================================================
		// WEB UI EXECUTION
		// =====================================================

		if ("Web_UI".equalsIgnoreCase(str_Execution_TYPE)) {

			System.out.println("Starting Web UI scenario: " + scenario.getName());

			ReportManager.startTest(scenario.getName(), "SMOKE");

			if (str_BrowserType == null || str_BrowserType.isEmpty()) {

				throw new Exception("Browser property is not set.");
			}

			browserfactory.setBrowser(str_BrowserType.toLowerCase());

			BrowserFactory.getBrowserContext().tracing()
					.start(new Tracing.StartOptions().setScreenshots(true).setSnapshots(true));

			return;
		}

		throw new Exception("Invalid execution type: " + str_Execution_TYPE);
	}

	// =========================================================
	// LOGIN REQUIRED
	// =========================================================

	/*
	 * @Before(value = "@LoginRequired", order = 2) public void loginRequired()
	 * throws Exception {
	 * 
	 * System.out.
	 * println("+++++++++++++++++++ LOGIN REQUIRED HOOK +++++++++++++++++++");
	 * 
	 * if ("Web_UI".equalsIgnoreCase(str_Execution_TYPE)) {
	 * 
	 * System.out.println("Login required - Starting login...");
	 * 
	 * Login_Page loginPage = new Login_Page();
	 * 
	 * loginPage.the_user_navigates_to_the_login_page();
	 * 
	 * loginPage.the_user_enters_valid_login_credentials();
	 * 
	 * loginPage.the_user_clicks_the_sign_in_button();
	 * 
	 * loginPage.the_user_should_be_successfully_logged_in();
	 * 
	 * System.out.println("Login completed successfully.");
	 * 
	 * } else {
	 * 
	 * System.out.println("LOGIN SKIPPED - API EXECUTION"); } }
	 */

	// =========================================================
	// AFTER HOOK
	// =========================================================

	@After
	public void after(Scenario scenario) throws Exception {

		System.out.println();
		System.out.println("+++++++++++++++++++ AFTER HOOKS +++++++++++++++++++");

		System.out.println("Scenario : " + scenario.getName());

		System.out.println("Scenario Status : " + (scenario.isFailed() ? "FAILED" : "PASSED"));

		// =====================================================
		// API
		// =====================================================

		if ("API".equalsIgnoreCase(str_Execution_TYPE)) {

			totalTestCases.add(scenario.getName());

			if (scenario.isFailed()) {

				failedTests.add(scenario.getName());

				System.out.println("==============================================");

				System.out.println("API SCENARIO : FAILED");

				System.out.println(scenario.getName());

				System.out.println("==============================================");

			} else {

				passedTests.add(scenario.getName());

				System.out.println("==============================================");

				System.out.println("API SCENARIO : PASSED");

				System.out.println(scenario.getName());

				System.out.println("==============================================");

			}

			ReportManager.endCurrentAPITest();

			return;
		}

		// =====================================================
		// WEB UI
		// =====================================================

		if ("Web_UI".equalsIgnoreCase(str_Execution_TYPE)) {

			String dateStamp = new SimpleDateFormat("dd.MM.yyyy").format(new Date());

			// -------------------------------------------------
			// STOP TRACE
			// -------------------------------------------------

			try {

				Path tracingDirectory = Paths.get(System.getProperty("user.dir"), "TracingReports", dateStamp);

				Files.createDirectories(tracingDirectory);

				Path tracePath = tracingDirectory.resolve(scenario.getName() + ".zip");

				BrowserFactory.getBrowserContext().tracing().stop(new Tracing.StopOptions().setPath(tracePath));

				System.out.println("Trace saved at : " + tracePath);

			} catch (Exception e) {

				System.out.println("Unable to stop Playwright tracing.");

				e.printStackTrace();
			}

			totalTestCases.add(scenario.getName());

			if (scenario.isFailed()) {

				failedTests.add(scenario.getName());

			} else {

				passedTests.add(scenario.getName());
			}

			ReportManager.endCurrentTest();
		}
	}
}
