package com.demo_QA_API_Automation.bdd.Report_utill;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.model.Media;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.demo_QA_API_Automation.bdd.Common_Utils.Constants;

public class ReportManager {

	// =========================================================
	// WEB UI REPORT
	// =========================================================

	public static ExtentReports extent;

	/*
	 * Thread ID -> ExtentTest
	 */
	public static Map<Long, ExtentTest> testMap = new ConcurrentHashMap<>();

	/*
	 * Test name -> ExtentTest
	 */
	public static Map<String, ExtentTest> extentMap = new ConcurrentHashMap<>();

	// =========================================================
	// START WEB REPORT
	// =========================================================

	public static synchronized void startReport() {

		if (extent == null) {

			System.out.println("+++++++++++++++++++ START WEB REPORT +++++++++++++++++++");

			extent = new ExtentReports();

			String timeStamp = new SimpleDateFormat("dd.MM.yyyy.HH.mm.ss").format(new Date());

			String dateStamp = new SimpleDateFormat("dd.MM.yyyy").format(new Date());

			ExtentSparkReporter spark = new ExtentSparkReporter(Constants.extent_reportPath);

			extent.attachReporter(spark);

			// -------------------------------------------------
			// REPORT CONFIGURATION
			// -------------------------------------------------

			spark.config().setTheme(Theme.STANDARD);

			spark.config().setDocumentTitle("Minis and More");

			spark.config().setReportName("MinisandMore_Web");

			// -------------------------------------------------
			// SYSTEM INFORMATION
			// -------------------------------------------------

			extent.setSystemInfo("Organization", "MinisandMore");

			extent.setSystemInfo("Domain", "Engineering (IT - Software)");

			extent.setSystemInfo("Skill", "Test Automation Engineer");

			System.out.println("ExtentReports initialized successfully.");

		} else {

			System.out.println("ExtentReports already initialized.");
		}
	}

	// =========================================================
	// START TEST
	// =========================================================

	public static synchronized void startTest(String testName, String categories) {

		if (extent == null) {

			System.out.println("ExtentReports is NULL.");

			System.out.println("Calling startReport() from startTest().");

			startReport();
		}

		String originalTestName = testName;

		if (extentMap.containsKey(testName)) {

			System.out.println("Duplicate test detected: " + testName);

			extent.removeTest(extentMap.get(testName));

			testName = "Rerun - " + testName;
		}

		ExtentTest test = extent.createTest(testName);

		if (categories != null && !categories.isEmpty()) {

			test.assignCategory(categories);
		}

		long threadId = Thread.currentThread().getId();

		testMap.put(threadId, test);

		extentMap.put(testName, test);

		System.out.println("ExtentTest CREATED");

		System.out.println("Test Name : " + testName);

		System.out.println("Thread ID : " + threadId);

		System.out.println("Current ExtentTest : " + getCurrentTest());
	}

	// =========================================================
	// GET CURRENT TEST
	// =========================================================

	public static ExtentTest getCurrentTest() {

		return testMap.get(Thread.currentThread().getId());
	}

	// =========================================================
	// LOG PASS
	// =========================================================

	public static void logPass(String message) {

		ExtentTest test = getCurrentTest();

		if (test != null) {

			test.log(Status.PASS, message);

		} else {

			System.out.println("ExtentTest is NULL. " + "Cannot log PASS: " + message);
		}
	}

	// =========================================================
	// LOG FAIL
	// =========================================================

	public static void logFail(String message) {

		ExtentTest test = getCurrentTest();

		if (test != null) {

			test.log(Status.FAIL, message);

			System.out.println("FAIL logged in ExtentReport: " + message);

		} else {

			System.out.println("ExtentTest is NULL. " + "Cannot log FAILURE: " + message);
		}
	}

	// =========================================================
	// LOG INFO
	// =========================================================

	public static void logInfo(String message) {

		ExtentTest test = getCurrentTest();

		if (test != null) {

			test.log(Status.INFO, message);

		} else {

			System.out.println("ExtentTest is NULL. " + "Cannot log INFO: " + message);
		}
	}

	// =========================================================
	// LOG INFO API WEB
	// =========================================================

	public static void logInfoAPIWEB(String message) {

		ExtentTest test = getCurrentTest();

		if (test != null) {

			test.log(Status.INFO, message);

		} else {

			System.out.println("ExtentTest is NULL. " + "Cannot log API WEB INFO: " + message);
		}
	}

	// =========================================================
	// SCREENSHOT
	// =========================================================

	public static void logScreenshot() throws IOException {

		ExtentTest test = getCurrentTest();

		if (test == null) {

			System.out.println("ExtentTest is NULL. " + "Screenshot will not be attached.");

			return;
		}

		String screenshot = Screenshot_Util.takeScreenshot();

		if (screenshot == null || screenshot.isEmpty()) {

			System.out.println("Screenshot data is NULL or empty.");

			return;
		}

		Media mediaModel = MediaEntityBuilder.createScreenCaptureFromBase64String(screenshot).build();

		test.fail("Screenshot on failure", mediaModel);

		System.out.println("Screenshot attached to ExtentReport.");
	}

	// =========================================================
	// END CURRENT TEST
	// =========================================================

	public static void endCurrentTest() {

		long threadId = Thread.currentThread().getId();

		ExtentTest test = testMap.get(threadId);

		if (test != null) {

			System.out.println("Ending ExtentTest for Thread: " + threadId);

			testMap.remove(threadId);

			System.out.println("ExtentTest ended successfully.");

		} else {

			System.out.println("ExtentTest is NULL. " + "Nothing to end.");
		}
	}

	// =========================================================
	// END WEB REPORT
	// =========================================================

	public static synchronized void endReport() {

		if (extent != null) {

			System.out.println("+++++++++++++++++++ END WEB REPORT +++++++++++++++++++");

			extent.flush();

			System.out.println("Web ExtentReport flushed successfully.");

		} else {

			System.out.println("ExtentReports is NULL. " + "Nothing to flush.");
		}
	}

	// =========================================================
	// API REPORT
	// =========================================================

	public static ExtentSparkReporter htmlReporterAPI;

	public static ExtentReports extentAPI;

	/*
	 * Thread ID -> API Scenario ExtentTest
	 */
	public static Map<Long, ExtentTest> testMapAPI = new ConcurrentHashMap<>();

	/*
	 * Thread ID -> Current API Step Node
	 *
	 * This is the important addition.
	 */
	public static Map<Long, ExtentTest> testMapAPINodes = new ConcurrentHashMap<>();

	public static ExtentTest testSuiteAPI;

	public static Map<String, ExtentTest> extentMapAPI = new ConcurrentHashMap<>();

	// =========================================================
	// START API REPORT
	// =========================================================

	public static synchronized void startApiReport() {

		if (extentAPI == null) {

			System.out.println("+++++++++++++++++++ START API REPORT +++++++++++++++++++");

			String reportPath = System.getProperty("user.dir") + "/Execution_Reports/API_Reports/" + "Extent_API.html";

			htmlReporterAPI = new ExtentSparkReporter(reportPath);

			extentAPI = new ExtentReports();

			extentAPI.attachReporter(htmlReporterAPI);

			extentAPI.setSystemInfo("Host Name", "BookStoreReport");

			extentAPI.setSystemInfo("Environment", "UAT");

			extentAPI.setSystemInfo("User Name", "Test User");

			htmlReporterAPI.config().setDocumentTitle("BookStore Application");

			htmlReporterAPI.config().setReportName("BookStore Application");

			htmlReporterAPI.config().setTheme(Theme.STANDARD);

			System.out.println("API ExtentReports initialized successfully.");

		} else {

			System.out.println("API ExtentReports already initialized.");
		}
	}

	// =========================================================
	// START API TEST
	// =========================================================

	public static synchronized void startTestAPI(String apiTestSuiteName, String categories) {

		if (extentAPI == null) {

			System.out.println("API ExtentReports is NULL.");

			startApiReport();
		}

		if (extentMapAPI.containsKey(apiTestSuiteName)) {

			extentAPI.removeTest(extentMapAPI.get(apiTestSuiteName));

			apiTestSuiteName = "Rerun - " + apiTestSuiteName;
		}

		testSuiteAPI = extentAPI.createTest(apiTestSuiteName);

		if (categories != null && !categories.isEmpty()) {

			testSuiteAPI.assignCategory(categories);
		}

		long threadId = Thread.currentThread().getId();

		testMapAPI.put(threadId, testSuiteAPI);

		extentMapAPI.put(apiTestSuiteName, testSuiteAPI);

		/*
		 * Make sure there is no old node associated with this thread.
		 */
		testMapAPINodes.remove(threadId);

		System.out.println("API ExtentTest CREATED: " + apiTestSuiteName);
	}

	// =========================================================
	// CREATE API NODE
	// =========================================================

	public static void createAPI_Node(String apiStepName) {

		long threadId = Thread.currentThread().getId();

		ExtentTest parentTest = testMapAPI.get(threadId);

		if (parentTest != null) {

			ExtentTest node = parentTest.createNode(apiStepName);

			testMapAPINodes.put(threadId, node);

			System.out.println("API Node Created: " + apiStepName);

		} else {

			System.out.println("API ExtentTest is NULL. " + "Cannot create API node: " + apiStepName);
		}
	}

	// =========================================================
	// GET CURRENT API TEST / NODE
	// =========================================================

	public static ExtentTest getCurrentAPITest() {

		long threadId = Thread.currentThread().getId();

		/*
		 * First return the current step node.
		 */
		ExtentTest node = testMapAPINodes.get(threadId);

		if (node != null) {

			return node;
		}

		/*
		 * Otherwise return the scenario.
		 */
		return testMapAPI.get(threadId);
	}

	// =========================================================
	// END CURRENT API NODE
	// =========================================================

	public static void endCurrentAPINode() {

		long threadId = Thread.currentThread().getId();

		testMapAPINodes.remove(threadId);

		System.out.println("API Step Node ended for Thread: " + threadId);
	}

	// =========================================================
	// API PASS
	// =========================================================

	public static void logPassAPI(String message) {

		ExtentTest test = getCurrentAPITest();

		if (test != null) {

			test.log(Status.PASS, message);

		} else {

			System.out.println("API ExtentTest is NULL. " + "Cannot log PASS: " + message);
		}
	}

	// =========================================================
	// API FAIL
	// =========================================================

	public static void logFailAPI(String message) {

		ExtentTest test = getCurrentAPITest();

		if (test != null) {

			test.log(Status.FAIL, message);

		} else {

			System.out.println("API ExtentTest is NULL. " + "Cannot log FAIL: " + message);
		}
	}

	// =========================================================
	// API INFO
	// =========================================================

	public static void logInfoAPI(String message) {

		ExtentTest test = getCurrentAPITest();

		if (test != null) {

			test.log(Status.INFO, message);

		} else {

			System.out.println("API ExtentTest is NULL. " + "Cannot log INFO: " + message);
		}
	}

	// =========================================================
	// END CURRENT API TEST
	// =========================================================

	public static void endCurrentAPITest() {

		long threadId = Thread.currentThread().getId();

		/*
		 * Remove any remaining API node.
		 */
		testMapAPINodes.remove(threadId);

		ExtentTest test = testMapAPI.get(threadId);

		if (test != null) {

			testMapAPI.remove(threadId);

			System.out.println("API ExtentTest ended successfully.");

		} else {

			System.out.println("API ExtentTest is NULL. " + "Nothing to end.");
		}
	}

	// =========================================================
	// END API REPORT
	// =========================================================

	public static synchronized void endAPIReport() {

		if (extentAPI != null) {

			extentAPI.flush();

			System.out.println("API ExtentReport flushed successfully.");

		} else {

			System.out.println("API ExtentReports is NULL.");
		}
	}

	// =========================================================
	// API/WEB SCREENSHOT INFO
	// =========================================================

	public static void logScreenshotInfo() throws IOException {

		ExtentTest test = getCurrentTest();

		if (test == null) {

			System.out.println("ExtentTest is NULL. " + "Screenshot will not be attached.");

			return;
		}

		String screenshot = Screenshot_Util.takeScreenshot();

		if (screenshot == null || screenshot.isEmpty()) {

			System.out.println("Screenshot data is NULL or empty.");

			return;
		}

		Media mediaModel = MediaEntityBuilder.createScreenCaptureFromBase64String(screenshot).build();

		test.pass("Screenshot", mediaModel);
	}
}