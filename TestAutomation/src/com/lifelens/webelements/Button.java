package com.lifelens.webelements;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.lifelens.browsers.FileProcessor;
import com.lifelens.browsers.WebElementProcessorCommon;
import com.lifelens.exceptions.ExceptionErrorCodes;
import com.lifelens.exceptions.TestAutomationException;
import com.lifelens.globals.Global;
import com.lifelens.globals.GlobalAttributes;
import com.lifelens.globals.GlobalCSSIdentifiers;
import com.lifelens.globals.GlobalCSSSelectors;
import com.lifelens.globals.GlobalTags;
import com.lifelens.htmlparser.HtmlParser;
import com.lifelens.interpreter.TestContext;

public class Button extends RemoteWebElement {

	private static Logger logger = Logger.getLogger(Button.class.getName());
	private String buttonName;
	public WebElement button;
	private int index;
	private int buttonHitCount = 1;
	public WebDriver browser;
	private String[] buttonIdentifiers = { GlobalCSSSelectors.BUTTON, GlobalCSSSelectors.PRIMARYBUTTON,
			GlobalCSSSelectors.SECONDARYBUTTON, GlobalCSSSelectors.CSOLPRIMARYBUTTON,
			GlobalCSSSelectors.CSOLSECONDARYBUTTON };

	private String[] FSTButtonIdentifiers = { GlobalCSSSelectors.CMSPRIMARYBUTTON,
			GlobalCSSSelectors.CMSSECONDARYBUTTON };
	private TestContext testContext;

	public Button(WebDriver browser, TestContext testContext, String buttonName, String index) {
		this.buttonName = buttonName;
		this.browser = browser;
		this.testContext = testContext;
		this.index = Integer.parseInt(index);
	}

	/**
	 * @return the testContext
	 */
	public TestContext getTestContext() {
		return testContext;
	}

	public boolean clickOnButton() throws TestAutomationException {
		logger.debug("Start of Button: clickOnButton ");

		try {
			browser.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
			WebElement buttonElement = null, decisionTableDialog = null;

			if (buttonName.equalsIgnoreCase(GlobalCSSIdentifiers.CLOSE)) {
				buttonElement = WebElementRetriever.getWebElementByClassName(browser, buttonName,
						GlobalCSSIdentifiers.DIGITDIALOGCLOSEICON);
			}

			if (testContext.getLoadIndicator()) {
				decisionTableDialog = browser.findElement(By.id(GlobalCSSIdentifiers.DIALOG));
			}

			// getting the button elements of the decision table
			if (decisionTableDialog != null && decisionTableDialog.isDisplayed()) {
				// Get the 'Save' button from decision table element if
				// not found in decision table dialog find 'Save' from browser
				if (buttonName.equalsIgnoreCase(GlobalCSSIdentifiers.SAVE)) {
					try {
						buttonElement = WebElementRetriever.getWebElementByClassName(decisionTableDialog, buttonName,
								GlobalCSSIdentifiers.SAVEBUTTON);
					} catch (Exception e) {
						buttonElement = getButtonWebElement("True");
					}
				}

				// Get the 'Cancel' button from decision table element if not
				// found
				// find 'Cancel' from browser
				else if (buttonName.equalsIgnoreCase(GlobalCSSIdentifiers.CANCEL)) {
					try {
						buttonElement = WebElementRetriever.getWebElementByClassName(decisionTableDialog, buttonName,
								GlobalCSSIdentifiers.CANCEL);
					} catch (Exception e) {
						buttonElement = getButtonWebElement("True");
					}
				}

				// if button not found in decision table get button from browser
				if (buttonElement == null) {
					buttonElement = getButtonWebElement("True");
				}

			} else {
				buttonElement = getButtonWebElement("True");

			}

			boolean isButtonFound = false;
			if (buttonElement != null) {
				String buttonName = buttonElement.getAttribute(GlobalAttributes.NAME);
				buttonElement.click();

				if (testContext.getLoadIndicator()) {
					WebElementProcessorCommon.waitForWebProcessing(browser, Global.timeout, Global.loadIndicatorCSS,
							Global.lazyLoadIndicatorClass);
				}
				// wait for Execute button in report to be enabled again
				if (StringUtils.isNotBlank(buttonName) && buttonName.equals("reportRunModePanel:runReportButton")) {
					try {
						WebElementProcessorCommon.waitForWebElementTobeEnabled(browser, Global.TIMEOUT_REPORT,
								buttonName);
					} catch (TestAutomationException te) {
						logger.info("Exception caught while waiting for 'Execute' button to appear on screen");
					}
				}

				isButtonFound = true;
			}

			WebElementProcessorCommon.logWebElementStatus("Button", buttonName, isButtonFound);

			logger.debug("End of Button: clickOnButton ");
			return isButtonFound;

		} finally {
			browser.manage().timeouts().implicitlyWait(Global.timeout, TimeUnit.SECONDS);
		}
	}

	public boolean clickOnFSTButton() throws TestAutomationException {
		logger.debug("Start of Button: clickOnFSTButton ");
		WebElement buttonElement = getFSTButtonWebElement("True");
		WebElementProcessorCommon.scrollToWebElement(browser, buttonElement);
		boolean isButtonFound = false;
		if (buttonElement != null) {
			clickOnFSTButton(buttonElement);
			isButtonFound = true;
		}

		// Below peace of code click on "Leave Page" button appeared on the
		// alert box after clicking on "Confirm" button
		if (buttonName.equalsIgnoreCase("Confirm")) {
			try {
				browser.switchTo().alert().accept();
			} catch (Exception e) {
				logger.info("Exception caught while looking for alert message after clicking on FST 'Confirm' button : "
						+ e.getMessage());
			}
		}
		WebElementProcessorCommon.logWebElementStatus("FST Button", buttonName, isButtonFound);
		logger.debug("End of Button: clickOnFSTButton ");
		return isButtonFound;
	}

	private WebElement getButtonWebElement(String existsIndicator) throws TestAutomationException {
		logger.debug("Start of Button: getButtonWebElement ");
		List<WebElement> buttonTaglist;

		for (String buttonIdentifier : buttonIdentifiers) {

			buttonTaglist = WebElementRetriever.getWebElementsByCSSSelector(browser, buttonName, buttonIdentifier);
			if (!buttonTaglist.isEmpty() && buttonFound(buttonTaglist))
				break;
			else
				button = null;

		}

		boolean buttonFound = (button == null) ? false : true;
		WebElementProcessorCommon.logWebElementStatus("Button", buttonName, buttonFound);
		if (!buttonFound && !existsIndicator.equalsIgnoreCase("False")) {
			throw new TestAutomationException(ExceptionErrorCodes.TA015_BUTTON_ELEMENT_NOT_FOUND_BY_BUTTON_NAME_ERROR,
					"Web elements with button name '" + buttonName + "' not found");
		}

		if (buttonName.equalsIgnoreCase(GlobalCSSIdentifiers.ADD)) {
			WebElement columnCutomisationButton = null;

			try {
				// short timeout as empty header or footer is common
				browser.manage().timeouts().implicitlyWait(Global.TIMEOUT_SHORT, TimeUnit.SECONDS);

				columnCutomisationButton = WebElementRetriever.getWebElementByCSSSelector(browser, buttonName,
						GlobalCSSSelectors.CONSTANTCOLUMNADDBUTTON);
			} catch (Exception e) {
				logger.error(e.getMessage());
			} finally {

				// back to the default timeout
				browser.manage().timeouts().implicitlyWait(Global.timeout, TimeUnit.SECONDS);
			}
			if (columnCutomisationButton != null) {
				button = columnCutomisationButton;
			}
		}

		logger.debug("End of Button: getButtonWebElement ");
		return button;
	}

	private WebElement getFSTButtonWebElement(String existsIndicator) throws TestAutomationException {
		logger.debug("Start of Button: getFSTButtonWebElement ");
		List<WebElement> buttonTaglist;

		for (String FSTIdentifier : FSTButtonIdentifiers) {
			buttonTaglist = WebElementRetriever.getWebElementsByCSSSelector(browser, buttonName, FSTIdentifier);

			if (!buttonTaglist.isEmpty() && buttonFound(buttonTaglist))
				break;
		}

		boolean buttonFound = (button == null) ? false : true;
		WebElementProcessorCommon.logWebElementStatus("Button", buttonName, buttonFound);
		if (!buttonFound && !existsIndicator.equalsIgnoreCase("False")) {
			throw new TestAutomationException(
					ExceptionErrorCodes.TA016_FST_BUTTON_ELEMENT_NOT_FOUND_BY_BUTTON_NAME_ERROR,
					"Web elements with button name '" + buttonName + "' not found");
		}
		logger.debug("End of Button: getFSTButtonWebElement ");
		return button;
	}

	private boolean buttonFound(List<WebElement> buttonTaglist) {
		WebElement buttonElement = null;
		logger.debug("Start of Button: buttonFound ");
		Iterator<WebElement> allButtons = buttonTaglist.iterator();
		boolean buttonFound = false;
		while (allButtons.hasNext()) {
			buttonElement = allButtons.next();
			if (buttonElement.isDisplayed()) {
				String weButtonName = getButtonName(buttonElement);
				if (weButtonName.equalsIgnoreCase(buttonName))
					if (buttonHitCount != index)
						buttonHitCount++;
					else {
						buttonFound = true;
						button = buttonElement;
						break;
					}
			} else {
				// wait for button to appear on screen
				browser.manage().timeouts().implicitlyWait(Global.TIMEOUT_SHORT, TimeUnit.SECONDS);
				if (buttonElement.isDisplayed()) {
					String weButtonName = getButtonName(buttonElement);
					if (weButtonName.equalsIgnoreCase(buttonName))
						if (buttonHitCount != index)
							buttonHitCount++;
						else {
							buttonFound = true;
							button = buttonElement;
							break;
						}
				}
			}
		}
		logger.debug("End of Button: buttonFound ");
		return buttonFound;
	}

	private String getButtonName(WebElement buttonElement) {
		logger.debug("Start of Button: getButtonName ");
		HtmlParser innerhtml = new HtmlParser(buttonElement);

		String weButtonName = innerhtml.getinnerText();

		if (weButtonName.equalsIgnoreCase(""))
			weButtonName = innerhtml.getAttributeValue(GlobalTags.BUTTON, GlobalAttributes.TITLE);

		if (weButtonName.equalsIgnoreCase(""))
			weButtonName = buttonElement.getAttribute(GlobalAttributes.VALUE);

		if (weButtonName.equalsIgnoreCase(""))
			weButtonName = innerhtml.getAttributeValue(GlobalTags.INPUT, GlobalAttributes.VALUE);
		logger.debug("End of Button: getButtonName ");
		return weButtonName;

	}

	/**
	 * Verify if the specified Button occurrence exists.
	 * 
	 * @param existsIndicator
	 *            Set to "false" to check that a button does not exist.
	 * @return boolean Success or Failure
	 * @throws TestAutomationException
	 */
	public boolean verifyButton(String existsIndicator, String enabledIndicator) throws TestAutomationException {
		logger.debug("Start of Button: verifyButton ");
		logger.debug("verify Button:" + buttonName);

		boolean rtnValue = false;

		try {
			browser.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			WebElement buttonElement = getButtonWebElement(existsIndicator);

			if (buttonElement == null) {
				if (existsIndicator.equalsIgnoreCase("False")) {
					rtnValue = true;
				}
			} else {
				if (!existsIndicator.equalsIgnoreCase("False")) {
					if (enabledIndicator == null) {
						rtnValue = true;
					} else {
						// buttonElement.isEnabled() always seems to return true
						// even if
						// button is disabled, so use
						// buttonElement.getAttribute("disabled") instead.
						if (enabledIndicator.equalsIgnoreCase("Enabled")
								&& buttonElement.getAttribute("disabled") == null) {
							rtnValue = true;
						}
						if (enabledIndicator.equalsIgnoreCase("Disabled")
								&& buttonElement.getAttribute("disabled").equalsIgnoreCase("true")) {
							rtnValue = true;
						}
					}
				}
			}

			logger.debug("End of Button: verifyButton ");
			return rtnValue;

		} finally {
			browser.manage().timeouts().implicitlyWait(Global.timeout, TimeUnit.SECONDS);
		}
	}

	/**
	 * Verify if the specified FST Button occurrence exists.
	 * 
	 * @param existsIndicator
	 *            Set to "false" to check that a button does not exist.
	 * @return boolean Success or Failure
	 * @throws TestAutomationException
	 */
	public boolean verifyFSTButton(String existsIndicator, String enabledIndicator) throws TestAutomationException {
		logger.debug("Start of FST Button: verifyFSTButton ");
		logger.debug("verify FST Button:" + buttonName);

		boolean rtnValue = false;

		try {
			browser.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			WebElement buttonElement = getFSTButtonWebElement(existsIndicator);

			if (buttonElement == null) {
				if (existsIndicator.equalsIgnoreCase("False")) {
					rtnValue = true;
				}
			} else {
				if (!existsIndicator.equalsIgnoreCase("False")) {
					if (enabledIndicator == null) {
						rtnValue = true;
					} else {
						if (enabledIndicator.equalsIgnoreCase("Enabled")
								&& buttonElement.getAttribute("disabled") == null) {
							rtnValue = true;
						}
						if (enabledIndicator.equalsIgnoreCase("Disabled")
								&& buttonElement.getAttribute("disabled").equalsIgnoreCase("true")) {
							rtnValue = true;
						}
					}
				}
			}

			logger.debug("End of Button: verifyButton ");
			return rtnValue;

		} finally {
			browser.manage().timeouts().implicitlyWait(Global.timeout, TimeUnit.SECONDS);
		}
	}

	/**
	 * Download the file pointed to by the 'Download' button.
	 * 
	 * @param outputFilePath
	 *            Either specify just the filename, a partial file path with
	 *            filename (e.g. "my test files/my file.csv" or a full file
	 *            path. If a filename only or a partial file path is specified
	 *            it is saved in a /Downloads/ folder under the current working
	 *            folder.
	 * @return boolean
	 * @throws TestAutomationException
	 */
	public boolean clickOnDownloadButton(String outputFilePath) throws TestAutomationException {
		logger.debug("Start of Button: clickOnDownloadButton ");

		// about to download file so remove the current download file
		testContext.setCurrentDownloadedFile(null);

		String resolvedUrl = retrieveDownloadUrlFromButton();

		FileProcessor fileProcessor = new FileProcessor(browser, testContext);

		// If user has not specified a filename, use original filename.
		if (outputFilePath.isEmpty()) {
			CloseableHttpResponse response = fileProcessor.httpGet(resolvedUrl);
			outputFilePath = fileProcessor.extractFilenameFromHttpResponse(response);
			if (outputFilePath == null) {
				throw new TestAutomationException(
						ExceptionErrorCodes.TA065_BUTTON_FILENAME_NOT_FOUND_IN_HTTP_RESPONSE_HEADER,
						"Filename not found in http response header");
			}
		}

		// Set output file path
		if (!outputFilePath.contains("/") && !outputFilePath.contains("\\")) {
			// File name only has been supplied - e.g. "my file.csv"
			outputFilePath = Global.getTestLabPath() + "/Downloads/" + outputFilePath;
		} else if (!outputFilePath.contains(":") && !outputFilePath.startsWith("//")
				&& !outputFilePath.startsWith("\\\\")) {
			// Partial file path & file name has been supplied - e.g.
			// "my test files/my file.csv"
			if (outputFilePath.startsWith("/") || outputFilePath.startsWith("\\")) {
				outputFilePath = Global.getTestLabPath() + "/Downloads" + outputFilePath;
			} else {
				outputFilePath = Global.getTestLabPath() + "/Downloads/" + outputFilePath;
			}
		}

		// Download the file to the path specified
		boolean fileDownloaded = new FileProcessor(browser, testContext).downloadFile(resolvedUrl, outputFilePath);

		logger.debug("End of Button: clickOnDownloadButton ");
		return fileDownloaded;
	}

	/**
	 * Get the url which this button points to and verify that the filename
	 * within that url is as expected. Expected filename can contain character
	 * wildcards (*) or numeric wildcards(#).
	 * 
	 * @param expectedFilename
	 * @return boolean
	 * @throws TestAutomationException
	 */
	public boolean verifyDownloadFilename(String expectedFilename) throws TestAutomationException {
		logger.debug("Start of Button: verifyDownloadFilename ");

		String resolvedUrl = retrieveDownloadUrlFromButton();

		FileProcessor fileProcessor = new FileProcessor(browser, testContext);
		CloseableHttpResponse response = fileProcessor.httpGet(resolvedUrl);
		String downloadFilename = fileProcessor.extractFilenameFromHttpResponse(response);
		if (downloadFilename == null) {
			throw new TestAutomationException(
					ExceptionErrorCodes.TA065_BUTTON_FILENAME_NOT_FOUND_IN_HTTP_RESPONSE_HEADER,
					"Filename not found in http response header");
		}

		boolean stringsMatch = true;

		if (expectedFilename.length() != downloadFilename.length()) {
			stringsMatch = false;
		} else {

			if (expectedFilename.contains("#") || expectedFilename.contains("*")) {
				for (int i = 0; i < expectedFilename.length(); i++) {
					if ((expectedFilename.charAt(i) == downloadFilename.charAt(i))
							|| (expectedFilename.charAt(i) == '#' && WebElementProcessorCommon
									.isNumeric(downloadFilename.substring(i, i + 1)))
							|| (expectedFilename.charAt(i) == '*' && !downloadFilename.substring(i, i + 1).isEmpty())) {
					} else {
						stringsMatch = false;
					}
				}
			} else {
				if (downloadFilename.equalsIgnoreCase(expectedFilename)) {
					stringsMatch = true;
				} else {
					stringsMatch = false;
				}
			}
		}

		logger.debug("End of Button: verifyDownloadFilename ");

		if (stringsMatch) {
			return true;
		} else {
			throw new TestAutomationException(ExceptionErrorCodes.TA066_BUTTON_FILENAME_DOES_NOT_MATCH,
					"Actual filename '" + downloadFilename + "' does not match expected filename '" + expectedFilename
							+ "'");
		}
	}

	/**
	 * Retrieve the url which this button points to.
	 * 
	 * @return
	 * @throws TestAutomationException
	 */
	private String retrieveDownloadUrlFromButton() throws TestAutomationException {
		// Retrieve download url from button
		WebElement buttonElement = getButtonWebElement("True");
		String relativeDownloadUrl = buttonElement.getAttribute(GlobalAttributes.ONCLICK);
		int beginIndex = relativeDownloadUrl.indexOf("href='") + 6;
		int endIndex = relativeDownloadUrl.indexOf("';");
		relativeDownloadUrl = relativeDownloadUrl.substring(beginIndex, endIndex);

		String resolvedUrl = WebElementProcessorCommon.resolveUri(browser, relativeDownloadUrl);
		return resolvedUrl;
	}

	private void clickOnFSTButton(WebElement buttonElement) throws TestAutomationException {
		String nameAttribute = getNameAttribute(buttonElement);
		WebDriverWait wait = new WebDriverWait(browser, 10);
		wait.until(ExpectedConditions.elementToBeClickable(By.name(nameAttribute)));
		browser.findElement(By.name(nameAttribute)).click();
	}

	private String getNameAttribute(WebElement buttonElement) {
		return buttonElement.getAttribute(GlobalAttributes.NAME);
	}
}
