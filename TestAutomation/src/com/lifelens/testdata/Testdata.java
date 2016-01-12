package com.lifelens.testdata;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.lifelens.exceptions.ExceptionErrorCodes;
import com.lifelens.exceptions.TestAutomationException;
import com.lifelens.globals.Global;

/**
 * This represents testdata. It is designed to implement parameterisation in the
 * framework. Testdata can be given in one master testdata sheet and tester can
 * use parameters to refer this testdata inside the testscripts.
 * 
 * 1. Parameters should be given either in the testdata sheet (defined in
 * config.properties file) or in config.properties file directly
 * 
 * 2. Testdata file is expected to be available in the testlab folder itself.
 * 
 * @author Srinivas Pasupulati (CO48633)
 * 
 * @version 1.0
 * 
 * @since 05.02.2014
 * 
 */
public class Testdata {

	private static Logger logger = Logger.getLogger(Testdata.class.getName());

	private HashMap<String, String> testdata = new HashMap<String, String>();
	private String testdataFilepath;
	private String testdataSheetName;
	private FileInputStream file;
	private XSSFWorkbook workbook;
	private Iterator<Row> rowIterator;
	private String[] header_columns;

	/**
	 * 
	 * @param testdatafilePath
	 *            - Testdata file path (Work book that is used to maintain
	 *            master testdata)
	 * @param testdataSheetName
	 *            - Testdata sheet inside the file(master testdata workbook)
	 */

	public Testdata(String testdatafilePath, String testdataSheetName) {
		logger.debug("Start of Testdata: Testdata constructor");
		logger.debug("Start of Testdata: Testdata constructor testdatafilePath == " + testdatafilePath
				+ " : testdataSheetName == " + testdataSheetName);
		this.testdataFilepath = testdatafilePath;
		this.testdataSheetName = testdataSheetName;
		logger.debug("end of Testdata: Testdata constructor");
	}

	/**
	 * It reads the test data if the Test data file exist. If the testdata file
	 * not exist, it logs the information and return to the calling module
	 * 
	 * @throws TestAutomationException
	 */

	public void readTestdata() throws TestAutomationException {
		logger.debug("Start of Testdata: readTestdata");
		if (testdatafileExists()) {
			openTestdataFile();
			readHeader();
			loadTestdata();
			loadRuntimeParameters();
		} else {
			logger.warn(Global.getTestdataFileName() + " file is not found in given TestLab folder name is : "
					+ Global.getTestlab());
		}

		logger.debug("End of Testdata: readTestdata");
	}

	/**
	 * It opens the test data file, read it to workbook object and close the
	 * file
	 * 
	 * @throws TestAutomationException
	 */

	private void openTestdataFile() throws TestAutomationException {
		logger.debug("Start of Testdata: openTestdataFile");
		try {
			file = new FileInputStream(new File(testdataFilepath));
			workbook = new XSSFWorkbook(file);
			rowIterator = workbook.getSheet(testdataSheetName).iterator();
		} catch (FileNotFoundException e) {
			throw new TestAutomationException(ExceptionErrorCodes.TA004_TEST_DATA_FILE_NOT_FOUND_ERROR, e.getMessage()
					+ " : Properties file not found in the path " + testdataFilepath, e);
		} catch (IOException e) {
			throw new TestAutomationException(ExceptionErrorCodes.TA005_TEST_DATA_FILE_LOAD_ERROR, e.getMessage()
					+ ": Unable to load the properties file in the path " + testdataFilepath, e);
		} finally {
			if (file != null) {
				try {
					file.close();
				} catch (IOException e) {
					throw new TestAutomationException(ExceptionErrorCodes.TA006_TEST_DATA_FILE_LOAD_ERROR,
							e.getMessage() + ": Unable to close the properties file input stream", e);
				}
			}
		}
		logger.debug("End of Testdata: openTestdataFile");
	}

	/**
	 * This mehtod eads the header and move the columns to header_columns string
	 * array
	 * 
	 * @throws TestAutomationException
	 */
	private void readHeader() throws TestAutomationException {
		logger.debug("Start of Testdata: readHeader");
		if (rowIterator != null) {
			Row row = rowIterator.next();
			Iterator<Cell> cellIterator = row.cellIterator();
			header_columns = getHeaderColumns(cellIterator);
		}
		logger.debug("End of Testdata: readHeader");
	}

	/**
	 * This method extract the testdata present in each cell of the worksheet
	 * and load into tesdata hashmp It ignores if the cell is blank/null
	 * 
	 * @throws TestAutomationException
	 */

	private void loadTestdata() throws TestAutomationException {
		logger.debug("Start of Testdata: loadTestdata");

		CellReader cellReader = new CellReader(workbook.getCreationHelper().createFormulaEvaluator());

		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			if (row == null) {
				// There is no data in this row, handle as needed
			} else {

				String testdataId = null;
				int colCounter = 0;

				// Row has data
				for (int cn = 0; cn < row.getLastCellNum(); cn++) {
					Cell cell = row.getCell(cn);

					if (cell == null) {
						// no data in a cell - just insert an empty string
						insertParameter(header_columns[colCounter] + testdataId, "");
					} else {
						if (colCounter == 0) {
							testdataId = cellReader.getStringCellValue(cell);
						} else {
							insertParameter(header_columns[colCounter] + testdataId,
									cellReader.getStringCellValue(cell));
						}
					}

					colCounter++;
				}
			}
		}

		logger.debug("End of Testdata: loadTestdata");
	}

	/**
	 * This method gets the Header columns from the header cell Iterator
	 * 
	 * @param cellIterator
	 *            testdata sheet header (cell Iterator)
	 * 
	 * @return String array with header columns
	 * @throws TestAutomationException
	 */
	private String[] getHeaderColumns(Iterator<Cell> cellIterator) throws TestAutomationException {
		logger.debug("Start of Testdata: getHeaderColumns");
		ArrayList<String> col_list = new ArrayList<String>();

		CellReader cellReader = new CellReader(workbook.getCreationHelper().createFormulaEvaluator());

		if (cellIterator != null) {
			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				col_list.add(cellReader.getStringCellValue(cell));
			}
		}

		logger.debug("End of Testdata: getHeaderColumns");
		return col_list.toArray(new String[col_list.size()]);

	}

	/**
	 * This method returns the testdata(value) of the given parameter
	 * 
	 * @param str
	 *            parameter
	 * @return the testdata(value) corresponds to the parameter
	 */

	public String getParameterValue(String str) {
		return testdata.get(str);
	}

	/**
	 * This method insert the parameter (preceded by '@') and its value in the
	 * testdata hashmap
	 * 
	 * @param parameter
	 *            Parameter
	 * @param value
	 *            Value corresponds to the parameter
	 */
	private void insertParameter(String parameter, String value) {
		testdata.put(Global.parameterIdentifier + parameter, value);
	}

	/**
	 * This method loads the Admin credentails and url from config.properties
	 * file into testdata hashmap
	 */

	private void loadRuntimeParameters() {
		logger.debug("Start of Testdata: loadRuntimeParameters");
		// Insert Admin credentials
		insertParameter(Global.AdminUsernamekey, Global.getAdminUserName());
		insertParameter(Global.AdminPasswordKey, Global.getAdminPassword());

		// Insert url
		insertParameter(Global.urlKey, Global.getUrl());
		logger.debug("End of Testdata: loadRuntimeParameters");
	}

	/**
	 * This method validates whether the testdata file exist and can be
	 * readable.
	 * 
	 * @return true if the testdata file exist and readable false if the
	 *         testdata file not exist
	 */

	private boolean testdatafileExists() {
		logger.debug("Start of Testdata: testdatafileExists");
		boolean isFileExist = false;
		File file = new File(testdataFilepath);

		if (file.isFile() && file.canRead()) {
			isFileExist = true;
		}
		logger.debug("End of Testdata: testdatafileExists: isFileExist== " + isFileExist);
		return isFileExist;
	}
}
