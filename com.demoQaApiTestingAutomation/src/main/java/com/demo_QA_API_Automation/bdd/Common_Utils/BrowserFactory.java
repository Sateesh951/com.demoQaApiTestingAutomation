package com.demo_QA_API_Automation.bdd.Common_Utils;

import java.awt.Dimension;
import java.awt.Toolkit;

import com.demo_QA_API_Automation.bdd.Report_utill.ReportManager;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.BrowserType.LaunchOptions;

public class BrowserFactory {

	static ThreadLocal<Playwright> tlPlaywright = new ThreadLocal<>();
	static ThreadLocal<Browser> tlBrowser = new ThreadLocal<>();
	static ThreadLocal<BrowserContext> tlBrowserContext = new ThreadLocal<>();
	static ThreadLocal<Page> tlPage = new ThreadLocal<>();

	public static BrowserFactory instance = null;
	public static Playwright playwright;
	public static Browser browse;

	private BrowserFactory() {

	}

	public static BrowserFactory getInstance() {

		if (instance == null) {
			instance = new BrowserFactory();
		}
		return instance;

	}

	public static Playwright getPlaywright() {
		return tlPlaywright.get();
	}

	public static Browser getBrowser() {
		return tlBrowser.get();
	}

	public static BrowserContext getBrowserContext() {
		return tlBrowserContext.get();
	}

	public static Page getPage() {
		return tlPage.get();
	}

	public void setBrowser(String Browser) {

		// playwright = Playwright.create();
		tlPlaywright.set(Playwright.create());

		switch (Browser.toLowerCase()) {
		case "chromium":
			tlBrowser.set(getPlaywright().chromium().launch(new BrowserType.LaunchOptions().setHeadless(false)));
			break;
		case "firefox":
			tlBrowser.set(getPlaywright().firefox().launch(new BrowserType.LaunchOptions().setHeadless(false)));
			break;
		case "safari":
			tlBrowser.set(getPlaywright().webkit().launch(new BrowserType.LaunchOptions().setHeadless(false)));
			break;
		case "chrome":
			tlBrowser.set(
					getPlaywright().chromium().launch(new LaunchOptions().setChannel("chrome").setHeadless(false)));
			break;
		case "edge":
			tlBrowser.set(
					getPlaywright().chromium().launch(new LaunchOptions().setChannel("msedge").setHeadless(false)));
			break;

		default:
			System.out.println("please pass the right browser name......");
			break;
		}

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int height = (int) screenSize.height;
		int width = (int) screenSize.width;
		System.out.println("Window size detected : " + width + " * " + height);
		ReportManager.logInfo("Window size detected : " + width + " * " + height);

		tlBrowserContext.set(getBrowser().newContext(new Browser.NewContextOptions().setViewportSize(width, height)));
		tlPage.set(getBrowserContext().newPage());

	}

}
