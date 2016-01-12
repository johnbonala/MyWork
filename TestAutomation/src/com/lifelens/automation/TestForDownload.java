package com.lifelens.automation;

import org.junit.Test;
import org.openqa.selenium.WebDriver;

import com.lifelens.browsers.EBrowser;
import com.lifelens.browsers.WebElementProcessor;
import com.lifelens.browsers.WebElementProcessorCommon;
import com.lifelens.exceptions.TestAutomationException;
import com.lifelens.globals.Global;
import com.lifelens.interpreter.TestContext;
import com.lifelens.testset.TestSet;

public class TestForDownload {

	@Test
	public void TestForDownloadButton() throws InterruptedException, TestAutomationException {
		// String ajaxXPath =
		// "//div[@id='loadIndicator' and (contains(concat(' ', @style, ' '), ' display: none; ') or contains(concat(' ', @style, ' '), ' display:none '))]";
		String ajaxCSS = Global.loadIndicatorCSS;
		WebDriver webDriver = EBrowser.valueOf(Global.getBrowser()).newInstance();
		WebElementProcessor browser = new WebElementProcessor(webDriver, new TestContext(new TestSet()));
		browser.geturl("https://test5vm.vebnet.com/ReFlexWeb/DnslVtQV.wTaUsI4h/public/page/login");
		browser.input("Username", "superuser");
		browser.input("Password", "muppet");
		browser.clickOnButton("Login", "1");
		browser.navigateTo("Admin > Provider Reports", "1");
		try {
			WebElementProcessorCommon.waitForWebProcessing(webDriver, 30, ajaxCSS, "");
			browser.selectDropdownValue("Reports*", "GFRP GSIPP Joiners");
			WebElementProcessorCommon.waitForWebProcessing(webDriver, 30, ajaxCSS, "");
			browser.input("Effective as of date*", "25/04/2014");
			WebElementProcessorCommon.waitForWebProcessing(webDriver, 30, ajaxCSS, "");
			browser.input("Process as of from date*", "12/04/2014");
			WebElementProcessorCommon.waitForWebProcessing(webDriver, 30, ajaxCSS, "");
			browser.selectDropdownValue("Benefit*", "GFRP1 - Option Based");
			WebElementProcessorCommon.waitForWebProcessing(webDriver, 30, ajaxCSS, "");
			browser.clickOnButton("Execute", "1");
			WebElementProcessorCommon.waitForWebProcessing(webDriver, 30, ajaxCSS, "");
			try {
				Thread.sleep(10000);
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
			browser.clickOnButton("Download...", "1");
		} catch (TestAutomationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}