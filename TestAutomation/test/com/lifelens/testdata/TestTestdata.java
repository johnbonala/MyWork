package com.lifelens.testdata;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.lifelens.exceptions.TestAutomationException;

public class TestTestdata {

	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();

	private File testDataFile;

	@Test
	public void GIVEN_test_data_file_that_contains_staffnumber_SHOULD_return_value() throws TestAutomationException {

		Testdata testData = new Testdata("test/resources/testdata_not_fetched.xlsx", "Sheet1");
		testData.readTestdata();
		assertEquals("9240801", testData.getParameterValue("@Username1"));
		assertEquals("9240802", testData.getParameterValue("@Username2"));
		assertEquals("9240803", testData.getParameterValue("@Username3"));
		assertEquals("9270802", testData.getParameterValue("@Username5"));
	}

	@Test
	public void GIVEN_test_data_file_that_contains_date_SHOULD_return_value() throws TestAutomationException {

		Testdata testData = new Testdata("test/resources/testdata_not_fetched.xlsx", "Sheet1");
		testData.readTestdata();
		assertEquals(getCurrentTimeStamp(), testData.getParameterValue("@CurrentDate1"));
	}

	@Test
	public void GIVEN_test_data_file_that_contains_date_after_blank_columns_SHOULD_return_value()
			throws TestAutomationException {

		Testdata testData = new Testdata("test/resources/testdata_not_fetched.xlsx", "Sheet1");
		testData.readTestdata();
		assertEquals("01/10/2014", testData.getParameterValue("@StartNextMonth1"));

	}

	@Test
	public void GIVEN_test_data_file_that_contains_cell_with_formula_SHOULD_return_value()
			throws TestAutomationException {

		Testdata testData = new Testdata("test/resources/testdata_not_fetched.xlsx", "Sheet1");
		testData.readTestdata();
		assertEquals(getCurrentTimeStamp(), testData.getParameterValue("@CurrentDate1"));

	}

	/*
	 * @Test(expected = TestAutomationException.class) public void
	 * GIVEN_test_data_file_is_not_there_in_testlab_SHOULD_throw_error() throws
	 * TestAutomationException { Testdata testData = new
	 * Testdata("test/resources/testdata.xlsx", "Sheet1");
	 * testData.readTestdata();
	 * 
	 * }
	 */

	private String getCurrentTimeStamp() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
		Date now = new Date();
		String strDate = sdfDate.format(now);
		return strDate;
	}
}
