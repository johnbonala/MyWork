package com.lifelens.webelements;

import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

import com.lifelens.browsers.WebElementProcessorCommon;
import com.lifelens.exceptions.ExceptionErrorCodes;
import com.lifelens.exceptions.TestAutomationException;
import com.lifelens.globals.Global;
import com.lifelens.globals.GlobalAttributes;
import com.lifelens.globals.GlobalCSSIdentifiers;
import com.lifelens.globals.GlobalCSSSelectors;
import com.lifelens.globals.GlobalTags;
import com.lifelens.htmlparser.HtmlParser;
import com.lifelens.interpreter.ArgList;
import com.lifelens.interpreter.TestContext;

/**
 * It represents the web table object. Web tables are present in lifelens in
 * number of places Eg. Search results, Logfile results, fund tables, enrolment
 * form etc
 * 
 * This helps to get webtable in a webpage and select cell inside the table.
 * This will be further extended to select the webelements (buttons, checkbox,
 * redio boxes) inside the table.
 * 
 * @author Srinivas Pasupulati (co48633)
 * 
 * @version 1.0
 * 
 * @since 06.05.2014
 * 
 */
public class Table extends RemoteWebElement {

	private static Logger logger = Logger.getLogger(Table.class.getName());

	private int tableIndex;
	private int rowIndex;
	private int colIndex;
	private WebElement table;
	private WebElement decisionTableDialog;
	private ArgList tableArgList;

	// private String tablexpath = "//table[string-length(@role)= 0]";
	private WebDriver browser;

	private String[] tableIdentifierTags = { GlobalTags.THEAD, GlobalTags.TBODY, GlobalTags.TFOOT };
	private String[] tableIdentifierCSS = { GlobalCSSSelectors.THEAD_CSS, GlobalCSSSelectors.TBODY_CSS,
			GlobalCSSSelectors.TFOOT_CSS };

	private TestContext testContext;

	/**
	 * 
	 * @param browser
	 *            browser object
	 * @param webElementProcessor
	 * @param tableIndex
	 *            table to be selected eg. table index - 1 means select 1st
	 *            table that is appearing in the active web page.
	 * @throws TestAutomationException
	 */
	public Table(WebDriver browser, TestContext testContext, int tableIndex) throws TestAutomationException {
		this.tableIndex = tableIndex;
		this.testContext = testContext;
		this.table = getTableFromBrowser(browser);
		this.browser = browser;
	}

	/**
	 * 
	 * @param browser
	 *            browser object
	 * @param tableArgList
	 *            "Table(tableIndex, rowIndex, ColumnIndex)"
	 * 
	 * @throws TestAutomationException
	 */
	public Table(WebDriver browser, TestContext testContext, ArgList tableArgList) throws TestAutomationException {
		this.tableArgList = tableArgList;
		tableIndex = tableArgList.getNumericTableParameter(0);
		rowIndex = tableArgList.getNumericTableParameter(1);
		colIndex = tableArgList.getNumericTableParameter(2);
		this.browser = browser;
		this.testContext = testContext;
		table = getTableFromBrowser(browser);
	}

	/**
	 * This extract the tables with no "role" attribute and returns the table
	 * (corresponds tableIndex)
	 * 
	 * @param browser
	 *            Active web browser
	 * 
	 * @return
	 * @throws TestAutomationException
	 */
	private WebElement getTableFromBrowser(WebDriver browser) throws TestAutomationException {
		WebElement table = null;
		List<WebElement> tablesList = null;

		if (testContext.getLoadIndicator()) {
			decisionTableDialog = browser.findElement(By.id(GlobalCSSIdentifiers.DIALOG));
			if (decisionTableDialog.isDisplayed()) {
				tablesList = WebElementRetriever.getWebElementsByCSSSelector(decisionTableDialog, "Table",
						GlobalCSSSelectors.TABLE_CSS);
			} else {
				tablesList = WebElementRetriever.getWebElementsByCSSSelector(browser, "Table",
						GlobalCSSSelectors.TABLE_CSS);
			}
		} else {
			// for the CSOL and TBP where load indicator does not exists
			tablesList = WebElementRetriever
					.getWebElementsByCSSSelector(browser, "Table", GlobalCSSSelectors.TABLE_CSS);
		}
		if (tablesList.size() > (tableIndex - 1)) {
			table = tablesList.get(tableIndex - 1);
		} else {
			throw new TestAutomationException(ExceptionErrorCodes.TA052_TABLE_ELEMENT_NOT_FOUND_ERROR,
					"No. of tables found in current page: " + tablesList.size() + " Table index is: " + tableIndex);
		}

		return table;
	}

	/**
	 * This returns the table (selected by getTable) cell element corresponds to
	 * the given row and column
	 * 
	 * @param rowIndex
	 *            Row number of the table
	 * @param colIndex
	 *            Column number of the table
	 * @return table cell (webelement) corresponds to the row and column index
	 * @throws TestAutomationException
	 */
	private WebElement getTableElement(int rowIndex, int colIndex) throws TestAutomationException {
		return getTableColumn(getTableRow(rowIndex), colIndex);
	}

	private WebElement getTableElement() throws TestAutomationException {
		return getTableColumn(getTableRow(rowIndex), colIndex);
	}

	private List<WebElement> getTableRows() throws TestAutomationException {
		return WebElementRetriever.getWebElementsByCSSSelector(table, "Table", GlobalCSSSelectors.TABLE_ROW_CSS);
	}

	private WebElement getTableElementByCSS(String cssSelector) throws TestAutomationException {
		return WebElementRetriever.getWebElementByCSSSelector(table, "Table Element", cssSelector);
	}

	private List<WebElement> getTableElementsByCSS(String cssSelector) throws TestAutomationException {
		return WebElementRetriever.getWebElementsByCSSSelector(table, "Table Element", cssSelector);
	}

	private List<WebElement> getTableRowsFromTableBlock(WebElement tableBlock) throws TestAutomationException {

		List<WebElement> tableRows = null;

		try {
			// short timeout as empty header or footer is common
			browser.manage().timeouts().implicitlyWait(Global.TIMEOUT_SHORT, TimeUnit.SECONDS);

			tableRows = WebElementRetriever.getWebElementsByCSSSelector(tableBlock, "Table Block",
					GlobalCSSSelectors.TABLE_ROW_CSS);
		} finally {
			// back to the default timeout
			browser.manage().timeouts().implicitlyWait(Global.timeout, TimeUnit.SECONDS);
		}

		return tableRows;
	}

	private WebElement getTableRow(int rowIndex) throws TestAutomationException {
		WebElement tableRow = null;
		int tableRowNumber = 0;
		int index = 0;

		ExtractTableRow: while (index < tableIdentifierCSS.length) {

			if (new HtmlParser(table).hasTag(tableIdentifierTags[index])) {

				List<WebElement> tableRows = getTableRowsFromTableBlock(getTableElementByCSS(tableIdentifierCSS[index]));

				for (WebElement tableRowElement : tableRows) {
					if (!new HtmlParser(tableRowElement).getinnerText().isEmpty()) {
						if (tableRowNumber == (rowIndex - 1)) {
							tableRow = tableRowElement;
							break ExtractTableRow;
						} else
							tableRowNumber++;
					}

				}
			}

			index++;
		}
		// when if Table Having Multiple tbody tags .

		if (tableRow == null) {
			tableRowNumber = 0;
			index = 0;

			ExtractTableRow: while (index < tableIdentifierCSS.length) {

				if (new HtmlParser(table).hasTag(tableIdentifierTags[index])) {

					List<WebElement> tableBodys = getTableElementsByCSS(tableIdentifierCSS[index]);
					if (tableBodys.size() > 0) {

						for (WebElement tablebodyElement : tableBodys) {
							List<WebElement> tableRows = getTableRowsFromTableBlock(tablebodyElement);

							for (WebElement tableRowElement : tableRows) {
								if (!new HtmlParser(tableRowElement).getinnerText().isEmpty()) {
									if (tableRowNumber == (rowIndex - 1)) {
										tableRow = tableRowElement;
										break ExtractTableRow;
									} else
										tableRowNumber++;
								}

							}
						}
					}
				}

				index++;
			}

		}

		if (tableRow == null)
			throw new TestAutomationException(
					ExceptionErrorCodes.TA095_TABLE_GET_TABLE_ROW_NO_ROW_RETRIEVED_WITH_TABLE_INDEX_AND_ROW_INDEX,
					"Table row is null with the given table index: " + tableIndex + " and Row index: " + rowIndex);

		if (tableRowNumber != (rowIndex - 1)) {
			throw new TestAutomationException(
					ExceptionErrorCodes.TA096_TABLE_GET_TABLE_NO_OF_ROWS_IN_TABLE_LESS_THAN_ROW_INDEX,
					"No of rows in the table with table index: " + tableIndex + " are: " + tableRowNumber
							+ " but Row index is: " + rowIndex);

		}

		return tableRow;
	}

	public WebElement getTableColumn(WebElement tableRow, int colIndex) throws TestAutomationException {
		return getColumns(tableRow).get(colIndex - 1);
	}

	private List<WebElement> getColumns(WebElement tableRow) throws TestAutomationException {
		return WebElementRetriever.getWebElementsByCSSSelector(tableRow, "Table column",
				GlobalCSSSelectors.TABLE_HEADER_CSS + " , " + GlobalCSSSelectors.TABLE_COLUMN_CSS);
	}

	private int getTableRowCount() throws TestAutomationException {
		return getTableRows().size();
	}

	public WebElement getMatchingRow(String stringToCompare, int colToBeVerified) throws TestAutomationException {
		logger.debug("Start of Table: getMatchingRow");
		WebElement element = null;
		for (int i = 1; i <= getTableRowCount(); i++) {
			WebElement cell = getTableElement(i, colToBeVerified);
			if (cell.getText().equalsIgnoreCase(stringToCompare)) {
				element = getTableRow(i);
			}
		}

		if (element == null) {
			WebElementProcessorCommon.logWebElementStatus("Table content", stringToCompare, false);
			logger.debug("String :" + stringToCompare + " not found in " + " Column " + colToBeVerified);
			throw new TestAutomationException(ExceptionErrorCodes.TA022_TABLE_ELEMENT_CELL_VALUE_NOT_MATCHING_ERROR,
					"String :" + stringToCompare + " not found in " + " Column " + colToBeVerified + " of the table");
		}
		logger.debug("End of Table: getMatchingRow");
		return element;
	}

	public WebElement getMatchingEditRow(String stringToCompare, int colToBeVerified) throws TestAutomationException {
		logger.debug("Start of Table: getMatchingRow");
		WebElement element = null;
		for (int i = 1; i <= getTableRowCount(); i++) {
			WebElement cell = getTableElement(i, colToBeVerified);
			if (cell.getText().equalsIgnoreCase(stringToCompare)) {
				element = getTableRow(i);
				break;
			}
		}

		if (element == null) {
			WebElementProcessorCommon.logWebElementStatus("Table content", stringToCompare, false);
			logger.debug("String :" + stringToCompare + " not found in " + " Column " + colToBeVerified);
			throw new TestAutomationException(ExceptionErrorCodes.TA022_TABLE_ELEMENT_CELL_VALUE_NOT_MATCHING_ERROR,
					"String :" + stringToCompare + " not found in " + " Column " + colToBeVerified + " of the table");
		}
		logger.debug("End of Table: getMatchingRow");
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
	public boolean verifyTableCellValue(String cell, String textToVerify) throws TestAutomationException {
		logger.debug("Start of Table: verifyTableCellValue");

		boolean textFound = false;
		String innerText = null;
		ArgList arglist = new ArgList(cell);
		tableIndex = arglist.getNumericParameter(0);
		if (!arglist.getParameter(1).equals("?")) {
			rowIndex = arglist.getNumericParameter(1);
		} else {
			rowIndex = 1;
		}
		colIndex = arglist.getNumericParameter(2);

		if (isParameter(textToVerify)) {
			textToVerify = testContext.getTestSet().getTestData().getParameterValue(textToVerify);
		} else if (textToVerify.contains(Global.parameterIdentifier)) {
			textToVerify = testContext.resolveParameters(textToVerify);
		}

		WebElement tableElement = getTableElement(rowIndex, colIndex);

		if ((tableElement.getAttribute(GlobalAttributes.ROLE) != null)
				&& tableElement.getAttribute(GlobalAttributes.ROLE).equals("textbox")) {
			innerText = tableElement.getAttribute(GlobalAttributes.VALUE);
		} else {
			innerText = new InnerElement(tableElement).getText();
		}

		if ("".equals(innerText) && innerText.length() == 0) {
			List<WebElement> textBoxElements = WebElementRetriever.getWebElementsByCSSSelector(table, "Table Element",
					GlobalCSSSelectors.INPUTBOX);
			for (WebElement tableTextBoxValues : textBoxElements) {
				innerText = tableTextBoxValues.getAttribute(GlobalAttributes.VALUE).trim();
				if (innerText.contains(textToVerify.trim()))
					break;
			}
		}

		if (textToVerify.equalsIgnoreCase("#NUMERIC#") && StringUtils.isNotBlank(innerText)) {
			isNumeric(cell, innerText);
			textFound = true;
		} else if (textToVerify.equalsIgnoreCase("#NUMERICWITHPOUNDSYMBOL#") && StringUtils.isNotBlank(innerText)) {
			StringTokenizer tokens = new StringTokenizer(innerText, "\u00A3", true);
			if (tokens.countTokens() < 2 || !tokens.nextToken().equals("\u00A3")) {
				throw new TestAutomationException(
						ExceptionErrorCodes.TA078_TABLE_ELEMENT_CELL_VALUE_NOT_CONTAINS_POUND_SYMBOL_ERROR,
						": \u00A3 not found in the cell: " + cell + " of the table");
			} else {
				isNumeric(cell, tokens.nextToken());
			}
			textFound = true;
		} else if (textToVerify.equalsIgnoreCase("#TEXT#") && StringUtils.isNotBlank(innerText)) {
			textFound = true;
		} else if ((StringUtils.isBlank(textToVerify) && StringUtils.isBlank(innerText))
				|| innerText.contains(textToVerify.trim())) {
			textFound = true;
		} else if (innerText.contains(textToVerify)) {
			textFound = true;
		} else if (!innerText.contains(textToVerify) && arglist.getParameter(1).equals("?")) {
			int rowCount = getTableRowCount();

			// If table row position is changing pass row number as "?" in test
			// case table parameter below fix will iterate to all the rows if it
			// finds any row with a same text will return true
			for (int i = 1; i <= rowCount; i++) {
				tableElement = getTableElement(i, colIndex);
				innerText = new InnerElement(tableElement).getText();
				if (innerText.equalsIgnoreCase(textToVerify)) {
					textFound = true;
					break;
				}
			}
		} else if (innerText.isEmpty()) {
			// TODO temporary fix need to change logic for handling below field
			try {
				WebElement wb = browser
						.findElement(By
								.name("selectionsContainer:selectionGroups:0:monolithicPanel:bodyContainer:formGroupPanel:selections:1:benefitOptions:tableRowOuterID:tableRowExpandoTD:expandoContentId:selectionForm:layout:rows:1:columns:0:cells:0:component:innerConfigContainer:selections:0:employeeCont:employeeContribution"));
				innerText = wb.getAttribute(GlobalAttributes.VALUE);
			} catch (Exception e) {

			}
		} else {

			throw new TestAutomationException(ExceptionErrorCodes.TA051_TABLE_ELEMENT_CELL_VALUE_NOT_FOUND_ERROR,
					"Text: " + textToVerify + " not found in the cell: " + cell
							+ " of the table. The Value found in the cell is : " + innerText);
		}

		WebElementProcessorCommon.logWebElementStatus("Table cell content", textToVerify, textFound);
		logger.debug("End of Table: verifyTableCellValue");
		return textFound;

	}

	/**
	 * browser is to verify the value inside the table.
	 * 
	 * @param cell
	 *            - it is the position of the table cell to be verified. Eg.
	 *            (1,2,3) It refers to the cell , 1st table (in the active page)
	 *            > 2nd row > 3rd column
	 * @throws TestAutomationException
	 */
	public String getTableCellValue(String cell) throws TestAutomationException {
		logger.debug("Start of Table: getTableCellValue");

		ArgList arglist = new ArgList(cell);
		tableIndex = arglist.getNumericParameter(0);
		rowIndex = arglist.getNumericParameter(1);
		colIndex = arglist.getNumericParameter(2);

		WebElement tableElement = getTableElement(rowIndex, colIndex);

		logger.debug("End of Table: getTableCellValue");
		return new InnerElement(tableElement).getText();
	}

	private void isNumeric(String cell, String innerText) throws TestAutomationException {
		try {
			logger.info("innerText:  " + innerText);
			Double.parseDouble(innerText.replaceAll(",", ""));
		} catch (NumberFormatException nfe) {
			throw new TestAutomationException(
					ExceptionErrorCodes.TA077_TABLE_ELEMENT_CELL_VALUE_NOT_A_NUMERIC_VALUE_ERROR, nfe.getMessage()
							+ ": Text: inner text not valid in the cell: " + cell + " of the table");
		}
	}

	/*
	 * Using "!new HtmlParser(tableRowElement).getinnerText().isEmpty()" instead
	 * below method sometimes it takes time to identify columns in a row and
	 * there value which is not needed. Keeping this method may require in
	 * future
	 */
	// private boolean isNotABlankRow(WebElement tableRow) throws
	// TestAutomationException {
	// boolean isNotABlankRow = false;
	// List<WebElement> tableRowDetails =
	// WebElementRetriever.getWebElementsByCSSSelector(tableRow,
	// "Table row details", GlobalTags.TH + " , " + GlobalTags.TD);
	// for (WebElement tableRowDetail : tableRowDetails) {
	// // System.out.println(new HtmlParser(tableRowDetail).getHtml());
	// if (!new HtmlParser(tableRowDetail).getHtml().trim().isEmpty()) {
	// isNotABlankRow = true;
	// break;
	// }
	// }
	// return isNotABlankRow;
	// }

	private WebElement getDropdown() throws TestAutomationException {
		try {
			// short timeout as empty header or footer is common
			browser.manage().timeouts().implicitlyWait(Global.TIMEOUT_SHORT, TimeUnit.SECONDS);
			return WebElementRetriever.getWebElementByCSSSelector(getTableElement(), "Table Dropdown",
					GlobalCSSSelectors.TABLE_DROPDOWN);
		} finally {
			// back to the default timeout
			browser.manage().timeouts().implicitlyWait(Global.timeout, TimeUnit.SECONDS);
		}
	}

	public String getDropdownId() throws TestAutomationException {
		if (testContext.getLoadIndicator()) {
			return WebElementRetriever.getWebElementId(getDropdown());
		} else {
			return WebElementRetriever.getWebElementId(getCSOLDropdown());
		}
	}

	private WebElement getCSOLDropdown() throws TestAutomationException {
		try {
			// short timeout as empty header or footer is common
			browser.manage().timeouts().implicitlyWait(Global.TIMEOUT_SHORT, TimeUnit.SECONDS);
			return WebElementRetriever.getWebElementByTagName(getTableElement(), "Table Dropdown", GlobalTags.SELECT);
		} finally {
			// back to the default timeout
			browser.manage().timeouts().implicitlyWait(Global.timeout, TimeUnit.SECONDS);
		}
	}

	public WebElement getInputbox() throws TestAutomationException {
		WebElement tableRow = getTableElement();

		try {
			// short timeout as empty header or footer is common
			browser.manage().timeouts().implicitlyWait(Global.TIMEOUT_SHORT, TimeUnit.SECONDS);
			return WebElementRetriever.getWebElementByCSSSelector(tableRow, "Table Inputbox",
					GlobalCSSSelectors.TABLE_INPUTBOX);
		} catch (Exception e) {
			// Sometimes the existing cssselector does not identify web element
			// so to handle such web elements below cssselector returns correct
			// webelement
			try {
				// short timeout as empty header or footer is common
				browser.manage().timeouts().implicitlyWait(Global.TIMEOUT_SHORT, TimeUnit.SECONDS);
				return WebElementRetriever.getWebElementByCSSSelector(tableRow, "Table Inputbox",
						GlobalCSSSelectors.INPUTBOX);
			} catch (Exception ex) {

				List<WebElement> inputBopxList = WebElementRetriever.getWebElementsByCSSSelector(tableRow,
						"Table Inputbox", GlobalCSSSelectors.INPUTBOXWITHOUTTEXT);

				// if there are more than one input boxes in a table column use
				// index
				if (inputBopxList.size() > 1 && !tableArgList.getNonTableArgumentListString().isEmpty()) {
					int index = Integer.parseInt(tableArgList.getNonTableArgumentListString());
					return inputBopxList.get(index - 1);
				} else {
					return WebElementRetriever.getWebElementByCSSSelector(tableRow, "Table Inputbox",
							GlobalCSSSelectors.INPUTBOXWITHOUTTEXT);
				}
			}

		} finally {
			// back to the default timeout
			browser.manage().timeouts().implicitlyWait(Global.timeout, TimeUnit.SECONDS);
		}
	}

	// private String getInputboxId() throws TestAutomationException {
	// return WebElementRetriever.getWebElementId(getInputbox());
	// }

	public WebElement getCheckbox(String existsIndicator) throws TestAutomationException {
		WebElement checkbox = null;
		WebElement tableRow = getTableElement();
		try {
			// short timeout as empty header or footer is common
			browser.manage().timeouts().implicitlyWait(Global.TIMEOUT_SHORT, TimeUnit.SECONDS);
			checkbox = WebElementRetriever.getWebElementByCSSSelector(tableRow, "Table Checkbox",
					GlobalCSSSelectors.TABLE_CHECKBOX);
		} catch (NoSuchElementException e) {
			if (!existsIndicator.equalsIgnoreCase("False")) {
				throw new TestAutomationException(ExceptionErrorCodes.TA119_TABLE_CHECKBOX_NOT_FOUND,
						"Expected checkbox to exist: " + existsIndicator, e);
			}
		} finally {
			// back to the default timeout
			browser.manage().timeouts().implicitlyWait(Global.timeout, TimeUnit.SECONDS);
		}

		return checkbox;
	}

	public WebElement getRadioButton() throws TestAutomationException {
		List<WebElement> inputBopxList = WebElementRetriever.getWebElementsByCSSSelector(getTableElement(),
				"Table Inputbox", GlobalCSSSelectors.TABLE_RADIOBUTTON);

		// if there are more than one input boxes in a table column use
		// index
		if (inputBopxList.size() > 1 && !tableArgList.getNonTableArgumentListString().isEmpty()) {
			int index = Integer.parseInt(tableArgList.getNonTableArgumentListString());
			return inputBopxList.get(index - 1);
		} else {
			return WebElementRetriever.getWebElementByCSSSelector(getTableElement(), "Table Inputbox",
					GlobalCSSSelectors.TABLE_RADIOBUTTON);
		}
	}

	// private String getRadioButtonId() throws TestAutomationException {
	// return WebElementRetriever.getWebElementId(getRadioButton());
	// }

	// private String getCheckboxId() throws TestAutomationException {
	// return WebElementRetriever.getWebElementId(getCheckbox());
	// }

	public WebElement getLink() throws TestAutomationException {
		return WebElementRetriever.getWebElementByTagName(getTableElement(), "Table Link", GlobalTags.A);
	}

	// private String getLinkId() throws TestAutomationException {
	// return WebElementRetriever.getWebElementId(getInputbox());
	// }

	/**
	 * Verifies the ParameterValue exist or not
	 * 
	 * @param cellStringValue
	 * @return
	 */
	private boolean isParameter(String cellStringValue) {
		if (cellStringValue.startsWith(Global.parameterIdentifier))
			return true;
		else
			return false;
	}

}
