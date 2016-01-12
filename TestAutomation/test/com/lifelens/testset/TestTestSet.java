package com.lifelens.testset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.lifelens.exceptions.TestAutomationException;
import com.lifelens.testdata.Testdata;

/**
 * JUnit test class for TestSet.java
 * 
 * @author co54151
 * 
 * @version 1.0
 * 
 * @since 18.11.2014
 * 
 */

public class TestTestSet {
	private Testdata testData;
	private TestSet testSet;
	private File tempSampleScript, tempTestDataScript;
	private static String priority = "High";

	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();

	@Before
	public void createTestData() throws IOException, TestAutomationException {
		// Preparing TestData Instance
		File testDataScript = new File("test/resources/testdata_not_fetched.xlsx");
		tempTestDataScript = new File("TemporaryTestDataScriptsFile.xlsx");
		FileUtils.copyFile(testDataScript, tempTestDataScript);

		testData = new Testdata(tempTestDataScript.getAbsolutePath(), "Sheet2");
		testData.readTestdata();
		// preparing TestSet Instance
		File sampleScript = new File("test/resources/SampleScripts.xlsx");
		tempSampleScript = new File("TemporarySampleScriptsFile.xlsx");
		FileUtils.copyFile(sampleScript, tempSampleScript);

		testSet = new TestSet(tempSampleScript, priority, testData);
	}

	/**
	 * Testing given testset file name is wrong. so it should throw exception.
	 */
	@Test(expected = FileNotFoundException.class)
	public void GIVEN_testset_file_name_is_wrong_SHOULD_throw_error() throws IOException, TestAutomationException {
		File sampleScript = new File("test/resources/TestSetSampleScripts.xlsx");
		tempSampleScript = new File("TemporarySampleScriptsFile.xlsx");
		FileUtils.copyFile(sampleScript, tempSampleScript);

		testSet = new TestSet(tempSampleScript, priority, testData);
		assertNull(testSet);
	}

	/**
	 * Testing given testset file name is null. so it should throw exception.
	 */
	@Test(expected = NullPointerException.class)
	public void GIVEN_testset_file_name_is_Null_SHOULD_throw_error() throws IOException, TestAutomationException {
		String testSetFilePath = null;
		File sampleScript = new File(testSetFilePath);
		tempSampleScript = new File("TemporarySampleScriptsFile.xlsx");
		FileUtils.copyFile(sampleScript, tempSampleScript);

		testSet = new TestSet(tempSampleScript, priority, testData);
		assertNull(testSet);
	}

	/**
	 * Testing testset having no testcases
	 */
	@Test(expected = TestAutomationException.class)
	public void GIVEN_testset_doesnot_contains_Sheet_Names() throws IOException, TestAutomationException {
		File testLabFolder = testFolder.newFolder();
		File testSetNoSheets = new File(testLabFolder, "TestSet_Having_NOSheets.xlsx");
		FileUtils.write(testSetNoSheets, "testSet Having  NoSheets ");
		testSet = new TestSet(testLabFolder.getAbsoluteFile(), priority, testData);

		assertNotNull(testSet);
		assertEquals(0, testSet.getWorkbook().getNumberOfSheets());
	}

	/**
	 * @throws TestAutomationException
	 * 
	 * 
	 */
	@Test
	public void test_setAndgetTestCase() throws TestAutomationException {
		testSet.setTestCase("Checkbox");
		assertTrue(testSet.getTestCase().hasTimer());
	}

	/**
	 * given testcase name is invalied. so it should throw exception
	 */
	@Test(expected = TestAutomationException.class)
	public void GIVEN_sheet_name_is_invalied_SHOULD_throw_error() throws TestAutomationException {
		testSet.setTestCase("new TestCase");
		assertEquals("new TestCase", testSet.getTestCase());
	}

	/**
	 * Testing given testSet having testCases
	 */
	@Test
	public void test_GIVEN_testset_sheets_count_SHOULD_more_then_zero() {
		testSet.loadSheetNames();
		assertTrue(testSet.testCases.size() > 0);
	}

	/**
	 * @throws TestAutomationException
	 */
	@Test
	public void test_indexTestCaseDetails() throws TestAutomationException {
		testSet.getIndexTestCaseDetails();
		String[][] testObject = testSet.getDataProviderObject();
		assertEquals("Log file", testObject[0][0]);
		assertEquals("Reports", testObject[1][0]);
		assertEquals("Checkbox", testObject[2][0]);
		assertEquals("Log file", testObject[0][1]);
		assertEquals("Reports", testObject[1][1]);
		assertEquals("Checkbox", testObject[2][1]);
		assertEquals("High", testObject[0][2]);
		assertEquals("High", testObject[1][2]);
		assertEquals("High", testObject[2][2]);
	}

	/**
	 * @throws IOException
	 * @throws TestAutomationException
	 */
	@Test(expected = Exception.class)
	public void test_indexsheet_contains_no_testcases_SHOULD_throw_error() throws IOException, TestAutomationException {
		File testLabForIndex = testFolder.newFolder();
		File indexSheet = new File(testLabForIndex, "testSet_no_testcases.xlsx");

		XSSFWorkbook workbook = new XSSFWorkbook();
		workbook.createSheet("Index");
		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(indexSheet));
		workbook.write(out);

		testSet = new TestSet(indexSheet, priority, testData);
		assertNotNull(testSet);
		testSet.getIndexTestCaseDetails();
	}

	/**
	 * TestSet should contains index sheet
	 */
	@Test
	public void test_GIVEN_testset_SHOULD_contains_index_sheet() {
		assertFalse(testSet.indexSheetFound());
		testSet.loadSheetNames();
		assertTrue(testSet.indexSheetFound());
	}

	@Test
	public void GIVEN_testset_file_SHOULD_load_testdata_file() {
		assertNotNull(testSet.getTestData());
		assertEquals(testData, testSet.getTestData());
	}

	/**
	 * @throws TestAutomationException
	 */
	@Test
	public void test_updateTestStatus_Results_In_Index_sheet() throws TestAutomationException {
		testSet.setTestCase("Log file");
		testSet.getIndexTestCaseDetails();

		// successful test status update check
		testSet.updateTestStatusInIndex(true);
		assertEquals("Pass", testSet.getWorkbook().getSheet("Index").getRow(1).getCell(5).getStringCellValue());

		// failure test status update check
		testSet.updateTestStatusInIndex(false);
		assertEquals("Fail", testSet.getWorkbook().getSheet("Index").getRow(1).getCell(5).getStringCellValue());
	}

	/**
	 * @throws IOException
	 */
	@Test
	public void GIVEN_testset_file_SHOULD_return_workbook() throws IOException {
		assertNotNull(testSet.getWorkbook());
		assertEquals("TemporarySampleScriptsFile.xlsx", testSet.getWorkbookName());
		assertEquals(tempSampleScript.getAbsolutePath(), testSet.getWorkbookNameAbsolutePath());
	}

	@Test(expected = TestAutomationException.class)
	public void test_getWorkbookLocation() throws TestAutomationException {
		testSet.getWorkbookLocation();
	}

}
