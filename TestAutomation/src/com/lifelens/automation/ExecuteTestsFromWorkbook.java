package com.lifelens.automation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.lifelens.browsers.EBrowser;
import com.lifelens.exceptions.TestAutomationException;
import com.lifelens.globals.Global;
import com.lifelens.interpreter.ExtractInstructions;
import com.lifelens.testdata.Testdata;
import com.lifelens.testset.TestSet;

public class ExecuteTestsFromWorkbook {

	private static Logger logger = Logger.getLogger(ExecuteTestsFromWorkbook.class.getName());
	private TestSet testset;
	private String testSuiteXLname;
	private FileOutputStream outFile;
	private WebDriver browser;

	@BeforeSuite
	public void loadGlobals() {
		logger.debug("Start of ExecuteTestsFromWorkbook: loadGlobals");
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
		logger.debug("End of ExecuteTestsFromWorkbook: loadGlobals");
	}

	@BeforeClass
	@Parameters(value = { "testsuite", "testPriority", "testLabPath" })
	private void openTestSet(String testsuite, String testPriority, String testLabPath) {
		logger.debug("Start of ExecuteTestsFromWorkbook: openTestSet");

		try {
			Global.setTestLabPath(testLabPath);
			Testdata testData = new Testdata(Global.getTestdatFileAbsolutepath(), Global.getTestdataSheetName());
			testData.readTestdata();

			browser = EBrowser.valueOf(Global.getBrowser()).newInstance();
			browser.manage().timeouts().implicitlyWait(Global.timeout, TimeUnit.SECONDS);
			browser.manage().window().maximize();

			testSuiteXLname = testLabPath + Global.pathSeperator + testsuite;
			testset = new TestSet(new File(testSuiteXLname), testPriority, testData);

		} catch (Exception ex) {
			logger.error("Exception caught while processing the test sets with the message: " + ex.getMessage());
			logger.error(ex);
		}
		logger.debug("End of ExecuteTestsFromWorkbook: openTestSet");
	}

	@Test(dataProvider = "testData")
	public void executeTest(String sheetName, String testcaseName, String testPriority) {
		logger.debug("Start of ExecuteTestsFromWorkbook: ExecuteTest");
		boolean result = false;
		try {
			ExtractInstructions instructions = new ExtractInstructions(browser, testset, sheetName, testcaseName,
					testPriority);
			result = instructions.execute();
			if (!result)
				Assert.fail(instructions.getTestStatusMessage());
		} catch (Exception e) {
			logger.error("Error thrown when executing the test sheet : " + sheetName + " " + e.getMessage());
			logger.error(e);
		} finally {
			logger.debug("ExecuteTestsFromWorkbook: ExecuteTest finally block");
			testset.updateTestStatusInIndex(result);
		}
		logger.debug("End of ExecuteTestsFromWorkbook: ExecuteTest");
	}

	@DataProvider
	public Object[][] testData() throws TestAutomationException {
		logger.debug("Start of ExecuteTestsFromWorkbook: testData");
		Object[][] testData = null;
		testset.loadSheetNames();
		if (testset.indexSheetFound()) {
			testset.getIndexTestCaseDetails();
			testset.validateIndexSheet();
			testData = testset.getDataProviderObject();
		} else {
			logger.warn(" Index not found in the TestSet : " + testSuiteXLname);
		}
		logger.debug("End of ExecuteTestsFromWorkbook: testData");
		return testData;
	}

	@AfterClass
	public void closeTestSet() {
		logger.debug("Start of ExecuteTestsFromWorkbook: closeTestSet");
		try {
			outFile = new FileOutputStream(new File(testSuiteXLname));
			testset.formatIndexSheet();
			testset.getWorkbook().write(outFile);
		} catch (Exception e) {
			logger.error("Error thrown when updating the test suit: " + testSuiteXLname + " " + e.getMessage());
			logger.error(e);
		} finally {
			try {
				if (outFile != null)
					outFile.close();
				if (browser != null)
					browser.quit();
			} catch (IOException e) {
				logger.error("Error thrown when closing the test suit: " + testSuiteXLname + " " + e.getMessage());
				logger.error(e);
			}

		}
		logger.debug("Start of ExecuteTestsFromWorkbook: closeTestSet");
	}

}
