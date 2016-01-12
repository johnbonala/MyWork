package com.lifelens.automation.tabulardata;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.googlecode.jcsv.CSVStrategy;
import com.googlecode.jcsv.reader.CSVReader;
import com.googlecode.jcsv.reader.internal.CSVReaderBuilder;
import com.googlecode.jcsv.reader.internal.CSVTokenizerImpl;
import com.googlecode.jcsv.reader.internal.DefaultCSVEntryParser;
import com.lifelens.exceptions.ExceptionErrorCodes;
import com.lifelens.exceptions.TestAutomationException;

public class CSVData implements TabularData {

	private static Logger logger = Logger.getLogger(CSVData.class.getName());
	private List<Map<String, Value>> rowData = new ArrayList<Map<String, Value>>();
	private boolean hasNamedHeaders = false;
	private List<Header> headers = new ArrayList<Header>();
	private boolean isHeaderRow;
	private String fileName;

	public CSVData(String fileName, boolean headerRow) throws TestAutomationException {
		this.fileName = fileName;
		this.isHeaderRow = headerRow;
		init();
	}

	public void init() throws TestAutomationException {
		logger.debug("Start of CSVData: init");
		FileReader fileReader = null;
		CSVReader<String[]> csvReader = null;
		try {
			fileReader = new FileReader(fileName);
			csvReader = new CSVReaderBuilder<String[]>(fileReader).entryParser(new DefaultCSVEntryParser())
					.strategy(CSVStrategy.UK_DEFAULT).tokenizer(new CSVTokenizerImpl()).build();

			if (isHeaderRow) {
				List<String> data = csvReader.readHeader();
				for (int i = 0; i < data.size(); i++) {
					String str = data.get(i);
					if (str != null && !str.isEmpty()) {
						// TODO temporary workaround! Figure out why corrupt
						// value coming
						if (str.equals("﻿Status")) {
							str = "Status";
						}
						Header header = new Header(str, i);
						headers.add(header);
					}
				}
				hasNamedHeaders = true;
			} else {
				int maxCols = 0;
				String[] data = null;
				do {
					data = csvReader.readNext();
					if (data != null && data.length > maxCols) {
						maxCols = data.length;
					}
				} while (data != null);

				for (int i = 0; i < maxCols; i++) {
					headers.add(new Header("" + (i + 1), i));
				}

				csvReader.close();
				fileReader.close();
				fileReader = new FileReader(fileName);
				csvReader = new CSVReaderBuilder<String[]>(fileReader).entryParser(new DefaultCSVEntryParser())
						.strategy(CSVStrategy.UK_DEFAULT).tokenizer(new CSVTokenizerImpl()).build();
			}

			String[] data = null;
			do {
				data = csvReader.readNext();
				if (data != null) {
					Map<String, Value> processed = processRow(data);
					if (processed != null) {
						rowData.add(processed);
					}
				}
			} while (data != null);
		} catch (FileNotFoundException ex) {
			throw new TestAutomationException(ExceptionErrorCodes.TA035_CVS_DATA_UNABLE_TO_FIND_WORKBOOK_ERROR,
					ex.getMessage() + ": Unable to load the cvs data file in the path " + fileName);
		} catch (IOException ex) {
			throw new TestAutomationException(ExceptionErrorCodes.TA036_CVS_DATA_UNABLE_TO_READ_WORKBOOK_ERROR,
					ex.getMessage() + ": Unable to read cvs data file in the path " + fileName);
		} finally {
			try {
				if (csvReader != null) {
					csvReader.close();
				}
				if (fileReader != null) {
					fileReader.close();
				}
			} catch (IOException ex) {
				throw new TestAutomationException(ExceptionErrorCodes.TA037_CVS_DATA_UNABLE_TO_CLOSE_WORKBOOK_ERROR,
						ex.getMessage() + ": Unable to close cvs data file in the path " + fileName);
			}
		}
		logger.debug("End of CSVData: init");
	}

	@Override
	public List<String> headerNames() {
		logger.debug("Start of CSVData: headerNames");
		List<String> headerNames = new ArrayList<String>();
		for (Header header : headers) {
			headerNames.add(header.headerName);
		}
		logger.debug("End of CSVData: headerNames");
		return headerNames;
	}

	@Override
	public Iterator<Map<String, Value>> rowData() {
		return rowData.iterator();
	}

	@Override
	public Map<String, Value> getRow(int rowNum) {
		return rowData.get(rowNum);
	}

	private Map<String, Value> processRow(String[] data) {
		logger.debug("Start of CSVData: processRow");
		Map<String, Value> map = new HashMap<String, Value>();
		boolean empty = true;
		int increaseBy = headers.size() - data.length;

		// If there are less values than the expected headers then increasing
		// the data array size to the size of headers and
		// and keeping the null data values as empty
		if (data.length < headers.size()) {
			data = increaseArray(data, increaseBy);
		}

		for (Header header : headers) {
			Value value = ValueFactory.valueFor(data[header.xssColumnNo]);
			if (!value.toString().isEmpty()) {
				empty = false;
			}
			map.put(header.headerName, value);
		}
		if (empty) {
			map = null;
		}
		logger.debug("End of CSVData: processRow");
		return map;
	}

	/**
	 * To increase the size of data when there is less data values then the
	 * header expected
	 * 
	 * @param data
	 * @param increaseBy
	 * @return
	 */
	public String[] increaseArray(String[] data, int increaseBy) {
		logger.debug("Start of CSVData: increaseArray");
		String[] newData = new String[data.length + increaseBy];
		for (int cnt = 0; cnt < data.length; cnt++) {
			newData[cnt] = data[cnt];
		}
		for (int cnt = 0; cnt < newData.length; cnt++) {
			if (newData[cnt] == null) {
				newData[cnt] = "";
			}
		}
		logger.debug("End of CSVData: increaseArray");
		return newData;
	}

	@Override
	public boolean hasNamedHeaders() {
		return hasNamedHeaders;
	}

	public int numRows() {
		return rowData.size();
	}
}
