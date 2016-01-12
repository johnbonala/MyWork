package com.lifelens.webelements;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
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
import com.lifelens.interpreter.ArgList;

/**
 * It represents the Label object in lifelens
 * 
 * This helps to identify the Label object and get the id of the web element
 * corresponds to this label
 * 
 * @author Srinivas Pasupulati (co48633)
 * 
 * @version 1.0
 * 
 * @since 28.05.2014
 * 
 */

public class Label extends RemoteWebElement {

	private static Logger logger = Logger.getLogger(Label.class.getName());

	private String labelName;
	private WebDriver browser;
	private WebElement dialog = null;

	/**
	 * Constructor
	 * 
	 * @param browser2
	 * @param labelName
	 */
	public Label(WebDriver browser, String labelName) {
		this.labelName = labelName.replace("*", "").trim();
		this.browser = browser;
	}

	public Label(WebDriver browser, WebElement dialog, String labelName) {
		this.labelName = labelName.replace("*", "").trim();
		this.browser = browser;
		this.dialog = dialog;
	}

	/**
	 * Get the Label WebElement.
	 * 
	 * @return Label web element
	 * @throws TestAutomationException
	 */

	public WebElement getLableWebElement() throws TestAutomationException {
		logger.debug("Start of Label: getLableWebElement");
		List<WebElement> allLabels;
		List<WebElement> allLabelHavingSameName = new ArrayList<WebElement>();

		try {
			browser.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);

			if (dialog != null)
				allLabels = WebElementRetriever.getWebElementsByCSSSelector(dialog, labelName,
						GlobalCSSIdentifiers.LABEL);
			else
				allLabels = WebElementRetriever.getWebElementsByCSSSelector(browser, labelName,
						GlobalCSSIdentifiers.LABEL);
			ArgList lableNameArg = new ArgList(labelName);

			// Taking label having same name in separate list
			for (int i = 0; i < allLabels.size(); i++) {
				String labelNameFromElement = new HtmlParser(allLabels.get(i)).getinnerText().replace("*", "").trim();
				if (labelNameFromElement.equalsIgnoreCase(lableNameArg.getParameter(0))) {
					allLabelHavingSameName.add(allLabels.get(i));
				}
			}

			Iterator<WebElement> labels = allLabels.iterator();
			WebElement labelElement = null;
			int i = 0;
			while (labels.hasNext()) {
				labelElement = labels.next();
				String labelNameFromElement = new HtmlParser(labelElement).getinnerText().replace("*", "").trim();

				// 1st if condition will check for the labels having same names
				// on the web page and then it will return the intended label
				// with the help of index passed
				if (lableNameArg.getParameter(0).equalsIgnoreCase(labelNameFromElement)
						&& (allLabelHavingSameName.size() > 1 || lableNameArg.getParmetersCount() > 1)) {
					// get the 1st element from list when the ArgList size is
					// equal to 1
					if (lableNameArg.getParmetersCount() > 1) {
						String index = lableNameArg.getParameter(1);
						i = Integer.parseInt(index);
					} else {
						labelElement = allLabelHavingSameName.get(0);
						break;
					}
					labelElement = allLabelHavingSameName.get(i - 1);
					break;
				} else if (labelName.equalsIgnoreCase(labelNameFromElement)) {
					break;
				} else if (labelNameFromElement.contains(labelName)) {
					// adding a condition if label name passed from test script
					// is not having the empty(hidden) spaces which are in
					// actual label
					String trimmedLabel = labelNameFromElement.substring(1, labelNameFromElement.length() - 1);
					if (trimmedLabel.equalsIgnoreCase(labelName)) {
						break;
					}
				}
				labelElement = null;
			}

			boolean labelFound = (labelElement == null) ? false : true;
			WebElementProcessorCommon.logWebElementStatus("Lable", labelName, labelFound);
			if (!labelFound) {
				throw new TestAutomationException(
						ExceptionErrorCodes.TA012_LABEL_ELEMENT_NOT_FOUND_BY_LABEL_NAME_ERROR,
						"Web elements with label name '" + labelName + "' not found");
			}
			logger.debug("End of Label: getLableWebElement");
			return labelElement;

		} finally {
			browser.manage().timeouts().implicitlyWait(Global.timeout, TimeUnit.SECONDS);
		}
	}

	/**
	 * It returns the ID of the web element corresponding to this label
	 * 
	 * @return id
	 * @throws TestAutomationException
	 */

	public String getLabelForAttribute() throws TestAutomationException {
		logger.debug("Start of Label: getLabelForAttribute");
		String valueOfForAttribute = getLableWebElement().getAttribute(GlobalAttributes.FOR);
		if (StringUtils.isBlank(valueOfForAttribute)) {
			throw new TestAutomationException(ExceptionErrorCodes.TA013_LABEL_ELEMENT_FOR_ATTRIBUTE_NOT_FOUND_ERROR,
					"For atrribute of the web elements with label name " + labelName + " not found");
		}
		logger.debug("End of Label: getLabelForAttribute");
		return valueOfForAttribute;
	}

}
