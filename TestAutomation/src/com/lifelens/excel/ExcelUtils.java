package com.lifelens.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.lifelens.exceptions.TestAutomationException;
import com.lifelens.testdata.CellReader;

/**
 * A class to hold most (all) of the excel functionality. Questions such as...
 * 
 * How many rows and columns does a sheet have?
 * 
 * Does a sheet exist?
 * 
 * What is the data for a particular cell?
 * 
 * 
 * @author stuartb
 * 
 */
public class ExcelUtils {

	/** the workbook **/
	private XSSFWorkbook workbook = null;

	/**
	 * Create a new excel util with the specified workbook.
	 * 
	 * @param workbook
	 *            the excel workbook
	 */
	public ExcelUtils(XSSFWorkbook workbook) {
		if (workbook == null) {
			throw new IllegalArgumentException("workbook should not be null");
		}

		this.workbook = workbook;
	}

	/**
	 * Create a new excel util with the specified file.
	 * 
	 * @param excelFile
	 *            the excel file
	 * @throws IOException
	 *             if the file cannot be opened or is not an excel file
	 */
	public ExcelUtils(File excelFile) throws IOException {
		if (excelFile == null) {
			throw new IllegalArgumentException("excel file should not be null");
		}

		FileInputStream file = new FileInputStream(excelFile);
		workbook = new XSSFWorkbook(file);
	}

	/**
	 * This method returns number of columns from the sheet
	 * 
	 * @param sheetName
	 * @return int as a number of columns in the sheet
	 */
	public int getColumnCount(String sheetName) {
		// check if sheet exists
		if (!new ExcelUtils(workbook).isSheetExist(sheetName))
			return -1;

		XSSFSheet sheet = workbook.getSheet(sheetName);
		Row row = sheet.getRow(0);

		if (row == null)
			return -1;

		return row.getLastCellNum();

	}

	/**
	 * This method returns number of rows from the sheet
	 * 
	 * @param sheetName
	 * @return int as a number of rows from the sheet
	 */
	public int getRowCount(String sheetName) {
		int index = workbook.getSheetIndex(sheetName);
		if (index == -1)
			return 0;
		else {
			XSSFSheet sheet = workbook.getSheetAt(index);
			int number = sheet.getLastRowNum() + 1;
			return number;
		}

	}

	/**
	 * This method finds out whether the sheet exists or not
	 * 
	 * @param sheetName
	 * @return boolean
	 */
	public boolean isSheetExist(String sheetName) {
		int index = workbook.getSheetIndex(sheetName);
		if (index == -1) {
			index = workbook.getSheetIndex(sheetName.toUpperCase());
			if (index == -1)
				return false;
			else
				return true;
		} else
			return true;
	}

	/**
	 * This method returns data in the cell of given row number and column
	 * number in the provided sheet
	 * 
	 * @param sheetName
	 *            the sheet name
	 * @param colNum
	 *            the column number
	 * @param rowNum
	 *            the row number
	 * @return cell data as a string
	 * 
	 * @throws TestAutomationException
	 */
	public String getCellData(String sheetName, int colNum, int rowNum) throws TestAutomationException {

		CellReader cellReader = new CellReader(workbook.getCreationHelper().createFormulaEvaluator());

		XSSFSheet sheet = workbook.getSheet(sheetName);
		if (sheet == null) {
			return "";
		}

		XSSFRow row = sheet.getRow(rowNum - 1);
		if (row == null) {
			return "";
		}

		XSSFCell cell = row.getCell(colNum);
		if (cell == null) {
			return "";
		}

		return cellReader.getStringCellValue(cell);
	}
}
