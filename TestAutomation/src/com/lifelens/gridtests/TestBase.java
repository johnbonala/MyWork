package com.lifelens.gridtests;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class TestBase {

	protected ThreadLocal<WebDriver> threadDriver = null;

	@BeforeMethod
	public void setUp() throws MalformedURLException {

		threadDriver = new ThreadLocal<WebDriver>();
		DesiredCapabilities dc = new DesiredCapabilities();
		FirefoxProfile fp = new FirefoxProfile();
		dc.setCapability(FirefoxDriver.PROFILE, fp);
		dc.setBrowserName(DesiredCapabilities.firefox().getBrowserName());
		threadDriver.set(new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), dc));
	}

	public WebDriver getDriver() {
		return threadDriver.get();
	}

	@AfterMethod
	public void closeBrowser() {
		getDriver().quit();

	}
}