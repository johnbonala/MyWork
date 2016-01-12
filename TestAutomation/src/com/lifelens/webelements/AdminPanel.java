package com.lifelens.webelements;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

import com.lifelens.browsers.WebElementProcessor;
import com.lifelens.exceptions.ExceptionErrorCodes;
import com.lifelens.exceptions.TestAutomationException;
import com.lifelens.globals.Global;
import com.lifelens.htmlparser.HtmlParser;

/**
 * This represents an Admin Panel in lifelens. Helps to identify the Admin Panel
 * object and perform various operations.
 * 
 * @author Alok Chatterji
 * 
 */
public class AdminPanel extends RemoteWebElement {

	private static final String PARENT_CHILD_SECTION = "ParentChildSection";
	private static final String CLOSED = "closed";
	private static final String ANCHOR_TAG = "a";
	private static final String EDIT = "Edit";
	private static final String BUTTON = "button";
	private static final String EXPANDABLE_CONTAINER = "expandableContainer";
	private static final String HEADER = "header";
	private static final String TITLE = "title";
	private static final String ADD = "Add";
	private static final String DELETE = "Delete";
	private static final String CANCEL = "Cancel";
	public WebDriver browser;
	private String headerText;
	private static Logger logger = Logger.getLogger(WebElementProcessor.class.getName());

	public AdminPanel(WebDriver browser, String headerText) {
		this.browser = browser;
		this.headerText = headerText;
	}

	/**
	 * To get the Admin Panel element.
	 * 
	 * @return WebElement
	 * @throws TestAutomationException
	 */
	public WebElement getPanel() throws TestAutomationException {
		List<WebElement> elements = WebElementRetriever.getWebElementsByClassName(browser, "Admin Pannels",
				PARENT_CHILD_SECTION);
		if (elements.size() > 0) {
			for (WebElement element : elements) {
				WebElement we = WebElementRetriever.getWebElementByClassName(element, headerText, TITLE);
				String elementHeader = new HtmlParser(we).getinnerText();
				if (elementHeader.equals(headerText))
					return element;
			}
		}
		throw new TestAutomationException(ExceptionErrorCodes.TA075_ADMIN_PANEL_WEB_ELEMENT_NOT_FOUND_ERROR,
				"Panel Element " + headerText + " not found!!");
	}

	/**
	 * To get all the text with in the Panel.
	 * 
	 * @return String
	 * @throws TestAutomationException
	 */
	public String getPanelText() throws TestAutomationException {
		return new HtmlParser(getPanel()).getinnerText();
	}

	/**
	 * To get the header element of Admin Panel.
	 * 
	 * @return WebElement
	 * @throws TestAutomationException
	 */
	public WebElement getHeaderElement() throws TestAutomationException {
		return WebElementRetriever.getWebElementByClassName(getPanel(), headerText, HEADER);
	}

	/**
	 * To get the body element of Admin Panel
	 * 
	 * @return WebElement
	 * @throws TestAutomationException
	 */
	public WebElement getBodyElement() throws TestAutomationException {
		return WebElementRetriever.getWebElementByClassName(getPanel(), headerText, EXPANDABLE_CONTAINER);
	}

	/**
	 * To get the Edit button element.
	 * 
	 * @return WebElement
	 * @throws TestAutomationException
	 */
	public WebElement getEditButtonElement() throws TestAutomationException {
		WebElement buttonElement = WebElementRetriever.getWebElementByTagName(getHeaderElement(), EDIT, BUTTON);
		if (new HtmlParser(buttonElement).getinnerText().trim().equals(EDIT)) {
			return buttonElement;
		}
		throw new TestAutomationException(ExceptionErrorCodes.TA076_ADMIN_PANEL_EDIT_BUTTON_ELEMENT_NOT_FOUND_ERROR,
				"Edit button element in " + headerText + " is not found!!");
	}

	/**
	 * To get the Add button element.
	 * 
	 * @return WebElement
	 * @throws TestAutomationException
	 */
	public WebElement getAddButtonElement() throws TestAutomationException {
		WebElement decisionTableDialog = browser.findElement(By.id("dialog"));
		String decisionTable = new HtmlParser(decisionTableDialog).getinnerText();
		WebElement buttonElement = null;

		// decision table component checking
		if (decisionTable != null && !(decisionTable.equals(CANCEL))) {
			// Get the 'Add' button element from decision table element if not
			// found get button from page
			try {
				// short timeout as empty header or footer is common
				browser.manage().timeouts().implicitlyWait(Global.TIMEOUT_SHORT, TimeUnit.SECONDS);

				buttonElement = WebElementRetriever.getWebElementByClassName(decisionTableDialog, ADD, "addItemButton");
			} catch (Exception e) {
				buttonElement = WebElementRetriever.getWebElementByClassName(browser, ADD, "addItemButton");
			} finally {
				// back to the default timeout
				browser.manage().timeouts().implicitlyWait(Global.timeout, TimeUnit.SECONDS);
			}
			if (new HtmlParser(buttonElement).getinnerText().trim().equals(ADD)) {
				return buttonElement;
			}
		} else {
			buttonElement = WebElementRetriever.getWebElementByClassName(browser, ADD, "addItemButton");
			if (new HtmlParser(buttonElement).getinnerText().trim().equals(ADD)) {
				return buttonElement;
			}
		}
		throw new TestAutomationException(ExceptionErrorCodes.TA080_ADMIN_PANEL_ADD_BUTTON_ELEMENT_NOT_FOUND_ERROR,
				"Add button not found!!");
	}

	/**
	 * To get the Delete button element.
	 * 
	 * @return WebElement
	 * @throws TestAutomationException
	 */
	public WebElement getDeleteButtonElement() throws TestAutomationException {
		WebElement decisionTableDialog = browser.findElement(By.id("dialog"));
		String decisionTable = new HtmlParser(decisionTableDialog).getinnerText();
		WebElement buttonElement = null;

		// decision table component checking
		if (decisionTable != null && !(decisionTable.equals(CANCEL))) {
			// Get the 'Delete' button element from decision table element if
			// not found get button from page
			try {
				// short timeout as empty header or footer is common
				browser.manage().timeouts().implicitlyWait(Global.TIMEOUT_SHORT, TimeUnit.SECONDS);
				buttonElement = WebElementRetriever.getWebElementByClassName(decisionTableDialog, DELETE,
						"deleteParentButton");
			} catch (Exception e) {
				buttonElement = WebElementRetriever.getWebElementByClassName(browser, DELETE, "deleteParentButton");
			} finally {
				// back to the default timeout
				browser.manage().timeouts().implicitlyWait(Global.timeout, TimeUnit.SECONDS);
			}
			if (new HtmlParser(buttonElement).getinnerText().trim().equals(DELETE)) {
				return buttonElement;
			}
		} else {
			buttonElement = WebElementRetriever.getWebElementByClassName(browser, DELETE, "deleteParentButton");
			if (new HtmlParser(buttonElement).getinnerText().trim().equals(DELETE)) {
				return buttonElement;
			}
		}
		throw new TestAutomationException(ExceptionErrorCodes.TA081_ADMIN_PANEL_DELETE_BUTTON_ELEMENT_NOT_FOUND_ERROR,
				"Delete button not found!!");
	}

	/**
	 * To check if the Panel is editable
	 * 
	 * @return boolean
	 * @throws TestAutomationException
	 */
	public boolean isPanelEditable() throws TestAutomationException {
		try {
			getEditButtonElement();
			logger.info("Edit button found - Panel is editable.");
			return true;
		} catch (NoSuchElementException e) {
			logger.info("Edit button not found - Panel is not editable.");
			return false;
		}
	}

	/**
	 * Expands the panel if it is not already expanded.
	 * 
	 * @throws TestAutomationException
	 */
	public boolean expandPanel() throws TestAutomationException {
		boolean flag;
		if (isPanelClosed()) {
			getHeaderElement().findElement(By.tagName(ANCHOR_TAG)).click();
			logger.info("Panel " + headerText + " expanded successfully.");
			flag = true;
		} else {
			logger.error("Panel " + headerText + " is already open.");
			flag = false;
		}
		return flag;
	}

	/**
	 * Collapse the panel if it is not already in collapsed state.
	 * 
	 * @throws TestAutomationException
	 */
	public boolean collapsePanel() throws TestAutomationException {
		boolean flag;
		if (!isPanelClosed()) {
			getHeaderElement().findElement(By.tagName(ANCHOR_TAG)).click();
			logger.info("Panel " + headerText + " collapsed successfully.");
			flag = true;
		} else {
			logger.error("Panel " + headerText + " is already in collapse state.");
			flag = false;
		}
		return flag;
	}

	/**
	 * To click the Edit button present in the panel.
	 * 
	 * @throws TestAutomationException
	 */
	public boolean clickOnEditButton() throws TestAutomationException {
		boolean flag;
		WebElement editButton = getEditButtonElement();
		if (editButton.isEnabled()) {
			getEditButtonElement().click();
			logger.info("You have clicked on Edit button");
			flag = true;
		} else {
			flag = false;
			logger.error("Edit button is disabled - Panel is not editable.");
		}
		return flag;
	}

	/**
	 * To click the Add button present in the panel.
	 * 
	 * @throws TestAutomationException
	 */
	public boolean clickOnAddButton() throws TestAutomationException {
		boolean flag;

		WebElement navigation = WebElementRetriever.getWebElementByClassName(browser, null, "title");
		String pageTitles = new HtmlParser(navigation).getinnerText();

		// for decision tables
		WebElement decisionTableNavigsation = WebElementRetriever.getWebElementByClassName(browser, null,
				"dijitDialogTitle");
		String addToDecisionTable = new HtmlParser(decisionTableNavigsation).getinnerText();

		if ((pageTitles.equalsIgnoreCase(headerText))
				|| (addToDecisionTable.equalsIgnoreCase(headerText) || addToDecisionTable.equalsIgnoreCase(""))) {

			WebElement addButton = getAddButtonElement();
			if (addButton.isEnabled()) {
				getAddButtonElement().click();
				logger.info("You have clicked on Add button");
				flag = true;
			} else {
				flag = false;
				logger.error("Add button is disabled - Can not add panel.");
			}
		} else {
			flag = false;
		}
		return flag;
	}

	/**
	 * To click the delete button present in the panel.
	 * 
	 * @throws TestAutomationException
	 */
	public boolean clickOnDeleteButton() throws TestAutomationException {
		boolean flag;
		WebElement deleteButton = getDeleteButtonElement();
		if (deleteButton.isEnabled()) {
			getDeleteButtonElement().click();
			logger.info("You have clicked on Delete button");
			flag = true;
		} else {
			flag = false;
			logger.error("Delete button is disabled - Can not delete panel.");
		}
		return flag;
	}

	/**
	 * To verify if the Panel is closed or open.
	 * 
	 * @return boolean
	 * @throws TestAutomationException
	 */
	public boolean isPanelClosed() throws TestAutomationException {
		try {
			return (getHeaderElement().findElement(By.className(CLOSED)) != null);
		} catch (NoSuchElementException e) {
			logger.info("Panel " + headerText + " is open.");
			return false;
		}
	}

}
