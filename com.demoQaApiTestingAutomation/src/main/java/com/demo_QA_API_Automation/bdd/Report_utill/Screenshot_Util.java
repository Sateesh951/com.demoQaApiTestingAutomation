package com.demo_QA_API_Automation.bdd.Report_utill;

import java.nio.file.Paths;
import java.util.Base64;

import com.demo_QA_API_Automation.bdd.Common_Utils.BrowserFactory;
import com.microsoft.playwright.Page;

public class Screenshot_Util {

	public static String takeScreenshot() {
		String path = System.getProperty("user.dir") + "/screenshot/" + System.currentTimeMillis() + ".png";
		// getPage().screenshot(new
		// Page.ScreenshotOptions().setPath(Paths.get(path)).setFullPage(true));

		byte[] buffer = BrowserFactory.getInstance().getPage()
				.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(path)).setFullPage(true));
		String base64Path = Base64.getEncoder().encodeToString(buffer);

		return base64Path;
	}
}
