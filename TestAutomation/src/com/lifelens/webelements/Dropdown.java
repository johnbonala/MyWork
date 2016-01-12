package com.lifelens.webelements;

import java.util.ArrayList;
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
import com.lifelens.globals.Global;
import com.lifelens.globals.GlobalCSSIdentifiers;
import com.lifelens.globals.GlobalCSSSelectors;
import com.lifelens.globals.GlobalCommonConstants;
import com.lifelens.globals.GlobalTags;
import com.lifelens.htmlparser.HtmlParser;
import com.lifelens.interpreter.ArgList;
import com.lifelens.interpreter.TestContext;

/**
 * It represents the Dropdown object in lifelens
 * 
 * This helps to get dropdown and select dropdown value
 * 
 * @author Srinivas Pasupulati (co48633)
 * 
 * @version 1.0
 * 
 * @since 28.05.2014
 * 
 *        Dropdown Types handled
 * 
 *        Type1 : Dropdowns with Labels. Both Label and dropdown are linked with
 *        id (label for attribute has the drodpown id
 * 
 *        Type2 : Dropdowns with Labels. but label and dropdowns are not linked.
 * 
 *        Type3 : Multiple dropdowns with single Label and label and dropdowns
 *        are not linked
 * 
 * 
 *        Type4 : Search dropdown in Toolkit (Eg: setup > Benefit > search :
 *        GFRP)
 * 
 *        Type5 : Dropdowns with no Labels. These can be identified with Table
 *        keyword (eg : Table(1,2,3))
 * 
 *        Type6 : Dropdowns with no Labels. These can be identified with
 *        DecisionTable keyword (eg : DecisionTable(Employee gender : male,
 *        Organisational filed 11: Yes))
 * 
 */

public class Dropdown extends RemoteWebElement {

	private String dropdownLabelName;

	private String dropdownValueToBeSelected;
	private WebElement dropdownElement;
	private List<WebElement> dropdownElementsList = null;
	public WebDriver browser;
	private static Logger logger = Logger.getLogger(Dropdown.class.getName());
	private String dropdownId;
	private int index = 0;
	private String dropdownType; // type1 = default, type2 = table
	private static final String SEARCH = "Search";
	private boolean widgetDropdownFlag = false;
	private ArgList dropdownLabelNameParams;
	private WebElement decisionTableDialog = null;
	private TestContext testContext;

	public Dropdown(WebDriver browser, String dropdownLabelName, TestContext testContext)
			throws TestAutomationException {
		setDropdownLabelName(dropdownLabelName);
		dropdownLabelNameParams = new ArgList(dropdownLabelName);
		this.browser = browser;
		this.testContext = testContext;

		if (this.testContext.getLoadIndicator()) {
			this.decisionTableDialog = browser.findElement(By.id(GlobalCSSIdentifiers.DIALOG));
		}

		setDropdownElement(getDropdownElementFromBrowser());
	}

	public Dropdown(WebDriver browser, WebElement dropdownElement) throws TestAutomationException {
		this.browser = browser;
		setDropdownElement(dropdownElement);
	}

	public Dropdown(WebDriver browser, TestContext testContext) throws TestAutomationException {
		this.browser = browser;
		this.testContext = testContext;
	}

	public boolean selectValueFromDropdown(String dropdownValueToBeSelected) throws TestAutomationException {
		logger.debug("Start of Dropdown: selectValueFromDropdown");
		int dropdownCount = 0;
		boolean dropdownValueSelected = false;

		ArgList argments = new ArgList(dropdownValueToBeSelected);

		// For multiple dropdowns
		if (argments.getParmetersCount() > 1 && !isStringNotArgList(dropdownValueToBeSelected)) {
			for (dropdownCount = 0; dropdownCount < argments.getParmetersCount(); dropdownCount++) {
				WebElementProcessorCommon.waitForWebProcessing(browser, Global.getTimeout(), Global.loadIndicatorCSS,
						Global.lazyLoadIndicatorClass);
				setIndex(dropdownCount);
				if (dropdownCount > 0) {
					// set the drop down element from dialog
					if (decisionTableDialog != null && decisionTableDialog.isDisplayed()) {
						setDropdownElementFromDialog(decisionTableDialog);
					} else {
						setDropdownElement(getDropdownElementFromBrowser());
					}
				}

				dropdownValueSelected = clickOnDropdownAndSelectDropdownValue(argments.getParameter(dropdownCount));
			}

		} else {

			dropdownValueSelected = clickOnDropdownAndSelectDropdownValue(dropdownValueToBeSelected);

		}

		if (testContext.getLoadIndicator()) {
			WebElementProcessorCommon.waitForWebProcessing(browser, 30, Global.loadIndicatorCSS,
					Global.lazyLoadIndicatorClass);
		}

		if (!dropdownValueSelected) {
			throw new TestAutomationException(ExceptionErrorCodes.TA112_DROPDOWN_ELEMENT_NOT_FOUND_IN_BROWSER,
					"Dropdown does not contains value: " + dropdownValueToBeSelected);
		}
		logger.debug("End of Dropdown: selectValueFromDropdown");
		return dropdownValueSelected;
	}

	/**
	 * To handle dropdown values which are not meant for arglist
	 * e.g."Integers, negatives in brackets : [1;(1)]"
	 * 
	 * 
	 * @param dropdownValueToBeSelected
	 * @return isStringNotArgList
	 */
	private boolean isStringNotArgList(String dropdownValueToBeSelected) {
		boolean isStringNotArgList = false;

		if (dropdownValueToBeSelected.contains(GlobalCommonConstants.OPENINGBRACKET)
				&& dropdownValueToBeSelected.contains(GlobalCommonConstants.CLOSINGBRACKET)
				&& dropdownValueToBeSelected.contains(GlobalCommonConstants.OPENINGPARENTHESES)
				&& dropdownValueToBeSelected.contains(GlobalCommonConstants.CLOSINGPARENTHESES)) {
			isStringNotArgList = true;
		}

		return isStringNotArgList;
	}

	/**
	 * This is to select the dropdown value from the dropdown
	 * 
	 * @param dropdownValue
	 *            dropdown value that need to bee selected from the dropdown
	 *            list
	 * @return true - if the dropdown value is available in the list and able to
	 *         select false - if the dropdown value is not present
	 * @throws TestAutomationException
	 */
	public boolean clickOnDropdownAndSelectDropdownValue(String dropdownValueToBeSelected)
			throws TestAutomationException {
		logger.debug("Start of Dropdown: selectDropdownValue");
		this.dropdownValueToBeSelected = dropdownValueToBeSelected;
		boolean isSelected = false;

		if (this.testContext.getLoadIndicator()) {
			clickOnDropdown();
			isSelected = selectDropdownValueFromMenuItems(getDropdownMenuItems());
		} else {
			isSelected = selectDropdownValueFromMenuItems(getDropdownMenuItems());
			if (isSelected) {
				getDropdownElement().click();
			}
		}

		WebElementProcessorCommon.logWebElementStatus("Dropdown value", dropdownValueToBeSelected, isSelected);

		logger.debug("End of Dropdown: selectDropdownValue");
		return isSelected;
	}

	/**
	 * Click on the Dropdown element
	 * 
	 * @return dropdown element when click action is successful
	 * @throws TestAutomationException
	 * 
	 * @exception WebElementNotFoundException
	 *                when the Dropdown not found
	 */
	public void clickOnDropdown() throws TestAutomationException {
		logger.debug("Start of Dropdown: clickOnDropdown");

		if (getDropdownElement() != null) {
			if (isWidgetDropdown())
				clickWidgetDropdown();
			else
				getDropdownElement().click();
		}

		if (testContext.getLoadIndicator()) {
			WebElementProcessorCommon.waitForWebProcessing(browser, Global.getTimeout(), Global.loadIndicatorCSS, "");
		}
		if (!getDropdownId().isEmpty()) {
			while (WebElementRetriever.getWebElementsByCSSSelector(browser, "dropdown status check ",
					"#" + getDropdownId() + "[aria-expanded='false']").size() > 0) {
				getDropdownElementFromBrowser().click();
			}
		}

		logger.debug("End of Dropdown: clickOnDropdown");
	}

	/**
	 * This return the dropdown web element corresponding to the label name.
	 * 
	 * @return dropdown element when it find the match
	 * @throws TestAutomationException
	 */
	private WebElement getDropdownElementFromBrowser() throws TestAutomationException {
		try {

			setDropdownId(getDropdownLabelId());
			if (getDropdownId() != null) {
				// getting the drop down elements of the decision table
				if (decisionTableDialog != null && decisionTableDialog.isDisplayed()) {
					if (dropdownLabelName.equalsIgnoreCase(SEARCH)) {
						setWidgetDropdownflag(true);
						return getWidgetDropdown(decisionTableDialog);

					} else if (dropdownLabelNameParams.hasDecisionTableKeyword()) {
						setWidgetDropdownflag(true);
						return getWidgetDropdown();
					} else {
						return WebElementRetriever.getWebElementById(browser, "dialog dropdown by id", getDropdownId());
					}
				} else if (dropdownLabelName.equalsIgnoreCase(SEARCH)) {
					setWidgetDropdownflag(true);
					return getWidgetDropdown();

				} else {
					if (!getDropdownId().equals("")) {
						return WebElementRetriever
								.getWebElementById(browser, "browser dropdown by id", getDropdownId());
					} else {
						// if the id attribute of the dropdown element is not
						// used

						List<WebElement> dropDownsList = browser.findElements(By.tagName(GlobalTags.SELECT));
						int dropDownNumbner = 0;

						if (!dropdownLabelNameParams.getNonTableArgumentListString().isEmpty()) {
							dropDownNumbner = Integer.parseInt(dropdownLabelNameParams.getNonTableArgumentListString());
						}

						if (dropDownNumbner != 0) {
							return dropDownsList.get(dropDownNumbner - 1);
						} else {
							return browser.findElement(By.tagName(GlobalTags.SELECT));
						}
					}
				}
			} else
				return getDropdownWebElementWithLabelNotLinked();

		} catch (TestAutomationException ex) {
			throw new TestAutomationException(ExceptionErrorCodes.TA112_DROPDOWN_ELEMENT_NOT_FOUND_IN_BROWSER,
					"Dropdown element not found in the browser ", ex);
		}
	}

	private void setDropdownElement(WebElement dropdownElement) throws TestAutomationException {
		this.dropdownElement = dropdownElement;
		setDropdownIdFromElement(dropdownElement);

		if (dropdownElement == null) {
			logger.error("dorpdown element is set to null");

			// throw new
			// TestAutomationException(ExceptionErrorCodes.TA110_DROPDOWN_ELEMENT_IS_NULL,
			// "Dropdown element is set to null");
		}
	}

	private WebElement getDropdownElement() {
		return dropdownElement;
	}

	/**
	 * This is to get the dropdown web element when the ID is not present.
	 * Assumption here is dropdown elements are present in the web elements with
	 * class "adjacentComponents"
	 * 
	 * @return WebElement if it is able to find the match.
	 * @throws TestAutomationException
	 * 
	 * @exception WebElementNotFoundException
	 *                when the matched dropdown element not present
	 */
	private WebElement getDropdownWebElementWithLabelNotLinked() throws TestAutomationException {
		logger.debug("Start of Dropdown: getDropdownWebElementWithLabelNotLinked");

		List<WebElement> allDDElementsWithNoLableInCP = null;

		WebElement dropdownElement = null;

		allDDElementsWithNoLableInCP = getDropdownElementsListWithNoLabelLinked();

		if (!allDDElementsWithNoLableInCP.isEmpty()) {
			dropdownElement = getDropdownElement(allDDElementsWithNoLableInCP, dropdownLabelName, getIndex());

			if (dropdownElement == null && LabelFoundInActivePage())
				dropdownElement = WebElementRetriever.getWebElementByTagName(allDDElementsWithNoLableInCP.get(0),
						dropdownLabelName, GlobalTags.TABLE);
			// return first dropdown when label is not linked with dropdown

			WebElementProcessorCommon.logWebElementStatus("Dropdown", dropdownLabelName,
					(dropdownElement == null) ? false : true);

			logger.debug("End of Dropdown: getDropdownWebElementWithNoID");
		}

		setDropdownId(WebElementRetriever.getWebElementId(dropdownElement));
		return dropdownElement;

	}

	private List<WebElement> getDropdownElementsListWithNoLabelLinked() throws TestAutomationException {

		List<WebElement> adjacentComponents = WebElementRetriever.getWebElementsByClassName(browser, dropdownLabelName,
				GlobalCSSIdentifiers.ADJACENTCOMPONENTS);

		return adjacentComponents;

	}

	public boolean selectFundPercentageFromDropdown(String fundName, String dropdownValue)
			throws TestAutomationException {
		logger.debug("Start of Dropdown: selectFundPercentageFromDropdown");
		int tableIndex = 1, refColIndex = 1, dropdownColumnIndex = 3;
		boolean result = false;

		Table table = new Table(browser, testContext, tableIndex);

		WebElement tableDropdown = table.getTableColumn(table.getMatchingRow(fundName, refColIndex),
				dropdownColumnIndex);

		setDropdownElement(WebElementRetriever.getWebElementByClassName(tableDropdown, dropdownLabelName,
				GlobalCSSIdentifiers.DIGITDOWNARROWBUTTON));

		clickOnDropdownAndSelectDropdownValue(dropdownValue);

		if (tableDropdown != null) {
			result = true;
		}

		logger.debug("End of Dropdown: selectFundPercentageFromDropdown");

		return result;

	}

	public List<String> getDropdownValuesList() throws TestAutomationException {

		logger.debug("Start of Dropdown: getDropdownValuesList");

		List<WebElement> allMenuItems = getDropdownMenuItems();

		List<String> dropdownValuesList = new ArrayList<String>();

		for (int i = 0; i < allMenuItems.size(); i++) {

			WebElement dropdownValueElement = allMenuItems.get(i);

			if (dropdownValueElement.isDisplayed()) {

				dropdownValuesList.add(new HtmlParser(dropdownValueElement).getinnerText());

			}

		}

		logger.debug("End of Dropdown: getDropdownValuesList");

		return dropdownValuesList;
	}

	private void setDropdownId(String dropdownId) {

		this.dropdownId = dropdownId;

	}

	private String getDropdownId() {

		return dropdownId;

	}

	private void setDropdownIdFromElement(WebElement dropdownElement) throws TestAutomationException {
		String dropdownId = WebElementRetriever.getWebElementId(dropdownElement);
		if (dropdownId != null)
			setDropdownId(dropdownId);
		else {
			logger.error("Dropdown id is null");
			setDropdownId("");
		}

		// throw new
		// TestAutomationException(ExceptionErrorCodes.TA111_DROPDOWN_ID_IS_NULL,
		// "Dropdown element id is null");
	}

	private WebElement getDropdownValuesListElement() throws TestAutomationException {
		WebElement dropDownValuesListElement = null;

		if (this.testContext.getLoadIndicator()) {
			dropDownValuesListElement = WebElementRetriever.getWebElementById(browser, getDropdownId(), getDropdownId()
					+ "_dropdown");
		} else {
			dropDownValuesListElement = WebElementRetriever
					.getWebElementById(browser, getDropdownId(), getDropdownId());
		}
		return dropDownValuesListElement;

	}

	private List<WebElement> getAllDropdownvalues(WebElement dropdownListElement) throws TestAutomationException {

		if (this.testContext.getLoadIndicator()) {
			return WebElementRetriever.getWebElementsByCSSSelector(dropdownListElement, dropdownLabelName,
					GlobalCSSSelectors.DROPDOWNMENUS);
		} else {
			return WebElementRetriever.getWebElementsByTagName(dropdownListElement, dropdownLabelName,
					GlobalTags.OPTION);
		}
	}

	public String getDropdownType() {

		return dropdownType;

	}

	public void setDropdownType(String dropdownType) {

		this.dropdownType = dropdownType;

	}

	private boolean LabelFoundInActivePage() throws TestAutomationException {

		if (new HtmlParser(WebElementRetriever.getWebElementByTagName(browser, dropdownLabelName, GlobalTags.BODY))
				.getinnerText().contains(dropdownLabelName))
			return true;
		else
			return false;
	}

	private WebElement getDropdownElement(List<WebElement> allDDElementsWithNoLableInCP, String dropdownLabelName,
			int index) throws TestAutomationException {
		List<WebElement> dropdownElementsList = null;
		for (int i = 0; i < allDDElementsWithNoLableInCP.size(); i++) {
			WebElement we = allDDElementsWithNoLableInCP.get(i);
			if (new HtmlParser(we).getinnerText().contains(dropdownLabelName)) {
				dropdownElementsList = getTableElements(we);
				setDropdownElementList(dropdownElementsList);
				return dropdownElementsList.get(index);
			}
		}

		return null;
	}

	private List<WebElement> getTableElements(WebElement we) throws TestAutomationException {

		return WebElementRetriever.getWebElementsByTagName(we, dropdownLabelName, GlobalTags.TABLE);

	}

	private void setDropdownElementList(List<WebElement> dropdownElementsList) {

		this.dropdownElementsList = dropdownElementsList;

	}

	private List<WebElement> getDropdownElementList() {

		return dropdownElementsList;

	}

	private boolean selectDropdownValueFromMenuItems(List<WebElement> allMenuItems) throws TestAutomationException {
		Iterator<WebElement> menuItems = allMenuItems.iterator();
		boolean isSelected = false;
		while (menuItems.hasNext()) {
			WebElement menuItem = menuItems.next();
			String dropdownText = new HtmlParser(menuItem).getinnerText();
			if (dropdownText.equalsIgnoreCase(dropdownValueToBeSelected)) {
				if (!menuItem.isDisplayed())
					continue;
				else {
					menuItem.click();
					isSelected = true;
					break;
				}
			}
		}

		return isSelected;

	}

	private List<WebElement> getDropdownMenuItems() throws TestAutomationException {
		if (!getDropdownId().equals("")) {
			return getAllDropdownvalues(getDropdownValuesListElement());
		} else {
			return WebElementRetriever.getWebElementsByTagName(dropdownElement, dropdownLabelName, GlobalTags.OPTION);
		}

	}

	public boolean verifyDropdownValues(String argumentList) throws TestAutomationException {
		logger.debug("Start of Dropdown: verifyDropdownValues");
		List<String> dropdownValuesFound = new ArrayList<String>();
		List<String> dropdownValuesNotFound = new ArrayList<String>();

		clickOnDropdown();
		ArgList arguments = new ArgList(argumentList);
		List<String> arglistToVerify = arguments.getArgumentList();
		List<String> dropdownValuesList = getDropdownValuesList();
		int arglistSize = arglistToVerify.size();
		for (int i = 0; i < arglistSize; i++) {
			String drpdownValue = arglistToVerify.get(i);
			if (dropdownValuesList.contains(drpdownValue)) {
				dropdownValuesFound.add(drpdownValue);
			} else {
				dropdownValuesNotFound.add(drpdownValue);
			}
		}

		clickOnDropdown(); // close the dropdown
		if (arglistSize == dropdownValuesFound.size()) {
			return true;
		}

		if (logger.isDebugEnabled()) {
			logger.warn("Verification failed : only few values are present in the dropdown list");
			logger.warn("Values not found : " + dropdownValuesNotFound.toString());
			logger.debug("Values found : " + dropdownValuesFound.toString());
			logger.debug("Count of values found : " + dropdownValuesFound.size() + " Expected count: " + arglistSize);
		}

		logger.debug("End of Dropdown: verifyDropdownValues");
		throw new TestAutomationException(ExceptionErrorCodes.TA022_TABLE_ELEMENT_CELL_VALUE_NOT_MATCHING_ERROR,
				"Dropdown values not matched. Count of values found is: " + dropdownValuesFound.size()
						+ " and expected count is: " + arglistSize);
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * Verifies if the dropdown default value matches the text supplied. Can
	 * also pass in 'Enabled' or 'Disabled' to check status of field.
	 * 
	 * @param dropdownLabel
	 *            Label name of dropdown
	 * @param dropdownValue
	 *            The text to match, or 'Enabled'/'Disabled'.
	 * @return
	 * @throws TestAutomationException
	 */
	public boolean verifyDropDownDefaultValue(String dropdownValue) throws TestAutomationException {

		logger.debug("Start of Dropdown: verifyDropDownDefaultValue");
		boolean valueMatches = false;
		String enabledDisabledState = getDropdownElement().getAttribute("disabled");

		if (dropdownValue.equalsIgnoreCase("Enabled") || dropdownValue.equalsIgnoreCase("Disabled")) {
			if (dropdownValue.equalsIgnoreCase("Enabled") && enabledDisabledState == null) {
				valueMatches = true;
			}
			if (dropdownValue.equalsIgnoreCase("Disabled") && enabledDisabledState.equalsIgnoreCase("true")) {
				valueMatches = true;
			}
		} else {

			if (dropdownValue.trim().equals(getDropdownElement().getText())) {
				valueMatches = true;
			} else {
				valueMatches = false;
				logger.error(dropdownValue + " is not value for field: " + dropdownLabelName);
			}
		}

		logger.debug("End of Dropdown: verifyDropDownDefaultValue");
		return valueMatches;
	}

	private String getDropdownLabelName() {
		return dropdownLabelName;
	}

	private void setDropdownLabelName(String dropdownLabelName) {
		this.dropdownLabelName = dropdownLabelName;

	}

	private WebElement getWidgetDropdown() throws TestAutomationException {

		// WebElement toolKit =
		// WebElementRetriever.getWebElementByClassName(decisionTableDialog,
		// getDropdownLabelName(),
		// "toolkit");
		// return (WebElementRetriever.getWebElementByClassName(toolKit,
		// getDropdownLabelName(), "dijitArrowButtonInner"));
		if (!getDropdownId().contains("widget") && !getDropdownId().contains("reflex"))
			setDropdownId("widget_" + getDropdownId());

		return (WebElementRetriever.getWebElementById(browser, "Search widget", getDropdownId()));

	}

	private WebElement getWidgetDropdown(WebElement decisionTableDialog) throws TestAutomationException {

		setDropdownId(new Label(browser, decisionTableDialog, getDropdownLabelName()).getLabelForAttribute());

		if (!getDropdownId().contains("widget"))
			setDropdownId("widget_" + getDropdownId());

		return (WebElementRetriever.getWebElementById(decisionTableDialog, "Search widget", getDropdownId()));

	}

	private void clickWidgetDropdown() throws TestAutomationException {

		(WebElementRetriever.getWebElementByCSSSelector(getDropdownElement(), "Search widget dropdown arrow",
				GlobalCSSSelectors.DROPDOWNARROW)).click();
	}

	private void setWidgetDropdownflag(boolean flag) {
		this.widgetDropdownFlag = flag;
	}

	private boolean isWidgetDropdown() {
		return widgetDropdownFlag;
	}

	private void setDropdownElementFromDialog(WebElement decisionTableDialog) throws TestAutomationException {

		if (getDropdownLabelName().equalsIgnoreCase(SEARCH)) {
			setWidgetDropdownflag(true);
			setDropdownElement(getWidgetDropdown(decisionTableDialog));

		} else {

			setDropdownElement(getDropdownElementFromBrowser());
		}
	}

	private String getDropdownLabelId() throws TestAutomationException {
		String LabelId = null;
		try {
			if (dropdownLabelNameParams.hasDecisionTableKeyword())
				LabelId = new DecisionTable(decisionTableDialog, dropdownLabelNameParams).getDropdownId();
			else if (dropdownLabelNameParams.hasTableKeyword())
				LabelId = new Table(browser, testContext, dropdownLabelNameParams).getDropdownId();
			else
				LabelId = new Label(browser, getDropdownLabelName()).getLabelForAttribute();

		} catch (TestAutomationException ex) {
			logger.info("LabelId is null for the label : " + getDropdownLabelName());
			logger.info(ex);
		}

		return LabelId;
	}
}
