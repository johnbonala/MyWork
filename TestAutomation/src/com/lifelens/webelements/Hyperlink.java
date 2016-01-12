package com.lifelens.webelements;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

import com.lifelens.browsers.WebElementProcessorCommon;
import com.lifelens.exceptions.ExceptionErrorCodes;
import com.lifelens.exceptions.TestAutomationException;
import com.lifelens.globals.Global;
import com.lifelens.globals.GlobalAttributes;
import com.lifelens.globals.GlobalCSSIdentifiers;
import com.lifelens.htmlparser.HtmlParser;
import com.lifelens.interpreter.ExtractInstructions;
import com.lifelens.interpreter.TestContext;

/**
 * It represents the Hyper link object in lifelens
 * 
 * This helps to identify the hyper link object and click on it
 * 
 * @author Srinivas Pasupulati (co48633)
 * 
 * @version 1.0
 * 
 * @since 28.05.2014
 * 
 */

public class Hyperlink extends RemoteWebElement {

	private static Logger logger = Logger.getLogger(Hyperlink.class.getName());
	private String hyperLinkName;
	private WebDriver browser;
	private WebElement navElement;
	private int index;
	private TestContext testContext;
	int counter = 1;

	// private String ajaxXPath =
	// "//div[@id='loadIndicator' and (contains(concat(' ', @style, ' '), ' display: none; ') or contains(concat(' ', @style, ' '), ' display:none '))]";

	public Hyperlink(WebDriver browser, String hyperLinkName, String index, TestContext testContext) {
		this.hyperLinkName = hyperLinkName;
		this.browser = browser;
		this.testContext = testContext;
		this.index = Integer.parseInt(index);
	}

	public Hyperlink(WebElement navElement, String hyperLinkName, String index, TestContext testContext) {
		this.hyperLinkName = hyperLinkName;
		this.navElement = navElement;
		this.testContext = testContext;
		this.index = Integer.parseInt(index);
	}

	/**
	 * This is to identify the hyperlink and click on it
	 * 
	 * @return
	 * @throws TestAutomationException
	 */
	public boolean clickOnlinkByCSS(String CSSValue) throws TestAutomationException {

		if (!ExtractInstructions.controlInIframe && testContext.getLoadIndicator())

			// If control is in a page where load indicator does not exists then
			// do not wait
			if (ExtractInstructions.isLoadIndicatorExists()) {
				WebElementProcessorCommon.waitForWebProcessing(browser, Global.getTimeout(), Global.loadIndicatorCSS,
						"");
			} else {
				testContext.setLoadIndicator(false);
			}

		return clickOnLink(WebElementRetriever.getWebElementsByCSSSelector(browser, hyperLinkName, CSSValue));

	}

	/**
	 * This is to identify the hyperlink and click on it
	 * 
	 * @return
	 * @throws TestAutomationException
	 */
	public boolean clickOnlinkByStyleClass(String styleClass) throws TestAutomationException {
		return clickOnLink(WebElementRetriever.getWebElementsByClassName(browser, hyperLinkName, styleClass));
	}

	/**
	 * This is to identify the hyperlink and click on it
	 * 
	 * @return
	 */
	public boolean clickOnNavigationLinkByTagName(String tagName) throws TestAutomationException {
		return clickOnLink(WebElementRetriever.getWebElementsByTagName(navElement, hyperLinkName, tagName));

	}

	private boolean clickOnLink(List<WebElement> navlinksElements) throws TestAutomationException {
		logger.debug("Start of Hyperlink: clickOnLink");
		Iterator<WebElement> navlinks = navlinksElements.iterator();
		boolean linkFound = false;

		while (navlinks.hasNext()) {
			WebElement navigationElment = navlinks.next();
			if (getlinkVisibleName(navigationElment).equalsIgnoreCase(hyperLinkName)) {
				if (counter != index) {
					counter++;
				} else {

					navigationElment.click();
					linkFound = true;
					break;
				}
			} else if (hyperLinkName.equalsIgnoreCase(GlobalCSSIdentifiers.CLOSEIFRAME)) {
				WebElement closeFrame = WebElementRetriever.getWebElementByClassName(browser, "close frame",
						GlobalCSSIdentifiers.FRAMECLOSE);
				closeFrame.click();
				linkFound = true;
				break;
			} else {
				// Below peace of code replaces the "&nbsp" with empty string
				// and compares the link name which is having no empty space
				String webHyperLinkName = getlinkVisibleName(navigationElment);
				webHyperLinkName = webHyperLinkName.replaceAll("\\u00A0", "");
				String hyperLinkNameWithNoEmptySpace = hyperLinkName.replaceAll(" ", "");
				if (webHyperLinkName.equalsIgnoreCase(hyperLinkNameWithNoEmptySpace)) {
					if (counter != index) {
						counter++;
					} else {
						navigationElment.click();
						linkFound = true;
						break;
					}
				}
			}
		}
		WebElementProcessorCommon.logWebElementStatus("Hyper Link", hyperLinkName, linkFound);
		// Handling LL&CSOL alert boxes
		// click's on "Leave Page" button on the alert box after clicking
		// hyperLink
		try {
			if (browser.switchTo().alert().getText() != null)
				ExtractInstructions.checkAlert();
		} catch (Exception exp) {
			logger.info("Problem occuered while finding the alert box : " + exp.getMessage());
		}

		if (!linkFound) {
			throw new TestAutomationException(
					ExceptionErrorCodes.TA010_WEB_ELEMENT_RETRIEVER_ELEMENTS_NOT_FOUND_BY_TAG_NAME_ERROR,
					"Link with name " + hyperLinkName + " not found.");
		}
		logger.debug("End of Hyperlink: clickOnLink");
		return linkFound;
	}

	private String getHyperLinkName() {
		return hyperLinkName;
	}

	private void setHyperLinkName(String hyperLinkName) {
		this.hyperLinkName = hyperLinkName;
	}

	private String getlinkVisibleName(WebElement navLink) {
		HtmlParser html = new HtmlParser(navLink);
		String linkVisibleName = html.getinnerText().trim();
		if (linkVisibleName.isEmpty())
			if (html.hasAttribute(GlobalAttributes.VALUE))
				linkVisibleName = new InnerElement(navLink).getAttribute(GlobalAttributes.VALUE).trim();

		return linkVisibleName;
	}

}
