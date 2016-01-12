package com.lifelens.webelements;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.lifelens.browsers.WebElementProcessorCommon;
import com.lifelens.exceptions.ExceptionErrorCodes;
import com.lifelens.exceptions.TestAutomationException;
import com.lifelens.globals.Global;
import com.lifelens.globals.GlobalCSSIdentifiers;
import com.lifelens.globals.GlobalCSSSelectors;
import com.lifelens.globals.GlobalTags;
import com.lifelens.htmlparser.HtmlParser;
import com.lifelens.interpreter.ArgList;
import com.lifelens.interpreter.TestContext;

/**
 * It represents the checkbox object in lifelens
 * 
 * This helps to identify the checkbox object and select it
 * 
 * @author Srinivas Pasupulati (co48633)
 * 
 * @version 1.0
 * 
 * @since 28.05.2014
 * 
 */

public class CheckBox extends RemoteWebElement {

	private String labelName;
	public WebDriver browser;
	private static Logger logger = Logger.getLogger(CheckBox.class.getName());
	private ArgList labelNameParams;
	private WebElement checkbox;
	private String existsIndicator = "True";
	private TestContext testContext;

	public CheckBox(WebDriver browser, TestContext testContext, String labelName) throws TestAutomationException {
		this(browser, testContext, labelName, "True");
	}

	public CheckBox(WebDriver browser, TestContext testContext, String labelName, String existsIndicator)
			throws TestAutomationException {
		this.existsIndicator = existsIndicator;
		labelNameParams = new ArgList(labelName);
		this.labelName = labelName;
		this.browser = browser;
		this.testContext = testContext;
		this.checkbox = getCheckboxFromBrowser();
	}

	/**
	 * Select or deselect the checkbox based on the boolean value supplied
	 * 
	 * @param booleanValue
	 *            : true - select the checkbox, throw warning if it is already
	 *            selected : false - deselect the checkbox, throw warning if it
	 *            is already not selected. : blank - toggle the value (select if
	 *            it is deselected and vice versa) : invalid value - throw error
	 *            if invalid value is supplied.
	 * @return true - if the action is performed false - if the action is not
	 *         performed/ found any other exception
	 */
	public boolean select(String booleanValue) throws TestAutomationException {
		logger.debug("CheckBox: select start ");
		boolean result = false;

		WebElement checkBoxElement = checkbox;

		if (checkBoxElement != null) {
			if (checkBoxElement.isEnabled()) {
				if (booleanValue.isEmpty()) {
					boolean state = checkBoxElement.isSelected();
					clickOnCheckbox(checkBoxElement);
					if (getCheckboxFromBrowser() != null) {
						result = (getCheckboxFromBrowser().isSelected() != state);
					} else {
						result = (getCheckboxFromBrowser().isSelected() != state);
					}
				} else if (booleanValue.equalsIgnoreCase("TRUE")) {
					if (!checkBoxElement.isSelected()) {
						clickOnCheckbox(checkBoxElement);
						if (getCheckboxFromBrowser() != null) {
							result = getCheckboxFromBrowser().isSelected();
						} else {
							result = checkbox.isSelected();
						}
					} else {
						result = true;
					}
				} else if (booleanValue.equalsIgnoreCase("FALSE")) {
					if (checkBoxElement.isSelected()) {
						clickOnCheckbox(checkBoxElement);
						// checkBoxElement.click();
						result = !getCheckboxFromBrowser().isSelected();
					} else {
						result = true;
					}
				} else {
					throw new TestAutomationException(ExceptionErrorCodes.TA053_CHECK_BOX_INVALID_BOOLEAN_VALUE_ERROR,
							"Invalid input parameter passed to select checkbox. Expected: TRUE/FALSE. Actual: "
									+ booleanValue);
				}
			}
			if (!result) {
				throw new TestAutomationException(
						ExceptionErrorCodes.TA084_CHECK_BOX_UNABLE_TO_SELECT_OR_DESELECT_CHECKBOX_ERROR,
						"Web elements with check box name " + labelName + " found but unable to select/deselect");
			}
		}
		if (!result) {
			throw new TestAutomationException(ExceptionErrorCodes.TA054_CHECK_BOX_ELEMENT_NOT_AVAILABLE_ERROR,
					"Web elements with check box name " + labelName + " not found or check box is not enabled");
		}
		logger.debug("CheckBox: select end ");
		return result;
	}

	/**
	 * Verify if the specified Checkbox exists. If parameter 2 is set to
	 * true/false it will additionally check that the checkbox value is
	 * ticked/unticked. Or if set to enabled/disabled it will check that the
	 * checkbox is enabled/disabled. If blank it simply verifies that the
	 * checkbox exists regardless of its value.
	 * 
	 * @param existsIndicator
	 *            optional "false" to verify that checkbox does not exist
	 * @param checkboxTicked
	 *            "true", "false", "enabled", "disabled" or blank
	 * @return true/false if checkbox exists in specified state
	 * @throws TestAutomationException
	 */
	public boolean verifyCheckbox(String existsIndicator, String checkboxTicked) throws TestAutomationException {
		logger.debug("Start of CheckBox: verifyCheckbox ");
		logger.debug("Verify CheckBox:" + labelName);

		boolean result = false;

		try {
			browser.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);

			WebElement checkBoxElement = checkbox;

			if (checkBoxElement == null) {
				if (existsIndicator.equalsIgnoreCase("False")) {
					result = true;
				}
			} else {
				if (!existsIndicator.equalsIgnoreCase("False")) {
					if (checkboxTicked.isEmpty()) {
						result = true;
					} else if (checkboxTicked.equalsIgnoreCase("TRUE")) {
						if (checkBoxElement.isSelected()) {
							result = true;
						}
					} else if (checkboxTicked.equalsIgnoreCase("FALSE")) {
						if (!checkBoxElement.isSelected()) {
							result = true;
						}
					} else if (checkboxTicked.equalsIgnoreCase("Enabled")) {
						if (checkBoxElement.isEnabled()) {
							result = true;
						}
					} else if (checkboxTicked.equalsIgnoreCase("Disabled")) {
						if (!checkBoxElement.isEnabled()) {
							result = true;
						}
					} else {
						throw new TestAutomationException(
								ExceptionErrorCodes.TA059_CHECK_BOX_INVALID_BOOLEAN_VALUE_ERROR,
								"Invalid input parameter passed to verify checkbox. Expected: True, False, Enabled or Disabled. Actual: "
										+ checkboxTicked);
					}
				}
			}
			logger.debug("End of CheckBox: verifyCheckbox ");
			return result;

		} finally {
			browser.manage().timeouts().implicitlyWait(Global.timeout, TimeUnit.SECONDS);
		}
	}

	private void clickOnCheckbox(WebElement checkBoxElement) throws TestAutomationException {
		if (!getCheckboxId().isEmpty()) {
			WebDriverWait wait = new WebDriverWait(browser, 10);
			wait.until(ExpectedConditions.elementToBeClickable(By.id(getCheckboxId())));
		}

		checkbox.click();
	}

	/**
	 * get the check box element
	 * 
	 * @return
	 * @throws TestAutomationException
	 */
	private WebElement getCheckboxFromBrowser() throws TestAutomationException {

		WebElement checkBoxElement = null;
		if (!labelName.equalsIgnoreCase(GlobalCSSIdentifiers.SHOWONLYCOREFUNDS)) {
			if (testContext.getLoadIndicator()) {
				WebElementProcessorCommon.waitForWebProcessing(browser, Global.getTimeout(), Global.loadIndicatorCSS,
						Global.lazyLoadIndicatorClass);
			}
		}
		if (labelNameParams.hasDecisionTableKeyword()) {
			WebElement decisionTableDialog = browser.findElement(By.id(GlobalCSSIdentifiers.DIALOG));
			checkBoxElement = new DecisionTable(decisionTableDialog, labelNameParams, existsIndicator)
					.getCheckbox(existsIndicator);

		} else if (labelNameParams.hasTableKeyword()) {
			try {
				checkBoxElement = new Table(browser, testContext, labelNameParams).getCheckbox(existsIndicator);
			} catch (TestAutomationException te) {
				List<WebElement> checkBoxes = WebElementRetriever.getWebElementsByCSSSelector(browser, labelName,
						GlobalCSSSelectors.TABLE_CHECKBOX);

				int checkBoxIndexInBrowser = Integer.parseInt(labelNameParams.getNonTableArgumentListString());

				checkBoxElement = checkBoxes.get(checkBoxIndexInBrowser - 1);
			}

		} else if (WebElementProcessorCommon.isNumeric(labelName)) {
			int number = Integer.parseInt(labelName);
			List<WebElement> checkBoxes = WebElementRetriever.getWebElementsByClassName(browser, labelName,
					GlobalCSSIdentifiers.DIGITCHECKBOXINPUT);
			checkBoxElement = checkBoxes.get(number - 1);
		} else {

			try {
				checkBoxElement = WebElementRetriever.getWebElementByLabelName(browser, labelName);
			} catch (Exception e) {
				// Fix for check box label names not having "for" attribute
				List<WebElement> divs = browser.findElements(By.tagName(GlobalTags.DIV));

				for (int i = 0; i < divs.size(); i++) {

					String innerText = new HtmlParser(divs.get(i)).getinnerText();
					// if '&nbsp' is present is a string for white space replace
					// it with empty string(\\u00A0 is a utf-8 character for
					// &nbsp) as trim() does not recognise this.
					innerText = innerText.replaceAll("\\u00A0", "");

					if (labelName.equalsIgnoreCase(innerText.trim())) {
						checkBoxElement = WebElementRetriever.getWebElementByTagName(divs.get(i), labelName,
								GlobalTags.INPUT);
						break;
					}
				}
			}
		}
		return checkBoxElement;
	}

	private String getCheckboxId() throws TestAutomationException {

		String checkboxId;

		try {
			checkboxId = WebElementRetriever.getWebElementId(checkbox);
		} catch (TestAutomationException ta) {
			logger.warn("ID Attribute not for the checkbox : " + labelName);
			checkboxId = "";
		}
		return checkboxId;

	}

}
