package com.lifelens.testset;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ComparisonOperator;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PatternFormatting;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFConditionalFormattingRule;
import org.apache.poi.xssf.usermodel.XSSFPatternFormatting;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFSheetConditionalFormatting;

import com.lifelens.exceptions.ExceptionErrorCodes;
import com.lifelens.exceptions.TestAutomationException;

public class TestCase implements IWorkSheet {

	private static Logger logger = Logger.getLogger(TestCase.class.getName());
	private XSSFSheet testsheet;
	private final String PASS = "Pass", FAIL = "Fail", INDEX = "Index";
	private String range;
	private boolean hasTimer;
	private boolean hasContinueExecution;

	private static final String TIMER_COLUMN_NAME = "Timer";
	private static final String CONTINUE_EXECUTION_COLUMN_NAME = "Continue Execution";

	public TestCase(XSSFSheet testsheet) throws TestAutomationException {

		if (testsheet == null) {
			throw new TestAutomationException(ExceptionErrorCodes.TA120_TEST_CASE_SHEET_NAME_NULL,
					"The sheet name does not exists");
		}

		this.testsheet = testsheet;
		this.hasTimer = checkTimerColumnExists();
		this.hasContinueExecution = checkContinueExecutionColumnExists();
	}

	/**
	 * Checks whether the 'Timer' column exists as a last column in test case
	 * 
	 * @return boolean isTimerColumnExists
	 */
	private boolean checkTimerColumnExists() {
		boolean isTimerColumnExists = false;

		XSSFRow headerRow = this.getWorkSheet().getRow(0);
		if (headerRow != null) {

			Iterator<Cell> cellIterator = headerRow.cellIterator();
			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				if (cell.getStringCellValue().equalsIgnoreCase(TIMER_COLUMN_NAME)) {
					isTimerColumnExists = true;
					break;
				}

			}
		}
		return isTimerColumnExists;
	}

	/**
	 * Checks whether the 'Continue Execution' column exists as a last column in
	 * test case
	 * 
	 * @return boolean isContinueExecutionColumnExists
	 */
	private boolean checkContinueExecutionColumnExists() {
		boolean isContinueExecutionColumnExists = false;

		XSSFRow headerRow = this.getWorkSheet().getRow(0);
		if (headerRow != null) {

			Iterator<Cell> cellIterator = headerRow.cellIterator();
			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				if (cell.getStringCellValue().equalsIgnoreCase(CONTINUE_EXECUTION_COLUMN_NAME)) {
					isContinueExecutionColumnExists = true;
					break;
				}

			}
		}
		return isContinueExecutionColumnExists;
	}

	public boolean hasTimer() {

		return hasTimer;
	}

	public boolean hasContinueExecution() {

		return hasContinueExecution;
	}

	@Override
	public XSSFSheet getWorkSheet() {
		return testsheet;
	}

	@Override
	public void setTestSheet(XSSFSheet testsheet) {
		this.testsheet = testsheet;
	}

	@Override
	public void updateTestStepResult(boolean returnValue, Row teststep, long timeTaken) {
		logger.debug("Start of TestCase: updateTestStepResult");
		if (returnValue) {
			logger.info("Action: " + teststep.getCell(0).getStringCellValue() + " '" + teststep.getCell(1) + "', '"
					+ teststep.getCell(2) + "' executed successfully," + " and time taken to complete the action is :"
					+ timeTaken + "ms");
			teststep.getCell(3).setCellValue(PASS);
		} else {
			teststep.getCell(3).setCellValue(FAIL);
			logger.debug("test step is failed and Testcase is terminated!!");
		}
		logger.debug("End of TestCase: updateTestStepResult");
	}

	@Override
	public TestCase setForamttingRange(String range) {
		this.range = range;
		return this;
	}

	@Override
	public void applyConditionalFormatting() {
		logger.debug("Start of TestCase: updateTestStepResult");

		XSSFSheetConditionalFormatting sheetFormatting = testsheet.getSheetConditionalFormatting();

		XSSFConditionalFormattingRule rule1 = sheetFormatting.createConditionalFormattingRule(ComparisonOperator.EQUAL,
				"\"Pass\"");
		XSSFPatternFormatting fill1 = rule1.createPatternFormatting();
		fill1.setFillBackgroundColor(IndexedColors.LIGHT_GREEN.index);
		fill1.setFillPattern(PatternFormatting.SOLID_FOREGROUND);

		XSSFConditionalFormattingRule rule2 = sheetFormatting.createConditionalFormattingRule(ComparisonOperator.EQUAL,
				"\"Fail\"");
		XSSFPatternFormatting fill2 = rule2.createPatternFormatting();
		fill2.setFillBackgroundColor(IndexedColors.RED.index);
		fill2.setFillPattern(PatternFormatting.SOLID_FOREGROUND);

		XSSFConditionalFormattingRule rule3 = sheetFormatting.createConditionalFormattingRule(ComparisonOperator.EQUAL,
				"\"No Run\"");
		XSSFPatternFormatting fill3 = rule3.createPatternFormatting();
		fill3.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.index);
		fill3.setFillPattern(PatternFormatting.SOLID_FOREGROUND);

		CellRangeAddress[] regions = { CellRangeAddress.valueOf(range) };
		sheetFormatting.addConditionalFormatting(regions, new XSSFConditionalFormattingRule[] { rule1, rule2, rule3 });

		logger.debug("End of TestCase: updateTestStepResult");
	}

}
