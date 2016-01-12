package com.lifelens.sample;

import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.lifelens.browsers.WebElementProcessor;
import com.lifelens.interpreter.TestContext;
import com.lifelens.testset.TestSet;

//Author : Srinivas Pasupulati
//Modified on 21 Nov 2013

public class PostJoin_UserDefined {
	public WebDriver currentPage;
	private String baseUrl;
	public String Filelocation;
	private StringBuffer verificationErrors = new StringBuffer();

	@Before
	public void setUp() throws Exception {

		// WebDriver currentPage = null;
		String browser = "FIREFOX";
		String dir = System.getProperty("user.dir");
		try {

			if (browser.equalsIgnoreCase("FIREFOX")) {
				currentPage = new FirefoxDriver();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Filelocation = "F:\\Srini\\Selenium\\dump\\";
		baseUrl = "https://test5vm.vebnet.com/ReFlexWeb/RC32_DnslVtQV.wTaUsI4h/public/page/login";
		currentPage.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
	}

	@Test
	public void testChangePassword() throws Exception {
		currentPage.navigate().to(baseUrl);
		WebElementProcessor browser = new WebElementProcessor(currentPage, new TestContext(new TestSet()));
		browser.input("Username", "899154");
		browser.input("Password", "Pa55w0rd");
		browser.clickOnButton("Login", "1");
		Thread.sleep(1000);
		browser.clickOnNavigationLink("Your pension", "1");
		browser.takeScreenshot("YourPensionPage", Filelocation);
		browser.clickOnButton("Manage your contributions", "1");
		Thread.sleep(2000);
		browser.takeScreenshot("PaymentysPage", Filelocation);
		Thread.sleep(2000);
		browser.clickOnButton("Complete changes", "1");
		Thread.sleep(1000);
		browser.takeScreenshot("ChangeWizardStep1", Filelocation);
	}

	@After
	public void tearDown() throws Exception {
		currentPage.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}
}
