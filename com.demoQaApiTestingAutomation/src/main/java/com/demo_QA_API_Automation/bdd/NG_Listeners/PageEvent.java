package com.demo_QA_API_Automation.bdd.NG_Listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.demo_QA_API_Automation.bdd.Common_Utils.BrowserFactory;
//import com.demo_QA_API_Automation.bdd.Common_Utils.VideoRecord;
import com.demo_QA_API_Automation.bdd.Report_utill.ReportManager;

public class PageEvent implements ITestListener {

	private final BrowserFactory browserFactory = BrowserFactory.getInstance();

	public static String strBrowser;

	// ============================================================
	// TEST START
	// ============================================================

	@Override
	public void onTestStart(ITestResult iTestResult) {

		System.out.println("+++++++++++++++++++++ onTestStart +++++++++++++++++++++");

		strBrowser = System.getProperty("Browser", "chrome");

		System.out.println("Execution started @ " + strBrowser + " browser & for type : Web UI");

		try {

			/*
			 * Video recording is handled by TestNG listener. Cucumber Hooks handle
			 * browser/context initialization.
			 */
			// VideoRecord.startRecord(iTestResult.getMethod().getMethodName());

		} catch (Exception e) {

			System.out.println("Unable to start video recording: " + e.getMessage());

		}
	}

	// ============================================================
	// TEST SUCCESS
	// ============================================================

	@Override
	public void onTestSuccess(ITestResult iTestResult) {

		System.out.println("+++++++++++++++++++++ onTestSuccess +++++++++++++++++++++");

		try {

			/*
			 * Stop video.
			 */
			// VideoRecord.stopRecord();

		} catch (Exception e) {

			System.out.println("Unable to stop video recording: " + e.getMessage());
		}

		/*
		 * DO NOT close the Playwright page here.
		 *
		 * CucumberHooks @After should handle browser/context cleanup.
		 */
	}

	// ============================================================
	// TEST FAILURE
	// ============================================================

	@Override
	public void onTestFailure(ITestResult iTestResult) {

		System.out.println("+++++++++++++++++++++ onTestFailure +++++++++++++++++++++");

		/*
		 * IMPORTANT:
		 *
		 * In Cucumber + TestNG, there may be no ExtentTest created for the current
		 * TestNG thread.
		 *
		 * Therefore NEVER directly call:
		 *
		 * ReportManager.logFail(...)
		 *
		 * without checking getCurrentTest().
		 */

		try {

			if (ReportManager.getCurrentTest() != null) {

				Throwable throwable = iTestResult.getThrowable();

				if (throwable != null) {

					ReportManager.logFail(throwable.toString());

				} else {

					ReportManager.logFail("Test failed");
				}

				/*
				 * Capture screenshot only if Playwright page exists.
				 */
				try {

					if (BrowserFactory.getPage() != null) {

						ReportManager.logScreenshot();

					}

				} catch (Exception screenshotException) {

					System.out.println("Screenshot capture failed: " + screenshotException.getMessage());
				}

			} else {

				System.out.println("ExtentTest is NULL in onTestFailure. " + "Skipping Extent failure logging.");
			}

		} catch (Exception e) {

			System.out.println("Error while processing test failure: " + e.getMessage());

		}

		/*
		 * Stop video independently.
		 */
		try {

			// VideoRecord.stopRecord();

		} catch (Exception e) {

			System.out.println("Unable to stop video recording: " + e.getMessage());
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
		 * The Cucumber lifecycle should control the ExtentTest.
		 */
	}

	// ============================================================
	// TEST SKIPPED
	// ============================================================

	@Override
	public void onTestSkipped(ITestResult iTestResult) {

		System.out.println("+++++++++++++++++++++ onTestSkipped +++++++++++++++++++++");

		try {

			if (ReportManager.getCurrentTest() != null) {

				String message = iTestResult.getMethod().getMethodName() + " is skipped.";

				ReportManager.logInfo(message);

			} else {

				System.out.println("ExtentTest is NULL for skipped test.");
			}

		} catch (Exception e) {

			System.out.println("Error while processing skipped test: " + e.getMessage());
		}
	}

	// ============================================================
	// SUITE FINISH
	// ============================================================

	@Override
	public void onFinish(ITestContext iTestContext) {

		System.out.println("+++++++++++++++++++++ PageEvent onFinish +++++++++++++++++++++");
	}

	// ============================================================
	// SUITE START
	// ============================================================

	@Override
	public void onStart(ITestContext iTestContext) {

		System.out.println("+++++++++++++++++++++ PageEvent onStart +++++++++++++++++++++");
	}

	// ============================================================
	// FAILED BUT WITHIN SUCCESS PERCENTAGE
	// ============================================================

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {

		System.out.println(
				"+++++++++++++++++++++ " + "onTestFailedButWithinSuccessPercentage " + "+++++++++++++++++++++");
	}

}
