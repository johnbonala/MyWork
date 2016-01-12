package com.lifelens.testset;

import static org.junit.Assert.assertEquals;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.lifelens.exceptions.TestAutomationException;

public class TestTestLab {

	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();

	/** our test lab **/
	private File testLabFolder;

	@Before
	public void createTestData() throws IOException {
		testLabFolder = testFolder.newFolder();

	}

	@Test(expected = TestAutomationException.class)
	public void GIVEN_test_lab_folder_is_a_file_SHOULD_throw_error() throws TestAutomationException, IOException {

		File testLabFolderWhichIsAFile = testFolder.newFile();

		new TestLab(testLabFolderWhichIsAFile.getAbsolutePath());

	}

	@Test(expected = TestAutomationException.class)
	public void GIVEN_test_lab_folder_does_not_exist_SHOULD_throw_error() throws TestAutomationException, IOException {

		new TestLab(testLabFolder.getAbsolutePath() + "-does-not-exist-folder");

	}

	@Test(expected = TestAutomationException.class)
	public void GIVEN_test_lab_folder_contains_1_file__but_not_excel_format_SHOULD_throw_error()
			throws TestAutomationException, IOException {

		File testSet1 = new File(testLabFolder, "test-set-1.xlsx");
		FileUtils.write(testSet1, "test set 1");
		TestLab testLab = new TestLab(testLabFolder.getAbsolutePath());
		testLab.extractTestSets();
	}

	@Test
	public void GIVEN_test_lab_folder_contains_1_excel_file_SHOULD_load_1_test_set() throws TestAutomationException,
			IOException {

		createTestSet("test-set-1.xlsx", "index");

		TestLab testLab = new TestLab(testLabFolder.getAbsolutePath());
		assertEquals(1, testLab.extractTestSets().size());
	}

	@Test
	public void GIVEN_test_lab_folder_contains_1_file_but_missing_index_sheet_SHOULD_load_0_test_sets()
			throws TestAutomationException, IOException {

		createTestSet("test-set-1.xlsx", "not-index");

		TestLab testLab = new TestLab(testLabFolder.getAbsolutePath());
		assertEquals(0, testLab.extractTestSets().size());
	}

	@Test
	public void GIVEN_test_lab_folder_contains_3_excel_files_SHOULD_load_3_test_sets() throws TestAutomationException,
			IOException {

		createTestSet("test-set-1.xlsx", "index");
		createTestSet("test-set-2.xlsx", "index");
		createTestSet("test-set-3.xlsx", "index");

		TestLab testLab = new TestLab(testLabFolder.getAbsolutePath());
		assertEquals(3, testLab.extractTestSets().size());
	}

	@Test
	public void GIVEN_test_lab_folder_contains_5_excel_files_SHOULD_load_5_test_sets_with_file_names_in_alphabetical_oder()
			throws TestAutomationException, IOException {

		createTestSet("a-test-set-1.xlsx", "index");
		createTestSet("b-test-set-2.xlsx", "index");
		createTestSet("c-test-set-3.xlsx", "index");
		createTestSet("1-test-set-4.xlsx", "index");
		createTestSet("3-test-set-5.xlsx", "index");

		TestLab testLab = new TestLab(testLabFolder.getAbsolutePath());
		List<String> testSets = testLab.extractTestSets();
		assertEquals("1-test-set-4.xlsx", testSets.get(0));
		assertEquals("3-test-set-5.xlsx", testSets.get(1));
		assertEquals("a-test-set-1.xlsx", testSets.get(2));
		assertEquals("b-test-set-2.xlsx", testSets.get(3));
		assertEquals("c-test-set-3.xlsx", testSets.get(4));
	}

	private void createTestSet(String testSetName, String sheetName) throws FileNotFoundException, IOException {
		File testSet1 = new File(testLabFolder, testSetName);
		XSSFWorkbook workbook = new XSSFWorkbook();
		workbook.createSheet(sheetName);
		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(testSet1));
		workbook.write(out);
	}
}
