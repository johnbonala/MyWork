package com.lifelens.webelements;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

import com.lifelens.browsers.WebElementProcessorCommon;
import com.lifelens.exceptions.TestAutomationException;
import com.lifelens.globals.GlobalCSSIdentifiers;
import com.lifelens.globals.GlobalTags;
import com.lifelens.htmlparser.HtmlParser;

/**
 * It represents the Spinner element in lifelens This helps to identify the
 * Spinner element and provide input to it. It is currently used as TimeSpinner,
 * to input current hours and minutes in Scheduled Import
 * 
 * @author Jayanti Rode (CO53985)
 * 
 * @since 18.08.2014
 * 
 */

public class Spinner extends RemoteWebElement {

	private static Logger logger = Logger.getLogger(Spinner.class.getName());
	private String spinnerName;
	private WebDriver browser;

	/**
	 * This is a type 1 keyword, accepts spinner name as a input
	 * 
	 * @param browser
	 * @param spinnerName
	 * @throws TestAutomationException
	 */
	public Spinner(WebDriver browser, String spinnerName) throws TestAutomationException {
		this.spinnerName = spinnerName;
		this.browser = browser;
	}

	/**
	 * It identifies the spinner element by id and send keys as current hour and
	 * minute to input text boxes
	 * 
	 * @return
	 * @throws TestAutomationException
	 */
	public boolean sendKeys() throws TestAutomationException {
		logger.debug("Start of Spinner: sendKeys");
		WebElement spinner = getSpinner();
		WebElement hourSpinner = spinner.findElement(By.id(GlobalCSSIdentifiers.HOURSPINNER));
		WebElement minuteSpinner = spinner.findElement(By.id(GlobalCSSIdentifiers.MINUTESPINNER));
		Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());

		int currentHour = localCalendar.get(Calendar.HOUR_OF_DAY);
		int currentMinute = localCalendar.get(Calendar.MINUTE);

		boolean isInputEnabled = false;
		if (hourSpinner != null) {
			if (hourSpinner.isEnabled()) {
				if (StringUtils.isNotEmpty(hourSpinner.getAttribute("value"))) {
					String hourDefaultValue = hourSpinner.getAttribute("value");
					for (int i = 0; i < hourDefaultValue.length(); i++) {
						hourSpinner.sendKeys("\u0008");
					}
					hourSpinner.sendKeys(Integer.toString(currentHour));
					isInputEnabled = true;
				} else {
					hourSpinner.sendKeys(Integer.toString(currentHour));
					isInputEnabled = true;
				}
			}
		}
		if (minuteSpinner != null) {
			if (minuteSpinner.isEnabled()) {
				if (StringUtils.isNotEmpty(minuteSpinner.getAttribute("value"))) {
					String minuteDefaultValue = minuteSpinner.getAttribute("value");
					for (int i = 0; i < minuteDefaultValue.length(); i++) {
						minuteSpinner.sendKeys("\u0008");
					}
					minuteSpinner.sendKeys(Integer.toString(currentMinute));
					isInputEnabled = true;
				} else {
					minuteSpinner.sendKeys(Integer.toString(currentMinute));
					isInputEnabled = true;
				}
			}
		}
		WebElementProcessorCommon.logWebElementStatus("TimeSpinner", spinnerName, isInputEnabled);
		logger.debug("End of Spinner: sendKeys");
		return isInputEnabled;
	}

	/**
	 * It retrieves the spinner element by classname and return it
	 * 
	 * @return
	 * @throws TestAutomationException
	 */
	private WebElement getSpinner() throws TestAutomationException {
		WebElement timeSpinner = null;

		try {
			timeSpinner = WebElementRetriever.getWebElementByClassName(browser, spinnerName,
					GlobalCSSIdentifiers.DOJOTIMESPINNER);
		} catch (Exception e) {

			List<WebElement> divs = browser.findElements(By.tagName(GlobalTags.DIV));
			List<WebElement> innerdivs = new ArrayList<WebElement>();

			for (int i = 0; i < divs.size(); i++) {
				if ((new HtmlParser(divs.get(i)).getinnerText()).contains(spinnerName)) {
					innerdivs = WebElementRetriever.getWebElementsByTagName(divs.get(i), spinnerName, GlobalTags.DIV);
					for (int j = 0; j < innerdivs.size(); j++) {
						if ((new HtmlParser(innerdivs.get(j)).getinnerText()).equalsIgnoreCase(spinnerName)) {
							timeSpinner = WebElementRetriever.getWebElementByClassName(innerdivs.get(j), spinnerName,
									GlobalCSSIdentifiers.DIGITINPUTINNER);
						}
					}
				}
			}
		}

		return timeSpinner;
	}
}