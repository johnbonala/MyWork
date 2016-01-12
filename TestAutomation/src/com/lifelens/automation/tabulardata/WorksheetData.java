package com.lifelens.automation.tabulardata;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.lifelens.exceptions.ExceptionErrorCodes;
import com.lifelens.exceptions.TestAutomationException;
import com.lifelens.globals.Global;
import com.lifelens.globals.GlobalCommonConstants;
import com.lifelens.testdata.Testdata;

public class WorksheetData implements TabularData {
	private static Logger logger = Logger.getLogger(WorksheetData.class.getName());
	private static final String HEADER = "<header>";
	private static final String NUMBERED = "<numbered>";

	private List<Map<String, Value>> rowData = new ArrayList<Map<String, Value>>();
	private List<Header> headers = new ArrayList<Header>();
	private String fileName;
	private String sheetName;
	private XSSFSheet sheet;
	private boolean hasNamedHeaders;

	public WorksheetData(String fileName, String sheetName) throws TestAutomationException {
		this.fileName = fileName;
		this.sheetName = sheetName;
		init();
	}

	private void init() throws TestAutomationException {
		logger.debug("Start of WorksheetData: init");
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(fileName);
			sheet = workbook.getSheet(sheetName);

			if (sheet == null) {
				throw new TestAutomationException(ExceptionErrorCodes.TA126_SHEET_MISSING_FROM_WORKBOOK,
						"Specified sheet [" + sheetName + "] not found in workbook: " + fileName);
			}

			int firstRowNum = sheet.getFirstRowNum();
			int lastRowNum = sheet.getLastRowNum();

			XSSFRow row = sheet.getRow(firstRowNum);
			XSSFCell cell = row.getCell(row.getFirstCellNum());
			String type = getStringCellValue(cell);
			if (HEADER.equals(type)) {
				hasNamedHeaders = true;
			} else if (!NUMBERED.equals(type)) {
				throw new TestAutomationException(
						ExceptionErrorCodes.TA032_WORK_SHEET_DATA_INVALID_FIRST_CELL_CONTENT_ERROR,
						"Expected <header> or <numbered> cells in sheet " + sheetName);
			}

			createHeaders(row, firstRowNum, lastRowNum);
			readInData(firstRowNum, lastRowNum);

		} catch (IOException ex) {
			throw new TestAutomationException(ExceptionErrorCodes.TA031_WORK_SHEET_DATA_UNABLE_TO_LOAD_WORKBOOK_ERROR,
					ex.getMessage() + ": Unable to load the properties file in the path " + fileName);
		}
		logger.debug("End of WorksheetData: init");
	}

	private void createHeaders(XSSFRow firstRow, int firstRowNum, int lastRowNum) throws TestAutomationException {
		logger.debug("Start of WorksheetData: createHeaders");
		int firstCell = firstRow.getFirstCellNum();
		int lastCell = firstRow.getLastCellNum();

		for (int i = firstCell + 1; i < lastCell; i++) {
			XSSFCell cell = firstRow.getCell(i);
			if (cell != null) {
				String value = getStringCellValue(cell);
				if (value != null) {
					value = value.trim();
					if (!value.isEmpty()) {
						Header header = new Header(value, i);
						boolean added = headers.add(header);
						if (!added) {
							throw new TestAutomationException(
									ExceptionErrorCodes.TA033_WORK_SHEET_DATA_DUPLICATE_CELL_CONTENT_ERROR,
									"Duplicate column header: " + value + " in the sheet " + sheetName);
						}
					}
				}
			}
		}

		if (headers.isEmpty()) {
			throw new TestAutomationException(ExceptionErrorCodes.TA034_WORK_SHEET_DATA_HEADER_NOT_PRESENTS_ERROR,
					"At least one header must be supplied in the sheet " + sheetName);
		}

		Collections.sort(headers);
		logger.debug("End of WorksheetData: createHeaders");
	}

	public void readInData(int firstRowNum, int lastRowNum) throws TestAutomationException {
		logger.debug("Start of WorksheetData: readInData");
		for (int i = firstRowNum + 1; i <= lastRowNum; i++) {
			Map<String, Value> columnValues = new HashMap<String, Value>();
			XSSFRow row = sheet.getRow(i);
			if (row != null) {
				Iterator<Header> it = headers.iterator();
				while (it.hasNext()) {
					Header header = it.next();
					XSSFCell cell = row.getCell(header.xssColumnNo);
					if (cell != null) {
						String value;
						Value cellValue = ValueFactory.valueFor(cell);
						value = cellValue.toString();
						value = retriveParamValue(value);

						// If a value contains parameter replace the parameter
						// value from the string
						if (value.contains(Global.parameterIdentifier)) {
							int beginIndex = value.indexOf(Global.parameterIdentifier);
							String testDataParameter = value.substring(beginIndex);
							String retrivedValue = retriveParamValue(testDataParameter);
							if (retrivedValue != null) {
								value = value.replaceAll(testDataParameter, retrivedValue);
							}
						}

						if (value != null) {
							value = value.trim();
							columnValues.put(header.headerName, ValueFactory.valueFor(value));

						} else {
							columnValues.put(header.headerName, ValueFactory.valueFor(""));
						}
					}
				}
				for (Value val : columnValues.values()) {
					if (!val.toString().isEmpty()) {
						columnValues.put("__sourceRow", ValueFactory.valueFor("" + i));
						rowData.add(columnValues);
						break;
					}
				}
			}
		}
		logger.debug("End of WorksheetData: readInData");
	}

	/**
	 * Added on 28/05/2014 Get Value of the parameter from testData sheet and
	 * replace to expectedData sheet.
	 * 
	 * @param value
	 * @return String
	 * @throws TestAutomationException
	 */
	private String retriveParamValue(String value) throws TestAutomationException {
		logger.debug("Start of WorksheetData: retriveParamValue");

		// if value is not a parameter but a email address containing @ and .
		if (isParameter(value)
				&& (!(value.contains(Global.parameterIdentifier) && value.contains(GlobalCommonConstants.DOT)))) {
			Testdata testdata = new Testdata(Global.getTestdatFileAbsolutepath(), Global.getTestdataSheetName());
			testdata.readTestdata();
			value = testdata.getParameterValue(value);
		}
		logger.debug("End of WorksheetData: retriveParamValue");
		return value;
	}

	/**
	 * Added on 28/05/2014 Verifies the ParameterValue exist or not
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

	private String getStringCellValue(Cell cell) {
		logger.debug("Start of WorksheetData: getStringCellValue");
		if (cell == null) {
			return null;
		} else if (cell.getCellType() != Cell.CELL_TYPE_STRING) {
			cell.setCellType(Cell.CELL_TYPE_STRING);
		}
		logger.debug("End of WorksheetData: getStringCellValue");
		return cell.getStringCellValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lifelens.automation.tabulardata.TabularData#headerNames()
	 */
	@Override
	public List<String> headerNames() {
		logger.debug("Start of WorksheetData: headerNames");
		List<String> headerNames = new ArrayList<String>();
		for (Header header : headers) {
			headerNames.add(header.headerName);
		}
		logger.debug("End of WorksheetData: headerNames");
		return headerNames;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lifelens.automation.tabulardata.TabularData#rowData()
	 */
	@Override
	public Iterator<Map<String, Value>> rowData() {
		return rowData.iterator();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lifelens.automation.tabulardata.TabularData#getRow(int)
	 */
	@Override
	public Map<String, Value> getRow(int rowNum) {
		return rowData.get(rowNum);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lifelens.automation.tabulardata.TabularData#hasNamedHeaders()
	 */
	@Override
	public boolean hasNamedHeaders() {
		return hasNamedHeaders;
	}

	public int numRows() {
		return rowData.size();
	}
}
