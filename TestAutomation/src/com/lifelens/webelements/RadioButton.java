package com.lifelens.webelements;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

import com.lifelens.browsers.WebElementProcessorCommon;
import com.lifelens.exceptions.ExceptionErrorCodes;
import com.lifelens.exceptions.TestAutomationException;
import com.lifelens.globals.Global;
import com.lifelens.globals.GlobalCSSIdentifiers;
import com.lifelens.interpreter.ArgList;
import com.lifelens.interpreter.TestContext;

public class RadioButton extends RemoteWebElement {

	private String labelName;
	public WebDriver browser;
	private ArgList labelNameParams;
	private TestContext testContext;
	private static Logger logger = Logger.getLogger(RadioButton.class.getName());

	public RadioButton(WebDriver browser, TestContext testContext, String labelName) throws TestAutomationException {
		labelNameParams = new ArgList(labelName);
		this.labelName = labelName;
		this.browser = browser;
		this.testContext = testContext;
	}

	public boolean select() throws TestAutomationException {
		logger.debug("Radio button: select start ");
		boolean result = false;

		WebElement radioButtonElement = getRadioButton();

		if (radioButtonElement != null) {
			if (!radioButtonElement.isSelected()) {
				radioButtonElement.click();
			}
			result = true;
		}

		logger.debug("RadioButton: select end ");

		if (result) {
			return true;
		}

		throw new TestAutomationException(ExceptionErrorCodes.TA094_RADIO_BUTTON_ELEMENT_NOT_AVAILABLE_ERROR,
				"Web elements with check box name " + labelName + " not found or check box is not enabled");
	}

	/**
	 * Verify if the specified RadioButton exists. If parameter 2 is set to
	 * true/false it will additionally check that the radio button value is
	 * ticked/unticked. Or if set to enabled/disabled it will check that the
	 * radio button is enabled/disabled. If blank it simply verifies that the
	 * radio button exists regardless of its value.
	 * 
	 * @param booleanValue
	 *            "true", "false", "enabled", "disabled" or blank
	 * @return true/false if RadioButton exists in specified state
	 * @throws TestAutomationException
	 */
	public boolean verifyRadioButton(String booleanValue) throws TestAutomationException {
		logger.debug("Start of Radio button: verifyRadioButton ");

		try {
			boolean result = false;
			browser.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);

			WebElement radioButtonElement = getRadioButton();

			if (radioButtonElement == null) {
				result = false;
			} else {
				if (booleanValue.isEmpty()) {
					result = true;
				} else if (booleanValue.equalsIgnoreCase("TRUE")) {
					if (radioButtonElement.isSelected()) {
						result = true;
					}
				} else if (booleanValue.equalsIgnoreCase("FALSE")) {
					if (!radioButtonElement.isSelected()) {
						result = true;
					}
				} else if (booleanValue.equalsIgnoreCase("Enabled")) {
					if (radioButtonElement.isEnabled()) {
						result = true;
					}
				} else if (booleanValue.equalsIgnoreCase("Disabled")) {
					if (!radioButtonElement.isEnabled()) {
						result = true;
					}
				} else {
					throw new TestAutomationException(
							ExceptionErrorCodes.TA092_RADIO_BUTTON_INVALID_BOOLEAN_VALUE_ERROR,
							"Invalid input parameter passed to select radio button. Expected: TRUE/FALSE. Actual: "
									+ booleanValue);
				}
			}
			logger.debug("End of Radio button: verifyRadioButton ");
			return result;

		} finally {
			browser.manage().timeouts().implicitlyWait(Global.timeout, TimeUnit.SECONDS);
		}
	}

	/**
	 * Find the radio button either by label name or index. NB radio buttons are
	 * also class = dijitCheckBoxInput
	 * 
	 * @return radio button WebElement
	 * @throws TestAutomationException
	 */
	private WebElement getRadioButton() throws TestAutomationException {
		logger.debug("Start of Radio button: getRadioButton");
		WebElement radioButtonElement;
		if (WebElementProcessorCommon.isNumeric(labelName)) {
			int number = Integer.parseInt(labelName);
			List<WebElement> radioButtons = WebElementRetriever.getWebElementsByClassName(browser, labelName,
					GlobalCSSIdentifiers.DIGITCHECKBOXINPUT);
			radioButtonElement = radioButtons.get(number - 1);
		} else if (labelNameParams.hasTableKeyword()) {
			radioButtonElement = new Table(browser, testContext, labelNameParams).getRadioButton();
		} else {
			radioButtonElement = WebElementRetriever.getWebElementByLabelName(browser, labelName);
		}
		logger.debug("End of Radio button: getRadioButton");
		return radioButtonElement;
	}

}
