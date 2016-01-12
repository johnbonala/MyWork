package com.lifelens.automation.tabulardata;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public interface TabularData {

	List<String> headerNames();

	Iterator<Map<String, Value>> rowData();

	Map<String, Value> getRow(int rowNum);

	int numRows();

	boolean hasNamedHeaders();

}