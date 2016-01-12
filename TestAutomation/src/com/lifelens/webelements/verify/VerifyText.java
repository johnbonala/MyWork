package com.lifelens.webelements.verify;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

import com.lifelens.browsers.WebElementProcessor;
import com.lifelens.exceptions.ExceptionErrorCodes;
import com.lifelens.exceptions.TestAutomationException;
import com.lifelens.htmlparser.HtmlParser;
import com.lifelens.interpreter.ArgList;
import com.lifelens.webelements.WebElementRetriever;

/**
 * This Class verifies the text associated with
 * Tags(H1,H2,H3,button,checkbox,Hyperlink,Carousel,radio,Accordian), class,
 * css, xpath
 * 
 * @author Alok Chatterji
 * 
 */
public class VerifyText extends RemoteWebElement {

	private static final String BY_LABEL = ElementList.CHECKBOX.getSearchType();
	private static final String TAG = ElementList.H1.getSearchType();
	private static final String OTHER = ElementList.CLASS.getSearchType();
	private static final String CLASS = ElementList.ACCORDIAN.getSearchType();
	private static final String ID = ElementList.FST_WIZARD_LABEL.getSearchType();
	private static final String TAG_ELEMENTS_RETURNED_NULL = "TagElements returned null.";
	private static Logger logger = Logger.getLogger(WebElementProcessor.class.getName());
	private static final int INDEX_1 = 1;
	private static final int INDEX_0 = 0;
	public WebDriver browser;

	private String textToVerify;
	private int index = 1;
	private String operationString;

	public VerifyText(WebDriver browser) {
		this.browser = browser;
	}

	public boolean verify(String argumentList, String textToVerifyParam) throws TestAutomationException {
		boolean verifyTextStatus = false;
		ArgList params = new ArgList(textToVerifyParam);
		if (params.getParmetersCount() == 0) {
			textToVerify = "";
		} else {
			textToVerify = params.getParameter(0);
		}
		List<String> arglistToVerify = getArgListToVerify(argumentList);
		int arglistSize = arglistToVerify.size();
		ElementList elements = ElementList.getTagName(arglistToVerify.get(INDEX_0));
		String operation = elements.getTagValue();
		String type = elements.getSearchType();
		validateArguments(arglistSize, operation, type);
		verifyTextStatus = processTag(arglistToVerify, arglistSize, operation, type);
		if (!verifyTextStatus && params.getParmetersCount() == 1)
			throw new TestAutomationException(ExceptionErrorCodes.TA071_VERIFY_ELEMENT_ELEMENT_NOT_FOUND_ERROR,
					textToVerify);
		// if we set verify element as false,then the action should pass.
		if (params.getParmetersCount() == 2 && !verifyTextStatus && params.getParameter(1) != null
				&& params.getParameter(1).equalsIgnoreCase("false")) {
			return true;
		}
		return verifyTextStatus;
	}

	private void validateArguments(int arglistSize, String operationParam, String typeParam)
			throws TestAutomationException {
		if (arglistSize > 2 || operationParam == null || typeParam == null)
			throw new TestAutomationException(ExceptionErrorCodes.TA070_VERIFY_ELEMENT_INVALID_INPUT_ERROR,
					"Invalid input");
	}

	private List<String> getArgListToVerify(String argumentList) throws TestAutomationException {
		ArgList arguments = new ArgList(argumentList);
		List<String> arglistToVerify = arguments.getArgumentList();
		return arglistToVerify;
	}

	private boolean processTag(List<String> arglistToVerify, int arglistSize, String operation, String type)
			throws TestAutomationException {
		int tempIndex = 1;
		extractTextToVerify(arglistToVerify, arglistSize, type);
		if (type.equals(BY_LABEL)) {
			return processLabel();
		}
		List<WebElement> elements = getInputForTag(operation, type);
		Iterator<WebElement> elementIterator = elements.iterator();
		while (elementIterator.hasNext()) {
			HtmlParser html = getHtml(operation, elementIterator);
			if (type.equals(TAG))
				if (html.getinnerText().trim().equalsIgnoreCase(textToVerify)
						|| html.getinnerText().trim().contains(textToVerify)) {
					if (tempIndex == index) {
						logger.info("Text Verified :" + textToVerify + " with index :" + index);
						return true;
					}
					tempIndex++;
				}
			if (type.equals(CLASS))
				if (html.getinnerText().trim().contains(textToVerify)) {
					if (tempIndex == index) {
						logger.info("Text Verified :" + textToVerify + " with index :" + index);
						return true;
					}
					tempIndex++;
				}

			if (type.equals(ID))
				if (html.getinnerText().trim().contains(textToVerify)) {
					if (tempIndex == index) {
						logger.info("Text Verified :" + textToVerify + " with index :" + index);
						return true;
					}
					tempIndex++;
				}
			if (type.equals(OTHER)) {
				if (html.getinnerText().trim().equalsIgnoreCase(textToVerify)) {
					logger.info("Text Verified :" + textToVerify);
					return true;
				}
				if (html.getinnerText().trim().contains(textToVerify)) {
					logger.info("Text Verified :" + textToVerify);
					return true;
				}
			}
		}
		logger.info("Text " + textToVerify + " not found with index : " + index);
		return false;
	}

	private boolean processLabel() throws TestAutomationException {
		WebElement element = processElementByLabel();
		if (element == null) {
			logger.info("Text " + textToVerify + " not found");
			return false;
		} else
			logger.info("Text " + textToVerify + "found");
		return true;
	}

	private HtmlParser getHtml(String operation, Iterator<WebElement> elementIterator) {
		WebElement element = elementIterator.next();
		HtmlParser html = new HtmlParser(element);
		logger.debug("html elements with " + operation + " : " + html.getinnerText().trim());
		logger.debug("Text to verify :" + textToVerify);
		return html;
	}

	private WebElement processElementByLabel() throws TestAutomationException {
		WebElement element;
		try {
			element = WebElementRetriever.getWebElementByLabelName(browser, textToVerify);
		} catch (TestAutomationException e) {
			logger.info(TAG_ELEMENTS_RETURNED_NULL);
			throw new TestAutomationException(ExceptionErrorCodes.TA069_VERIFY_ELEMENT_INVALID_INPUT_ERROR,
					"Invalid input :" + textToVerify);
		}
		return element;
	}

	private List<WebElement> getInputForTag(String operation, String type) throws TestAutomationException {
		List<WebElement> elements = getElements(operation, type, operationString);
		if (elements == null || elements.size() <= 0)
			throw new TestAutomationException(ExceptionErrorCodes.TA068_VERIFY_ELEMENT_INVALID_INPUT_ERROR,
					TAG_ELEMENTS_RETURNED_NULL);
		return elements;
	}

	private List<WebElement> getElements(String operation, String type, String operationString) {
		List<WebElement> tagElements = null;
		if (type.equals(TAG))
			tagElements = browser.findElements(By.tagName(operation));
		if (type.equals(CLASS))
			tagElements = browser.findElements(By.className(operation));
		if (type.equals(ID))
			tagElements = browser.findElements(By.id(operation));
		if (type.equals(OTHER)) {
			tagElements = processTypeOther(operation, operationString, tagElements);
		}
		return tagElements;
	}

	private List<WebElement> processTypeOther(String operation, String operationString, List<WebElement> tagElements) {
		if (operation.equals(ElementList.XPATH.getTagValue()))
			tagElements = browser.findElements(By.xpath(operationString));
		if (operation.equals(ElementList.CLASS.getTagValue()))
			tagElements = browser.findElements(By.className(operationString));
		if (operation.equals(ElementList.CSS.getTagValue()))
			tagElements = browser.findElements(By.cssSelector(operationString));
		return tagElements;
	}

	private void extractTextToVerify(List<String> arglistToVerify, int arglistSize, String type)
			throws TestAutomationException {

		if (type.equals(OTHER)) {
			operationString = arglistToVerify.get(INDEX_1);
		} else if (arglistSize == 2) {
			int argIndex = Integer.parseInt(arglistToVerify.get(INDEX_1));
			if (argIndex >= 1)
				index = argIndex;
			else
				throw new TestAutomationException(ExceptionErrorCodes.TA067_VERIFY_ELEMENT_INVALID_INPUT_ERROR,
						"Invalid input :" + textToVerify);
		}
	}

}
