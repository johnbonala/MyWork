package com.lifelens.automation.tabulardata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test class for ComparisonResult
 * 
 * @author venkata.kintali(CO54151)
 * 
 * @version 1.0
 * 
 * @since 27.10.2014
 * 
 */

public class TestComparisonResult {

	private List<Map<String, Value>> unmatchedRows = new ArrayList<Map<String, Value>>();
	private List<Map<String, Value>> matchedRows = new ArrayList<Map<String, Value>>();
	private String rowsDescription = "Rows comparison Between Xlsx and Cvs Files";
	private ComparisonResult comparisonResult = null, matchedRowsResult = null;

	@Before
	public void createTestResources() {

		/** filling dummy data into list objects */
		getDummyMatchedAndUnMatchedRows();

		comparisonResult = new ComparisonResult(rowsDescription, unmatchedRows);
		matchedRowsResult = new ComparisonResult(rowsDescription, unmatchedRows, matchedRows);
	}

	@Test
	public void testConstructor() {
		assertEquals(rowsDescription, comparisonResult.getDescription());
		assertFalse(comparisonResult.isSuccessful());
		assertEquals(unmatchedRows, comparisonResult.getUnmatchedRows());
	}

	@Test
	public void testThreeParamConstructor() {
		assertEquals(rowsDescription, matchedRowsResult.getDescription());
		assertFalse(matchedRowsResult.isSuccessful());
		assertEquals(unmatchedRows, comparisonResult.getUnmatchedRows());
		assertEquals(matchedRows, matchedRowsResult.getMatchedRows());

	}

	@Test
	public void testIsSuccessful() {
		// test for isSuccessful() method with unMatched Rows
		assertFalse(comparisonResult.isSuccessful());
		assertTrue(unmatchedRows.size() > 0);

		// test for isSuccessful() with empty unmatched rows
		unmatchedRows = new ArrayList<Map<String, Value>>();
		comparisonResult = new ComparisonResult(rowsDescription, unmatchedRows);
		assertTrue(comparisonResult.isSuccessful());
	}

	@Test
	public void Test_Get_UnmatchedRows() {
		assertEquals(unmatchedRows, comparisonResult.getUnmatchedRows());
		assertFalse(comparisonResult.getUnmatchedRows().isEmpty());
		assertEquals(unmatchedRows.size(), comparisonResult.getUnmatchedRows().size());
	}

	@Test
	public void Test_Get_MatchedRows() {
		assertEquals(matchedRows, matchedRowsResult.getMatchedRows());
		assertFalse(matchedRowsResult.getUnmatchedRows().isEmpty());
		assertEquals(matchedRows.size(), matchedRowsResult.getMatchedRows().size());
	}

	@Test
	public void Test_GetDescription() {
		// passing here actual Description and expected Description
		assertEquals(rowsDescription, comparisonResult.getDescription());
		assertNotNull(comparisonResult.getDescription() != null);
		assertTrue(rowsDescription.equals(comparisonResult.getDescription()));
	}

	private void getDummyMatchedAndUnMatchedRows() {
		// dummy Data for xlsx
		// adding each map entry to list.
		Map<String, Value> exlsxValue = new HashMap<String, Value>();
		exlsxValue.put("UserName", new Value("superuser"));
		unmatchedRows.add(exlsxValue);

		Map<String, Value> exlsxValuep = new HashMap<String, Value>();
		exlsxValuep.put("PassWord", new Value("muppet"));
		unmatchedRows.add(exlsxValuep);

		// dummy Data for cvs
		// adding each map entry to list.
		Map<String, Value> cvsValue = new HashMap<String, Value>();
		cvsValue.put("UserName", new Value("superuser1"));
		matchedRows.add(cvsValue);

		Map<String, Value> cvsValuep = new HashMap<String, Value>();
		cvsValuep.put("UserName", new Value("muppet1"));
		matchedRows.add(cvsValuep);

		Map<String, Value> cvsValueb = new HashMap<String, Value>();
		cvsValueb.put("Click Here", new Value("Login"));
	}
}
