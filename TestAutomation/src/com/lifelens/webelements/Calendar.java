package com.lifelens.webelements;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

import com.lifelens.browsers.WebElementProcessorCommon;
import com.lifelens.exceptions.ExceptionErrorCodes;
import com.lifelens.exceptions.TestAutomationException;
import com.lifelens.globals.GlobalCSSIdentifiers;
import com.lifelens.globals.GlobalCSSSelectors;
import com.lifelens.htmlparser.HtmlParser;
import com.lifelens.interpreter.ArgList;
import com.lifelens.interpreter.TestContext;

/**
 * It represents the date picker object in lifelen's which selects the date from
 * the calendar
 * 
 * @author Pratik.Gilda(CO50636)
 * 
 * @version 1.0
 * 
 * @since 17.10.2014
 * 
 */
public class Calendar extends RemoteWebElement {

	/** The label name for the calendar field */
	private String labelName;

	/** The browser */
	private WebDriver browser;

	/** The test context */
	private TestContext testContext;

	/** The calendar element */
	private WebElement calendar;

	/** The calendar id */
	private String calendarId;

	/** The logger */
	private static Logger logger = Logger.getLogger(Calendar.class.getName());

	/** The day,month and year */
	int day, month, year;

	/** The widget calendar */
	private WebElement widgetCalender;

	/** The widget pop up */
	private WebElement popUp;

	/** The labelNameParams for Table and Decision Table calendar element */
	private ArgList labelNameParams;

	/** The decisionTableDialog */
	private WebElement decisionTableDialog = null;

	/**
	 * Instantiates a new calendar
	 * 
	 * @param browser
	 *            the browser
	 * @param labelName
	 *            the labelName
	 * @param testContext
	 *            the testContext
	 */
	public Calendar(WebDriver browser, String labelName, TestContext testContext) throws TestAutomationException {
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

		calendar = getCalendar();
		calendarId = WebElementRetriever.getWebElementId(calendar);
		setCalendarId(calendarId);
	}

	/**
	 * This is to select the date from the calendar
	 * 
	 * @param dateToBeSelected
	 *            dateToBeSelected
	 * 
	 * @return true - if the date is available in the list and able to select
	 *         false - if the date is not present
	 * 
	 * @throws TestAutomationException
	 */
	public boolean selectDateFromCalendar(String dateToBeSelected) throws TestAutomationException {
		logger.debug("Start of calendar: selectValueFromCalendar");
		ArgList argments = new ArgList(dateToBeSelected);

		if (argments.getParmetersCount() == 3) {
			day = argments.getNumericParameter(0);
			month = argments.getNumericParameter(1);
			year = argments.getNumericParameter(2);
		} else {
			throw new TestAutomationException(ExceptionErrorCodes.TA024_ARG_LIST_INVALID_INPUT_ARGUMENTS_LENGTH_ERROR,
					" argParams length is " + argments.getParmetersCount()
							+ ". But it should be 3 for date value in DD,MM,YYYY format ");
		}

		boolean isCalenderExists = clickWidgetCalender();

		if (isCalenderExists) {
			selectDate(month);
		}

		WebElementProcessorCommon.logWebElementStatus("Calendar Date value", dateToBeSelected, isCalenderExists);

		logger.debug("End of calendar: selectValueFromCalendar");
		return isCalenderExists;
	}

	/**
	 * Get the calendar element
	 * 
	 * @return calendar
	 * @throws TestAutomationException
	 */
	private WebElement getCalendar() throws TestAutomationException {
		WebElement calendar = null;

		if (labelNameParams.hasDecisionTableKeyword()) {
			calendar = new DecisionTable(decisionTableDialog, labelNameParams).getInputbox();
		} else if (labelNameParams.hasTableKeyword()) {
			calendar = new Table(browser, testContext, labelNameParams).getInputbox();
		} else {
			calendar = WebElementRetriever.getWebElementByLabelName(browser, labelName);
		}

		return calendar;
	}

	/**
	 * This is to click on the arrow button of the calendar
	 * 
	 * @return true if calendar is opened
	 * 
	 * @throws TestAutomationException
	 */
	private boolean clickWidgetCalender() throws TestAutomationException {
		boolean isCalenderExists = false;
		widgetCalender = WebElementRetriever.getWebElementByCSSSelector(getWidgetCalendar(),
				"Search widget dropdown arrow", GlobalCSSSelectors.DROPDOWNARROW);

		setWidgetCalender(widgetCalender);

		if (widgetCalender != null) {
			widgetCalender.click();
			isCalenderExists = true;
		}

		return isCalenderExists;
	}

	/**
	 * Set the widget calendar
	 * 
	 * @param widgetCalender
	 * 
	 * @throws TestAutomationException
	 */
	private void setWidgetCalender(WebElement widgetCalender) throws TestAutomationException {
		this.widgetCalender = widgetCalender;
		setCalendarIdFromElement(widgetCalender);

		if (widgetCalender == null) {
			logger.error("dorpdown element is set to null");
		}
	}

	/**
	 * Set the widget calendar id
	 * 
	 * @param widgetCalender
	 * 
	 * @throws TestAutomationException
	 */
	private void setCalendarIdFromElement(WebElement widgetCalender) throws TestAutomationException {
		String calendarId = WebElementRetriever.getWebElementId(widgetCalender);
		if (calendarId != null)
			setCalendarId(calendarId);
		else {
			logger.error("Calendar id is null");
			setCalendarId("");
		}
	}

	/**
	 * This is to select date from calendar
	 * 
	 * @param month
	 * 
	 * @return result
	 * 
	 * @throws TestAutomationException
	 */
	private boolean selectDate(int month) throws TestAutomationException {
		boolean isMonthSelected, isYearSelected, result = false;

		popUp = WebElementRetriever.getWebElementById(browser, "Calendar Month's", getCalendarId() + "_popup");

		isMonthSelected = selectMonthFromMonthList();

		isYearSelected = selectYearFromYearContainer();

		if (isMonthSelected && isYearSelected) {
			result = selectDayFromDayContainer();
		}

		return result;
	}

	/**
	 * This is to select year from years container
	 * 
	 * @return result
	 * 
	 * @throws TestAutomationException
	 */
	private boolean selectYearFromYearContainer() throws TestAutomationException {
		boolean isYearSelected = false;

		WebElement calendarYear = WebElementRetriever.getWebElementByCSSSelector(popUp, "Calendar Tear",
				GlobalCSSSelectors.CALENDAR_YEAR_LABEL);

		WebElement currentYearElement = WebElementRetriever.getWebElementByCSSSelector(calendarYear, "CurrentYear",
				GlobalCSSSelectors.CALENDAR_CURRENT_YEAR_LABEL);

		String yearValue = new HtmlParser(currentYearElement).getinnerText();
		int currentYear = Integer.parseInt(yearValue);

		if (year == currentYear) {
			// If year to be selected is current year
			isYearSelected = true;
		} else if (year > currentYear) {
			// If year to be selected is next year
			int increaseYear = year - currentYear;
			WebElement increaseYearValue;
			increaseYearValue = WebElementRetriever.getWebElementByCSSSelector(calendarYear, "Increse Year",
					GlobalCSSSelectors.CALENDAR_NEXT_YEAR_LABEL);

			for (int j = 0; j < increaseYear; j++) {
				increaseYearValue.click();
				isYearSelected = true;
			}
		} else if (year < currentYear) {
			// If year to be selected is previous year
			int decreaseYear = currentYear - year;
			WebElement decreaseYearValue;
			decreaseYearValue = WebElementRetriever.getWebElementByCSSSelector(calendarYear, "Decrease Year",
					GlobalCSSSelectors.CALENDAR_PREVIOUS_YEAR_LABEL);

			for (int j = 0; j < decreaseYear; j++) {
				decreaseYearValue.click();
				isYearSelected = true;
			}
		}

		return isYearSelected;
	}

	/**
	 * This is to select month from months container
	 * 
	 * @return result
	 * 
	 * @throws TestAutomationException
	 */
	private boolean selectMonthFromMonthList() throws TestAutomationException {
		List<WebElement> calendarMonths = WebElementRetriever.getWebElementsByCSSSelector(popUp, "Calendar Month's",
				GlobalCSSSelectors.CALENDAR_MONTHS);
		Iterator<WebElement> menuItems = calendarMonths.iterator();
		popUp = WebElementRetriever.getWebElementById(browser, "Calender Pop Up", getCalendarId() + "_popup");
		boolean isMonthSelected = false;

		while (menuItems.hasNext()) {
			WebElement menuItem = menuItems.next();
			String monthValue = new HtmlParser(menuItem).getinnerText();

			if (menuItem.isDisplayed()) {
				int index = 0;
				for (int j = 0; j < calendarMonths.size(); j++) {
					if (monthValue.equals(new HtmlParser(calendarMonths.get(j)).getinnerText())) {
						index = j;
						break;
					}
				}

				if (index == (month - 1)) {
					// If month to be selected is current month
					isMonthSelected = true;
				} else if (index > (month - 1)) {
					// If month to be selected is previous month
					int decreaseMonth = index - (month - 1);

					WebElement previousMonth = WebElementRetriever.getWebElementByCSSSelector(popUp, "Decrease month",
							GlobalCSSSelectors.CALENDAR_PREVIOUS_MONTH);

					for (int i = 0; i < decreaseMonth; i++) {
						previousMonth.click();
						isMonthSelected = true;
					}
				} else if (index < (month - 1)) {
					// If month to be selected is next month
					int increaseMonth = (month - 1) - index;

					WebElement nextMonth = WebElementRetriever.getWebElementByCSSSelector(popUp, "Decrease month",
							GlobalCSSSelectors.CALENDAR_NEXT_MONTH);

					for (int i = 0; i < increaseMonth; i++) {
						nextMonth.click();
						isMonthSelected = true;
					}
				}
			}
		}

		return isMonthSelected;
	}

	/**
	 * This is to select day from days container
	 * 
	 * @return result
	 * 
	 * @throws TestAutomationException
	 */
	private boolean selectDayFromDayContainer() throws TestAutomationException {
		boolean isDaySelected = false;
		List<WebElement> calendarDays = WebElementRetriever.getWebElementsByCSSSelector(popUp, "Calendar Day's",
				GlobalCSSSelectors.CALENDAR_CURRENT_MONTH_LABEL);
		Iterator<WebElement> menuItems = calendarDays.iterator();
		String dayToBeSelected = Integer.toString(day);

		while (menuItems.hasNext()) {
			WebElement menuItem = menuItems.next();
			String dayValue = new HtmlParser(menuItem).getinnerText();
			if (dayValue.equals(dayToBeSelected)) {
				menuItem.click();
				isDaySelected = true;
				break;
			}
		}

		return isDaySelected;
	}

	/**
	 * Get the widgetCalendar
	 * 
	 * @return the widgetCalendar
	 */
	private WebElement getWidgetCalendar() throws TestAutomationException {
		if (!getCalendarId().contains("widget"))
			setCalendarId("widget_" + getCalendarId());

		return (WebElementRetriever.getWebElementById(browser, "Widget Calendar", getWidgetCalendarId()));
	}

	/**
	 * Get the widgetCalendarId
	 * 
	 * @return the widgetCalendarId
	 */
	private String getWidgetCalendarId() throws TestAutomationException {
		String calenderId = WebElementRetriever.getWebElementId(calendar);
		String widgetCalendarId = "widget_" + calenderId;

		return widgetCalendarId;
	}

	/**
	 * Get the calendarId
	 * 
	 * @return the calendarId
	 */
	private String getCalendarId() throws TestAutomationException {
		calendarId = WebElementRetriever.getWebElementId(calendar);
		return calendarId;
	}

	/**
	 * Set the calendarId
	 * 
	 * @param calendarId
	 *            the calendar id
	 */
	private void setCalendarId(String calendarId) {
		this.calendarId = calendarId;
	}
}
