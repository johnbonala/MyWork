package com.lifelens.webelements.v3;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.lifelens.exceptions.TestAutomationException;
import com.lifelens.globals.GlobalCSSSelectors;
import com.lifelens.interpreter.TestContext;
import com.lifelens.webelements.Table;
import com.lifelens.webelements.WebElementRetriever;

public class ColumnCustomisation {

	private static Logger logger = Logger.getLogger(ColumnCustomisation.class.getName());
	public WebDriver browser;
	private String defaultHeader;
	private TestContext testContext;

	public ColumnCustomisation(WebDriver browser, TestContext testContext, String defaultHeader) {
		this.browser = browser;
		this.defaultHeader = defaultHeader;
		this.testContext = testContext;
	}

	public boolean clickColumnCustomisationEditButton() throws TestAutomationException {
		logger.debug("Start of ColumnCustomisation: clickColumnCustomisationEditButton ");
		boolean result = false;

		Table table = new Table(browser, testContext, 1);
		WebElement row = table.getMatchingEditRow(defaultHeader, 1);

		WebElement editButton = WebElementRetriever.getWebElementByCSSSelector(row, "defaultHeader",
				GlobalCSSSelectors.MULTIPLEEDITBUTTON);

		if (editButton != null) {
			editButton.click();
			result = true;
		}

		return result;
	}

}
