package com.lifelens.excel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import au.com.bytecode.opencsv.CSV;
import au.com.bytecode.opencsv.CSVWriteProc;
import au.com.bytecode.opencsv.CSVWriter;

import com.lifelens.automation.tabulardata.FileBuilder;
import com.lifelens.exceptions.TestAutomationException;

public class TestFileBuilder {

	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();

	private File csvFile;
	// private File textFile;
	private String quotedString = "'Change " + '"' + '"' + '"' + '"' + "Addlane1" + '"' + '"' + '"' + '"' + "'";

	@Before
	public void createTestData() throws IOException {
		csvFile = testFolder.newFile("TestFileBuilder.csv");
	}

	@Test(expected = TestAutomationException.class)
	public void GIVEN_csv_file_does_not_exist_SHOULD_throw_error() throws TestAutomationException {
		FileBuilder.csvToExcelConversion("no file");
	}

	@Test
	public void testCsvToExcelConversion() throws IOException, TestAutomationException {

		CSV csv = CSV.separator(',').create();

		// create rows in csv file
		csv.write(csvFile, new CSVWriteProc() {

			public void process(CSVWriter out) {
				out.writeNext("Column1", "Column2");
				out.writeNext("Value1", "Value2");
				out.writeNext(quotedString, quotedString);
			}
		});

		XSSFWorkbook wb = FileBuilder.csvToExcelConversion(csvFile.getAbsolutePath());

		// verify excel file is being generated and csv values
		assertEquals("Column1", wb.getSheetAt(0).getRow(0).getCell(0).getStringCellValue());
		assertEquals("Column2", wb.getSheetAt(0).getRow(0).getCell(1).getStringCellValue());
		assertEquals("Value1", wb.getSheetAt(0).getRow(1).getCell(0).getStringCellValue());
		assertEquals("Value2", wb.getSheetAt(0).getRow(1).getCell(1).getStringCellValue());
		assertEquals(quotedString, wb.getSheetAt(0).getRow(2).getCell(0).getStringCellValue());
		assertEquals(quotedString, wb.getSheetAt(0).getRow(2).getCell(1).getStringCellValue());

	}

	@Test
	public void GIVEN_csvFile_with_3_rows_SHOULD_report_3_rows_in_workbook() throws IOException,
			TestAutomationException {

		CSV csv = CSV.separator(',').quote('"').create();

		// create three rows in csv file
		csv.write(csvFile, new CSVWriteProc() {
			public void process(CSVWriter out) {
				out.writeNext("Column1", "Column2");
				out.writeNext("Value1", "Value2");
				out.writeNext("Value3", "Value4");
			}
		});

		XSSFWorkbook wb = FileBuilder.csvToExcelConversion(csvFile.getAbsolutePath());
		ExcelUtils excelUtils = new ExcelUtils(wb);
		assertEquals(3, excelUtils.getRowCount(FileBuilder.WORKSHEETNAME));

		// verifying columns
		assertEquals(2, excelUtils.getColumnCount(FileBuilder.WORKSHEETNAME));
	}

	@Test
	public void testTxtToExcelConversion() throws IOException, TestAutomationException {
		String convertedCsvFileName = FileBuilder.txtToCSVConversion(createTestFile().getAbsolutePath());
		XSSFWorkbook wb = FileBuilder.csvToExcelConversion(convertedCsvFileName);

		// verify excel file is being generated and csv values
		assertEquals("5421310009", wb.getSheetAt(0).getRow(0).getCell(0).getStringCellValue());
		assertEquals("54213100010", wb.getSheetAt(0).getRow(1).getCell(0).getStringCellValue());
	}

	@Test(expected = TestAutomationException.class)
	public void GIVEN_text_file_does_not_exist_SHOULD_throw_error() throws TestAutomationException {
		FileBuilder.txtToCSVConversion("no file");
	}

	@Test
	public void GIVEN_txtFile_with_2_rows_SHOULD_create_workbook_with_sheet_with_2_rows() throws IOException,
			TestAutomationException {
		String convertedCsvFileName = FileBuilder.txtToCSVConversion(createTestFile().getAbsolutePath());
		XSSFWorkbook wb = FileBuilder.csvToExcelConversion(convertedCsvFileName);
		ExcelUtils excelUtils = new ExcelUtils(wb);
		// comparing textfile rows and generated workbook rows
		assertEquals(2, excelUtils.getRowCount(FileBuilder.WORKSHEETNAME));
	}

	@Test(expected = NullPointerException.class)
	public void GIVEN_text_file_is_existed_but_no_data_SHOULD_throw_error() throws IOException, TestAutomationException {
		// creating text file with no data.
		File emptytxtfile = testFolder.newFile("TextFileWithoutData.txt");
		BufferedWriter out = new BufferedWriter(new FileWriter(emptytxtfile));
		out.write("");
		out.close();

		XSSFWorkbook wb = FileBuilder.csvToExcelConversion(FileBuilder.txtToCSVConversion(emptytxtfile
				.getAbsolutePath()));
		assertNull(wb.getSheetAt(0).getRow(0).getCell(0).getStringCellValue());
	}

	@Test
	public void GIVEN_csv_file_exists_SHOULD_create_workbook_with_sheet_name() throws TestAutomationException,
			IOException {
		Workbook excelFile = FileBuilder.csvToExcelConversion(csvFile.getCanonicalPath());
		Sheet sheet = excelFile.getSheet(FileBuilder.WORKSHEETNAME);
		assertNotNull(sheet);
	}

	@After
	public void cleanUp() {
		// delete csv file that is created
		new File(FileBuilder.CONVERTEDCSVFILENAME).delete();
	}

	// creating text file with data.
	private File createTestFile() throws IOException {
		File csvFile = testFolder.newFile("TestTextFile.txt");
		BufferedWriter out = new BufferedWriter(new FileWriter(csvFile));
		out.write("5421310009   09230\n");
		out.write("54213100010   151200\n");
		out.close();
		return csvFile;
	}
}
