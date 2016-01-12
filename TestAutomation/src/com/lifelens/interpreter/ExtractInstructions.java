package com.lifelens.interpreter;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.lifelens.browsers.WebElementProcessor;
import com.lifelens.browsers.WebElementProcessorCommon;
import com.lifelens.exceptions.ExceptionErrorCodes;
import com.lifelens.exceptions.TestAutomationException;
import com.lifelens.globals.Global;
import com.lifelens.testdata.CellReader;
import com.lifelens.testset.TestSet;
import com.lifelens.webelements.WebElementRetriever;

public class ExtractInstructions {

	private static Logger logger = Logger.getLogger(ExtractInstructions.class.getName());
	public static String loadIndicatorXPath = "//div[@id='loadIndicator' and (contains(concat(' ', @style, ' '), ' display: none; ') or contains(concat(' ', @style, ' '), ' display:none '))]";
	// public static String loadIndicatorCSS =
	// "div#loadIndicator[style*='display: none'], div#loadIndicator[style*='display:none']";
	// public static String loadIndicatorCSS = "div#loadIndicator";
	public static String loadIndicatorCSS = Global.loadIndicatorCSS;
	public static String lazyLoadIndicatorClass = "lazyLoadingIndicator";
	public TestSet testset;
	public static WebDriver browser;
	public static boolean controlInIframe = false;
	private String testcaseName, testPrority, sheetName, actionKeyword;
	private static final String END = "END";
	private int rowCounter = 0;
	boolean testStepPassed, testCasePassed = true;
	private Row teststep;
	private String type;
	private FormulaEvaluator evaluator;
	private String testStatusMessage = StringUtils.EMPTY;
	private boolean isTimerColumnExists = false;
	private boolean isContinueExecutionColumnExists = false;
	private long totalStartTime, toalEndTime, toalTime, startTime;

	public ExtractInstructions(WebDriver browser, TestSet testset, String sheetName, String testcaseName,
			String testPrority) {
		logger.debug("Start of ExtractInstructions: constructor ");
		this.testset = testset;
		this.sheetName = sheetName;
		this.testcaseName = testcaseName;
		this.testPrority = testPrority;
		this.browser = browser;
		this.evaluator = testset.getWorkbook().getCreationHelper().createFormulaEvaluator();
		logger.debug("End of ExtractInstructions: constructor ");
	}

	public boolean ajaxReqestPending() {
		try {
			WebElementProcessorCommon.waitForWebProcessing(browser, Global.getTimeout(), loadIndicatorCSS,
					lazyLoadIndicatorClass);
		} catch (TestAutomationException e) {
			logger.error("TIMED OUT with error: " + e.getMessage());
			testStepPassed = false;
			updateTestStepResult(testStepPassed);
			return true;
		}

		return false;
	}

	private boolean endofTestCase() {
		if (actionKeyword.equalsIgnoreCase(END))
			return true;
		else
			return false;
	}

	public void applyFormatting() {
		testset.formatTestCaseSheet();
	}

	private void updateTestStepResult(boolean testStepPassed) {
		if (!endofTestCase())
			testset.getTestCase().updateTestStepResult(testStepPassed, teststep, calculateTime());
	}

	@SuppressWarnings("rawtypes")
	public boolean execute() {
		logger.debug("Start of ExtractInstructions: execute ");
		TestContext testContext = new TestContext(testset);

		totalStartTime = System.currentTimeMillis();
		WebElementProcessor processor = new WebElementProcessor(browser, testContext);
		try {
			resetIframeControlFlag();
			logger.info("Executing the Test case  : " + testcaseName);
			// setting up the test case name to be executed
			testset.setTestCase(sheetName);
			Iterator<Row> rowIterator = testset.getTestCase().getWorkSheet().iterator();
			teststep = rowIterator.next();
			isTimerColumnExists = testset.getTestCase().hasTimer();
			isContinueExecutionColumnExists = testset.getTestCase().hasContinueExecution();
			rowCounter++; // Ignore Header
			boolean isLoadIndicatorExists = false;

			while (rowIterator.hasNext()) {
				testStepPassed = true;
				teststep = rowIterator.next();
				rowCounter++;
				Cell actionCell = teststep.getCell(0);
				if (requestedToSkipTestStep(actionCell))
					continue;
				else
					actionKeyword = actionCell.getStringCellValue().toUpperCase().trim().replace(" ", "_");

				// as soon as we encounter a blank keyword then stop
				if (StringUtils.isBlank(actionKeyword)) {
					logger.info("Action keyword is empty.");
					break;
				}

				if (StringUtils.isNotBlank(actionKeyword) && !endofTestCase()) {
					String methodName = null;
					Class[] inputParametersTypes;
					Object[] actionParams = null;
					methodName = Keyword.valueOf(actionKeyword).getMethodName();
					type = Keyword.valueOf(actionKeyword).getType();
					inputParametersTypes = getClassArray(type);
					try {
						actionParams = getObjectArray(teststep, type);
					} catch (TestAutomationException te) {
						logger.error(te.getMessage());
						if (isActionFailing(false, teststep)) {
							if (checkContinueExecutionColumn()) {
								continue;
							}
							testStepPassed = false;
							testCasePassed = false;
							logger.info("Action :" + teststep.getCell(0).getStringCellValue()
									+ "  is failed and remaining actions will not execute");
							break;
						} else {
							continue;
						}
					}
					if (logger.isDebugEnabled()) {
						logger.debug("Executing the Test case  : methodName: " + methodName);
						logger.debug("Executing the Test case  : inputParametersTypes: " + inputParametersTypes);
						logger.debug("Executing the Test case  : actionParams: " + actionParams);
						logger.debug("Executing the Test case  : actionKeyword: " + actionKeyword);
					}
					Method method = processor.getClass().getDeclaredMethod(methodName, inputParametersTypes);
					startTime = System.currentTimeMillis();

					try {
						testStepPassed = (Boolean) method.invoke(processor, actionParams);
					} catch (Exception ee) {
						if (checkContinueExecutionColumn()) {
							logger.error(ee.getMessage());
							// to take the screenshots of failed action's to
							// failure folder
							catchedExceptionLogging(processor, ee);
							continue;
						} else {
							if (teststep.getLastCellNum() == 5 || teststep.getLastCellNum() == 6) {
								if (!testStepPassed) {
									catchedExceptionLogging(processor, ee);
									break;
								}
							} else {
								catchedExceptionLogging(processor, ee);
								break;
							}
						}
					}

					calculateTime();
					if (actionKeyword.equalsIgnoreCase("LOGIN TO URL")
							|| actionKeyword.equalsIgnoreCase("LOGIN_TO_URL")) {
						checkAlert();
						isLoadIndicatorExists = isLoadIndicatorExists();
						testContext.setLoadIndicator(isLoadIndicatorExists);
					}

					updateTestStepResult(testStepPassed);
					if (isActionFailing(testStepPassed, teststep)) {
						if (!testStepPassed && checkContinueExecutionColumn()) {
							continue;
						}
						testStepPassed = false;
						testCasePassed = false;
						logger.info("Action :" + teststep.getCell(0).getStringCellValue()
								+ "  is failed and remaining actions will not execute");
						break;
					}

					if (testContext.getLoadIndicator()) {
						// wait for ajax processing if load indicator exists or
						// action is failed
						if (ajaxReqestPending() || !testStepPassed) {
							testCasePassed = false;
							testset.getTestCase().updateTestStepResult(testStepPassed, teststep, calculateTime());
							break;
						}
					} else if (!isLoadIndicatorExists
							&& (!actionKeyword.equalsIgnoreCase("Verify_Excel_Text") || !actionKeyword
									.equalsIgnoreCase("Verify_Csv_Text"))) {
						Cell urlCell = testset.getTestCase().getWorkSheet().getRow(1).getCell(1);
						String url = urlCell.getStringCellValue();

						if (isParameter(url)) {
							url = replaceParameter(urlCell.getStringCellValue());

							// Condition to handle TBP and CSOL page which does
							// not
							// have load indicator
							if (!url.equalsIgnoreCase("https://svn.vebnet.com/tbpsso.htm")
									&& !url.equalsIgnoreCase("http://cvsprod/viewcvs/viewcvs.cgi/CSOLTest/CSOLInternal.htm?rev=HEAD&cvsroot=CSOL&content-type=text/html")) {
								testStepPassed = false;
								testCasePassed = false;
								testset.getTestCase().updateTestStepResult(false, teststep, calculateTime());
								logger.info("load indicator does not exists, can not execute next action");
								break;
							}
						}
					}

				} else {
					// Sometimes the spreadsheet has corrupt rows beyond the END
					// statement.
					// Therefore want to stop after END keyword encountered even
					// if rowIterator.hasNext() is true.
					break;
				}
			}
			toalEndTime = System.currentTimeMillis();
			toalTime = toalEndTime - totalStartTime;
			logger.info("To complete the test case total time required is : " + toalTime);
			if (endofTestCase()) {
				if (isTimerColumnExists) {
					Cell timerCell = teststep.getCell(4);
					if (timerCell != null) {
						timerCell.setCellValue("Total time: " + Long.toString(toalTime) + " ms");
					} else {
						logger.info("Timer cell does not exists hence total time can not be shown.");
					}
				}
			}
		} catch (Exception e) {
			catchedExceptionLogging(processor, e);
		} finally {
			if (testStepPassed == false) {
				calculateTime();
			}
		}

		updateTestStepResult(testStepPassed);
		applyFormatting();
		logger.info("No of commands executed : " + rowCounter);
		logger.info("Test case  : " + testcaseName + " execution is completed!");
		logger.debug("End of ExtractInstructions: execute : testCasePassed == " + testCasePassed);

		return testCasePassed;
	}

	/**
	 * Below method click's on "Leave Page" button if random alert box appeared
	 * on screen
	 */
	public static void checkAlert() {
		try {
			browser.switchTo().alert().accept();
			browser.switchTo().defaultContent();
		} catch (Exception e) {
		}
	}

	/**
	 * checking continue execution column parameter
	 * 
	 * @return
	 */
	private boolean checkContinueExecutionColumn() {
		// If it is set to "Y" then returns true else returns false
		if (teststep.getLastCellNum() == 5 || teststep.getLastCellNum() == 6) {
			if (isContinueExecutionColumnExists
					&& (teststep.getCell(4).getStringCellValue().equalsIgnoreCase("Y") || (teststep.getCell(5) != null && teststep
							.getCell(5).getStringCellValue().equalsIgnoreCase("Y")))) {
				testStepPassed = false;
				testCasePassed = false;
				updateTestStepResult(testStepPassed);
				calculateTime();
				return true;
			} else {
				testStepPassed = false;
				testCasePassed = false;
				updateTestStepResult(testStepPassed);
				calculateTime();
			}
		}
		return false;
	}

	/**
	 * If one action is failed in between whole testcase remaining actions are
	 * will not execute
	 * 
	 * @param teststepStatus
	 * @param teststep
	 * @return
	 */
	private boolean isActionFailing(boolean teststepStatus, Row teststep) {
		return (!teststepStatus && teststep.getCell(3).getStringCellValue().equalsIgnoreCase("Fail")) ? true : false;
	}

	/**
	 * Calculate time for completing action execution
	 */
	private long calculateTime() {
		long endTime = System.currentTimeMillis();
		long time = endTime - startTime;
		if (isTimerColumnExists) {
			Cell timerCell = teststep.getCell(4);
			timerCell.setCellValue(Long.toString(time) + " ms");
			return time;
		} else {
			// if "Timer" column is not used in test case
			return time;
		}
	}

	public Object[] getObjectArray(Row row, String type) throws TestAutomationException {
		logger.debug("Start of ExtractInstructions: getObjectArray ");
		logger.debug("ExtractInstructions: getObjectArray: row: " + row + " Type: " + type);

		CellReader cellReader = new CellReader(evaluator);

		if (type.equals("TYPE1")) {
			// Type 1 indicates that parameter 1 is mandatory and parameter 2 is
			// ignored.
			String inputCellValue = cellReader.getStringCellValue(row.getCell(1, Row.CREATE_NULL_AS_BLANK));
			inputCellValue = replaceParameter(inputCellValue);

			if (StringUtils.isBlank(inputCellValue)) {
				throw new TestAutomationException(
						ExceptionErrorCodes.TA050_EXTRACT_INSTRUCTIONS_INVALID_INPUT_DATA_FOR_KEYWORD_ERROR,
						"An input is expected for the key word: " + actionKeyword + " of type: " + type);
			}

			return new Object[] { new String(inputCellValue) };

		} else if (type.equals("TYPE2")) {
			// Type 2 indicates that parameters 1 is mandatory, but parameter 2
			// is optional for some keywords.
			String inputCellValue1 = cellReader.getStringCellValue(row.getCell(1, Row.CREATE_NULL_AS_BLANK)).trim();
			inputCellValue1 = replaceParameter(inputCellValue1);
			inputCellValue1 = setOptionalParameter1(inputCellValue1);

			String inputCellValue2 = cellReader.getStringCellValue(row.getCell(2, Row.CREATE_NULL_AS_BLANK)).trim();
			inputCellValue2 = replaceParameter(inputCellValue2);
			inputCellValue2 = setOptionalParameter2(inputCellValue2);

			if (StringUtils.isBlank(inputCellValue1) || StringUtils.isBlank(inputCellValue2)) {
				throw new TestAutomationException(
						ExceptionErrorCodes.TA050_EXTRACT_INSTRUCTIONS_INVALID_INPUT_DATA_FOR_KEYWORD_ERROR,
						"Two inputs are expected for the key word: " + actionKeyword + " of type: " + type);
			}

			return new Object[] { new String(inputCellValue1.equalsIgnoreCase("#empty#") ? "" : inputCellValue1),
					new String(inputCellValue2.equalsIgnoreCase("#empty#") ? "" : inputCellValue2) };
		}

		else
			logger.debug("End of ExtractInstructions: getObjectArray ");
		return null;

	}

	private String replaceParameter(String inputCellValue) throws TestAutomationException {
		if (isParameter(inputCellValue)) {
			inputCellValue = testset.getTestData().getParameterValue(inputCellValue);
			if (StringUtils.isBlank(inputCellValue)) {
				throw new TestAutomationException(
						ExceptionErrorCodes.TA049_EXTRACT_INSTRUCTIONS_TEST_DATA_NOT_AVAILABLE_ERROR,
						"Test data for the input parameter: " + inputCellValue + " for the key word: " + actionKeyword
								+ " is not available ");
			}
		}
		return inputCellValue;
	}

	private boolean isParameter(String cellStringValue) {
		if (cellStringValue.startsWith(Global.parameterIdentifier))
			return true;
		else
			return false;
	}

	/**
	 * Some keywords have an optional parameter 1. This method will set the
	 * correct value.
	 * 
	 * @param inputCellValue1
	 * @return
	 */
	private String setOptionalParameter1(String inputCellValue1) {
		if (StringUtils.isBlank(inputCellValue1)) {
			if (actionKeyword.equalsIgnoreCase("DOWNLOAD_LOG_FILE")) {
				inputCellValue1 = "#empty#";
			}

		}
		return inputCellValue1;
	}

	/**
	 * Some keywords have an optional parameter 2. This method will set the
	 * correct value.
	 * 
	 * @param inputCellValue2
	 * @return
	 */
	private String setOptionalParameter2(String inputCellValue2) {
		if (StringUtils.isBlank(inputCellValue2)) {

			if (actionKeyword.equalsIgnoreCase("CLICK_ON_BUTTON")
					|| actionKeyword.equalsIgnoreCase("CLICK_ON_FST_BUTTON")
					|| actionKeyword.equalsIgnoreCase("VERIFY_BUTTON")
					|| actionKeyword.equalsIgnoreCase("VERIFY_ON_FST_BUTTON")
					|| actionKeyword.equalsIgnoreCase("CLICK_ON_LISTBOX_OPTION")
					|| actionKeyword.equalsIgnoreCase("NAVIGATE_TO") || actionKeyword.equalsIgnoreCase("CLICK_ON_LINK")
					|| actionKeyword.equalsIgnoreCase("CLICK_ON_NAVIGATION_LINK")
					|| actionKeyword.equalsIgnoreCase("CLICK_ON_TAB")) {
				inputCellValue2 = "1";
			}
			if (actionKeyword.equalsIgnoreCase("SELECT_CHECKBOX") || actionKeyword.equalsIgnoreCase("VERIFY_CHECKBOX")
					|| actionKeyword.equalsIgnoreCase("TAKE_SCREENSHOT") || actionKeyword.equalsIgnoreCase("INPUT")
					|| actionKeyword.equalsIgnoreCase("VERIFY_ELEMENT")
					|| actionKeyword.equalsIgnoreCase("VERIFY_EXCEL_TEXT")
					|| actionKeyword.equalsIgnoreCase("SAVE_PDF")
					|| actionKeyword.equalsIgnoreCase("VERIFY_TABLE_TEXT")
					|| actionKeyword.equalsIgnoreCase("SWITCH_TO_IFRAME")
					|| actionKeyword.equalsIgnoreCase("CLICK_ON_DOWNLOAD")
					|| actionKeyword.equalsIgnoreCase("VERIFY_RADIO_BUTTON")
					|| actionKeyword.equalsIgnoreCase("VERIFY_CSV_TEXT")
					|| actionKeyword.equalsIgnoreCase("SELECT_FST_FUND")
					|| actionKeyword.equalsIgnoreCase("VERIFY_INPUT_FIELD")
					|| actionKeyword.equalsIgnoreCase("VERIFY_TEXT")) {
				inputCellValue2 = "#empty#";
			}
		}
		return inputCellValue2;
	}

	@SuppressWarnings("rawtypes")
	private Class[] getClassArray(String type) {

		if (type.equals("TYPE1"))
			return new Class[] { String.class };
		else if (type.equals("TYPE2"))
			return new Class[] { String.class, String.class };
		else
			System.out.println("Not an expected ParametersType ");

		return null;
	}

	public String getTestStatusMessage() {
		return testStatusMessage.replaceAll("\u00A3", "").replace("com.lifelens.exceptions.TestAutomationException:",
				"");
	}

	public void setTestStatusMessage(String testStatusMessage) {
		this.testStatusMessage = testStatusMessage;
	}

	private boolean requestedToSkipTestStep(Cell cell) {
		CellStyle style = cell.getCellStyle();
		Font cellFont = cell.getSheet().getWorkbook().getFontAt(style.getFontIndex());
		if (cellFont.getStrikeout())
			return true;
		else
			return false;

	}

	public static void resetIframeControlFlag() {
		controlInIframe = false;
	}

	/**
	 * Checks if load indicator exists or not on a web page
	 * 
	 * @return isLoadIndicatorExists
	 */
	public static boolean isLoadIndicatorExists() throws TestAutomationException {
		List<WebElement> divs;
		try {
			// going back to normal timeout
			browser.manage().timeouts().implicitlyWait(Global.TIMEOUT_SHORT, TimeUnit.SECONDS);
			divs = browser.findElements(By.tagName("div"));
		} finally {
			// going back to normal timeout
			browser.manage().timeouts().implicitlyWait(Global.timeout, TimeUnit.SECONDS);
		}
		boolean isLoadIndicatorExists = false;

		if (!divs.isEmpty()) {
			try {
				// short timeout as empty header or footer is common
				browser.manage().timeouts().implicitlyWait(Global.TIMEOUT_SHORT, TimeUnit.SECONDS);
				for (int i = 0; i < divs.size(); i++) {

					String divId = WebElementRetriever.getWebElementId(divs.get(i));

					if (divId.equals("loadIndicator")) {
						isLoadIndicatorExists = true;
						break;
					}
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			} finally {
				// back to the default timeout
				browser.manage().timeouts().implicitlyWait(Global.timeout, TimeUnit.SECONDS);
			}
		}
		return isLoadIndicatorExists;
	}

	/**
	 * exceptions and logging
	 * 
	 * @param processor
	 * @param e
	 */
	private void catchedExceptionLogging(WebElementProcessor processor, Exception e) {
		TestAutomationException excp = null;
		String errorCode = "";
		if (e instanceof TestAutomationException) {
			excp = (TestAutomationException) e;
			errorCode = excp.getCode();
			logger.error("Error Code: " + errorCode + " - Message: '" + excp.getMessage()
					+ "' error raised while executing the keyword: " + actionKeyword);
			this.testStatusMessage = " Message: '" + excp.getMessage() + "' error raised while executing the keyword: "
					+ actionKeyword + " at row number: " + rowCounter;
		} else if (e.getCause() instanceof TestAutomationException) {
			excp = (TestAutomationException) e.getCause();
			errorCode = excp.getCode();
			logger.error("Error Code: " + errorCode + " - Message: '" + excp.getMessage()
					+ "' error raised while executing the keyword: " + actionKeyword);
			this.testStatusMessage = " Message: '" + excp.getMessage() + "' error raised while executing the keyword: "
					+ actionKeyword + " at row number: " + rowCounter;
		} else {
			this.testStatusMessage = " Message: '" + e.getMessage() + "' error raised while executing the keyword: "
					+ actionKeyword + " at row number: " + rowCounter;
			logger.error("Error occured while executing Key Command: " + actionKeyword);
			logger.error("Testcase : " + testcaseName + "   failed!!");
			logger.error(e, e);
		}

		try {
			processor.takeScreenshot(
					testset.getWorkbookName().replaceAll("\\.", "_") + "_" + sheetName.replaceAll(" ", "_") + "_"
							+ actionKeyword + "_Row_" + rowCounter + "_" + errorCode, "Failures");
		} catch (TestAutomationException e1) {
			logger.error("Error Code: " + e1.getCode() + " - Message: '" + e1.getMessage()
					+ "' error raised while executing the keyword: " + actionKeyword);
			this.testStatusMessage = " Message: '" + excp.getMessage() + "' error raised while executing the keyword: "
					+ actionKeyword + " at row number: " + rowCounter;
		}
		e.printStackTrace();
		testStepPassed = false;
		testCasePassed = false;
	}
}
