package com.lifelens.automation.tabulardata;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import au.com.bytecode.opencsv.CSV;
import au.com.bytecode.opencsv.CSVReadProc;

import com.lifelens.exceptions.ExceptionErrorCodes;
import com.lifelens.exceptions.TestAutomationException;

public class FileBuilder {

	private static Logger logger = Logger.getLogger(FileBuilder.class.getName());
	public static String WORKBOOKNAME = "CSVToExcel.xlsx";
	public static String CONVERTEDCSVFILENAME = "Conveted.csv";
	public static String WORKSHEETNAME = "csv data";

	/**
	 * Method to convert csv file to excel file
	 * 
	 * @param actualCsvFilename
	 * @return workbook
	 */
	public static XSSFWorkbook csvToExcelConversion(String actualCsvFilename) throws TestAutomationException {
		logger.debug("Start of FileBuilder: csvToExcelConversion");

		final XSSFWorkbook wb = new XSSFWorkbook();
		final XSSFSheet sheet = wb.createSheet(WORKSHEETNAME);

		// check the file exists
		if (!new File(actualCsvFilename).isFile()) {
			throw new TestAutomationException(ExceptionErrorCodes.TA028_FILE_PROCESSOR_UNABLE_TO_READ_FILE_ERROR,
					"Unable to read file from: " + actualCsvFilename);
		}
		// define format of CSV file one time and use everywhere
		// human readable configuration
		CSV csv = CSV.separator(',').create();

		// do not throw checked exceptions
		if (actualCsvFilename.contains(".csv")) {
			csv.read(actualCsvFilename, new CSVReadProc() {
				public void procRow(int rowIndex, String... values) {
					List<String> csvData = Arrays.asList(values);
					Row row = sheet.createRow(rowIndex);

					// Iterating rows and copying csv values to excel cells
					for (int i = 0; i < csvData.size(); i++) {
						row.createCell(i).setCellValue(csvData.get(i));
					}
				}
			});
		}
		logger.debug("End of FileProcessor: csvToExcelConversion");
		return wb;
	}

	/**
	 * Method to convert text file to csv file
	 * 
	 * @param actualTxtFileName
	 * @return csv file name
	 */
	public static String txtToCSVConversion(String actualTxtFileName) throws TestAutomationException {
		// read from text file and write to csv file then write to excel
		// for cell comparison
		List<String> linesList;
		File delemittedCSVFileName = null;
		try {
			File input = new File(actualTxtFileName);
			delemittedCSVFileName = new File(CONVERTEDCSVFILENAME);
			Scanner sc = new Scanner(input);

			linesList = new ArrayList<String>();

			// convert txt file data into csv line by line
			while (sc.hasNextLine()) {
				String lineText = sc.nextLine();
				String emptyBlock = lineText.substring(lineText.indexOf(" "), lineText.lastIndexOf(" ") + 1);
				String delimittedString = lineText.replace(emptyBlock, ",");
				linesList.add(delimittedString);
			}

			try {
				FileUtils.writeLines(delemittedCSVFileName, linesList);
			} catch (IOException e) {
				throw new TestAutomationException(ExceptionErrorCodes.TA002_PROPERTIES_FILE_LOAD_ERROR, e.getMessage()
						+ ": Unable to load the CSV file in the path " + delemittedCSVFileName);
			}
		} catch (FileNotFoundException e) {
			throw new TestAutomationException(ExceptionErrorCodes.TA004_TEST_DATA_FILE_NOT_FOUND_ERROR, e.getMessage()
					+ " : CSV file not found in the path " + delemittedCSVFileName, e);
		}

		return delemittedCSVFileName.getAbsolutePath();
	}
}
