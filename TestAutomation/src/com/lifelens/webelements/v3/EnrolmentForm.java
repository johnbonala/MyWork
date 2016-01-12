package com.lifelens.webelements.v3;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

import com.lifelens.exceptions.ExceptionErrorCodes;
import com.lifelens.exceptions.TestAutomationException;
import com.lifelens.globals.Global;
import com.lifelens.htmlparser.HtmlParser;
import com.lifelens.webelements.WebElementRetriever;

/**
 * This class represents the enrolment form, which is a list of all the
 * employee's benefits. Each benefit has a button associated with it (usually
 * "Change" or "View"). This class allows us to click on this button by
 * supplying the benefit name.
 */
public class EnrolmentForm extends RemoteWebElement {

	private static Logger logger = Logger.getLogger(EnrolmentForm.class.getName());
	private static final String BENEFIT_CSS_IDENTIFIER = ".enrolmentData.BenefitAndStrapline";
	private static final String ACTION_BUTTON_CSS_IDENTIFIER = ".actions.last";
	public WebDriver browser;
	private String benefitName;

	public EnrolmentForm(WebDriver browser, String benefitName) {
		this.browser = browser;
		this.benefitName = benefitName;
	}

	public boolean clickEnrolmentFormBenefitButton() throws TestAutomationException {
		logger.debug("Start of EnrolmentForm: clickEnrolmentFormBenefitButton ");

		try {
			browser.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
			WebElement buttonElement = null;
			boolean isButtonFound = false;

			try {
				buttonElement = getBenefitActionButton();
				if (buttonElement != null) {
					buttonElement.click();
					isButtonFound = true;
				}
			} catch (TestAutomationException te) {
				isButtonFound = false;
				logger.info(te.getMessage());
			}

			logger.debug("End of EnrolmentForm: clickEnrolmentFormBenefitButton ");
			return isButtonFound;
		} finally {
			browser.manage().timeouts().implicitlyWait(Global.getTimeout(), TimeUnit.SECONDS);
		}
	}

	/**
	 * Get the action button which corresponds to the benefit selected.
	 * 
	 * @return WebElement button
	 * @throws TestAutomationException
	 */
	public WebElement getBenefitActionButton() throws TestAutomationException {
		logger.debug("Start of EnrolmentForm: getBenefitActionButton");
		List<WebElement> benefitList;
		WebElement actionButton;
		List<WebElement> actionButtons;

		benefitList = WebElementRetriever.getWebElementsByCSSSelector(browser, benefitName, BENEFIT_CSS_IDENTIFIER);

		int index = getBenefitIndex(benefitList);

		List<WebElement> actionButtonsParents = WebElementRetriever.getWebElementsByCSSSelector(browser, "Button",
				ACTION_BUTTON_CSS_IDENTIFIER);

		// (index - 1) gives the java zero-based index number. However here we
		// use (index + 1) as we want to skip the first 2 occurrences of
		// ACTION_BUTTON_CSS_IDENTIFIER which are header & footer as we're only
		// interested in the benefits.

		// short timeout as empty header or footer is common
		try {
			browser.manage().timeouts().implicitlyWait(Global.TIMEOUT_SHORT, TimeUnit.SECONDS);
			actionButtons = actionButtonsParents.get(index + 1).findElements(By.className("primary"));
		} finally {
			// back to the default timeout
			browser.manage().timeouts().implicitlyWait(Global.timeout, TimeUnit.SECONDS);
		}
		if (actionButtons.size() == 0) {
			throw new TestAutomationException(ExceptionErrorCodes.TA100_ENROLMENT_FORM_BUTTON_NOT_FOUND,
					"No button found for benefit name '" + benefitName + "'");
		} else {
			actionButton = actionButtons.get(0);
		}

		logger.debug("End of EnrolmentForm: getBenefitActionButton");
		return actionButton;
	}

	/**
	 * Find the occurrence number of the benefit in the list.
	 * 
	 * @param benefitTaglist
	 *            List of benefits.
	 * @return occurrence number of the benefit selected.
	 * @throws TestAutomationException
	 */
	public int getBenefitIndex(List<WebElement> benefitTaglist) throws TestAutomationException {
		logger.debug("Start of EnrolmentForm: getBenefitIndex");

		WebElement button = null;
		boolean benefitFound = false;
		int buttonHitCount = 1;

		Iterator<WebElement> allBenefits = benefitTaglist.iterator();
		while (allBenefits.hasNext() && benefitFound == false) {
			button = allBenefits.next();
			String buttonName = new HtmlParser(button).getinnerText();
			if (buttonName.equalsIgnoreCase(benefitName)) {
				benefitFound = true;
			} else {
				buttonHitCount++;
			}
		}

		if (!benefitFound) {
			throw new TestAutomationException(ExceptionErrorCodes.TA099_ENROLMENT_FORM_BENEFIT_NOT_FOUND,
					"Benefit name '" + benefitName + "' not found in " + (buttonHitCount - 1) + " benefits.");
		}

		logger.debug("End of EnrolmentForm: getBenefitIndex");
		return buttonHitCount;
	}
}
