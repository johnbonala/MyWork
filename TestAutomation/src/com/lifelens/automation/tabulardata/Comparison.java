package com.lifelens.automation.tabulardata;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

public class Comparison {
	private static Logger logger = Logger.getLogger(Comparison.class.getName());

	public static ComparisonResult compare(TabularData expected, TabularData actual) {
		return compare(expected, actual, false);
	}

	/**
	 * Match expected against actual. 1. TabularData may be numbered or have
	 * named headers. Expected and actual must either both be numbered or both
	 * have named headers otherwise they do not match 2.a In the case of named
	 * headers, actual must have at least all the same headed columns as
	 * expected (it may also have additional columns) 2.b In the case of
	 * numbered columns, actual must have at least as many columns as the
	 * highest column number in expected 3. Each row in Expected must match
	 * against one unique row in actual i.e. once an expected row has been
	 * matched against an actual row, neither of those rows should be involved
	 * in any further matching operations 4. Ordering of rows is treated as
	 * irrelevant for matching purposes
	 * 
	 * @param expected
	 * @param actual
	 * @param requireOrdered
	 * @return
	 */
	public static ComparisonResult compare(TabularData expected, TabularData actual, boolean requireOrdered) {
		logger.debug("Start of Comparison: compare");

		ComparisonResult result = null;
		result = ensureSameHeaderType(expected, actual);
		if (result == null) {
			if (expected.hasNamedHeaders()) {
				result = ensureNamedHeadersMatch(expected, actual);
			} else {
				result = ensureNumberedColumnsMatch(expected, actual);
			}
		}
		if (result != null) {
			return result;
		}

		Set<Integer> matchedRowsInExpected = new HashSet<Integer>();
		Set<Integer> matchedRowsInActual = new HashSet<Integer>();
		int numActualRows = actual.numRows();
		int numExpectedRows = expected.numRows();

		for (int e = 0; e < numExpectedRows; e++) {
			if (matchedRowsInExpected.contains(e)) {
				continue;
			}
			Map<String, Value> expectedRow = expected.getRow(e);
			int startPos = 0;
			for (int a = startPos; a < numActualRows; a++) {
				if (matchedRowsInActual.contains(a)) {
					continue;
				}
				Map<String, Value> actualRow = actual.getRow(a);
				if (doRowsMatch(expectedRow, actualRow)) {
					matchedRowsInExpected.add(e);
					matchedRowsInActual.add(a);
					if (requireOrdered) {
						startPos = a + 1;
					}
					break;
				}
			}

		}
		List<Map<String, Value>> unmatchedRows = new ArrayList<Map<String, Value>>();
		List<Map<String, Value>> matchedRows = new ArrayList<Map<String, Value>>();
		String comparisionMessage;
		for (int i = 0; i < numExpectedRows; i++) {
			if (matchedRowsInExpected.contains(i)) {
				matchedRows.add(expected.getRow(i));
				continue;
			} else {
				unmatchedRows.add(expected.getRow(i));
			}
		}

		if (matchedRowsInExpected.size() == expected.numRows()) {
			comparisionMessage = "All rows successfully matched";
		} else {
			comparisionMessage = unmatchedRows.size() + " rows did not match";
		}
		logger.debug("End of Comparison: compare");
		return new ComparisonResult(comparisionMessage, unmatchedRows, matchedRows);
	}

	private static ComparisonResult ensureSameHeaderType(TabularData expected, TabularData actual) {
		logger.debug("Start of Comparison: ensureSameHeaderType");
		String description = null;
		ComparisonResult result = null;
		if ((expected.hasNamedHeaders() && !actual.hasNamedHeaders())) {
			description = "[Expected] has named column  headers but [Actual] data does not";
		} else if (!expected.hasNamedHeaders() && actual.hasNamedHeaders()) {
			description = "[Expected] has numbered columns but [Actual] data does not";
		}
		if (description != null) {
			result = new ComparisonResult(description, allRows(expected));
		}
		logger.debug("End of Comparison: ensureSameHeaderType");
		return result;
	}

	private static ComparisonResult ensureNamedHeadersMatch(TabularData expected, TabularData actual) {
		logger.debug("Start of Comparison: ensureNamedHeadersMatch");
		List<String> actualHeaders = actual.headerNames();
		List<String> missingHeaders = new ArrayList<String>();
		for (String expectedHeader : expected.headerNames()) {
			if (!actualHeaders.contains(expectedHeader)) {
				missingHeaders.add(expectedHeader);
			}
		}
		ComparisonResult result = null;
		if (!missingHeaders.isEmpty()) {
			StringBuilder builder = new StringBuilder();
			builder.append("[Actual] is missing named columns specified in [Expected]. Namely: ");
			boolean comma = false;
			for (String missing : missingHeaders) {
				if (comma) {
					builder.append(", ");
				}
				builder.append(missing);
			}
			result = new ComparisonResult(builder.toString(), allRows(expected));
		}
		logger.debug("End of Comparison: ensureNamedHeadersMatch");
		return result;
	}

	private static ComparisonResult ensureNumberedColumnsMatch(TabularData expected, TabularData actual) {
		logger.debug("Start of Comparison: ensureNumberedColumnsMatch");
		int maxCol = -1;
		for (String header : expected.headerNames()) {
			int col = Integer.valueOf(header);
			if (col > maxCol) {
				maxCol = col;
			}
		}
		ComparisonResult result = null;
		int size = actual.headerNames().size();
		if (size < maxCol) {
			StringBuilder builder = new StringBuilder();
			builder.append("[Expected] has a column numbered ");
			builder.append(maxCol);
			builder.append(" but [Actual] only has ");
			builder.append(size);
			builder.append(" columns");
			result = new ComparisonResult(builder.toString(), allRows(expected));
		}
		logger.debug("End of Comparison: ensureNumberedColumnsMatch");
		return result;
	}

	private static List<Map<String, Value>> allRows(TabularData data) {
		logger.debug("Start of Comparison: allRows");
		List<Map<String, Value>> rows = new ArrayList<Map<String, Value>>();
		Iterator<Map<String, Value>> it = data.rowData();
		while (it.hasNext()) {
			rows.add(it.next());
		}
		logger.debug("Start of Comparison: allRows");
		return rows;
	}

	private static boolean doRowsMatch(Map<String, Value> expected, Map<String, Value> actual) {
		logger.debug("Start of Comparison: doRowsMatch");
		boolean rowsMatched = true;
		for (String key : expected.keySet()) {
			if (key.equals("__sourceRow")) {
				continue;
			}
			Value expectedValue = expected.get(key);
			Value actualValue = actual.get(key);
			if (!expectedValue.equals(actualValue)) {
				rowsMatched = false;
				break;
			}
		}
		logger.debug("End of Comparison: doRowsMatch");
		return rowsMatched;
	}

}
