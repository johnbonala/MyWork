package com.lifelens.automation.tabulardata;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.lifelens.exceptions.TestAutomationException;

public class TestFileBuilder {

	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();

	private File csvFile;

	@Before
	public void createTestData() throws IOException {
		csvFile = testFolder.newFile("TestFileBuilder.csv");
		BufferedWriter out = new BufferedWriter(new FileWriter(csvFile));
		out.write("one, 1, 2, 3\n");
		out.write("two, 4, 5, 6\n");
		out.write("three, 7, 8, 9\n");
		out.close();
	}

	@Test(expected = TestAutomationException.class)
	public void GIVEN_csv_file_that_does_not_exist_SHOULD_throw_error() throws TestAutomationException {

		FileBuilder.csvToExcelConversion("no file");

	}

	@Test
	public void GIVEN_csv_file_exists_SHOULD_create_workbook_with_sheet_name() throws TestAutomationException,
			IOException {

		Workbook excelFile = FileBuilder.csvToExcelConversion(csvFile.getCanonicalPath());
		Sheet sheet = excelFile.getSheet(FileBuilder.WORKSHEETNAME);
		assertNotNull(sheet);

	}

	@Test
	public void GIVEN_csv_file_with_3_rows_SHOULD_create_workbook_with_sheet_with_3_rows()
			throws TestAutomationException, IOException {

		Workbook excelFile = FileBuilder.csvToExcelConversion(csvFile.getCanonicalPath());
		Sheet sheet = excelFile.getSheet(FileBuilder.WORKSHEETNAME);
		assertNotNull(sheet.getRow(0));
		assertNotNull(sheet.getRow(1));
		assertNotNull(sheet.getRow(2));
	}

	@After
	public void cleanUp() {
		// at this point the junit rule has not cleared down the resource - but
		// it is cleared just after the after call
		assertThat(testFolder.getRoot().exists(), is(true));
		assertThat(csvFile.exists(), is(true));

		// also delete the workbook that is created
		new File(FileBuilder.WORKBOOKNAME).delete();
	}
}
