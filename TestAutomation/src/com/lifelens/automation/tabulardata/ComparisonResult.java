package com.lifelens.automation.tabulardata;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ComparisonResult {

	private String description;
	private List<Map<String, Value>> unmatchedRows = new ArrayList<Map<String, Value>>();
	private List<Map<String, Value>> matchedRows = new ArrayList<Map<String, Value>>();

	public ComparisonResult(String description, List<Map<String, Value>> unmatchedRows) {
		this.description = description;
		this.unmatchedRows = unmatchedRows;
	}

	public ComparisonResult(String description, List<Map<String, Value>> unmatchedRows,
			List<Map<String, Value>> matchedRows) {
		this.description = description;
		this.unmatchedRows = unmatchedRows;
		this.matchedRows = matchedRows;
	}

	public boolean isSuccessful() {
		return (unmatchedRows == null || unmatchedRows.isEmpty());
	}

	public List<Map<String, Value>> getUnmatchedRows() {
		return unmatchedRows;
	}

	public List<Map<String, Value>> getMatchedRows() {
		return matchedRows;
	}

	public String getDescription() {
		return description;
	}

}
