package com.lifelens.webelements;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

import com.lifelens.browsers.WebElementProcessor;
import com.lifelens.browsers.WebElementProcessorCommon;
import com.lifelens.exceptions.ExceptionErrorCodes;
import com.lifelens.exceptions.TestAutomationException;
import com.lifelens.globals.Global;
import com.lifelens.globals.GlobalCSSIdentifiers;
import com.lifelens.globals.GlobalTags;
import com.lifelens.htmlparser.HtmlParser;
import com.lifelens.interpreter.TestContext;

public class TopNavigation extends RemoteWebElement {
	private static Logger logger = Logger.getLogger(TopNavigation.class.getName());
	private String navigationPath;
	private String[] tokens;
	public WebDriver browser;
	private WebElement navigationElement;
	private WebElementProcessor processor;
	private String index;
	private TestContext testContext;

	public TopNavigation(WebDriver browser, TestContext testContext, String navigationPath, String index) {
		this.browser = browser;
		this.navigationPath = navigationPath;
		this.index = index;
		this.testContext = testContext;
		processor = new WebElementProcessor(browser, testContext);
	}

	public boolean navigate() throws TestAutomationException {
		if (testContext.getLoadIndicator()) {
			WebElementProcessorCommon.waitForWebProcessing(browser, 30, Global.loadIndicatorCSS,
					Global.lazyLoadIndicatorClass);
		}
		logger.debug("Start of TopNavigation: navigate");
		tokens = getTokens(navigationPath);

		boolean isNavigated = false;
		if (tokens.length != 0) {
			List<WebElement> navs = WebElementRetriever.getWebElementsByClassName(browser, navigationPath,
					GlobalCSSIdentifiers.TOPLINKS);
			Iterator<WebElement> topel = navs.iterator();
			while (topel.hasNext()) {
				navigationElement = topel.next();
				String label = navigationElement.getText();
				if (label.equalsIgnoreCase(tokens[0])) {
					WebElement innterNavigation = WebElementRetriever.getWebElementByTagName(navigationElement,
							navigationPath, GlobalTags.A);
					innterNavigation.click();
					if (tokens.length == 1) {
						isNavigated = true;
					} else {
						isNavigated = new Hyperlink(navigationElement, tokens[1], index, testContext)
								.clickOnNavigationLinkByTagName(GlobalTags.A);
						try {
							// wait for OK button to appear if appeared click on
							// button
							browser.manage().timeouts().implicitlyWait(Global.TIMEOUT_SHORT, TimeUnit.SECONDS);
							boolean verify = processor.verifyButton("OK", " ");
							if (verify) {
								processor.clickOnButton("OK", "1");
							}
						} catch (Exception e) {

						} finally {
							// back to the default timeout
							browser.manage().timeouts().implicitlyWait(Global.timeout, TimeUnit.SECONDS);
						}
					}
					break;
				}
			}
		} else {
			throw new TestAutomationException(ExceptionErrorCodes.TA023_ARG_LIST_INVALID_INPUT_ARGUMENTS_ERROR,
					"Navigation path " + navigationPath + " is blank or Invalid");
		}

		WebElementProcessorCommon.logWebElementStatus("Navigator", navigationPath, isNavigated);
		logger.debug("End of TopNavigation: navigate");
		return isNavigated;
	}

	private String[] getTokens(String navigationPath) {
		logger.debug("Start of TopNavigation: getTokens");
		String delimters = ">";
		String[] tokens = navigationPath.split(delimters);

		for (int i = 0; i < tokens.length; i++) {
			tokens[i] = tokens[i].trim();
		}
		logger.debug("End of TopNavigation: getTokens");
		return tokens;
	}

	public String getSubNavNameFromHTML(WebElement subNav) {

		/**
		 * String subNavNameFromHTML; subNavNameFromHTML =
		 * subNav.getAttribute("innerHTML"); subNavNameFromHTML =
		 * subNavNameFromHTML.replace("<span>", ""); subNavNameFromHTML =
		 * subNavNameFromHTML.replace("</span>", ""); subNavNameFromHTML =
		 * subNavNameFromHTML.trim(); return subNavNameFromHTML;
		 **/
		return new HtmlParser(subNav).getinnerText();
	}
}
