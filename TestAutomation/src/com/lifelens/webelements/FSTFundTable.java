package com.lifelens.webelements;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

import com.lifelens.browsers.WebElementProcessorCommon;
import com.lifelens.exceptions.ExceptionErrorCodes;
import com.lifelens.exceptions.TestAutomationException;
import com.lifelens.globals.Global;
import com.lifelens.globals.GlobalAttributes;
import com.lifelens.globals.GlobalCSSIdentifiers;
import com.lifelens.globals.GlobalTags;
import com.lifelens.htmlparser.HtmlParser;
import com.lifelens.interpreter.ArgList;

/**
 * It represents the web FST table object. Web tables are present in FST tool in
 * lifelens both in pre join and post join Eg. Lifestyle profiles, core funds
 * table
 * 
 * This helps to select funds and profiles from the FST Tables. This will be
 * further extended to select the webelements (buttons, checkbox, redio boxes)
 * inside the table.
 * 
 * @author Srinivas Pasupulati (co48633)
 * 
 * @version 1.0
 * 
 * @since 15.05.2014
 * 
 */
public class FSTFundTable extends RemoteWebElement {

	private WebDriver browser;
	private WebElement table;
	private int fundNameColIndex = 1;
	private int checkboxColIndex = 6;
	private int fundPercentageInputboxColIndex = 6;
	private int fundFactsheetColIndex = 5;
	private String fundName;
	private static Logger logger = Logger.getLogger(FSTFundTable.class.getName());

	/**
	 * 
	 * @param browser
	 *            browser object
	 */
	public FSTFundTable(WebDriver browser) {
		this.browser = browser;
	}

	/**
	 * 
	 * @param browser
	 *            browser object
	 * @param fundName
	 *            String fundName
	 */
	public FSTFundTable(WebDriver browser, String fundName) {
		this.browser = browser;
		this.fundName = fundName;
	}

	public boolean selectFSTFund(String booleanValue) throws TestAutomationException {
		logger.debug("Start of FSTFundTable: selectFSTFund");
		browser.manage().timeouts().implicitlyWait(Global.timeout, TimeUnit.SECONDS);
		table = WebElementRetriever.getWebElementById(browser, "fundsTable", GlobalCSSIdentifiers.FUNDSTABLEID);
		WebElement fundCheckbox = getCheckBox(getFundTableElement(fundName, checkboxColIndex));
		boolean result = false;

		WebElementProcessorCommon.scrollToWebElement(browser, fundCheckbox);

		if (booleanValue.equalsIgnoreCase("False")) {
			if (fundCheckbox.isEnabled() && fundCheckbox.isSelected()) {
				fundCheckbox.click();
				result = true;
			} else {
				logger.error("FST fund : " + fundName + "  has been disabled/ alreday been not selected");
				result = true;
			}
		} else if (fundCheckbox.isEnabled() && !fundCheckbox.isSelected()) {
			fundCheckbox.click();
			result = true;
		} else {
			logger.error("FST fund : " + fundName + "  has been disabled/ alreday been selected");
			result = true;
		}

		logger.debug("End of FSTFundTable: selectFSTFund");

		return result;
	}

	public boolean insetFSTFundPercentage(String fundName, String percentage) throws TestAutomationException {
		logger.debug("Start of FSTFundTable: insetFSTFundPercentage");
		table = WebElementRetriever.getWebElementById(browser, "fundsTable", GlobalCSSIdentifiers.FUNDSTABLEID);
		WebElement fundInputbox = getFundTableElement(fundName, fundPercentageInputboxColIndex);
		WebElementProcessorCommon.scrollToWebElement(browser, fundInputbox);
		fundInputbox.click();
		// .SENDKEYS is not working to input insertFST fund percentage. so
		// extracting the html and changing the value attribute and updating
		// the webelement html
		String html = new HtmlParser(fundInputbox).getHtml();
		String htmlUpdated = Jsoup.parse(html).getElementsByTag(GlobalTags.INPUT)
				.attr(GlobalAttributes.VALUE, percentage).toString();
		((JavascriptExecutor) browser)
				.executeScript("arguments[0].innerHTML = arguments[1]", fundInputbox, htmlUpdated);
		logger.debug("End of FSTFundTable: insetFSTFundPercentage");

		return true;
	}

	private List<WebElement> getTableRows() throws TestAutomationException {
		return WebElementRetriever.getWebElementsByTagName(table, "fundsTable", GlobalTags.TR);
	}

	public WebElement getFundTableElement(String fundName, int index) throws TestAutomationException {
		logger.debug("Start of FSTFundTable: getFundCheckboxElement");
		List<WebElement> tableCols;
		List<WebElement> tableRows;

		tableRows = getTableRows();
		WebElement element = null;
		for (int i = 0; i < tableRows.size(); i++) {
			tableCols = WebElementRetriever.getWebElementsByCSSSelector(tableRows.get(i), fundName, GlobalTags.TH
					+ " , " + GlobalTags.TD);
			if (new HtmlParser(tableCols.get(fundNameColIndex)).getinnerText().equalsIgnoreCase(fundName)) {
				element = tableCols.get(index);
			}
		}
		WebElementProcessorCommon
				.logWebElementStatus("Funds table element", fundName, (element == null ? false : true));
		logger.debug("End of FSTFundTable: getFundCheckboxElement");
		if (element == null)
			throw new TestAutomationException(
					ExceptionErrorCodes.TA089_FST_FUNDTABLE_ELEMENT_NOT_FOUND_BY_FUNDNAME_AND_INDEX,
					"Fund table Web elements with fund name '" + fundName + "' and index '" + index + "' not found.");
		return element;
	}

	/**
	 * browser is to verify the value inside the table.
	 * 
	 * @param cell
	 *            - it is the position of the table cell to be verified. Eg.
	 *            (1,2,3) It refers to the cell , 1st table (in the active page)
	 *            > 2nd row > 3rd column
	 * @param textToVerify
	 *            It is the text to be verified in the above cell
	 * @throws TestAutomationException
	 */
	public boolean clickOnFactSheetLink(String argments) throws TestAutomationException {
		logger.debug("Start of FSTFundTable: clickOnFactSheetLink");
		WebElement fundFactSheetElement = null;
		table = WebElementRetriever.getWebElementById(browser, "fundsTable", GlobalCSSIdentifiers.FUNDSTABLEID);
		List<WebElement> tableRows = getTableRows();
		ArgList arglist = new ArgList(argments);
		if (arglist.getParmetersCount() == 2) {
			List<WebElement> tableCols = WebElementRetriever.getWebElementsByCSSSelector(
					tableRows.get(arglist.getNumericParameter(0)), "factSheet", GlobalTags.TH + " , " + GlobalTags.TD);
			fundFactSheetElement = tableCols.get(arglist.getNumericParameter(1) - 1);
		} else if (arglist.getParmetersCount() == 1) {

			fundFactSheetElement = getFundTableElement(arglist.getParameter(0), fundFactsheetColIndex);
		} else
			throw new TestAutomationException(
					ExceptionErrorCodes.TA091_CLICK_ON_FUND_FACT_SHEET_INPUT1_PARAMETER_IS_NOT_VALID,
					"clickOnFactSheetLink : Input parameter '" + argments + "' is not valid.");

		WebElementProcessorCommon.scrollToWebElement(browser, fundFactSheetElement);

		WebElementRetriever.getWebElementByCSSSelector(fundFactSheetElement, "factSheetLink", GlobalTags.A).click();

		logger.debug("End of FSTFundTable: clickOnFactSheetLink");

		return true;
	}

	private WebElement getCheckBox(WebElement checkBoxElement) throws TestAutomationException {

		return WebElementRetriever.getWebElementByCSSSelector(checkBoxElement, "Extract Fund checkbox",
				GlobalTags.INPUT);
	}

}
