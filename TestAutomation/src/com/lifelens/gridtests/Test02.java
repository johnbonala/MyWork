package com.lifelens.gridtests;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.lifelens.automation.ExecuteTestsFromWorkbook_Debug;
import com.lifelens.automation.ProperitesFile;
import com.lifelens.browsers.EBrowser;
import com.lifelens.globals.Global;
import com.lifelens.interpreter.ExtractInstructions;
import com.lifelens.testdata.Testdata;
import com.lifelens.testset.TestLab;
import com.lifelens.testset.TestSet;

public class Test02 extends TestBase {

	private static Logger logger = Logger.getLogger(ExecuteTestsFromWorkbook_Debug.class.getName());
	TestSet testset;
	String testSuiteXLname, testSuiteXLname1;
	String testLabPath;
	FileInputStream testSuiteXL;
	FileOutputStream outFile;
	WebDriver browser;
	Testdata testdata;
	static String testPriority = "ALL";

	@BeforeSuite
	public void LoadGlobals() throws Exception {
		ProperitesFile configProperties = new ProperitesFile(Global.propertiesFileName);
		configProperties.setSettings();
	}

	@BeforeClass
	private void OpenTestSet() {
		try {
			testLabPath = getTestlabPath();
			Global.setTestLabPath(testLabPath);
			testdata = new Testdata(Global.getTestdatFileAbsolutepath(), Global.getTestdataSheetName());
			testdata.readTestdata();

			browser = EBrowser.valueOf(Global.getBrowser()).newInstance();
			browser.manage().timeouts().implicitlyWait(Global.timeout, TimeUnit.SECONDS);
			browser.manage().window().maximize();
		} catch (Exception e) {
			logger.error(e);
		}
	}

	private String getTestlabPath() {
		String testLabPath = Global.getCurrentDirectory() + Global.pathSeperator + "TestLab" + Global.pathSeperator
				+ Global.node2;

		return testLabPath;
	}

	@Test
	public void ExecuteTest() {
		boolean result = false;
		try {
			TestLab testlab = new TestLab(testLabPath);
			List<String> testsets = testlab.extractTestSets();

			for (int i = 0; i < testsets.size(); i++) {
				testSuiteXLname = Global.getTestLabPath() + Global.pathSeperator + testsets.get(i);
				testset = new TestSet(new File(testSuiteXLname), testPriority, testdata);
				testset.getIndexTestCaseDetails();
				testset.loadSheetNames();

				if (testset.indexSheetFound()) {
					testset.getIndexTestCaseDetails();
					testset.validateIndexSheet();

					int cols = 3;
					int counter = 0;
					String[][] testObject = new String[testset.indextestCaseDetails.size()][cols];

					for (String sheetName : testset.indextestCaseDetails.keySet()) {
						String[] IndexCols = testset.indextestCaseDetails.get(sheetName);
						testObject[counter][0] = sheetName; // test sheet name
						testObject[counter][1] = IndexCols[0]; // testCase name
						testObject[counter][2] = IndexCols[1]; // test priority

						ExtractInstructions instructions = new ExtractInstructions(browser, testset,
								testObject[counter][0], testObject[counter][1], testObject[counter][2]);
						result = instructions.execute();
						testset.updateTestStatusInIndex(result);

						if (!result) {
							logger.info(instructions.getTestStatusMessage());
						}

						counter++;
					}

					try {
						// testset.updateTestStatusInIndex(result);
						outFile = new FileOutputStream(new File(testSuiteXLname));
						testset.formatIndexSheet();
						testset.getWorkbook().write(outFile);
					} catch (Exception e) {
						logger.error(e);
					} finally {
						try {
							outFile.close();
							// browser.quit();
						} catch (IOException e) {
							logger.error(e);
						}
					}

				}

			}
		} catch (Exception e) {

			logger.error(e);
		} finally {
			logger.debug("ExecuteTestsFromWorkbook: ExecuteTest finally block");
			testset.updateTestStatusInIndex(result);
		}
		logger.debug("End of ExecuteTestsFromWorkbook: ExecuteTest");
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
