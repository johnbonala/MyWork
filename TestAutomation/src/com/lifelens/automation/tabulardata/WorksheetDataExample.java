package com.lifelens.automation.tabulardata;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class WorksheetDataExample {

	public static void main(String[] args) throws Exception {

		TabularData wd1 = new WorksheetData("c:\\temp\\automation_workbook.xlsx", "MySheet");
		outputWorksheetData(wd1);

		TabularData wd2 = new CSVData("c:\\temp\\automation_workbook_headers.csv", true);
		outputWorksheetData(wd2);

		ComparisonResult result = Comparison.compare(wd1, wd2);

		TabularData wd3 = new WorksheetData("c:\\temp\\automation_workbook.xlsx", "MySheet2");
		outputWorksheetData(wd3);

		TabularData wd4 = new CSVData("c:\\temp\\automation_workbook_numbered.csv", false);
		outputWorksheetData(wd4);

		result = Comparison.compare(wd3, wd4);
	}

	public static StringBuilder outputWorksheetData(TabularData data) {

		if (data.hasNamedHeaders()) {
			System.out.println("Data has named Headers");
		} else {
			System.out.println("Data has numbered columns");
		}

		List<String> headerNames = data.headerNames();
		List<Integer> cellSizes = new ArrayList<Integer>();
		for (String header : headerNames) {
			cellSizes.add(header.length());
		}

		Iterator<Map<String, Value>> rowIterator = data.rowData();
		while (rowIterator.hasNext()) {
			Map<String, Value> row = rowIterator.next();
			for (int i = 0; i < headerNames.size(); i++) {
				String header = headerNames.get(i);
				Value value = row.get(header);
				int size = 3;
				if (value != null) {
					size = value.toString().length();
				}
				if (size > cellSizes.get(i)) {
					cellSizes.set(i, size);
				}
			}
		}

		StringBuilder headerString = new StringBuilder();
		for (int i = 0; i < headerNames.size(); i++) {
			int width = cellSizes.get(i);
			String header = headerNames.get(i);
			headerString.append(header);
			for (int j = 0; j < (width - header.length() + 2); j++) {
				headerString.append(' ');
			}
		}

		rowIterator = data.rowData();
		StringBuilder builder = new StringBuilder();
		while (rowIterator.hasNext()) {
			Map<String, Value> row = rowIterator.next();
			for (int i = 0; i < headerNames.size(); i++) {
				String header = headerNames.get(i);
				String str = " . ";
				Value value = row.get(header);
				if (value != null) {
					if (!value.toString().isEmpty()) {
						str = value.toString();
					}
				}
				int width = cellSizes.get(i);
				builder.append(str);
				for (int j = 0; j < (width - str.length() + 2); j++) {
					builder.append(' ');
				}
			}
			builder.append("\n");
		}

		return builder;
	}
}
