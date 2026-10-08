package com.demo_QA_API_Automation.bdd.RunnerClass;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.demo_QA_API_Automation.bdd.NG_Listeners.PageEvent;
import com.demo_QA_API_Automation.bdd.NG_Listeners.SuiteEvent;
import com.demo_QA_API_Automation.bdd.Report_utill.ReportManager;

import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.FeatureWrapper;
import io.cucumber.testng.PickleWrapper;
import io.cucumber.testng.TestNGCucumberRunner;

@Listeners({ SuiteEvent.class, PageEvent.class })

@CucumberOptions(

		features = "src/test/java/com/demo_QA_API_Automation/bdd/feature/Post_BookStore_Books.feature",

		glue = { "com.demo_QA_API_Automation.bdd.hooks", "com.demo_QA_API_Automation.bdd.stepDefinitions" },

		tags = "@PostPositive",

		plugin = {

				"pretty",

				"html:target/site/cucumber-pretty/cucumber.html",

				"json:target/cucumber/cucumber.json",

				"rerun:target/rerun.txt" },

		monochrome = true,

		publish = true,

		dryRun = false)
public class APITestRunner {

	private TestNGCucumberRunner testNGCucumberRunner;

	// =========================================================
	// BEFORE CLASS
	// =========================================================

	@BeforeClass(alwaysRun = true)
	public void setUpClass() {

		System.out.println();
		System.out.println("================================================");

		System.out.println("              API TEST RUNNER");

		System.out.println("================================================");

		// -----------------------------------------------------
		// Initialize API Extent Report
		// -----------------------------------------------------

		ReportManager.startApiReport();

		// -----------------------------------------------------
		// Initialize Cucumber
		// -----------------------------------------------------

		testNGCucumberRunner = new TestNGCucumberRunner(this.getClass());

		System.out.println("Cucumber TestNG Runner initialized.");
	}

	// =========================================================
	// SCENARIO DATA PROVIDER
	// =========================================================

	@DataProvider(name = "scenarios", parallel = false)
	public Object[][] scenarios() {

		System.out.println("+++++++++++++++++++++ @DataProvider +++++++++++++++++++++");

		if (testNGCucumberRunner == null) {

			testNGCucumberRunner = new TestNGCucumberRunner(this.getClass());
		}

		return testNGCucumberRunner.provideScenarios();
	}

	// =========================================================
	// RUN SCENARIO
	// =========================================================

	@Test(groups = "cucumber", description = "Run Cucumber API Scenario", dataProvider = "scenarios")
	public void scenario(PickleWrapper pickleEventWrapper, FeatureWrapper cucumberFeatureWrapper) throws Throwable {

		System.out.println();
		System.out.println("+++++++++++++++++++++ RUN SCENARIO +++++++++++++++++++++");

		testNGCucumberRunner.runScenario(pickleEventWrapper.getPickle());
	}

	// =========================================================
	// AFTER CLASS
	// =========================================================

	@AfterClass(alwaysRun = true)
	public void tearDownClass() {

		System.out.println();
		System.out.println("+++++++++++++++++++++ @AfterClass +++++++++++++++++++++");

		if (testNGCucumberRunner != null) {

			testNGCucumberRunner.finish();

			testNGCucumberRunner = null;
		}

		// -----------------------------------------------------
		// IMPORTANT:
		// Flush Extent API Report
		// -----------------------------------------------------

		ReportManager.endAPIReport();

		System.out.println("API Extent Report generated successfully.");
	}
}