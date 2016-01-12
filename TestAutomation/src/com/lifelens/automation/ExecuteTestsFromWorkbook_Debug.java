package com.lifelens.automation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.lifelens.browsers.EBrowser;
import com.lifelens.exceptions.TestAutomationException;
import com.lifelens.globals.Global;
import com.lifelens.interpreter.ExtractInstructions;
import com.lifelens.testdata.Testdata;
import com.lifelens.testset.TestSet;

public class ExecuteTestsFromWorkbook_Debug {

	private static Logger logger = Logger.getLogger(ExecuteTestsFromWorkbook_Debug.class.getName());
	TestSet testset;
	String testSuiteXLname;
	FileInputStream testSuiteXL;
	FileOutputStream outFile;
	WebDriver browser;
	static String testsuite = "SampleScripts.xlsx";
	static String testPriority = "ALL";

	@BeforeSuite
	public void LoadGlobals() throws Exception {
		ProperitesFile configProperties = new ProperitesFile(Global.propertiesFileName);
		configProperties.setSettings();
	}

	@BeforeClass
	private void OpenTestSet() {
		try {
			Global.setTestLabPath(Global.getCurrentDirectory() + Global.pathSeperator + "TestLab"
					+ Global.pathSeperator + Global.getTestlab());
			Testdata testdata = new Testdata(Global.getTestdatFileAbsolutepath(), Global.getTestdataSheetName());
			testdata.readTestdata();

			browser = EBrowser.valueOf(Global.getBrowser()).newInstance();
			browser.manage().timeouts().implicitlyWait(Global.timeout, TimeUnit.SECONDS);
			browser.manage().window().maximize();

			testSuiteXLname = Global.getTestLabPath() + Global.pathSeperator + testsuite;
			testset = new TestSet(new File(testSuiteXLname), testPriority, testdata);
		} catch (Exception e) {
			logger.error(e);
		}
	}

	@Test(dataProvider = "TestData")
	public void ExecuteTest(String sheetName, String testcaseName, String testPriority) {
		boolean result = false;
		try {
			ExtractInstructions instructions = new ExtractInstructions(browser, testset, sheetName, testcaseName,
					testPriority);
			result = instructions.execute();

			if (!result)
				Assert.fail(instructions.getTestStatusMessage());

		} catch (Exception e) {

			logger.error(e);
		} finally {
			logger.debug("ExecuteTestsFromWorkbook: ExecuteTest finally block");
			testset.updateTestStatusInIndex(result);
		}
		logger.debug("End of ExecuteTestsFromWorkbook: ExecuteTest");
	}

	@DataProvider
	public Object[][] TestData() throws TestAutomationException {
		testset.loadSheetNames();
		if (testset.indexSheetFound()) {
			testset.getIndexTestCaseDetails();
			testset.validateIndexSheet();
			return testset.getDataProviderObject();
		} else {
			logger.warn(" Index not found in the TestSet : " + testSuiteXLname);
			return null;
		}

	}

	@AfterClass
	public void CloseTestSet() {

		try {
			outFile = new FileOutputStream(new File(testSuiteXLname));
			testset.formatIndexSheet();
			testset.getWorkbook().write(outFile);
		} catch (Exception e) {
			logger.error(e);
		} finally {
			try {
				outFile.close();
				browser.quit();
			} catch (IOException e) {
				logger.error(e);
			}

		}

	}

}
