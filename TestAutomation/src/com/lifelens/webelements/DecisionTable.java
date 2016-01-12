package com.lifelens.webelements;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

import com.lifelens.exceptions.ExceptionErrorCodes;
import com.lifelens.exceptions.TestAutomationException;
import com.lifelens.globals.GlobalCSSSelectors;
import com.lifelens.globals.GlobalCommonConstants;
import com.lifelens.htmlparser.HtmlParser;
import com.lifelens.interpreter.ArgList;

/**
 * This element is used to test elements in the DecicionTable like InputTextBox,
 * Checkbox and radiobuttons
 * 
 * This is useful for the testing of the elements which do not have label along
 * with them
 * 
 * DecisionTable(TableDimension: TableDimensionValue, RowDimension:
 * RowDimensionValue, ColDimension: ColDimensionValue)
 * 
 * @author
 * 
 */
public class DecisionTable extends RemoteWebElement {

	private static Logger logger = Logger.getLogger(DecisionTable.class.getName());//

	private int decisionTableDimension;
	private int rowIndex = 0;
	private int colIndex = 0;
	private String rowHeaderDimension;
	private String rowHeaderDimensionValue;
	private String colHeaderDimension;
	private String colHeaderDimensionValue;
	private String tableHeaderDimension;
	private String tableHeaderDimensionValue;
	public WebElement decisionTableDialog;
	private WebElement decisionTable;
	private ArgList arglist;
	// Element extracted with row and col index
	public WebElement decisionTableElement;
	private int _3dimensional;
	private String existsIndicator = "True";

	/**
	 * This parameterised constructor requires decisionTableDialog and arglist
	 * as a parameters
	 * 
	 * @param decisionTableDialog
	 * @param arglist
	 * @throws TestAutomationException
	 */
	public DecisionTable(WebElement decisionTableDialog, ArgList arglist) throws TestAutomationException {
		this(decisionTableDialog, arglist, "True");
	}

	public DecisionTable(WebElement decisionTableDialog, ArgList arglist, String existsIndicator)
			throws TestAutomationException {
		this.existsIndicator = existsIndicator;
		this.arglist = arglist;
		this.decisionTableDialog = decisionTableDialog;
		setDecisionTableWebElement();
	}

	/**
	 * Retrieves dimension grids list
	 * 
	 * @return List<WebElement>
	 * @throws TestAutomationException
	 */
	private List<WebElement> getDimensionGrids() throws TestAutomationException {
		return WebElementRetriever.getWebElementsByCSSSelector(decisionTableDialog, "Dimension Grid",
				GlobalCSSSelectors.DIV_DIMENSIONGRIDS);
	}

	/**
	 * Retrieves DimensioTable element
	 * 
	 * @return WebElement
	 * @throws TestAutomationException
	 */
	private WebElement getDTElement() throws TestAutomationException {
		return getDTBodyCols(getDTBodyRows().get(rowIndex)).get(colIndex);
	}

	/**
	 * Retrieves Dimension Table header rows
	 * 
	 * @return List<WebElement>
	 * @throws TestAutomationException
	 */
	private List<WebElement> getDTHeaderRows() throws TestAutomationException {
		return WebElementRetriever.getWebElementsByCSSSelector(decisionTable, "Decision table headers ",
				GlobalCSSSelectors.DIV_DIMENSIONHEADER);
	}

	/**
	 * Retrieves Dimension Table body rows
	 * 
	 * @return List<WebElement>
	 * @throws TestAutomationException
	 */
	private List<WebElement> getDTBodyRows() throws TestAutomationException {
		return WebElementRetriever.getWebElementsByCSSSelector(getDTBodyWebElement(), "Decision table headers ",
				GlobalCSSSelectors.TABLE_ROW_CSS);
	}

	/**
	 * Retrieves Dimension Table's body element
	 * 
	 * @return WebElement
	 * @throws TestAutomationException
	 */
	private WebElement getDTBodyWebElement() throws TestAutomationException {
		return WebElementRetriever.getWebElementByCSSSelector(decisionTable, "Decision table headers ",
				GlobalCSSSelectors.DIV_DIMENSIONTABLEHEADER);
	}

	/**
	 * Retrieves Dimension Table's Header element
	 * 
	 * @return WebElement
	 * @throws TestAutomationException
	 */
	private WebElement getDTHeaderWebElement() throws TestAutomationException {
		return WebElementRetriever.getWebElementByCSSSelector(decisionTable, "Decision table header element ",
				GlobalCSSSelectors.DIV_DIMENSIONTABLETHHEADER);
	}

	/**
	 * Retrieves Dimension Table's column dimensions
	 * 
	 * @return WebElement
	 * @throws TestAutomationException
	 */
	private WebElement getDTColDimensionElement() throws TestAutomationException {
		return WebElementRetriever.getWebElementByCSSSelector(getDTHeaderWebElement(), "Decision table Col dimension ",
				GlobalCSSSelectors.COLUMNDIMENSION);
	}

	/**
	 * Retrieves Dimension Table's row dimensions
	 * 
	 * @return WebElement
	 * @throws TestAutomationException
	 */
	private WebElement getDTRowDimensionElement() throws TestAutomationException {
		return WebElementRetriever.getWebElementByCSSSelector(getDTHeaderWebElement(), "Decision table row dimension ",
				GlobalCSSSelectors.ROWDIMENSION);
	}

	/**
	 * Retrieves Dimension Table's column Headers
	 * 
	 * @return List<WebElement>
	 * @throws TestAutomationException
	 */
	private List<WebElement> getDTColHeaders() throws TestAutomationException {
		return WebElementRetriever.getWebElementsByCSSSelector(getDTHeaderRows().get(1), "Header Rows",
				GlobalCSSSelectors.COLUMNHEADERS);
	}

	/**
	 * Retrieves subheaders
	 * 
	 * @return List<WebElement>
	 * @throws TestAutomationException
	 */
	private List<WebElement> getDTColSubHeaders() throws TestAutomationException {
		if (getDTHeaderRows().size() > 2) {
			return WebElementRetriever.getWebElementsByCSSSelector(getDTHeaderRows().get(2), "Sub Header Rows",
					GlobalCSSSelectors.SUBHEADING);
		} else {
			return WebElementRetriever.getWebElementsByCSSSelector(getDTHeaderRows().get(1), "Sub Header Rows",
					GlobalCSSSelectors.DIMENSIONELEMENTS);
		}
	}

	/**
	 * Retrieves Dimension Table's row Headers in body
	 * 
	 * @return List<WebElement>
	 * @throws TestAutomationException
	 */
	private List<WebElement> getDTBodyRowHeaders() throws TestAutomationException {
		return WebElementRetriever.getWebElementsByCSSSelector(getDTBodyWebElement(), "Body col header",
				GlobalCSSSelectors.MATRIXROW);
	}

	/**
	 * Retrieves Dimension Table's columns in body
	 * 
	 * @param decisionTableRow
	 * @return List<WebElement>
	 * @throws TestAutomationException
	 */
	private List<WebElement> getDTBodyCols(WebElement decisionTableRow) throws TestAutomationException {
		return WebElementRetriever.getWebElementsByCSSSelector(decisionTableRow, "Extract columns",
				GlobalCSSSelectors.DTBODYCOLS);
	}

	/**
	 * Sets index of the row in Decision table
	 * 
	 * @throws TestAutomationException
	 */
	private void setRowIndex() throws TestAutomationException {
		logger.debug("Start of DecisionTable: setRowIndex");
		boolean rowHeaderFound = false;
		int rowIndexCounter = 0;
		if (decisionTableDimension > 0) {
			if (!new HtmlParser(getDTRowDimensionElement()).getinnerText().contains(rowHeaderDimension)
					&& !existsIndicator.equalsIgnoreCase("False"))
				throw new TestAutomationException(ExceptionErrorCodes.TA106_DECISION_TABLE_ROW_DIMENSION_NOT_FOUND,
						"Decision table Row dimension not found. Dimension name: " + rowHeaderDimension);
			for (WebElement rowHeader : getDTBodyRowHeaders()) {
				if (new HtmlParser(rowHeader).getinnerText().contains(rowHeaderDimensionValue)) {
					rowHeaderFound = true;
					rowIndex = rowIndexCounter;
					break;
				}
				rowIndexCounter++;
			}
			if (!rowHeaderFound && !existsIndicator.equalsIgnoreCase("False"))
				throw new TestAutomationException(
						ExceptionErrorCodes.TA103_DECISION_TABLE_ROW_NOT_FOUND_WITH_ROW_DIMENSION_AND_VALUE,
						"Decision table Row dimension not found. Dimension name: " + rowHeaderDimension + ": "
								+ rowHeaderDimensionValue);
		} else {
			rowIndex = rowIndexCounter;
		}
		logger.debug("End of DecisionTable: setRowIndex");
	}

	/**
	 * Sets column index in decision table
	 * 
	 * @throws TestAutomationException
	 */
	private void setColIndex() throws TestAutomationException {
		logger.debug("Start of DecisionTable: setColIndex");
		boolean colHeaderFound = false;
		int colIndexCounter = 1;
		String colSubHeaderDimensionValue = null, colHeaderValue;
		if (decisionTableDimension > 1) {
			if (!new HtmlParser(getDTColDimensionElement()).getinnerText().contains(colHeaderDimension)
					&& !existsIndicator.equalsIgnoreCase("False"))
				throw new TestAutomationException(ExceptionErrorCodes.TA107_DECISION_TABLE_COLUMN_DIMENSION_NOT_FOUND,
						"Decision table Column dimension not found. Dimension name: " + colHeaderDimension);

			if (colHeaderDimensionValue.contains(GlobalCommonConstants.OPEN_BRACKET)
					&& !colHeaderDimensionValue.trim().startsWith(GlobalCommonConstants.OPEN_BRACKET)) {
				colSubHeaderDimensionValue = colHeaderDimensionValue.substring(
						colHeaderDimensionValue.indexOf(GlobalCommonConstants.OPEN_BRACKET),
						colHeaderDimensionValue.length());
				colHeaderValue = colHeaderDimensionValue.substring(0,
						colHeaderDimensionValue.indexOf(GlobalCommonConstants.OPEN_BRACKET));

			} else {
				colHeaderValue = colHeaderDimensionValue;
			}

			for (WebElement colHeader : getDTColHeaders()) {

				if (new HtmlParser(colHeader).getinnerText().contains(colHeaderValue)) {
					colHeaderFound = true;
					colIndex = colIndexCounter;
					break;
				}
				colIndexCounter++;
			}

			if (!getDTColSubHeaders().isEmpty() && getDTColSubHeaders().size() > 0) {

				int subColsSize = getDTColSubHeaders().size() / getDTColHeaders().size();

				for (int subIndex = 0; subIndex < subColsSize; subIndex++) {

					String subHeader = null;
					if (colHeaderDimensionValue.contains(GlobalCommonConstants.OPEN_BRACKET)
							&& !colHeaderDimensionValue.trim().startsWith(GlobalCommonConstants.OPEN_BRACKET)) {
						subHeader = colSubHeaderDimensionValue;
					} else if (colSubHeaderDimensionValue == null) {
						subHeader = colHeaderDimensionValue;
					}
					String subHeaderNoBrackets = colHeaderDimensionValue;

					if (subHeader.trim().startsWith(GlobalCommonConstants.OPEN_BRACKET)
							&& subHeader.trim().endsWith(GlobalCommonConstants.CLOSE_BRACKET)) {
						subHeaderNoBrackets = subHeader.substring(1, subHeader.length() - 1);
					}

					if (new HtmlParser(getDTColSubHeaders().get(subIndex)).getinnerText().contains(subHeaderNoBrackets)) {
						colHeaderFound = true;

						if (colIndex <= 1)
							colIndex = subIndex + 1;
						else if (colIndex == 2)
							colIndex = (subColsSize + (subIndex + 1));
						else if (colIndex > 2)
							colIndex = colIndex + (subColsSize + (subIndex + 1));
						break;
					}
				}

			}

			if (!colHeaderFound && !existsIndicator.equalsIgnoreCase("False"))
				throw new TestAutomationException(
						ExceptionErrorCodes.TA104_DECISION_TABLE_COLUMN_NOT_FOUND_WITH_COL_DIMENSION_AND_VALUE,
						"Decision table col dimension not found. Dimension name: " + rowHeaderDimension + ": "
								+ rowHeaderDimensionValue);
		} else
			colIndex = colIndexCounter;
		logger.debug("End of DecisionTable: setColIndex");
	}

	/**
	 * retrieves decision table from panel
	 * 
	 * @return WebElement
	 * @throws TestAutomationException
	 */
	private WebElement getDecisionTableFromDialog() throws TestAutomationException {
		logger.debug("Start of DecisionTable: getDecisionTableFromDialog");
		WebElement decisionTable = null;
		String tableHeader = tableHeaderDimension + ": " + tableHeaderDimensionValue;
		boolean tableFound = false;
		int tableIndexCounter = 0;

		if (!isThreeDimensional()) {
			decisionTable = getDimensionGrids().get(tableIndexCounter);
			tableFound = true;
		} else {
			for (WebElement dimensionGrid : getDimensionGrids()) {
				WebElement tableHeaderElement = WebElementRetriever.getWebElementByCSSSelector(dimensionGrid,
						"DecisonTable Grid", GlobalCSSSelectors.TABLEHEAADER);
				if (tableHeaderElement.getText().contains(tableHeader)) {
					tableFound = true;
					decisionTable = getDimensionGrids().get(tableIndexCounter);
					break;
				}

				tableIndexCounter++;
			}
		}
		if (tableFound)
			return decisionTable;
		else
			throw new TestAutomationException(
					ExceptionErrorCodes.TA102_DECISION_TABLE_NOT_FOUND_WITH_TABLE_DIMENSION_AND_VALUE,
					"Decision table not found with the table dimension and value: " + tableHeader);
	}

	// private boolean hasWebElement() {
	// return (!new HtmlParser(decisionTableElement).getHtml().isEmpty() ? true
	// : false);
	// }

	/**
	 * checks whether decision table contains checkboxes
	 * 
	 * @return boolean
	 */
	// private boolean hasCheckbox() {
	// return (new
	// HtmlParser(decisionTableElement).getHtml().contains("dimensionGridCheckBox")
	// ? true : false);
	// }

	/**
	 * checks whether decision table contains radio buttons
	 * 
	 * @return boolean
	 */
	// private boolean hasRadioButton() {
	// need to find out the class name for decision table radio button to
	// complete this method
	// return (new
	// HtmlParser(getDecisionTableElement()).getHtml().contains("dimensionGridCheckBox")?
	// true : false);
	// return false;
	// }

	/**
	 * checks whether decision table contains input textbox
	 * 
	 * @return
	 */
	// private boolean hasInputbox() {
	// return (new
	// HtmlParser(decisionTableElement).getHtml().contains("dimensionGridText")
	// ? true : false);
	// }

	/**
	 * checks whether decision table contains dropdown
	 * 
	 * @return boolean
	 */
	// private boolean hasDropdown() {
	// return (new
	// HtmlParser(decisionTableElement).getHtml().contains("dimensionGridDropDown")
	// ? true : false);
	// }

	/**
	 * Retrieves dropdown from decision table
	 * 
	 * @return WebElement
	 * @throws TestAutomationException
	 */
	private WebElement getDropdown() throws TestAutomationException {
		return (WebElementRetriever.getWebElementByCSSSelector(decisionTableElement,
				"Decision table Dropdown Element ", ".dimensionGridDropDown"));
	}

	/**
	 * retrieves dropdown id in descision table
	 * 
	 * @return String
	 * @throws TestAutomationException
	 */
	public String getDropdownId() throws TestAutomationException {
		return WebElementRetriever.getWebElementId(getDropdown());
	}

	/**
	 * Retrieves checkbox from decision table
	 * 
	 * @return WebElement
	 * @throws TestAutomationException
	 */
	public WebElement getCheckbox(String existsIndicator) throws TestAutomationException {
		WebElement checkbox = null;
		try {
			checkbox = WebElementRetriever.getWebElementByCSSSelector(decisionTableElement,
					"Decision table Checkbox Element ", GlobalCSSSelectors.DIMENSIONGRIDCHECKBOX);
		} catch (NoSuchElementException e) {
			if (!existsIndicator.equalsIgnoreCase("False")) {
				throw new TestAutomationException(ExceptionErrorCodes.TA118_DECISION_TABLE_CHECKBOX_NOT_FOUND,
						"Expected checkbox to exist: " + existsIndicator, e);
			}
		}

		return checkbox;
	}

	/**
	 * Retrieves checkbox Id in the decision table
	 * 
	 * @return String
	 * @throws TestAutomationException
	 */
	// private String getCheckboxId() throws TestAutomationException {
	// return WebElementRetriever.getWebElementId(getCheckbox());
	// }

	/**
	 * Retrieves inputbox from decision table
	 * 
	 * @return WebElement
	 * @throws TestAutomationException
	 */
	public WebElement getInputbox() throws TestAutomationException {

		return (WebElementRetriever.getWebElementByCSSSelector(decisionTableElement,
				"Decision table inputbox Element ", GlobalCSSSelectors.DIMENSIONGRIDINPUTBOX));
	}

	/**
	 * Retrieves inputbox Id in the decision table
	 * 
	 * @return String
	 * @throws TestAutomationException
	 */
	// private String getInputboxId() throws TestAutomationException {
	// return WebElementRetriever.getWebElementId(getInputbox());
	//
	// }

	/**
	 * Checks whether decision table is 3 dimensional
	 * 
	 * @return boolean
	 */
	private boolean isThreeDimensional() {
		return (decisionTableDimension == _3dimensional ? true : false);
	}

	/**
	 * Sets Decision table dimensions like tableheader, rowheader, columnheader
	 * 
	 * @throws TestAutomationException
	 */
	private void setParameters() throws TestAutomationException {
		logger.debug("Start of DecisionTable: setParameters");
		ArgList dtParams = arglist;
		dtParams.extractDecisionTableNameAndValueParameters();
		decisionTableDimension = dtParams.getDecisionTableArgNameArray().length;

		if (decisionTableDimension == 3) {
			tableHeaderDimension = dtParams.getDecisionTableArgNameArray()[0];
			tableHeaderDimensionValue = dtParams.getDecisionTableArgValueArray()[0];
			rowHeaderDimension = dtParams.getDecisionTableArgNameArray()[1];
			rowHeaderDimensionValue = dtParams.getDecisionTableArgValueArray()[1];
			colHeaderDimension = dtParams.getDecisionTableArgNameArray()[2];
			colHeaderDimensionValue = dtParams.getDecisionTableArgValueArray()[2];
		}

		else if (decisionTableDimension == 2) {
			rowHeaderDimension = dtParams.getDecisionTableArgNameArray()[0];
			rowHeaderDimensionValue = dtParams.getDecisionTableArgValueArray()[0];
			colHeaderDimension = dtParams.getDecisionTableArgNameArray()[1];
			colHeaderDimensionValue = dtParams.getDecisionTableArgValueArray()[1];
		}

		else if (decisionTableDimension == 1) {
			rowHeaderDimension = dtParams.getDecisionTableArgNameArray()[0];
			rowHeaderDimensionValue = dtParams.getDecisionTableArgValueArray()[0];
		}

		else if (decisionTableDimension > 3) {
			throw new TestAutomationException(
					ExceptionErrorCodes.TA108_DECISION_TABLE_GREATER_THAN_THREE_DIMENSIONS_NOT_SUPPORTED,
					" can not support decision tables with more than three dimensions. Dimensions specified: "
							+ decisionTableDimension);
		} else {
			throw new TestAutomationException(
					ExceptionErrorCodes.TA109_DECISION_TABLE_DIMENSIONS_NOT_SPECIFIED_IN_ARGUMENTS,
					" Doesn't have dimensions in the arugment: " + arglist.getArglistString());
		}
		setIndices();
	}

	/**
	 * Set indices for Decision table, row and column
	 * 
	 * @throws TestAutomationException
	 */
	private void setIndices() throws TestAutomationException {
		decisionTable = getDecisionTableFromDialog();
		setRowIndex();
		setColIndex();
	}

	/**
	 * sets decision table web element
	 * 
	 * @throws TestAutomationException
	 */
	public void setDecisionTableWebElement() throws TestAutomationException {
		setParameters();
		decisionTableElement = getDTElement();
	}

}
