package com.lifelens.browsers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.lifelens.exceptions.ExceptionErrorCodes;
import com.lifelens.exceptions.TestAutomationException;
import com.lifelens.globals.Global;

public class WebElementProcessorCommon {

	private static Logger logger = Logger.getLogger(WebElementProcessorCommon.class.getName());

	public static void waitForWebProcessing(WebDriver browser, int timeOutInSeconds, final String lazyloadIndicatorCSS)
			throws TestAutomationException {

		logger.debug("Start of WebElementProcessorCommon: waitForWebProcessing");
		if (logger.isDebugEnabled()) {
			logger.debug("WebElementProcessorCommon: waitForWebProcessing: browser ==" + browser);
			logger.debug("WebElementProcessorCommon: waitForWebProcessing: timeOutInSeconds ==" + timeOutInSeconds);
			logger.debug("WebElementProcessorCommon: waitForWebProcessing: CSS ==" + lazyloadIndicatorCSS);
		}

		try {
			waitForBrowser(browser, timeOutInSeconds);

		} catch (TimeoutException ex) {
			throw new TestAutomationException(ExceptionErrorCodes.TA007_WEB_ELEMENT_PROCESSING_COMMON_TIMED_OUT_ERROR,
					ex.getMessage() + " : Web content not loaded with in the timeout period", ex);
		}
		logger.debug("End of WebElementProcessorCommon: waitForWebProcessing");
	}

	private static void waitForBrowser(WebDriver browser, int timeOutInSeconds) {

		try {
			// nullify the implicit wait
			browser.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);

			new WebDriverWait(browser, timeOutInSeconds) {
			}.until(new ExpectedCondition<Boolean>() {

				@Override
				public Boolean apply(WebDriver driver) {
					return !driver.findElement(By.cssSelector(Global.lazyLoadIndicatorClass)).isDisplayed();
				}
			});

		} finally {
			// reset the implicit wait
			browser.manage().timeouts().implicitlyWait(timeOutInSeconds, TimeUnit.SECONDS);
		}
	}

	public static void waitForWebProcessing(WebDriver browser, int timeOutInSeconds, final String loadIndicatorCSS,
			final String lazyLoadClass) throws TestAutomationException {
		logger.debug("Start of WebElementProcessorCommon: waitForWebProcessing");
		if (logger.isDebugEnabled()) {
			logger.debug("WebElementProcessorCommon: waitForWebProcessing: browser ==" + browser);
			logger.debug("WebElementProcessorCommon: waitForWebProcessing: timeOutInSeconds ==" + timeOutInSeconds);
			logger.debug("WebElementProcessorCommon: waitForWebProcessing: CSS ==" + loadIndicatorCSS);
			logger.debug("WebElementProcessorCommon: waitForWebProcessing: lazyLoadIndicator ==" + lazyLoadClass);

		}

		try {
			// make the implicit wait zero
			browser.manage().timeouts().implicitlyWait(Global.TIMEOUT_ZERO, TimeUnit.SECONDS);

			// and then apply this custom explicit wait
			new WebDriverWait(browser, timeOutInSeconds) {
			}.until(new ExpectedCondition<Boolean>() {

				@Override
				public Boolean apply(WebDriver driver) {
					// System.out.println(lazyLoadClass + " " +
					// loadIndicatorCSS);
					// String loadIndicatorCSS = "div#loadIndicator";
					if (StringUtils.isBlank(loadIndicatorCSS)) {
						if (StringUtils.isBlank(lazyLoadClass))
							return true;
						else
							return (driver.findElements(By.className(lazyLoadClass)).size() == 0);
					} else if (StringUtils.isBlank(lazyLoadClass))
						return (!driver.findElement(By.cssSelector(loadIndicatorCSS)).isDisplayed());
					else {
						return (driver.findElements(By.className(lazyLoadClass)).size() == 0)
								&& (!driver.findElement(By.cssSelector(loadIndicatorCSS)).isDisplayed());
					}
				}
			});

		} catch (TimeoutException ex) {
			throw new TestAutomationException(ExceptionErrorCodes.TA007_WEB_ELEMENT_PROCESSING_COMMON_TIMED_OUT_ERROR,
					ex.getMessage() + " : Web content not loaded with in the timeout period", ex);
		} finally {
			// make sure we are back at the default wait when we leave
			browser.manage().timeouts().implicitlyWait(Global.timeout, TimeUnit.SECONDS);
		}

		logger.debug("End of WebElementProcessorCommon: waitForWebProcessing");
	}

	public static void scrollToWebElement(WebDriver browser, WebElement we) throws TestAutomationException {
		logger.debug("Start of WebElementProcessorCommon: scrollToWebElement");

		try {
			((JavascriptExecutor) browser).executeScript("arguments[0].scrollIntoView(true);", we);
			Thread.sleep(500);
		} catch (InterruptedException ex) {
			throw new TestAutomationException(ExceptionErrorCodes.TA008_WEB_ELEMENT_PROCESSING_COMMON_INTERUPTED_ERROR,
					ex.getMessage() + " : Unable to scroll through web content", ex);
		}

		logger.debug("End of WebElementProcessorCommon: scrollToWebElement");
	}

	/**
	 * Resolve a relative url (e.g. ../../filename) using the url of the current
	 * page.
	 * 
	 * @param url
	 *            the relative url to resolve
	 * @return the url resolved to the current page
	 * @throws TestAutomationException
	 */
	public static String resolveUri(WebDriver browser, String url) throws TestAutomationException {
		logger.debug("Start of WebElementProcessorCommon: resolveUri");

		String resolvedUrl = null;
		if (url.startsWith("http")) {
			resolvedUrl = url;
		} else {
			String currentUrl = browser.getCurrentUrl();
			URI resolvedUri = null;
			URI currentUri = null;

			try {
				currentUri = new URI(currentUrl);
			} catch (URISyntaxException ex) {
				throw new TestAutomationException(
						ExceptionErrorCodes.TA020_WEB_ELEMENT_PROCESSING_COMMON_URI_SYNTAX_ERROR, ex.getMessage()
								+ " : URI syntax exception on '" + currentUrl + "'", ex);
			}

			try {
				URI uri = new URI(url);
				resolvedUri = currentUri.resolve(uri);
			} catch (URISyntaxException ex) {
				throw new TestAutomationException(
						ExceptionErrorCodes.TA020_WEB_ELEMENT_PROCESSING_COMMON_URI_SYNTAX_ERROR, ex.getMessage()
								+ " : URI syntax exception on '" + url + "'", ex);
			}

			logger.debug("url " + url + " resolved to " + resolvedUri);
			resolvedUrl = resolvedUri.toString();

		}
		logger.debug("End of WebElementProcessorCommon: resolveUri");

		return resolvedUrl;
	}

	/**
	 * Store the session that was established by Selenium which includes the
	 * userid & password given at login. Extract the cookies that are kept in
	 * Selenium's WebDriver object, and transform them to Apache's CookieStore
	 * object.
	 * 
	 * @param browser
	 * 
	 * @return CookieStore - the Apache CookieStore object
	 */
	public static CookieStore storeSeleniumCookies(WebDriver browser) {
		logger.debug("Start of WebElementProcessorCommon: storeSeleniumCookies");
		Set<Cookie> seleniumCookies = browser.manage().getCookies();
		CookieStore cookieStore = new BasicCookieStore();
		for (Cookie seleniumCookie : seleniumCookies) {
			BasicClientCookie basicClientCookie = new BasicClientCookie(seleniumCookie.getName(),
					seleniumCookie.getValue());
			basicClientCookie.setDomain(seleniumCookie.getDomain());
			basicClientCookie.setExpiryDate(seleniumCookie.getExpiry());
			basicClientCookie.setPath(seleniumCookie.getPath());
			cookieStore.addCookie(basicClientCookie);
		}
		logger.debug("End of WebElementProcessorCommon: storeSeleniumCookies");
		return cookieStore;
	}

	public static void logWebElementStatus(String webElementType, String webElementName, boolean found) {
		if (logger.isDebugEnabled()) {
			if (found) {
				logger.debug(webElementType + " '" + webElementName + "' found in the current page!!");
			} else {
				logger.debug(webElementType + " '" + webElementName + "' not found in the current page!!");
			}
		}
	}

	public static void waitForWebElementTobeEnabled(WebDriver browser, int timeOutInSeconds, final String name)
			throws TestAutomationException {
		logger.debug("Start of WebElementProcessorCommon: waitForWebElementTobeEnabled");
		if (logger.isDebugEnabled()) {
			logger.debug("WebElementProcessorCommon: waitForWebProcessing: browser ==" + browser);
			logger.debug("WebElementProcessorCommon: waitForWebProcessing: timeOutInSeconds ==" + timeOutInSeconds);
			logger.debug("WebElementProcessorCommon: waitForWebProcessing: name ==" + name);
		}

		try {
			browser.manage().timeouts().implicitlyWait(Global.TIMEOUT_ZERO, TimeUnit.SECONDS);

			new WebDriverWait(browser, timeOutInSeconds) {
			}.until(new ExpectedCondition<Boolean>() {

				@Override
				public Boolean apply(WebDriver driver) {
					return ((driver.findElement(By.name(name))).isEnabled());
				}
			});

		} catch (TimeoutException ex) {
			throw new TestAutomationException(
					ExceptionErrorCodes.TA074_WAIT_FOR_WEBELEMENT_TOBE_ENABELED_TIMEEDOUT_ERROR, ex.getMessage()
							+ " : Web element is not enabled with in the timeout period", ex);
		} catch (WebDriverException wex) {
			throw new TestAutomationException(
					ExceptionErrorCodes.TA074_WAIT_FOR_WEBELEMENT_TOBE_ENABELED_TIMEEDOUT_ERROR, wex.getMessage()
							+ " : Web driver is not enabled with in the timeout period", wex);
		} finally {
			browser.manage().timeouts().implicitlyWait(Global.timeout, TimeUnit.SECONDS);
		}
		logger.debug("End of WebElementProcessorCommon: waitForWebElementTobeEnabled");
	}

	/**
	 * Test if the input string contains only digits (i.e. is a positive
	 * integer)
	 * 
	 * @param str
	 *            String to test
	 * @return boolean indicating if input string is a positive integer
	 */
	public static boolean isNumeric(String str) {
		return str.matches("^\\d+$");
	}
}
