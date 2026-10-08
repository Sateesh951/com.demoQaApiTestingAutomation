package com.demo_QA_API_Automation.bdd.NG_Listeners;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.testng.IExecutionListener;
import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.xml.XmlSuite;

import com.demo_QA_API_Automation.bdd.Common_Utils.BrowserFactory;
import com.demo_QA_API_Automation.bdd.Report_utill.ReportManager;
import com.opencsv.CSVWriter;

public class SuiteEvent extends TestListenerAdapter implements ISuiteListener, IExecutionListener, IReporter {

	private CSVWriter writer;

	public List<String[]> data = new ArrayList<>();

	public static String str_Execution_TYPE;

// =========================================================
// SUITE START
// =========================================================

	@Override
	public void onStart(ISuite suite) {

		System.out.println("+++++++++++++++++++ SUITE START +++++++++++++++++++");

	}

// =========================================================
// SUITE FINISH
// =========================================================

	@Override
	public void onFinish(ISuite suite) {

		System.out.println("+++++++++++++++++++ SUITE FINISH +++++++++++++++++++");

	}

// =========================================================
// EXECUTION START
// =========================================================

	@Override
	public void onExecutionStart() {

		System.out.println("+++++++++++++++++++ EXECUTION STARTED +++++++++++++++++++");

// Read execution type
		str_Execution_TYPE = System.getProperty("execType");

// Read browser
		String browser = System.getProperty("Browser");

// Read suite XML
		String suite = System.getProperty("suiteXMLFile", "testng.xml");

		System.out.println("====================================");

		System.out.println("Browser       : " + browser);

		System.out.println("ExecutionType : " + str_Execution_TYPE);

		System.out.println("Suite XML     : " + suite);

		System.out.println("====================================");

// =====================================================
// VALIDATE EXECUTION TYPE
// =====================================================

		if (str_Execution_TYPE == null || str_Execution_TYPE.isEmpty()) {

			System.out.println("ERROR: execType is not set.");

			return;
		}

// =====================================================
// WEB UI
// =====================================================

		if ("Web_UI".equalsIgnoreCase(str_Execution_TYPE)) {

			System.out.println("Starting Web UI Extent Report...");

			ReportManager.startReport();

		}

// =====================================================
// API
// =====================================================

		else if ("API".equalsIgnoreCase(str_Execution_TYPE)) {

			System.out.println("Starting API Extent Report...");

			ReportManager.startApiReport();

		}

// =====================================================
// INVALID TYPE
// =====================================================

		else {

			System.out.println("[-] Please set execution type " + "[Web_UI or API]");
		}
	}

// =========================================================
// EXECUTION FINISH
// =========================================================

	@Override
	public void onExecutionFinish() {

		System.out.println("+++++++++++++++++++ EXECUTION FINISHED +++++++++++++++++++");

		String browser = System.getProperty("Browser");

// =====================================================
// WEB UI
// =====================================================

		if ("Web_UI".equalsIgnoreCase(str_Execution_TYPE)) {

			// -------------------------------------------------
			// ADD BROWSER INFORMATION
			// -------------------------------------------------

			if (ReportManager.extent != null) {

				ReportManager.extent.setSystemInfo("Browser Name", String.valueOf(browser));
			}

			// -------------------------------------------------
			// CLOSE BROWSER
			// -------------------------------------------------

			try {

				if (BrowserFactory.getBrowser() != null) {

					BrowserFactory.getBrowser().close();

					System.out.println("Browser closed successfully.");
				}

			} catch (Exception e) {

				System.out.println("Unable to close browser.");

				e.printStackTrace();
			}

			// -------------------------------------------------
			// CLOSE PLAYWRIGHT
			// -------------------------------------------------

			try {

				if (BrowserFactory.getPlaywright() != null) {

					BrowserFactory.getPlaywright().close();

					System.out.println("Playwright closed successfully.");
				}

			} catch (Exception e) {

				System.out.println("Unable to close Playwright.");

				e.printStackTrace();
			}

			// -------------------------------------------------
			// FLUSH EXTENT REPORT
			// -------------------------------------------------

			ReportManager.endReport();

		}

// =====================================================
// API
// =====================================================

		else if ("API".equalsIgnoreCase(str_Execution_TYPE)) {

			ReportManager.endAPIReport();

		}

// =====================================================
// INVALID
// =====================================================

		else {

			System.out.println("[-] Invalid execution type.");
		}
	}

// =========================================================
// TEST FAILURE
// =========================================================

	@Override
	public void onTestFailure(ITestResult iTestResult) {

		System.out.println("+++++++++++++++++++++ ON TEST FAILURE +++++++++++++++++++++");

		Throwable throwable = iTestResult.getThrowable();

// =====================================================
// WEB UI FAILURE
// =====================================================

		if ("Web_UI".equalsIgnoreCase(str_Execution_TYPE)) {

			// -------------------------------------------------
			// LOG FAILURE
			// -------------------------------------------------

			if (throwable != null) {

				ReportManager.logFail(throwable.toString());

			} else {

				ReportManager.logFail("Test case failed.");
			}

			// -------------------------------------------------
			// SCREENSHOT
			// -------------------------------------------------

			try {

				ReportManager.logScreenshot();

			} catch (IOException e) {

				System.out.println("Unable to capture screenshot.");

				e.printStackTrace();
			}

			/*
			 * IMPORTANT:
			 *
			 * DO NOT call:
			 *
			 * ReportManager.endCurrentTest();
			 *
			 * here.
			 *
			 * CucumberHooks @After will handle endCurrentTest().
			 */
		}

// =====================================================
// API FAILURE
// =====================================================

		else if ("API".equalsIgnoreCase(str_Execution_TYPE)) {

			if (throwable != null) {

				ReportManager.logFailAPI(throwable.toString());

			} else {

				ReportManager.logFailAPI("API test case failed.");
			}
		}

// =====================================================
// VIDEO STOP
// =====================================================

		try {

			/*
			 * Keep this only if VideoRecord is used in your framework.
			 *
			 * VideoRecord.stopRecord();
			 */

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

// =========================================================
// TEST SUCCESS
// =========================================================

	@Override
	public void onTestSuccess(ITestResult iTestResult) {

		System.out.println("+++++++++++++++++++++ ON TEST SUCCESS +++++++++++++++++++++");

		if ("Web_UI".equalsIgnoreCase(str_Execution_TYPE)) {

			ReportManager.logPass("Test scenario passed successfully.");

		} else if ("API".equalsIgnoreCase(str_Execution_TYPE)) {

			ReportManager.logPassAPI("API test scenario passed successfully.");
		}
	}

// =========================================================
// TEST SKIPPED
// =========================================================

	@Override
	public void onTestSkipped(ITestResult iTestResult) {

		System.out.println("+++++++++++++++++++++ ON TEST SKIPPED +++++++++++++++++++++");

		Throwable throwable = iTestResult.getThrowable();

		String message = throwable != null ? throwable.toString() : "Test scenario skipped.";

		if ("Web_UI".equalsIgnoreCase(str_Execution_TYPE)) {

			ReportManager.logInfo("SKIPPED: " + message);

		} else if ("API".equalsIgnoreCase(str_Execution_TYPE)) {

			ReportManager.logInfoAPI("SKIPPED: " + message);
		}
	}

// =========================================================
// GENERATE CSV REPORT
// =========================================================

	@Override
	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {

		System.out.println("+++++++++++++++++++ GENERATING CSV REPORT +++++++++++++++++++");

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		String csv = "./test.csv";

		try {

			writer = new CSVWriter(new FileWriter(csv));

		} catch (IOException e) {

			e.printStackTrace();

			return;
		}

// Clear previous data
		data.clear();

// =====================================================
// LOOP THROUGH SUITES
// =====================================================

		for (ISuite iSuite : suites) {

			Map<String, ISuiteResult> results = iSuite.getResults();

			Set<String> keys = results.keySet();

			for (String key : keys) {

				ITestContext context = results.get(key).getTestContext();

				// =================================================
				// FAILED TESTS
				// =================================================

				IResultMap failedResultMap = context.getFailedTests();

				Collection<ITestNGMethod> failedMethods = failedResultMap.getAllMethods();

				for (ITestNGMethod method : failedMethods) {

					Date dateTime = new Date(method.getDate());

					String dateDate = dateFormat.format(dateTime);

					String[] dateTimeArray = dateDate.split(" ");

					data.add(new String[] {

							dateTimeArray[0],

							dateTimeArray[1],

							method.getMethodName(),

							method.getDescription(),

							"FAIL" });
				}

				// =================================================
				// PASSED TESTS
				// =================================================

				IResultMap passedResultMap = context.getPassedTests();

				Collection<ITestNGMethod> passedMethods = passedResultMap.getAllMethods();

				for (ITestNGMethod method : passedMethods) {

					Date dateTime = new Date(method.getDate());

					String dateDate = dateFormat.format(dateTime);

					String[] dateTimeArray = dateDate.split(" ");

					data.add(new String[] {

							dateTimeArray[0],

							dateTimeArray[1],

							method.getMethodName(),

							method.getDescription(),

							"PASS" });
				}
			}
		}

// =====================================================
// CSV HEADER
// =====================================================

		String[] header = {

				"Date",

				"Time",

				"Test Case ID",

				"Description",

				"Result" };

		writer.writeNext(header);

// Write all test data
		writer.writeAll(data);

// =====================================================
// CLOSE CSV
// =====================================================

		try {

			writer.close();

			System.out.println("CSV report generated: " + csv);

		} catch (IOException e) {

			e.printStackTrace();
		}
	}
}
