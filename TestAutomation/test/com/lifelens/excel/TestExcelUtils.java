package com.lifelens.excel;

import static org.junit.Assert.assertEquals;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.lifelens.exceptions.TestAutomationException;

public class TestExcelUtils {

	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();

	private File excelFile;

	@Before
	public void createTestData() throws IOException {
		excelFile = testFolder.newFile("TestExcelUtils.xls");

		// create workbook with 3 rows
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("sheet1");
		XSSFRow row1 = sheet.createRow(0);
		XSSFRow row2 = sheet.createRow(1);
		XSSFRow row3 = sheet.createRow(2);

		// add data to rows
		String[] data1 = { "one", "two", "three" };
		createDataInRow(row1, data1);
		String[] data2 = { "four", "five", "six" };
		createDataInRow(row2, data2);
		String[] data3 = { "seven", "eight", "nine" };
		createDataInRow(row3, data3);

		// write the file out
		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(excelFile));
		workbook.write(out);

	}

	private void createDataInRow(XSSFRow row, String[] data) {
		for (int i = 0; i < data.length; i++) {
			XSSFCell cell = row.createCell(i);
			cell.setCellValue(data[i]);
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void GIVEN_null_workbook_SHOULD_throw_error() {

		XSSFWorkbook nullWorkbook = null;
		new ExcelUtils(nullWorkbook);

	}

	@Test(expected = IllegalArgumentException.class)
	public void GIVEN_null_file_SHOULD_throw_error() throws IOException {

		File nullFile = null;
		new ExcelUtils(nullFile);

	}

	@Test
	public void GIVEN_workbook_with_sheet_WHEN_ask_for_row_count_with_wrong_sheet_SHOULD_return_zero()
			throws IOException {

		ExcelUtils excelUtils = new ExcelUtils(excelFile);
		assertEquals(0, excelUtils.getRowCount("sheet_wrong"));

	}

	@Test
	public void GIVEN_workbook_with_sheet_WHEN_ask_for_column_count_with_wrong_sheet_SHOULD_return_minus1()
			throws IOException {

		ExcelUtils excelUtils = new ExcelUtils(excelFile);
		assertEquals(-1, excelUtils.getColumnCount("sheet_wrong"));

	}

	@Test
	public void GIVEN_workbook_with_3_rows_SHOULD_report_3_rows() throws IOException {

		ExcelUtils excelUtils = new ExcelUtils(excelFile);
		assertEquals(3, excelUtils.getRowCount("sheet1"));

	}

	@Test
	public void GIVEN_workbook_with_3_columns_SHOULD_report_3_columns() throws IOException {

		ExcelUtils excelUtils = new ExcelUtils(excelFile);
		assertEquals(3, excelUtils.getColumnCount("sheet1"));

	}

	@Test
	public void GIVEN_workbook_with_string_data_SHOULD_return_correct_cell_data() throws IOException,
			TestAutomationException {

		ExcelUtils excelUtils = new ExcelUtils(excelFile);
		assertEquals("one", excelUtils.getCellData("sheet1", 0, 1));
		assertEquals("two", excelUtils.getCellData("sheet1", 1, 1));
		assertEquals("three", excelUtils.getCellData("sheet1", 2, 1));

		assertEquals("seven", excelUtils.getCellData("sheet1", 0, 3));
		assertEquals("eight", excelUtils.getCellData("sheet1", 1, 3));
		assertEquals("nine", excelUtils.getCellData("sheet1", 2, 3));

	}

	@Test
	public void GIVEN_workbook_with_string_data_WHEN_ask_for_data_in_wrong_sheet_SHOULD_return_empty_string()
			throws IOException, TestAutomationException {

		ExcelUtils excelUtils = new ExcelUtils(excelFile);
		assertEquals("", excelUtils.getCellData("sheet_wrong", 0, 1));
	}

	@Test
	public void GIVEN_workbook_with_string_data_WHEN_ask_for_data_in_row_zero_SHOULD_return_empty_string()
			throws IOException, TestAutomationException {

		ExcelUtils excelUtils = new ExcelUtils(excelFile);
		assertEquals("", excelUtils.getCellData("sheet1", 1, 0));
	}

	@Test
	public void GIVEN_workbook_with_string_data_WHEN_ask_for_data_in_cell_that_does_not_exist_SHOULD_return_empty_string()
			throws IOException, TestAutomationException {

		ExcelUtils excelUtils = new ExcelUtils(excelFile);
		assertEquals("", excelUtils.getCellData("sheet1", 36, 1));
	}
}
