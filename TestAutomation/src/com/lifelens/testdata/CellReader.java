package com.lifelens.testdata;

import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFCell;

import com.lifelens.exceptions.TestAutomationException;

/**
 * This class is used to read values from excel cells.
 * 
 * @author stuartb
 * 
 */
public class CellReader {

	private static Logger logger = Logger.getLogger(CellReader.class.getName());

	private DataFormatter df = new DataFormatter();
	private FormulaEvaluator evaluator;

	public CellReader(FormulaEvaluator formEval) {
		if (formEval == null) {
			throw new IllegalArgumentException("formulaevaluator cannot be null");
		}

		this.evaluator = formEval;
	}

	/**
	 * This method return the String value of the given cell
	 * 
	 * @param type
	 *            type
	 * 
	 * @param cell
	 *            testdata sheet cell
	 * 
	 * @return It return the string cell value
	 */
	public String getStringCellValue(Cell cell) throws TestAutomationException {

		logger.debug("Start of Testdata: getStringCellValue");

		if (logger.isDebugEnabled()) {
			logger.debug("Testdata: getStringCellValue: Cell Type == " + cell.getCellType());
		}

		String rtnValue = "NOT_A_VALID_INPUT";
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_NUMERIC:
			if (df.formatCellValue(cell).contains("/")) {
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				rtnValue = sdf.format(cell.getDateCellValue());
			} else {
				rtnValue = df.formatCellValue(cell);
			}

			break;

		case Cell.CELL_TYPE_STRING:
			rtnValue = cell.getStringCellValue();
			break;

		case Cell.CELL_TYPE_BLANK:
			rtnValue = cell.getStringCellValue();
			break;

		case XSSFCell.CELL_TYPE_FORMULA: {
			evaluator.clearAllCachedResultValues();

			CellValue evaluatedCell = evaluator.evaluate(cell);
			rtnValue = evaluatedCell.getStringValue();

			// if Cell Value is Number Type Formula .
			if (rtnValue == null)
				rtnValue = evaluatedCell.getNumberValue() + "";

			return rtnValue;
		}

		default:
			rtnValue = "NOT_A_VALID_INPUT";
			break;
		}

		logger.debug("End of Testdata: getStringCellValue: return value " + rtnValue);
		return rtnValue;
	}

}
