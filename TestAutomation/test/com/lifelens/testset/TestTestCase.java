package com.lifelens.testset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.lifelens.exceptions.TestAutomationException;

/**
 * JUnit test cases for TestCase
 * 
 * @author venkata.kintali(C054151)
 * 
 * @version 1.0
 * 
 * @since 12.11.2014
 * 
 */

public class TestTestCase {
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private TestCase testcase, dataTestcase;
	private Row sheetRow;

	/** The temporary folder */
	@Rule
	public TemporaryFolder temporaryFolder = new TemporaryFolder();

	@Before
	public void createTestData() throws IOException, TestAutomationException {
		File testCaseFile = new File("test/resources/SampleScripts.xlsx");
		File newTestCaseFile = temporaryFolder.newFile("TemporarySampleTestCase.xlsx");
		// copying SampleTestCase script to temporary file
		FileUtils.copyFile(testCaseFile, newTestCaseFile);

		assertTrue(testCaseFile.exists());
		assertTrue(newTestCaseFile.exists());

		FileInputStream fis = new FileInputStream(newTestCaseFile);
		workbook = new XSSFWorkbook(fis);

		// getting sheets from WorkBook .
		sheet = workbook.getSheet("TestCaseData");
		assertNotNull(sheet);
		testcase = new TestCase(sheet);
		sheet = workbook.getSheet("LogInTestCase");
		assertNotNull(sheet);
		dataTestcase = new TestCase(sheet);
	}

	@Test
	public void testConstructor() {
		assertNotNull(testcase);
		assertTrue(testcase.hasTimer());
		assertTrue(testcase.hasContinueExecution());
	}

	@Test(expected = TestAutomationException.class)
	public void GIVEN_sheet_name_is_Null_SHOULD_throw_error() throws TestAutomationException {
		testcase = new TestCase(null);
		assertNull(testcase);
	}

	@Test(expected = TestAutomationException.class)
	public void GIVEN_testcase_name_is_inValid_SHOULD_throw_error() throws TestAutomationException {
		sheet = workbook.getSheet("InValid_TestCase");
		testcase = new TestCase(sheet);
	}

	@Test
	public void GIVEN_valid_testcase_SHOULD_set_worksheet() throws TestAutomationException {
		sheet = workbook.getSheet("Index");
		testcase = new TestCase(sheet);
		assertEquals(sheet, testcase.getWorkSheet());
	}

	@Test
	public void GIVEN_testcase_sheet_with_timer_SHOULD_have_timer_column() {
		assertTrue(testcase.hasTimer());
	}

	@Test
	public void GIVEN_testcase_without_timer_SHOULD_not_show_timer_column() {
		assertFalse(dataTestcase.hasTimer());
	}

	@Test
	public void GIVEN_testcase_sheet_with_continue_execution_SHOULD_have_continue_execution_column() {
		assertTrue(testcase.hasContinueExecution());
	}

	@Test
	public void GIVEN_testcase_without_continue_execution_SHOULD_not_show_continue_execution_column() {
		assertFalse(dataTestcase.hasContinueExecution());
	}

	@Test
	public void testSetAndGetTestSheet() {
		testcase.setTestSheet(workbook.getSheet("LogInTestCase"));
		assertEquals("LogInTestCase", testcase.getWorkSheet().getSheetName());
	}

	@Test
	public void GIVEN_pass_to_update_status_in_index_SHOULD_set_status_to_pass() {
		sheetRow = getRowFromTestCase(1);
		dataTestcase.updateTestStepResult(true, sheetRow, 0);
		assertEquals("Pass", sheetRow.getCell(3).getStringCellValue());
	}

	@Test
	public void GIVEN_fail_to_update_status_in_index_SHOULD_set_status_to_fail() {
		sheetRow = getRowFromTestCase(2);
		dataTestcase.updateTestStepResult(false, sheetRow, 0);
		assertEquals("Fail", sheetRow.getCell(3).getStringCellValue());
	}

	@Test
	public void test_GIVEN_set_formatting_range() {
		String foramatRange = "D" + "2:" + "D" + workbook.getSheet("LogInTestCase").getLastRowNum();
		assertEquals(dataTestcase, dataTestcase.setForamttingRange(foramatRange));
	}

	// test case for applyConditionalFormatting()
	/*
	 * // @Test public void test_GIVEN_Shett_ConditionalFormatting() { String
	 * foramatRange = "D" + "2:" + "D" +
	 * workbook.getSheet("LogInTestCase").getLastRowNum() + 1;
	 * dataTestcase.setForamttingRange
	 * (foramatRange).applyConditionalFormatting(); sheetRow =
	 * getRowFromTestCase(1);
	 * sheetRow.getCell(3).getCellStyle().getFillForegroundColor();
	 * assertEquals((short) 48,
	 * sheetRow.getCell(3).getCellStyle().getFillForegroundColor()); }
	 */

	// Fetching TableRow From Sheet
	private Row getRowFromTestCase(int rowIndex) {
		int rowCount = 0;
		Iterator<Row> rowIterator = workbook.getSheet("LogInTestCase").iterator();

		while (rowIterator.hasNext()) {
			rowIterator.next();
			rowCount++;

			if (rowCount == rowIndex) {
				sheetRow = rowIterator.next();
				break;
			}
		}
		return sheetRow;
	}
}
