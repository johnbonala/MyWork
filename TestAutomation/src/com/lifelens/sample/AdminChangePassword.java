package com.lifelens.sample;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.openqa.selenium.WebDriver;

import com.lifelens.browsers.EBrowser;
import com.lifelens.browsers.WebElementProcessor;
import com.lifelens.browsers.WebElementProcessorCommon;
import com.lifelens.globals.Global;
import com.lifelens.interpreter.TestContext;
import com.lifelens.testset.TestSet;
import com.lifelens.webelements.AdminPanel;

public class AdminChangePassword {
	private String baseUrl;

	@Test
	public void testChangePassword() throws Exception {

		String loadIndicatorCSS = Global.loadIndicatorCSS;
		// String ajaxXPath =
		// "//div[@id='loadIndicator' and (contains(concat(' ', @style, ' '), ' display: none; ') or contains(concat(' ', @style, ' '), ' display:none '))]";
		WebDriver webDriver = EBrowser.valueOf(Global.getBrowser()).newInstance();
		WebElementProcessor browser = new WebElementProcessor(webDriver, new TestContext(new TestSet()));
		baseUrl = "https://test5vm.vebnet.com/ReFlexWeb/RC32_DnslVtQV.wTaUsI4h/public/page/login";
		webDriver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		webDriver.navigate().to(baseUrl);
		browser.input("Username", "CO48633");
		browser.input("Password", "Pa55w0rd");
		browser.clickOnButton("Login", "1");
		browser.navigateTo("Admin > Manage Employee", "1");
		browser.clickOnButton("Search", "1");
		browser.input("Staff number", "412113");
		WebElementProcessorCommon.waitForWebProcessing(webDriver, 30, loadIndicatorCSS, "");
		browser.selectDropdownValue("Action to perform*", "Modify employee");
		browser.clickOnButton("Go", "1");
		WebElementProcessorCommon.waitForWebProcessing(webDriver, 35, loadIndicatorCSS, "");
		browser.input("Password", "Pa55word");
		browser.clickOnButton("Save", "1");
		WebElementProcessorCommon.waitForWebProcessing(webDriver, 30, loadIndicatorCSS, "");
		browser.selectRadioButton("Bypass adjustment processing");
		WebElementProcessorCommon.waitForWebProcessing(webDriver, 30, loadIndicatorCSS, "");
		browser.clickOnButton("Continue", "1");
		WebElementProcessorCommon.waitForWebProcessing(webDriver, 30, loadIndicatorCSS, "");
		webDriver.quit();
	}

	@Test
	public void testAdminPanel() throws Exception {

		// String ajaxXPath =
		// "//div[@id='loadIndicator' and (contains(concat(' ', @style, ' '), ' display: none; ') or contains(concat(' ', @style, ' '), ' display:none '))]";

		String loadIndicatorCSS = Global.loadIndicatorCSS;

		WebDriver webDriver = EBrowser.valueOf(Global.getBrowser()).newInstance();

		WebElementProcessor browser = new WebElementProcessor(webDriver, new TestContext(new TestSet()));

		baseUrl = "https://test5vm.vebnet.com/ReFlexWeb/DnslVtQV.wTaUsI4h/public/page/login";

		webDriver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

		webDriver.navigate().to(baseUrl);

		browser.input("Username", "co48633");

		browser.input("Password", "Pa55w0rd");

		browser.clickOnButton("Login", "1");

		browser.navigateTo("Setup > Benefit", "1");

		WebElementProcessorCommon.waitForWebProcessing(webDriver, 60, loadIndicatorCSS, "");

		AdminPanel panel = new AdminPanel(webDriver, "Configuration");

		System.out.println(panel.getPanel());

		System.out.println(panel.getPanelText());

		panel.clickOnEditButton();

		System.out.println(panel.isPanelClosed());

		System.out.println(panel.isPanelEditable());
		panel.clickOnEditButton();

		panel.collapsePanel();
		// webDriver.quit();

	}
}
