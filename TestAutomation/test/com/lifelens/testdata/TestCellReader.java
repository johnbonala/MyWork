package com.lifelens.testdata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.lifelens.exceptions.TestAutomationException;

/**
 * JUnit test class for CellReader
 * 
 * @author venkata.kintali(CO54151)
 * 
 * @version 1.0
 * 
 * @since 10.11.2014
 * 
 */
public class TestCellReader {

	// CHECKSTYLE:OFF
	/** The temporary folder */
	@Rule
	public TemporaryFolder temporaryFolder = new TemporaryFolder();

	// CHECKSTYLE:ON

	/* The XSSWorkbook */
	private XSSFWorkbook workbook = null;
	/* The XSSFSheet */
	private XSSFSheet sheet = null;
	/* The CellReader */
	private CellReader cellreader = null;
	/* The FormulaEvaluator */
	private FormulaEvaluator formEval = null;

	@Before
	public void createTestData() throws IOException {

		File sampleFile = new File("test/resources/SampleScripts.xlsx");
		File newSampleFile = temporaryFolder.newFile("TemporarySampleScripts.xlsx");

		// copying sample script to temporary file
		FileUtils.copyFile(sampleFile, newSampleFile);

		assertTrue(sampleFile.exists());
		assertTrue(newSampleFile.exists());

		FileInputStream fis = new FileInputStream(newSampleFile);
		workbook = new XSSFWorkbook(fis);
		sheet = workbook.getSheet("TestCellReader");
		assertNotNull(sheet);
		// getting FormulaEvaluator Instance
		formEval = workbook.getCreationHelper().createFormulaEvaluator();
		cellreader = new CellReader(formEval);
	}

	@Test(expected = IllegalArgumentException.class)
	public void GIVEN_null_formula_evaluator_WHEN_create_cell_reader_SHOULD_fail() throws TestAutomationException {
		CellReader cellreader = new CellReader(null);
		assertNull(cellreader);
	}

	@Test
	public void testCellReaderConstructor() {
		assertNotNull(new CellReader(formEval));
	}

	@Test
	public void GIVEN_date_value_SHOULD_return_Date_formated_Cell() throws TestAutomationException {
		// Testing Cell type has Date .
		assertEquals("01/01/2015", cellreader.getStringCellValue(sheet.getRow(0).getCell(0)));
	}

	@Test
	public void GIVEN_numeric_value_SHOULD_return_Numeric_formated_Cell() throws TestAutomationException {
		// Testing Cell type has Numeric .
		assertEquals("484848", cellreader.getStringCellValue(sheet.getRow(1).getCell(0)));
	}

	@Test
	public void GIVEN_string_Value_SHOULD_return_String_formated_Cell() throws TestAutomationException {
		// Testing Cell type has String .
		assertEquals("superuser", cellreader.getStringCellValue(sheet.getRow(2).getCell(0)));
	}

	@Test
	public void GIVEN_formula_value_SHOULD_return_Formula_Type_Cell() throws TestAutomationException {
		// Testing Cell type has Double Formula ( =A6*A7).
		assertEquals("16.0", cellreader.getStringCellValue(sheet.getRow(3).getCell(0)));
	}

	@Test
	public void GIVEN_formula_value_SHOULD_return_Formula_Type1_Cell() throws TestAutomationException {
		// Testing Cell type has Number Formula
		assertEquals("16", cellreader.getStringCellValue(sheet.getRow(7).getCell(0)));
	}

	@Test
	public void GIVEN_blank_value_SHOULD_return_Blank_formated_Cell() throws TestAutomationException {
		// Testing Cell type has Blank .
		assertEquals("", cellreader.getStringCellValue(sheet.getRow(4).getCell(0)));
		assertFalse(cellreader.getStringCellValue(sheet.getRow(4).getCell(0)).length() > 0);
	}

	@Test
	public void GIVEN_value_iS_NotValidInput_Cell() throws TestAutomationException {
		// Testing InValid Input Cell .
		assertEquals("NOT_A_VALID_INPUT", cellreader.getStringCellValue(sheet.getRow(8).getCell(0)));
	}

}
