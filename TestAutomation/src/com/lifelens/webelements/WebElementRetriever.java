package com.lifelens.webelements;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.lifelens.exceptions.ExceptionErrorCodes;
import com.lifelens.exceptions.TestAutomationException;
import com.lifelens.globals.Global;

public class WebElementRetriever {

	private static Logger logger = Logger.getLogger(WebElementRetriever.class.getName());

	public static WebElement getWebElementById(WebDriver browser, String webElementName, String id)
			throws TestAutomationException {
		logger.debug("WebElementFinderHelper: getWebElementById start ");
		WebElement element = null;

		try {

			element = browser.findElement(By.id(id));

		} catch (Exception e) {
			logger.error("WebElementFinderHelper: getWebElementById: Web element with name '" + webElementName
					+ " and  id :" + id + "' not found in current screen", e);
		}

		if (element == null) {
			throw new TestAutomationException(
					ExceptionErrorCodes.TA009_WEB_ELEMENT_RETRIEVER_ELEMENT_NOT_FOUND_BY_ID_ERROR,
					"Web element with name '" + webElementName + "' and id '" + id + "' not found.");
		} else {
			logger.debug("WebElementFinderHelper: getWebElementById: Web element with name '" + webElementName
					+ "' found in current screen");
		}
		logger.debug("WebElementFinderHelper: getWebElementById end ");

		return element;
	}

	public static WebElement getWebElementById(WebElement browser, String webElementName, String id)
			throws TestAutomationException {
		logger.debug("WebElementFinderHelper: getWebElementById start ");
		WebElement element = null;

		try {

			element = browser.findElement(By.id(id));

		} catch (Exception e) {
			logger.error("WebElementFinderHelper: getWebElementById: Web element with name '" + webElementName
					+ " and  id :" + id + "' not found in current screen", e);
		}

		if (element == null) {
			throw new TestAutomationException(
					ExceptionErrorCodes.TA009_WEB_ELEMENT_RETRIEVER_ELEMENT_NOT_FOUND_BY_ID_ERROR,
					"Web element with name '" + webElementName + "' and id '" + id + "' not found.");
		} else {
			logger.debug("WebElementFinderHelper: getWebElementById: Web element with name '" + webElementName
					+ "' found in current screen");
		}
		logger.debug("WebElementFinderHelper: getWebElementById end ");

		return element;
	}

	public static List<WebElement> getWebElementsByTagName(WebDriver browser, String webElementName, String tagName)
			throws TestAutomationException {
		logger.debug("WebElementFinderHelper: getWebElementByTagName start ");
		List<WebElement> webElements = browser.findElements(By.tagName(tagName));
		if (webElements == null) {
			throw new TestAutomationException(
					ExceptionErrorCodes.TA010_WEB_ELEMENT_RETRIEVER_ELEMENTS_NOT_FOUND_BY_TAG_NAME_ERROR,
					"Web elements with name '" + webElementName + "' and tag name '" + tagName + "' not found.");
		} else {
			logger.debug("WebElementFinderHelper: getWebElementByTagName: Web element with name '" + webElementName
					+ "' found in current screen");
		}
		logger.debug("End of WebElementFinderHelper: getWebElementByTagName");
		return webElements;
	}

	public static WebElement getWebElementByTagName(WebDriver browser, String webElementName, String tagName)
			throws TestAutomationException {
		logger.debug("WebElementFinderHelper: getWebElementByTagName start ");
		WebElement webElement = browser.findElement(By.tagName(tagName));
		if (webElement == null) {
			throw new TestAutomationException(
					ExceptionErrorCodes.TA046_WEB_ELEMENT_RETRIEVER_ELEMENT_NOT_FOUND_BY_TAG_NAME_ERROR,
					"Web elements with  name '" + webElementName + "' and tag name '" + tagName + "' not found.");
		} else {
			logger.debug("WebElementFinderHelper: getWebElementsByTagName: Web element with name '" + webElementName
					+ "' found in current screen");
		}
		logger.debug("End of WebElementFinderHelper: getWebElementByTagName");
		return webElement;
	}

	public static List<WebElement> getWebElementsByTagName(WebElement element, String webElementName, String tagName)
			throws TestAutomationException {
		logger.debug("WebElementFinderHelper: getWebElementByTagName start ");
		List<WebElement> webElements = element.findElements(By.tagName(tagName));
		if (webElements == null) {
			throw new TestAutomationException(
					ExceptionErrorCodes.TA010_WEB_ELEMENT_RETRIEVER_ELEMENTS_NOT_FOUND_BY_TAG_NAME_ERROR,
					"Web elements with name '" + webElementName + "' and tag name '" + tagName + "' not found.");
		} else {
			logger.debug("WebElementFinderHelper: getWebElementByTagName: Web element with name '" + webElementName
					+ "' found in current screen");
		}
		logger.debug("End of WebElementFinderHelper: getWebElementByTagName");
		return webElements;
	}

	public static WebElement getWebElementByTagName(WebElement element, String webElementName, String tagName)
			throws TestAutomationException {
		logger.debug("Start of WebElementFinderHelper: getWebElementByTagName");
		WebElement webElement = element.findElement(By.tagName(tagName));
		if (webElement == null) {
			throw new TestAutomationException(
					ExceptionErrorCodes.TA019_WEB_ELEMENT_RETRIEVER_ELEMENT_NOT_FOUND_BY_TAG_NAME_ERROR,
					"Web elements with  name '" + webElementName + "' and tag name '" + tagName + "' not found.");
		} else {
			logger.debug("WebElementFinderHelper: getWebElementsByTagName: Web element with name '" + webElementName
					+ "' found in current screen");
		}
		logger.debug("End of WebElementFinderHelper: getWebElementByTagName");
		return webElement;
	}

	public static List<WebElement> getWebElementsByXPath(WebDriver browser, String webElementName, String xPath)
			throws TestAutomationException {
		logger.debug("WebElementFinderHelper: getWebElementByXPath start ");
		List<WebElement> webElements = browser.findElements(By.xpath(xPath));
		if (webElements == null) {
			throw new TestAutomationException(
					ExceptionErrorCodes.TA011_WEB_ELEMENT_RETRIEVER_ELEMENTS_NOT_FOUND_BY_XPATH_NAME_ERROR,
					"Web elements with  name '" + webElementName + "' and xpath '" + xPath + "' not found.");
		} else {
			logger.debug("WebElementFinderHelper: getWebElementByXPath: Web element with name '" + webElementName
					+ "' found in current screen");
		}
		logger.debug("WebElementFinderHelper: getWebElementByXPath end ");
		return webElements;
	}

	public static List<WebElement> getWebElementsByXPath(WebElement webElement, String webElementName, String xPath)
			throws TestAutomationException {
		logger.debug("WebElementFinderHelper: getWebElementByXPath start ");
		List<WebElement> webElements = webElement.findElements(By.xpath(xPath));
		if (webElements == null) {
			throw new TestAutomationException(
					ExceptionErrorCodes.TA021_WEB_ELEMENT_RETRIEVER_ELEMENTS_NOT_FOUND_BY_XPATH_NAME_ERROR,
					"Web elements with  name '" + webElementName + "' and xpath '" + xPath + "' not found.");
		} else {
			logger.debug("WebElementFinderHelper: getWebElementByXPath: Web element with name '" + webElementName
					+ "' found in current screen");
		}
		logger.debug("WebElementFinderHelper: getWebElementByXPath end ");
		return webElements;
	}

	public static WebElement getWebElementByXPath(WebElement webElement, String webElementName, String xPath)
			throws TestAutomationException {
		logger.debug("WebElementFinderHelper: getWebElementByXPath start ");
		WebElement element = webElement.findElement(By.xpath(xPath));
		if (element == null) {
			throw new TestAutomationException(
					ExceptionErrorCodes.TA030_WEB_ELEMENT_RETRIEVER_ELEMENTS_NOT_FOUND_BY_XPATH_NAME_ERROR,
					"Web elements with  name '" + webElementName + "' and xpath '" + xPath + "' not found.");
		} else {
			logger.debug("WebElementFinderHelper: getWebElementByXPath: Web element with name '" + webElementName
					+ "' found in current screen");
		}
		logger.debug("WebElementFinderHelper: getWebElementByXPath end ");
		return element;
	}

	public static List<WebElement> getWebElementsByClassName(WebDriver browser, String webElementName, String className)
			throws TestAutomationException {
		logger.debug("WebElementFinderHelper: getWebElementsByClassName start ");

		try {
			browser.manage().timeouts().implicitlyWait(Global.TIMEOUT_ZERO, TimeUnit.SECONDS);

			List<WebElement> webElements = browser.findElements(By.className(className));
			if (webElements == null) {
				throw new TestAutomationException(
						ExceptionErrorCodes.TA014_WEB_ELEMENT_RETRIEVER_ELEMENTS_NOT_FOUND_BY_CLASS_NAME_ERROR,
						"Web elements with  name '" + webElementName + "' and class name '" + className
								+ "' not found.");
			} else {
				logger.debug("WebElementFinderHelper: getWebElemenstByClassName: Web element with name '"
						+ webElementName + "' found in current screen");
			}
			logger.debug("WebElementFinderHelper: getWebElementsByClassName end ");

			return webElements;
		} finally {
			browser.manage().timeouts().implicitlyWait(Global.timeout, TimeUnit.SECONDS);
		}
	}

	public static List<WebElement> getWebElementsByClassName(WebElement element, String webElementName, String className)
			throws TestAutomationException {
		logger.debug("WebElementFinderHelper: getWebElementsByClassName start ");
		List<WebElement> webElements = element.findElements(By.className(className));
		if (webElements == null) {
			throw new TestAutomationException(
					ExceptionErrorCodes.TA048_WEB_ELEMENT_RETRIEVER_ELEMENTS_NOT_FOUND_BY_CLASS_NAME_ERROR,
					"Web elements with  name '" + webElementName + "' and class name '" + className + "' not found.");
		} else {
			logger.debug("WebElementFinderHelper: getWebElemenstByClassName: Web element with name '" + webElementName
					+ "' found in current screen");
		}
		logger.debug("WebElementFinderHelper: getWebElementsByClassName end ");
		return webElements;
	}

	public static WebElement getWebElementByClassName(WebDriver browser, String webElementName, String className)
			throws TestAutomationException {
		logger.debug("WebElementFinderHelper: getWebElementByClassName start ");
		WebElement webElement = browser.findElement(By.className(className));
		if (webElement == null) {
			throw new TestAutomationException(
					ExceptionErrorCodes.TA018_WEB_ELEMENT_RETRIEVER_ELEMENT_NOT_FOUND_BY_BROWSER_CLASS_NAME_ERROR,
					"Web elements with  name '" + webElementName + "' and class name '" + className + "' not found.");
		} else {
			logger.debug("WebElementFinderHelper: getWebElementByClassName: Web element with name '" + webElementName
					+ "' found in current screen");
		}
		logger.debug("WebElementFinderHelper: getWebElementByClassName end ");
		return webElement;
	}

	public static WebElement getWebElementByClassName(WebElement element, String webElementName, String className)
			throws TestAutomationException {
		logger.debug("WebElementFinderHelper: getWebElementByClassName start ");
		WebElement webElement = element.findElement(By.className(className));
		if (webElement == null) {
			throw new TestAutomationException(
					ExceptionErrorCodes.TA017_WEB_ELEMENT_RETRIEVER_ELEMENT_NOT_FOUND_BY_ELEMENTS_CLASS_NAME_ERROR,
					"Web elements with  name '" + webElementName + "' and class name '" + className + "' not found.");
		} else {
			logger.debug("WebElementFinderHelper: getWebElementByClassName: Web element with name '" + webElementName
					+ "' found in current screen");
		}
		logger.debug("WebElementFinderHelper: getWebElementByClassName end ");
		return webElement;
	}

	/**
	 * Find the webElement for the label name passed in
	 * 
	 * @param labelName
	 * @return WebElement
	 * @throws TestAutomationException
	 */
	public static WebElement getWebElementByLabelName(WebDriver browser, String labelName)
			throws TestAutomationException {
		logger.debug("WebElementProcessorCommon: getWebElementByLabelName start ");
		return getWebElementById(browser, labelName, new Label(browser, labelName).getLabelForAttribute());

	}

	/**
	 * Returns the id attribute of webelement.
	 * 
	 * @param we
	 * @return
	 * @throws TestAutomationException
	 */
	public static String getWebElementId(WebElement we) throws TestAutomationException {
		String id = null;

		try {
			id = we.getAttribute("id");
		} catch (Exception ex) {
			throw new TestAutomationException(ExceptionErrorCodes.TA115_WEBELEMENT_DOES_NOT_HAVE_ID_ATTRIBUTE,
					"Web Element doesn't have id attribute. Exception thrown when extracting id attribute"
							+ " for web element : /n" + we.getAttribute("innerHTML"), ex);
		}

		return id;
	}

	public static List<WebElement> getWebElementsByCSSSelector(WebDriver browser, String webElementName,
			String cssSelector) throws TestAutomationException {
		logger.debug("WebElementFinderHelper: getWebElementsByClassName start ");

		try {
			browser.manage().timeouts().implicitlyWait(Global.TIMEOUT_ZERO, TimeUnit.SECONDS);

			List<WebElement> webElements = browser.findElements(By.cssSelector(cssSelector));
			if (webElements == null) {
				throw new TestAutomationException(
						ExceptionErrorCodes.TA085_WEB_ELEMENT_RETRIEVER_ELEMENTS_NOT_FOUND_BY_BROWSER_CSS_SELECTOR_ERROR,
						"Web elements with  name '" + webElementName + "' and css selector '" + cssSelector
								+ "' not found.");
			} else {
				logger.debug("WebElementFinderHelper: getWebElemenstByCSSSelector: Web element with name '"
						+ webElementName + "' found in current screen");
			}
			logger.debug("WebElementFinderHelper: getWebElementsByClassName end ");

			return webElements;
		} finally {
			browser.manage().timeouts().implicitlyWait(Global.getTimeout(), TimeUnit.SECONDS);
		}
	}

	public static WebElement getWebElementByCSSSelector(WebDriver browser, String webElementName, String cssSelector)
			throws TestAutomationException {
		logger.debug("WebElementFinderHelper: getWebElementByClassName start ");

		WebElement webElement = browser.findElement(By.cssSelector(cssSelector));
		if (webElement == null) {
			throw new TestAutomationException(
					ExceptionErrorCodes.TA086_WEB_ELEMENT_RETRIEVER_ELEMENT_NOT_FOUND_BY_BROWSER_CSS_SELECTOR_ERROR,
					"Web elements with  name '" + webElementName + "' and css selector '" + cssSelector
							+ "' not found.");
		} else {
			logger.debug("WebElementFinderHelper: getWebElementByCssSelector: Web element with name '" + webElementName
					+ "' found in current screen");
		}
		logger.debug("WebElementFinderHelper: getWebElementByClassName end ");

		return webElement;
	}

	public static List<WebElement> getWebElementsByCSSSelector(WebElement browser, String webElementName,
			String cssSelector) throws TestAutomationException {
		logger.debug("WebElementFinderHelper: getWebElementsByClassName start ");

		List<WebElement> webElements = browser.findElements(By.cssSelector(cssSelector));
		if (webElements == null) {
			throw new TestAutomationException(
					ExceptionErrorCodes.TA087_WEB_ELEMENT_RETRIEVER_ELEMENTS_NOT_FOUND_BY_ELEMENTS_CSS_SELECTOR_ERROR,
					"Web elements with  name '" + webElementName + "' and css selector '" + cssSelector
							+ "' not found.");
		} else {
			logger.debug("WebElementFinderHelper: getWebElemenstByCSSSelector: Web element with name '"
					+ webElementName + "' found in current screen");
		}
		logger.debug("WebElementFinderHelper: getWebElementsByClassName end ");
		return webElements;
	}

	public static WebElement getWebElementByCSSSelector(WebElement browser, String webElementName, String cssSelector)
			throws TestAutomationException {
		logger.debug("WebElementFinderHelper: getWebElementByClassName start ");
		WebElement webElement = browser.findElement(By.cssSelector(cssSelector));
		if (webElement == null) {
			throw new TestAutomationException(
					ExceptionErrorCodes.TA088_WEB_ELEMENT_RETRIEVER_ELEMENT_NOT_FOUND_BY_ELEMENT_CSS_SELECTOR_ERROR,
					"Web elements with  name '" + webElementName + "' and css selector '" + cssSelector
							+ "' not found.");
		} else {
			logger.debug("WebElementFinderHelper: getWebElementByCssSelector: Web element with name '" + webElementName
					+ "' found in current screen");
		}
		logger.debug("WebElementFinderHelper: getWebElementByClassName end ");
		return webElement;
	}

}
