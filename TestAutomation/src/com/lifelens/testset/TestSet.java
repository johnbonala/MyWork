package com.lifelens.testset;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.lifelens.exceptions.ExceptionErrorCodes;
import com.lifelens.exceptions.TestAutomationException;
import com.lifelens.testdata.Testdata;

/**
 * This class represent a set of tests. A test set is defined by a tester in an
 * excel workbook.
 * 
 * The TestSet will be one of many inside a TestLab. A TestSet contains 1 or
 * more TestCases.
 * 
 * @author stuartb
 * 
 */
public class TestSet implements IWorkBook {

	private static Logger logger = Logger.getLogger(TestSet.class.getName());

	private File testSetFile;
	private XSSFWorkbook workbook;
	private TestCase testcase;
	private String priority;
	private TestCase indexTestCase;

	private final String PASS = "Pass";
	private final String FAIL = "Fail";
	private final String INDEX = "Index";
	private final String indexStatusColumn = "E";
	private final String testcaseStatusColumn = "D";

	public List<String> testCases = new ArrayList<String>();
	public LinkedHashMap<String, String[]> indextestCaseDetails = new LinkedHashMap<String, String[]>();
	private Testdata testData;

	/**
	 * Create a new TestSet from a file.
	 * 
	 * @param workbookFile
	 * @param priority
	 * @param testData
	 * @throws TestAutomationException
	 */
	public TestSet(File workbookFile, String priority, Testdata testData) throws TestAutomationException {

		testSetFile = workbookFile;

		FileInputStream fis;
		try {
			fis = new FileInputStream(workbookFile);
			workbook = new XSSFWorkbook(fis);
		} catch (FileNotFoundException e) {
			throw new TestAutomationException(ExceptionErrorCodes.TA072_TESTLAB_FILE_NOT_FOUND_ERROR,
					"TestSet file cannot be opened: " + workbookFile.getAbsolutePath());
		} catch (IOException e) {
			throw new TestAutomationException(ExceptionErrorCodes.TA073_TESTLAB_FILE_LOAD_ERROR,
					"TestSet file cannot be read: " + workbookFile.getAbsolutePath());
		}

		this.priority = priority;
		this.testData = testData;

		indexTestCase = new TestCase(workbook.getSheet(INDEX));
	}

	// Used for only unit tests
	public TestSet() {
	}

	@Override
	public TestCase getTestCase() {
		return testcase;
	}

	@Override
	public void setTestCase(String testsheetName) throws TestAutomationException {
		XSSFSheet sheet = workbook.getSheet(testsheetName);
		if (sheet == null) {
			throw new TestAutomationException(ExceptionErrorCodes.TA120_TEST_CASE_SHEET_NAME_NULL,
					" the sheet with name" + testsheetName + " not exists for workbook: " + getWorkbookName());

		}

		this.testcase = new TestCase(workbook.getSheet(testsheetName));
	}

	@Override
	public XSSFWorkbook getWorkbook() {
		return workbook;
	}

	@Override
	public String[][] getDataProviderObject() {
		logger.debug("Start of TestSet: getDataProviderObject");
		int cols = 3;
		int counter = 0;
		String[][] testObject = new String[indextestCaseDetails.size()][cols];

		for (String sheetName : indextestCaseDetails.keySet()) {
			String[] IndexCols = indextestCaseDetails.get(sheetName);
			testObject[counter][0] = sheetName; // test sheet Name
			testObject[counter][1] = IndexCols[0]; // testCase Name
			testObject[counter][2] = IndexCols[1]; // test priority
			counter++;
		}
		logger.debug("End of TestSet: getDataProviderObject");
		return testObject;
	}

	@Override
	public void loadSheetNames() {
		logger.debug("Start of TestSet: getSheetNames");
		for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
			String sheetname = workbook.getSheetName(i);
			testCases.add(sheetname);
		}
		logger.debug("End of TestSet: getSheetNames");
	}

	@Override
	public void getIndexTestCaseDetails() throws TestAutomationException {
		logger.debug("Start of TestSet: getIndexTestCaseDetails");
		/*
		 * Test Suite Excel template Column A - TestCase SNo Column B -
		 * TestCaseSheetName Column C - TestName Column D - TestPriority
		 */
		int SheetNameCol = 1, TestNameCol = 2, TestPriorityCol = 3, testRunRequiredCol = 4;
		final String ALL = "ALL";

		Iterator<Row> rowIterator = indexTestCase.getWorkSheet().iterator();
		Row row = rowIterator.next(); // Ignore header

		int testCaseNumber = 0;
		while (rowIterator.hasNext()) {
			testCaseNumber++;
			row = rowIterator.next();
			Cell sheetNameCell = row.getCell(SheetNameCol);
			if (sheetNameCell == null) {
				throw new TestAutomationException(ExceptionErrorCodes.TA120_TEST_CASE_SHEET_NAME_NULL, "testcase "
						+ testCaseNumber + " the test case sheet name not set in index sheet for workbook: "
						+ getWorkbookName());
			}

			logger.info("TestSet: getIndexTestCaseDetails: Sheet Name" + row.getCell(SheetNameCol).getStringCellValue());
			String testRunRequiredValue = row.getCell(testRunRequiredCol).getStringCellValue();
			boolean testRunRequired = (StringUtils.isNotBlank(testRunRequiredValue) && testRunRequiredValue
					.equalsIgnoreCase("Y")) ? true : false;
			String testPriority = row.getCell(TestPriorityCol).getStringCellValue();
			if (testRunRequired && (testPriority.equalsIgnoreCase(priority) || priority.equalsIgnoreCase(ALL))) {
				String testCaseSheetName = row.getCell(SheetNameCol).getStringCellValue();
				String testName = row.getCell(TestNameCol).getStringCellValue();
				String rowNumber = String.valueOf(row.getRowNum());
				indextestCaseDetails.put(testCaseSheetName, new String[] { testName, testPriority, rowNumber });
			}
		}
		logger.debug("End of TestSet: getIndexTestCaseDetails");
	}

	@Override
	public boolean indexSheetFound() {
		if (testCases.contains(INDEX))
			return true;
		else
			return false;
	}

	@Override
	public void validateIndexSheet() {
		logger.debug("Start of TestSet: validateIndexSheet");
		Iterator<String> iterator = indextestCaseDetails.keySet().iterator();
		if (iterator.hasNext()) {
			String sheetname = iterator.next();
			if (!testCases.contains(sheetname)) {
				logger.debug("Sheet Name  : " + sheetname + " not found ");
				iterator.remove();
			}
		}
		logger.debug("End of TestSet: validateIndexSheet");
	}

	@Override
	public void updateTestStatusInIndex(boolean testCasePassed) {
		logger.debug("Start of TestSet: updateTestStatusInIndex");
		String[] rowDeatails = indextestCaseDetails.get(testcase.getWorkSheet().getSheetName());

		int rowNum = 0;

		if (rowDeatails != null) {
			rowNum = Integer.parseInt(rowDeatails[2]);
			if (testCasePassed)
				indexTestCase.getWorkSheet().getRow(rowNum).getCell(5).setCellValue(PASS);
			else
				indexTestCase.getWorkSheet().getRow(rowNum).getCell(5).setCellValue(FAIL);
		}
		logger.debug("End of TestSet: updateTestStatusInIndex");
	}

	public void formatIndexSheet() {
		logger.debug("Start of TestSet: formatIndexSheet");
		int lastcol = indexTestCase.getWorkSheet().getLastRowNum() + 1;
		indexTestCase.setForamttingRange(indexStatusColumn + "2:" + indexStatusColumn + lastcol)
				.applyConditionalFormatting();
		logger.debug("End of TestSet: formatIndexSheet");
	}

	public void formatTestCaseSheet() {
		logger.debug("Start of TestSet: formatTestCaseSheet");
		int lastcol = testcase.getWorkSheet().getLastRowNum() + 1;
		testcase.setForamttingRange(testcaseStatusColumn + "2:" + testcaseStatusColumn + lastcol)
				.applyConditionalFormatting();
		logger.debug("End of TestSet: formatTestCaseSheet");
	}

	public String getWorkbookName() {
		return testSetFile.getName();
	}

	public String getWorkbookNameAbsolutePath() {
		return testSetFile.getAbsolutePath();
	}

	public String getWorkbookLocation() throws TestAutomationException {
		if (testSetFile.getParentFile() != null) {
			return testSetFile.getParentFile().getAbsolutePath();
		} else {
			throw new TestAutomationException(ExceptionErrorCodes.TA073_TESTLAB_FILE_LOAD_ERROR,
					"TestSet paernt file not found");
		}
	}

	/**
	 * @return the testData
	 */
	public Testdata getTestData() {
		return testData;
	}

}