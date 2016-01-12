package com.lifelens.webelements;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import com.lifelens.exceptions.ExceptionErrorCodes;
import com.lifelens.exceptions.TestAutomationException;
import com.lifelens.globals.Global;
import com.lifelens.globals.GlobalCSSIdentifiers;
import com.lifelens.globals.GlobalCSSSelectors;
import com.lifelens.interpreter.ExtractInstructions;
import com.lifelens.interpreter.TestContext;

public class FSTIframe {
	public WebDriver browser;
	private TestContext testContext;

	private String iframeTitle;
	private String FSTIframeTitle = "FUND SELECTION TOOL";
	private String impersonateTitle = "IMPERSONATION";

	private static Logger logger = Logger.getLogger(FSTFundTable.class.getName());

	/**
	 * @param browser
	 *            browser object
	 * @param testContext
	 *            browser testContextS
	 * @param iframeTitle
	 *            iframeTitle
	 * @throws TestAutomationException
	 */
	public FSTIframe(WebDriver browser, TestContext testContext, String iframeTitle) throws TestAutomationException {
		this.browser = browser;
		this.testContext = testContext;
		this.iframeTitle = iframeTitle;
		validateFrameTitle();
	}

	public FSTIframe(WebDriver browser) throws TestAutomationException {
		this.browser = browser;
	}

	private boolean validateFrameTitle() throws TestAutomationException {

		if (!(FSTIframeTitle.equalsIgnoreCase(iframeTitle) || impersonateTitle.equalsIgnoreCase(iframeTitle))) {
			throw new TestAutomationException(ExceptionErrorCodes.TA079_NOT_A_VALID_IFRAME_ERROR,
					"Test Autoamtion doesn't support the iframe with title " + iframeTitle);
		} else
			return true;
	}

	public boolean switchToFSTIframe(String action) throws TestAutomationException {
		logger.debug("Start of FSTIframe: SwitchToFSTIframe(action) ");

		if (action.equalsIgnoreCase("TRUE")) {
			if (!ExtractInstructions.controlInIframe) {
				SwitchToFSTIframe();
				return true;
			} else {
				// If user want to move to different
				SwitchToFSTIframe();
				logger.debug("Control is alreday inside the FST iframe. ");
				return true;
			}

		} else if (action.equalsIgnoreCase("FALSE")) {
			if (ExtractInstructions.controlInIframe) {
				SwitchToDefault();
				return true;
			} else {
				logger.debug("Control is NOT inside the FST iframe. " + "Hence switch iframe action is cancelled ");
			}
		} else if (action.isEmpty()) {
			if (!ExtractInstructions.controlInIframe) {
				SwitchToFSTIframe();
				return true;
			} else if (ExtractInstructions.controlInIframe) {
				SwitchToDefault();
				return true;
			} else {
				logger.debug("Control is Neither inside the FST iframe "
						+ " not in lifelens. Hence  switch iframe action cancelled ");
			}

		} else {
			logger.error("Action : " + action + " is not a valid parameter for SWITCH_IFRAME. ");
		}
		logger.debug("End of FSTIframe: SwitchToFSTIframe(action) ");
		return false;
	}

	private boolean SwitchToFSTIframe() throws TestAutomationException {
		logger.debug("Start of FSTIframe: SwitchToFSTIframe ");

		// If iframeTitle is null switch to first window
		if (iframeTitle == null || iframeTitle.equalsIgnoreCase(FSTIframeTitle)) {
			browser.switchTo()
					.frame(WebElementRetriever.getWebElementByClassName(browser, "fundsTable",
							GlobalCSSIdentifiers.TOOLFRAME)).manage().window().maximize();
		} else if (iframeTitle.equalsIgnoreCase(impersonateTitle)) {
			browser.switchTo()
					.frame(WebElementRetriever.getWebElementByCSSSelector(browser, "impersonation",
							GlobalCSSSelectors.IMPERSONATION)).manage().window().maximize();
			testContext.setIsImpersonateScreen(true);
		}
		ExtractInstructions.loadIndicatorCSS = "";
		ExtractInstructions.controlInIframe = true;
		logger.debug("End of FSTIframe: SwitchToFSTIframe ");
		return true;
	}

	private boolean SwitchToDefault() {
		logger.debug("Start of FSTIframe: SwitchToDefault ");
		browser.switchTo().defaultContent();
		ExtractInstructions.loadIndicatorCSS = Global.loadIndicatorCSS;
		ExtractInstructions.controlInIframe = false;
		logger.debug("End of FSTIframe: SwitchToDefault ");
		return true;
	}

	public boolean hasIframe() {
		int iframeCount = 0;

		try {
			iframeCount = WebElementRetriever.getWebElementsByClassName(browser, "Iframe Check",
					GlobalCSSIdentifiers.TOOLFRAME).size();
		} catch (TestAutomationException e) {
			e.printStackTrace();
		}
		return (iframeCount > 0);
	}
}
