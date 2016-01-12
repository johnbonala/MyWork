package com.lifelens.browsers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PatternFormatting;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.lifelens.automation.tabulardata.FileBuilder;
import com.lifelens.exceptions.TestAutomationException;
import com.lifelens.globals.Global;
import com.lifelens.interpreter.TestContext;
import com.lifelens.testdata.Testdata;
import com.lifelens.testset.TestSet;

/**
 * JUnit test class for FileProcessor
 * 
 * @author Pratik.Gilda(CO50636)
 * 
 * @version 1.0
 * 
 * @since 24.10.2014
 * 
 */
public class TestFileProcessor {

	@Rule
	public TemporaryFolder temporaryFolder = new TemporaryFolder();

	private Testdata testData;

	private TestSet testSet;

	private TestContext testContext;

	private FileProcessor fileProcessor;

	private File temporarySampleFile;

	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();

	@Before
	public void createTestData() throws InvalidFormatException, IOException, TestAutomationException {
		File sampleFile = new File("test/resources/TestSampleScripts.xlsx");
		temporarySampleFile = temporaryFolder.newFile("TemporarySampleScripts.xlsx");

		// copying sample script to temporary file
		FileUtils.copyFile(sampleFile, temporarySampleFile);

		testData = new Testdata("test/resources/testdata_not_fetched.xlsx", "Sheet1");
		testData.readTestdata();
		testSet = new TestSet(temporarySampleFile, "1", testData);
		testContext = new TestContext(testSet);
		Global.setTestLabPath("test/");

		fileProcessor = new FileProcessor(testContext);
	}

	@Test
	public void GIVEN_same_expected_data_as_actual_SHOULD_pass_compare() throws TestAutomationException, IOException {
		boolean result = fileProcessor.compareFiles(testSet, temporarySampleFile, "Expected_report_output", "",
				"report_output.csv");

		assertTrue(result);
		// there should not be a cell with red style if compare is successful
		assertFalse(isUnmatchedCellMarkedUp("Expected_report_output"));
	}

	@Test
	public void GIVEN_less_expected_data_SHOULD_pass_compare_if_data_is_same() throws TestAutomationException,
			IOException {
		boolean result = fileProcessor.compareFiles(testSet, temporarySampleFile, "Expected_report_output small", "",
				"report_output.csv");

		assertTrue(result);
		// there should not be a cell with red style if compare is successful
		assertFalse(isUnmatchedCellMarkedUp("Expected_report_output small"));
	}

	@Test
	public void GIVEN_wrong_expected_data_SHOULD_fail_compare() throws TestAutomationException, IOException {
		boolean result = fileProcessor.compareFiles(testSet, temporarySampleFile, "Expected_log_file", "",
				"report_output.csv");

		assertFalse(result);
		// there should be a cell\cells with red style if compare is
		// not successful
		assertTrue(isUnmatchedCellMarkedUp("Expected_log_file"));
	}

	@Test
	public void GIVEN_wrong_expected_data_SHOULD_fail_compare_and_do_correct_markup() throws TestAutomationException,
			IOException {
		boolean result = fileProcessor.compareFiles(testSet, temporarySampleFile, "Sheet1", "", "report_output.csv");

		assertFalse(result);
		// there should be a cell\cells with red style if compare is
		// not successful
		assertTrue(isUnmatchedCellMarkedUp("Sheet1"));
	}

	@Test
	public void GIVEN_textToVerify_SHOULD_verify_from_excel_file() throws TestAutomationException, IOException {
		// test for correct value
		boolean result = fileProcessor.verifyExcelText(testSet, temporarySampleFile, "Expected_log_file", 2, 1,
				"JQ1855");
		assertTrue(result);

		// test for incorrect value
		result = fileProcessor.verifyExcelText(testSet, temporarySampleFile, "Expected_log_file", 2, 1,
				"Incorrect value");
		assertFalse(result);

		// check that cell style is applied or not after failed result
		Cell cell = testSet.getWorkbook().getSheet("Expected_log_file").getRow(1).getCell(1);
		assertFalse(IndexedColors.LIGHT_GREEN.index == cell.getCellStyle().getFillForegroundColor());
		assertFalse(PatternFormatting.SOLID_FOREGROUND == cell.getCellStyle().getFillPattern());

		// test for empty value
		result = fileProcessor.verifyExcelText(testSet, temporarySampleFile, "Expected_log_file", 7, 3, "");
		assertTrue(result);

		FileInputStream fis = new FileInputStream(temporarySampleFile);
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		// check that cell style is applied or not after successful result
		cell = workbook.getSheet("Expected_log_file").getRow(6).getCell(3);
		assertEquals(IndexedColors.LIGHT_GREEN.index, cell.getCellStyle().getFillForegroundColor());
		assertEquals(PatternFormatting.SOLID_FOREGROUND, cell.getCellStyle().getFillPattern());
	}

	@Test
	public void GIVEN_textToVerify_SHOULD_verify_from_csv_file() throws TestAutomationException {
		// test for correct value
		boolean result = fileProcessor.verifyCsvText("(report_output.csv,1,1)", "JQ1855");
		assertTrue(result);
		// check that converted file does not exists
		assertFalse(new File(FileBuilder.WORKBOOKNAME).exists());

		// test for incorrect value
		result = fileProcessor.verifyCsvText("(report_output.csv,1,1)", "Incorrect value");
		assertFalse(result);
		// check that converted file does not exists
		assertFalse(new File(FileBuilder.WORKBOOKNAME).exists());

		// test for empty value
		result = fileProcessor.verifyCsvText("(report_output.csv,2,1)", "");
		assertTrue(result);
		// check that converted file does not exists
		assertFalse(new File(FileBuilder.WORKBOOKNAME).exists());
	}

	/**
	 * Checks if unmatched cells are marked up or not
	 * 
	 * @return isMarkedUP
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private boolean isUnmatchedCellMarkedUp(String sheetName) throws FileNotFoundException, IOException {
		boolean isMarkedUp = false;

		FileInputStream fis = new FileInputStream(temporarySampleFile);
		XSSFWorkbook workbook = new XSSFWorkbook(fis);

		XSSFSheet sheet = workbook.getSheet(sheetName);
		Cell cell;

		// check if red colour is marked up or not for unmatched cells
		for (int i = 0; i < sheet.getLastRowNum(); i++) {
			for (int j = 0; j < sheet.getRow(0).getLastCellNum(); j++) {
				cell = testSet.getWorkbook().getSheet(sheetName).getRow(i).getCell(j);

				if ((cell != null) && (cell.getCellStyle().getFillForegroundColor() == IndexedColors.RED.index)) {
					isMarkedUp = true;
				}
			}

		}
		return isMarkedUp;
	}
}
