package com.lifelens.automation.tabulardata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test class for ValueFactory
 * 
 * @author venkata.kintali(CO54151)
 * 
 * @version 1.0
 * 
 * @since 30.10.2014
 * 
 */
public class TestValueFactory {

	/* The XSSWorkbook */
	private XSSFWorkbook workbook = null;
	private XSSFSheet sheet = null;

	@Before
	public void createTestData() throws IOException {
		File testSampleFile = new File("test/resources/SampleScripts.xlsx");

		assertTrue("Test sample script file not exists", testSampleFile.exists());
		FileInputStream fis = new FileInputStream(testSampleFile);
		workbook = new XSSFWorkbook(fis);
		// getting ValueFactory Sheet from WorkBook .
		sheet = workbook.getSheet("ValueFactory");

		assertNotNull("Test sample script ValueFactory sheet not exists", sheet);
	}

	@Test
	public void GIVEN_value_is_special_datetime_tag_SHOULD_return_current_datetime_in_short_format()
			throws ParseException {
		Value datetimeValue = ValueFactory.valueFor("{dateTime}");
		assertTrue(datetimeValue.toString().contains(getCurrentTimeStamp()));
	}

	@Test
	public void GIVEN_value_is_special_any_tag_SHOULD_return_current_value_with_any() throws ParseException {
		Value anyValue = ValueFactory.valueFor("{any}");
		assertEquals(Value.acceptAny(), anyValue);
		assertEquals("??", anyValue.toString());
	}

	@Test
	public void GIVEN_string_type_cell_is_SHOULD_return_String_TypeValue() {
		// Testing Cell type has String .
		Cell cell = sheet.getRow(0).getCell(0);
		assertEquals(new Value("superuser"), ValueFactory.valueFor(cell));
	}

	@Test
	public void GIVEN_date_type_cell_is_SHOULD_return_Date_TypeValue() {

		// Testing Cell type has Date .
		Cell cell = sheet.getRow(1).getCell(0);
		assertEquals(new Value("01/01/2015"), ValueFactory.valueFor(cell));
	}

	@Test
	public void GIVEN_double_type_cell_is_SHOULD_return_Double_TypeValue() {
		// Testing Cell type has Double .
		Cell cell = sheet.getRow(2).getCell(0);
		assertEquals(new Value("242424"), ValueFactory.valueFor(cell));
	}

	@Test
	public void GIVEN_boolean_type_cell_is_SHOULD_return_Boolean_True_Value() {
		// Testing Cell type has Boolean="true" .
		Cell cell = sheet.getRow(3).getCell(0);
		assertEquals(new Value("true"), ValueFactory.valueFor(cell));
	}

	@Test
	public void GIVEN_boolean_type_cell_is_SHOULD_return_Boolean_False_Value() {
		// Testing Cell type has Boolean="false" .
		Cell cell = sheet.getRow(4).getCell(0);
		assertEquals(new Value("false"), ValueFactory.valueFor(cell));
	}

	@Test
	public void GIVEN_blank_type_cell_is_SHOULD_return_Blank_Type() {
		// Testing Cell type has Blank
		Cell cell = sheet.getRow(5).getCell(0);
		assertEquals(new Value(""), ValueFactory.valueFor(cell));
		assertTrue(ValueFactory.valueFor(cell).toString().equals(""));
	}

	@Test
	public void GIVEN_trimmed_cell_value_is_SHOULD_give_trimmed_value() {
		// Testing Cell type has Blank
		Cell cell = sheet.getRow(6).getCell(0);
		assertTrue(ValueFactory.valueFor(cell).toString().equals("muppet"));
	}

	private String getCurrentTimeStamp() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yy");
		Date now = new Date();
		String currentDate = sdfDate.format(now);
		return currentDate;
	}
}
