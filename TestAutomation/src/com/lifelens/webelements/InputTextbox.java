package com.lifelens.webelements;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

import com.lifelens.browsers.WebElementProcessorCommon;
import com.lifelens.exceptions.TestAutomationException;
import com.lifelens.globals.GlobalAttributes;
import com.lifelens.globals.GlobalCSSIdentifiers;
import com.lifelens.globals.GlobalTags;
import com.lifelens.htmlparser.HtmlParser;
import com.lifelens.interpreter.ArgList;
import com.lifelens.interpreter.TestContext;

/**
 * It represents the Input Text box object in lifelens
 * 
 * This helps to identify the input text box object and insert content in it
 * 
 * @author Srinivas Pasupulati (co48633)
 * 
 * @version 1.0
 * 
 * @since 28.05.2014
 * 
 */

public class InputTextbox extends RemoteWebElement {

	private static Logger logger = Logger.getLogger(InputTextbox.class.getName());
	private String labelName;
	private WebDriver browser;
	private ArgList labelNameParams;
	private WebElement decisionTableDialog = null;
	private WebElement inputtextbox = null;
	private TestContext testContext;

	public InputTextbox(WebDriver browser, String labelName, TestContext testContext) throws TestAutomationException {
		labelNameParams = new ArgList(labelName);
		this.labelName = labelName;
		this.browser = browser;
		this.testContext = testContext;

		if (this.testContext.getLoadIndicator()) {
			try {
				this.decisionTableDialog = browser.findElement(By.id(GlobalCSSIdentifiers.DIALOG));
			} catch (Exception e) {

			}
		}
	}

	/**
	 * It get the input text box and send keys to the text box
	 * 
	 * @param inputText
	 * @return
	 * @throws TestAutomationException
	 */
	public boolean sendKeys(String inputText) throws TestAutomationException {
		logger.debug("Start of InputTextbox: sendKeys");

		inputtextbox = getInputBox();

		// for TBP and CSOL buttons do not type
		if (labelName.equalsIgnoreCase(GlobalCSSIdentifiers.POSTTOKEN)
				|| labelName.equalsIgnoreCase(GlobalCSSIdentifiers.LOGINSOI4)) {
			return true;
		}
		if (inputtextbox == null) {
			// below fix is required for one of CSOL text area
			if (labelName.equalsIgnoreCase(GlobalCSSIdentifiers.MESSAGE)) {
				inputtextbox = WebElementRetriever
						.getWebElementById(browser, "labelName", GlobalCSSIdentifiers.MESSAGE);
			} else {
				inputtextbox = WebElementRetriever.getWebElementByClassName(browser, "labelName",
						GlobalCSSIdentifiers.DIGITINPUTINNER);
			}
		}

		boolean isInputEnabled = false;

		if (inputtextbox.isEnabled()) {
			// Don't do clear() as the field has to be editable for this to
			// work.
			// An example of non-editable fields where we have to input text
			// is
			// a file open dialog.
			// inputtextbox.sendKeys(""); // clear input field first
			if (StringUtils.isNotEmpty(inputtextbox.getAttribute(GlobalAttributes.VALUE))) {

				String defalutValue = inputtextbox.getAttribute(GlobalAttributes.VALUE);

				for (int i = 0; i < defalutValue.length(); i++) {
					inputtextbox.sendKeys("\u0008");
				}

				inputtextbox.sendKeys(inputText);
				isInputEnabled = true;
			} else {
				inputtextbox.sendKeys(inputText);
				isInputEnabled = true;
			}
		} else {
			isInputEnabled = false;
		}
		WebElementProcessorCommon.logWebElementStatus("Input", labelName, isInputEnabled);
		logger.debug("End of InputTextbox: sendKeys");
		return isInputEnabled;
	}

	/**
	 * get the input box element
	 * 
	 * @return
	 * @throws TestAutomationException
	 */
	private WebElement getInputBox() throws TestAutomationException {
		WebElement inputTextBox = null;

		try {
			if (labelNameParams.hasDecisionTableKeyword()) {
				inputTextBox = new DecisionTable(decisionTableDialog, labelNameParams).getInputbox();
			} else if (labelNameParams.hasTableKeyword()) {
				inputTextBox = new Table(browser, testContext, labelNameParams).getInputbox();
			} else {
				// below peace of code is added to automate TBP harness and CSOL
				// internal launcher page
				if (labelName.equalsIgnoreCase(GlobalCSSIdentifiers.POSTTOKEN)
						|| labelName.equalsIgnoreCase(GlobalCSSIdentifiers.LOGINSOI4)) {
					List<WebElement> list = WebElementRetriever.getWebElementsByTagName(browser, "List of input boxes",
							GlobalTags.INPUT);
					for (int i = 0; i < list.size(); i++) {
						WebElement textBox = list.get(i);
						if (textBox.getAttribute(GlobalAttributes.VALUE).equals(GlobalCSSIdentifiers.POSTTOKEN)) {
							inputTextBox = textBox;
							inputTextBox.click();
							testContext.setLoadIndicator(true);
							break;
						} else if (textBox.getAttribute(GlobalAttributes.VALUE).equals(labelName)) {
							inputTextBox = textBox;
							inputTextBox.click();
							break;
						}
					}
				} else {
					inputTextBox = WebElementRetriever.getWebElementByLabelName(browser, labelName);
				}
			}
		} catch (Exception e) {
			// Fix for input box label names not having "for" attribute
			logger.info("The label " + labelName + " is not having associated for attribute");
			List<WebElement> divs = browser.findElements(By.tagName(GlobalTags.DIV));
			List<WebElement> innerdivs = new ArrayList<WebElement>();

			for (int i = 0; i < divs.size(); i++) {
				if ((new HtmlParser(divs.get(i)).getinnerText()).contains(labelName)) {
					innerdivs = WebElementRetriever.getWebElementsByTagName(divs.get(i), labelName, GlobalTags.DIV);
					for (int j = 0; j < innerdivs.size(); j++) {
						if ((new HtmlParser(innerdivs.get(j)).getinnerText()).equalsIgnoreCase(labelName)) {
							inputTextBox = WebElementRetriever.getWebElementByClassName(innerdivs.get(j), "labelName",
									GlobalCSSIdentifiers.DIGITINPUTINNER);
						}
					}
				}
			}
		}

		return inputTextBox;
	}

	/**
	 * Check's if text in input field matches the passed value. Can also pass in
	 * 'Enabled' or 'Disabled' to check status of field.
	 * 
	 * @param text
	 *            the text to match, or 'Enabled'/'Disabled'.
	 * @return boolean success/failure
	 * @throws TestAutomationException
	 */
	public boolean verifyDefaultValue(String text) throws TestAutomationException {
		logger.debug("Start of InputTextbox: verifyDefaultValue");
		WebElement inputTextbox = WebElementRetriever.getWebElementByLabelName(browser, labelName);
		String inputBoxValue = inputTextbox.getAttribute(GlobalAttributes.VALUE);
		boolean valueMatches = false;

		if (inputBoxValue != null) {
			if (text.equalsIgnoreCase("Enabled") || text.equalsIgnoreCase("Disabled")) {
				if (text.equalsIgnoreCase("Enabled") && inputTextbox.isEnabled()) {
					valueMatches = true;
				}

				if (text.equalsIgnoreCase("Disabled") && !inputTextbox.isEnabled()) {
					valueMatches = true;
				}
			} else {

				if (inputBoxValue.equals(text)) {
					valueMatches = true;
				} else {
					valueMatches = false;
					logger.error(text + " is not value for field: " + labelName);
				}
			}
		} else {
			valueMatches = false;
			logger.error("No value exists for field: " + labelName);
		}

		logger.debug("End of InputTextbox: verifyDefaultValue");
		return valueMatches;
	}
}
