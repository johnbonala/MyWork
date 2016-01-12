package com.lifelens.automation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import com.lifelens.browsers.EBrowser;
import com.lifelens.exceptions.TestAutomationException;
import com.lifelens.globals.Global;
import com.lifelens.interpreter.ExtractInstructions;
import com.lifelens.testdata.Testdata;
import com.lifelens.testset.TestSet;

public class TestAutomationMain {

	private static Logger logger = Logger.getLogger(TestAutomationMain.class);

	public static void main(String[] args) throws Exception {

		File testSetFile = new File(args[0]);

		// TODO file names mustn't have any spaces otherwise exception thrown
		// here
		if (!testSetFile.isFile()) {
			throw new IllegalArgumentException();
		}

		new TestAutomationMain(testSetFile).run();
	}

	private File testSetFile;

	public TestAutomationMain(File testSetFile) {
		this.testSetFile = testSetFile;
	}

	public void run() throws Exception {

		logger.debug("starting...");

		WebDriver webDriver = null;

		try {
			loadGlobals();

			String requiredTestPriority = "All";

			logger.debug("testSetFile: " + testSetFile.getAbsolutePath());

			webDriver = EBrowser.valueOf(Global.getBrowser()).newInstance();
			webDriver.manage().timeouts().implicitlyWait(Global.timeout, TimeUnit.SECONDS);
			webDriver.manage().window().maximize();

			Testdata testData = loadTestData();

			TestSet testset = loadTestset(requiredTestPriority, testSetFile, testData);

			String[][] indexDetails = testset.getDataProviderObject();
			for (int row = 0; row < indexDetails.length; row++) {
				String sheetName = indexDetails[row][0];
				String testcaseName = indexDetails[row][1];
				String testPriority = indexDetails[row][2];

				runTests(webDriver, sheetName, testcaseName, testset, testPriority);
			}

			writeWorkbook(testSetFile, testset);

		} finally {
			if (webDriver != null) {
				webDriver.quit();
			}
		}

		logger.debug("done.");
	}

	private void runTests(WebDriver webDriver, String sheetName, String testcaseName, TestSet testset,
			String testPriority) throws TestAutomationException {
		boolean result = false;
		try {
			ExtractInstructions instructions = new ExtractInstructions(webDriver, testset, sheetName, testcaseName,
					testPriority);
			result = instructions.execute();
			logger.debug("result: " + result);
		} catch (Exception e) {
			logger.error("Error thrown when executing the test sheet : " + sheetName + " " + e.getMessage());
			logger.error(e);
		} finally {
			if (testset != null) {
				testset.updateTestStatusInIndex(result);
			}
		}
	}

	private TestSet loadTestset(String testPriority, File file, Testdata testData) throws TestAutomationException {
		TestSet testset;
		testset = new TestSet(file, testPriority, testData);
		testset.loadSheetNames();
		if (testset.indexSheetFound()) {
			testset.getIndexTestCaseDetails();
			testset.validateIndexSheet();

		} else {
			logger.warn(" Index sheet not found in workbook");
		}
		return testset;
	}

	private Testdata loadTestData() throws TestAutomationException {
		String testdataFilepathAndFilename = testSetFile.getAbsolutePath();
		String testdataFilepath = testdataFilepathAndFilename.substring(0,
				testdataFilepathAndFilename.lastIndexOf('\\'));
		Global.setTestLabPath(testdataFilepath);
		Testdata testData = new Testdata(Global.getTestdatFileAbsolutepath(), Global.getTestdataSheetName());
		testData.readTestdata();
		return testData;
	}

	private void writeWorkbook(File file, TestSet testset) throws FileNotFoundException, IOException {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
			testset.getWorkbook().write(out);
		} finally {
			IOUtils.closeQuietly(out);
		}
	}

	public void loadGlobals() {
		logger.debug("Start of TestAutomationMain: loadGlobals");
		try {
			ProperitesFile configProperties = new ProperitesFile(Global.propertiesFileName);
			configProperties.setSettings();
		} catch (TestAutomationException ex) {
			logger.error("Exception caught while loading the poperties or test data with the error code: "
					+ ex.getCode() + " and message: " + ex.getMessage());
		} catch (Exception ex) {
			logger.error("Exception caught while loading the poperties or test data with the message: "
					+ ex.getMessage());
			logger.error(ex);
		}
		logger.debug("End of TestAutomationMain: loadGlobals");
	}
}
