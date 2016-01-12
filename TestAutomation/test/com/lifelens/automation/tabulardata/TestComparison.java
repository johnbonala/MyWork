package com.lifelens.automation.tabulardata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import au.com.bytecode.opencsv.CSV;
import au.com.bytecode.opencsv.CSVWriteProc;
import au.com.bytecode.opencsv.CSVWriter;

import com.lifelens.exceptions.TestAutomationException;

/**
 * JUnit test class for Comparison
 * 
 * @author venkata.kintali(C054151)
 * 
 * @version 1.0
 * 
 * @since 04.11.2014
 * 
 */
public class TestComparison {
	private List<Map<String, Value>> unmatchedRows = new ArrayList<Map<String, Value>>();
	private List<Map<String, Value>> matchedRows = new ArrayList<Map<String, Value>>();

	/** sheet name */
	private String sheetName = "ComparisonData";
	private TabularData actualXlsxData = null, expectedCsvData = null;

	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();

	@Before
	public void createTestResources() throws IOException, TestAutomationException {
		/** getting matched and unMatched list objects */
		getMatchedAndUnMatchedRows();

		// creating csv file with data
		File csvFile = testFolder.newFile("TestCsvFil.csv");
		CSV csv = CSV.separator(',').create();
		// create rows in csv file
		csv.write(csvFile, new CSVWriteProc() {

			public void process(CSVWriter out) {
				out.writeNext("UserId", "Password");
				out.writeNext("superuser1", "muppet1");
				out.writeNext("superuser2", "muppet2");
				out.writeNext("Ibmuser3", "muppet2");
				out.writeNext("ALK993", "muppet");
			}
		});
		expectedCsvData = new CSVData(csvFile.getAbsolutePath(), true);

		actualXlsxData = new WorksheetData("test/resources/SampleScripts.xlsx", sheetName);
	}

	@Test
	public void testCompare() {
		ComparisonResult comparisonResult = new ComparisonResult(unmatchedRows.size() + " rows did not match",
				unmatchedRows, matchedRows);

		ComparisonResult comparisonResult1 = Comparison.compare(expectedCsvData, actualXlsxData);

		assertEquals(comparisonResult.getDescription(), comparisonResult1.getDescription());
		assertEquals(comparisonResult.getUnmatchedRows().size(), comparisonResult1.getUnmatchedRows().size());
		assertEquals(comparisonResult.getMatchedRows().size(), comparisonResult1.getMatchedRows().size());
	}

	@Test
	public void testComparisonAndComparisonResult() {
		ComparisonResult comparisonResult = new ComparisonResult(unmatchedRows.size() + " rows did not match",
				unmatchedRows, matchedRows);

		assertEquals(comparisonResult.getMatchedRows().size(),
				Comparison.compare(expectedCsvData, actualXlsxData, false).getMatchedRows().size());
		assertEquals(comparisonResult.getUnmatchedRows().size(),
				Comparison.compare(expectedCsvData, actualXlsxData, false).getUnmatchedRows().size());
		assertEquals(comparisonResult.getDescription(), Comparison.compare(expectedCsvData, actualXlsxData, false)
				.getDescription());

		assertNotNull(Comparison.compare(expectedCsvData, actualXlsxData, false));

	}

	private void getMatchedAndUnMatchedRows() {

		Map<String, Value> matchedValue = new HashMap<String, Value>();
		matchedValue.put("superuser1", new Value("muppet1"));
		matchedRows.add(matchedValue);

		Map<String, Value> matchedValue2 = new HashMap<String, Value>();
		matchedValue2.put("superuser2", new Value("muppet2"));
		matchedRows.add(matchedValue2);

		Map<String, Value> unMatchedValue = new HashMap<String, Value>();
		unMatchedValue.put("Ibmuser3", new Value("muppet2"));
		unmatchedRows.add(unMatchedValue);

		Map<String, Value> unMatchedValue2 = new HashMap<String, Value>();
		unMatchedValue2.put("ALK993", new Value("muppet"));
		unmatchedRows.add(unMatchedValue2);

	}

}
