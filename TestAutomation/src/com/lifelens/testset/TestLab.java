package com.lifelens.testset;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.POIXMLException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.lifelens.exceptions.ExceptionErrorCodes;
import com.lifelens.exceptions.TestAutomationException;

/**
 * 
 * Represents TestLab folder. Each TestLab will contain 1 or more TestSets. Each
 * TestSet is contained in an excel workbook file.
 * 
 * Valid TestSets are...
 * 
 * 1. excel files - only xlsx files
 * 
 * 2. not temp files - files starting with '~$'
 * 
 * 3. only excel files with a worksheet called 'index'
 * 
 * Each TestSet contains a set of TestCases specific to a module/functionality
 * 
 * @author Srinivas Pasupuulati (CO48633)
 * @version 1.0
 * @Since 02.05.2014
 * 
 */
public class TestLab {

	/** some strings to help load the test sets */
	private static final String EXCEL_FILE_EXT = ".xlsx";
	private static final String INDEX_SHEET_NAME = "index";
	private static final String TEMP_FILE_PREFIX = "~$";

	/** the folder where the test lab lives */
	private File testLab;

	/**
	 * Create a new test lab.
	 * 
	 * @param testLabAbsolutePath
	 *            path to the test lab
	 * @throws TestAutomationException
	 *             if specified folder does not exist or is not a folder
	 */
	public TestLab(String testLabAbsolutePath) throws TestAutomationException {
		this.testLab = new File(testLabAbsolutePath);

		if (!testLab.exists()) {
			throw new TestAutomationException(ExceptionErrorCodes.TA122_TESTLAB_DIRECTORY_DOES_NOT_EXIST,
					"TestLab directory does not exist: " + testLabAbsolutePath);
		}

		if (!testLab.isDirectory()) {
			throw new TestAutomationException(ExceptionErrorCodes.TA123_TESTLAB_IS_NOT_A_DIRECTORY,
					"Specified TestLab is not a directory: " + testLab.getAbsolutePath());
		}
	}

	/**
	 * Get an alphabetical sorted list of TestSets in the TestLab.
	 * 
	 * @return list of test sets It validates all the files inside the TestLab
	 *         and returns the list of TestSets
	 * @throws TestAutomationException
	 *             if cannot read file
	 */
	public List<String> extractTestSets() throws TestAutomationException {

		// get the files in the test lab folder and alphabetical sort
		File[] filesArray = testLab.listFiles();
		Arrays.sort(filesArray);

		List<String> testFiles = new ArrayList<String>();

		for (final File fileEntry : filesArray) {
			// check file is an excel 2007+ file, does not have a temp file
			// prefix and contains an index sheet
			if (fileEntry.getName().contains(EXCEL_FILE_EXT) && !fileEntry.getName().contains(TEMP_FILE_PREFIX)
					&& fileHasIndexSheet(fileEntry)) {

				testFiles.add(fileEntry.getName());
			}
		}

		return testFiles;
	}

	/**
	 * 
	 * @param inputfile
	 *            This is the file to be validate whether it is a testset or not
	 * 
	 * @return true - if the Excel work book has index sheet
	 * @throws TestAutomationException
	 * 
	 */
	private boolean fileHasIndexSheet(File inputfile) throws TestAutomationException {
		try {
			FileInputStream file = new FileInputStream(inputfile);
			XSSFWorkbook workbook = new XSSFWorkbook(file);

			if (workbook.getSheet(INDEX_SHEET_NAME) != null)
				return true;
			else
				return false;
		} catch (FileNotFoundException e) {
			throw new TestAutomationException(ExceptionErrorCodes.TA072_TESTLAB_FILE_NOT_FOUND_ERROR, e.getMessage()
					+ " : Cannot read or find file: " + inputfile.getAbsolutePath());
		} catch (IOException e) {
			throw new TestAutomationException(ExceptionErrorCodes.TA073_TESTLAB_FILE_LOAD_ERROR, e.getMessage()
					+ ": Unable to read file: " + inputfile.getAbsolutePath());
		} catch (POIXMLException e) {
			throw new TestAutomationException(ExceptionErrorCodes.TA124_TESTLAB_FILE_NOT_IN_EXCEL_FORMAT,
					e.getMessage() + ": POI library unable to read file: " + inputfile.getAbsolutePath());
		}

	}

}