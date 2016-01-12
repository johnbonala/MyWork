package com.lifelens.webelements;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebDriver;

import com.lifelens.browsers.WebElementProcessor;
import com.lifelens.browsers.WebElementProcessorCommon;
import com.lifelens.excel.ExcelUtils;
import com.lifelens.exceptions.ExceptionErrorCodes;
import com.lifelens.exceptions.TestAutomationException;
import com.lifelens.globals.Global;
import com.lifelens.interpreter.ArgList;
import com.lifelens.interpreter.TestContext;

/**
 * This element is used to change password of the employees given in testdata If
 * employee is new joiner then to change password it is required to answer some
 * memorable questions. Those questions and answers are taken from testdata's
 * Sheet1: columns Question and Answer
 * 
 * @author jayanti_rode
 * 
 */
public class Password {
	private static Logger logger = Logger.getLogger(Label.class.getName());
	private WebDriver webDriver = null;
	private WebElementProcessor browser = null;
	private FileInputStream file = null;
	private XSSFWorkbook workbook = null;
	private XSSFSheet employeeSheet = null;
	private String testDataFileName;
	private String employeeTestDataSheet;
	private String tempraryPwd = "Py55w0rx";
	private Map<String, String> empData = new HashMap<String, String>();
	boolean isEmployeeAlreadyExist;
	boolean isNoEmployeeFound;
	private Map<String, String> existingEmpData = new HashMap<String, String>();
	private String loadIndicatorCSS = Global.loadIndicatorCSS;
	private String userNameLabel;
	private String passwordLabel;
	private String adminLogoutLabel;
	private String employeeLogoutLabel;
	private boolean isIAgreeCheckboxAvailable;
	private TestContext testContext;
	private static final String FIRSTMEMORABLEQUESTIONKEY = "First memorable question";
	private static final String FIRSTMEMORABLEANSWERKEY = "*First memorable answer";
	private static final String SECONDMEMORABLEQUESTIONKEY = "Second memorable question";
	private static final String SECONDMEMORABLEANSWERKEY = "*Second memorable answer";
	private static final String THIRDMEMORABLEQUESTIONKEY = "Third memorable question";
	private static final String THIRDMEMORABLEANSWERKEY = "*Third memorable answer";
	private static final String FOURTHMEMORABLEQUESTIONKEY = "Fourth memorable question";
	private static final String FOURTHMEMORABLEANSWERKEY = "*Fourth memorable answer";
	private static final String NOEMPLOYEEFOUND = "No employees found.";

	/**
	 * This element is used to reset passwords of the employees in the given
	 * test data file. Test data file have columns Sr. no. StaffNumber and
	 * Password
	 * 
	 * @param webDriver
	 * @throws TestAutomationException
	 */
	public Password(WebDriver webDriver, TestContext testContext, String sheetName, String labelNames)
			throws TestAutomationException {
		this.webDriver = webDriver;
		testDataFileName = Global.getTestdatFileAbsolutepath();
		this.testContext = testContext;

		ArgList argList = new ArgList(labelNames);
		userNameLabel = argList.getParameter(0);
		passwordLabel = argList.getParameter(1);
		adminLogoutLabel = argList.getParameter(2);
		employeeLogoutLabel = argList.getParameter(3);

		if (!sheetName.equalsIgnoreCase(null)) {
			employeeTestDataSheet = sheetName;
		}
	}

	/**
	 * This is the method which is called when CHANGE_PASSWORD keyword is
	 * triggered. This method retrieves data from configuration and testdata
	 * file and take actions to reset the password
	 * 
	 * @return boolean
	 * @throws TestAutomationException
	 */
	public boolean changePassword() throws TestAutomationException {
		logger.debug("Start of Password: changePassword");
		// setting load indicator "true" as there will not be login to url
		// keyword for change password script

		try {

			testContext.setLoadIndicator(true);
			browser = new WebElementProcessor(webDriver, testContext);
			String url = testContext.getTestSet().getTestData().getParameterValue("@url1");
			String adminUserName = testContext.getTestSet().getTestData().getParameterValue("@adminUserName1");
			String adminPassword = testContext.getTestSet().getTestData().getParameterValue("@adminPassword1");
			webDriver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
			webDriver.navigate().to(url);
			browser.input(userNameLabel, adminUserName);
			browser.input(passwordLabel, adminPassword);
			browser.clickOnButton("Login", "1");
			browser.navigateTo("Admin > Manage Employee", "1");
			empData = retrieveEmployeeTestData();

			if (empData.size() > 0) {
				Iterator<Entry<String, String>> entries = empData.entrySet().iterator();
				while (entries.hasNext()) {
					Entry<String, String> thisEntry = ((Entry<String, String>) entries.next());
					Object key = thisEntry.getKey();
					Object value = thisEntry.getValue();
					browser.input("Staff number", (String) key);
					browser.clickOnButton("Search", "1");
					WebElementProcessorCommon.waitForWebProcessing(webDriver, 40, loadIndicatorCSS, "");
					isNoEmployeeFound = checkEmployeeFound((String) key);
					if (!isNoEmployeeFound) {
						browser.selectDropdownValue("Action to perform*", "Modify employee");
						WebElementProcessorCommon.waitForWebProcessing(webDriver, 30, loadIndicatorCSS, "");
						browser.clickOnButton("Go", "");
						WebElementProcessorCommon.waitForWebProcessing(webDriver, 30, loadIndicatorCSS, "");
						browser.input(passwordLabel, tempraryPwd);
						browser.clickOnButton("Save", "1");
						WebElementProcessorCommon.waitForWebProcessing(webDriver, 30, loadIndicatorCSS, "");
						browser.selectRadioButton("Bypass adjustment processing");
						WebElementProcessorCommon.waitForWebProcessing(webDriver, 30, loadIndicatorCSS, "");
						browser.clickOnButton("Continue", "1");
						WebElementProcessorCommon.waitForWebProcessing(webDriver, 30, loadIndicatorCSS, "");
						existingEmpData.put((String) key, (String) value);
					}
				}
				browser.clickOnNavigationLink(adminLogoutLabel, "1");
				WebElementProcessorCommon.waitForWebProcessing(webDriver, 30, loadIndicatorCSS, "");
				if (existingEmpData.size() > 0) {
					employeeLogin(existingEmpData);
				}
			}
			logger.debug("End of Password: changePassword");
			return true;

		} finally {
			// removing the implicit wait not required for change password
			// action
			// webDriver.manage().timeouts().implicitlyWait(Global.timeout,
			// TimeUnit.SECONDS);
		}
	}

	/**
	 * This method returns Map of StaffNumber and Password from the employee
	 * sheet
	 * 
	 * @return Map<String, String>, map having staffnumber as a key and password
	 *         as its associated value
	 * @throws TestAutomationException
	 */
	private Map<String, String> retrieveEmployeeTestData() throws TestAutomationException {
		logger.debug("Start of Password: retrieveEmployeeTestData");
		String staffNumber = null;
		String password = null;
		int startColumn = 2, startRow = 2;
		if (employeeTestDataSheet != null) {
			employeeSheet = getEmployeeSheet(employeeTestDataSheet);
			if (employeeSheet != null) {
				ExcelUtils exelUtils = new ExcelUtils(workbook);
				for (int col = 1; col < exelUtils.getColumnCount(employeeTestDataSheet) - 1; col++) {
					// Executing till the last row
					for (int row = startRow; row <= exelUtils.getRowCount(employeeTestDataSheet); row++) {
						staffNumber = exelUtils.getCellData(employeeTestDataSheet, col, row);
						password = exelUtils.getCellData(employeeTestDataSheet, startColumn, row);
						if (staffNumber != null && !staffNumber.isEmpty()) {
							if (password != null && !password.isEmpty()) {
								empData.put(staffNumber, password);
							}
						}
					}
				}
			}
		}
		logger.debug("End of Password: retrieveEmployeeTestData");
		return empData;
	}

	/**
	 * This method returns sheet having employee data
	 * 
	 * @param employeeTestDataSheet
	 * @return XSSFSheet as a employeeSheet
	 * @throws TestAutomationException
	 */
	private XSSFSheet getEmployeeSheet(String employeeTestDataSheet) throws TestAutomationException {
		logger.debug("Start of Password: getEmployeeSheet");
		try {
			file = new FileInputStream(new File(testDataFileName));
			workbook = new XSSFWorkbook(file);
			if (employeeTestDataSheet != null) {
				employeeSheet = workbook.getSheet(employeeTestDataSheet);
			}
		} catch (FileNotFoundException e) {
			throw new TestAutomationException(ExceptionErrorCodes.TA004_TEST_DATA_FILE_NOT_FOUND_ERROR, e.getMessage()
					+ " : Properties file not found in the path " + testDataFileName, e);
		} catch (IOException e) {
			throw new TestAutomationException(ExceptionErrorCodes.TA005_TEST_DATA_FILE_LOAD_ERROR, e.getMessage()
					+ ": Unable to load the properties file in the path " + testDataFileName, e);
		} finally {
			if (file != null) {
				try {
					file.close();
				} catch (IOException e) {
					throw new TestAutomationException(ExceptionErrorCodes.TA006_TEST_DATA_FILE_LOAD_ERROR,
							e.getMessage() + ": Unable to close the properties file input stream", e);
				}
			}
		}
		logger.debug("End of Password: getEmployeeSheet");
		return employeeSheet;
	}

	/**
	 * In this method, steps are written to login to employee screen and reset
	 * final password for logged in employee Here, as per employee is existing
	 * or new action is taken
	 * 
	 * @param employeeData
	 * @return boolean
	 * @throws TestAutomationException
	 */
	public boolean employeeLogin(Map<String, String> employeeData) throws TestAutomationException {
		logger.debug("Start of Password: employeeLogin");
		Iterator<Entry<String, String>> entries = employeeData.entrySet().iterator();
		boolean isPasswordChanged = false;
		if (entries != null) {
			while (entries.hasNext()) {
				Entry<String, String> thisEntry = ((Entry<String, String>) entries.next());
				Object key = thisEntry.getKey();
				String value = thisEntry.getValue();
				WebElementProcessorCommon.waitForWebProcessing(webDriver, 30, loadIndicatorCSS, "");
				browser.input(userNameLabel, (String) key);
				browser.input(passwordLabel, tempraryPwd);
				browser.clickOnButton("Login", "1");
				browser.input("* New password", value);
				browser.input("* Confirm password", value);
				isEmployeeAlreadyExist = checkEmployeeAlreadyExist();
				// If employee exists click on "Finish" else click on "Next"
				if (isEmployeeAlreadyExist) {
					WebElementProcessorCommon.waitForWebProcessing(webDriver, 30, loadIndicatorCSS, "");
					browser.clickOnButton("Finish", "1");
					WebElementProcessorCommon.waitForWebProcessing(webDriver, 30, loadIndicatorCSS, "");
					browser.navigateTo(employeeLogoutLabel, "1");
					WebElementProcessorCommon.waitForWebProcessing(webDriver, 30, loadIndicatorCSS, "");
				} else {
					browser.clickOnButton("Next", "1");
					WebElementProcessorCommon.waitForWebProcessing(webDriver, 30, loadIndicatorCSS, "");
					browser.selectDropdownValue(FIRSTMEMORABLEQUESTIONKEY, testContext.getTestSet().getTestData()
							.getParameterValue("@Question1"));
					browser.input(FIRSTMEMORABLEANSWERKEY,
							testContext.getTestSet().getTestData().getParameterValue("@Answer1"));
					WebElementProcessorCommon.waitForWebProcessing(webDriver, 30, loadIndicatorCSS, "");
					browser.selectDropdownValue(SECONDMEMORABLEQUESTIONKEY, testContext.getTestSet().getTestData()
							.getParameterValue("@Question2"));
					browser.input(SECONDMEMORABLEANSWERKEY,
							testContext.getTestSet().getTestData().getParameterValue("@Answer2"));
					WebElementProcessorCommon.waitForWebProcessing(webDriver, 30, loadIndicatorCSS, "");
					browser.selectDropdownValue(THIRDMEMORABLEQUESTIONKEY, testContext.getTestSet().getTestData()
							.getParameterValue("@Question3"));
					browser.input(THIRDMEMORABLEANSWERKEY,
							testContext.getTestSet().getTestData().getParameterValue("@Answer3"));
					WebElementProcessorCommon.waitForWebProcessing(webDriver, 30, loadIndicatorCSS, "");
					browser.selectDropdownValue(FOURTHMEMORABLEQUESTIONKEY, testContext.getTestSet().getTestData()
							.getParameterValue("@Question4"));
					browser.input(FOURTHMEMORABLEANSWERKEY,
							testContext.getTestSet().getTestData().getParameterValue("@Answer4"));
					browser.clickOnButton("Next", "1");
					if (checkIagreeCheckbox()) {
						browser.selectCheckBox("* I agree", "TRUE");
					}
					browser.clickOnButton("Next", "1");
					WebElementProcessorCommon.waitForWebProcessing(webDriver, 30, loadIndicatorCSS, "");
					isEmployeeAlreadyExist = checkEmployeeAlreadyExist();
					if (!isEmployeeAlreadyExist) {
						browser.clickOnButton("Next", "1");
						WebElementProcessorCommon.waitForWebProcessing(webDriver, 30, loadIndicatorCSS, "");
					}
					browser.clickOnButton("Finish", "1");
					WebElementProcessorCommon.waitForWebProcessing(webDriver, 40, loadIndicatorCSS, "");
					browser.navigateTo(employeeLogoutLabel, "1");
					WebElementProcessorCommon.waitForWebProcessing(webDriver, 30, loadIndicatorCSS, "");

				}

			}
			isPasswordChanged = true;
		} else {
			throw new TestAutomationException(ExceptionErrorCodes.TA114_EMPLOYEE_LIST_IS_NULL,
					"Employee List for change password is null");
		}
		logger.debug("End of Password: employeeLogin");
		return isPasswordChanged;
	}

	/**
	 * Checks whether employee found on click of Search button
	 * 
	 * @param staffNumber
	 * @return boolean isNoEmployeeFound
	 */
	private boolean checkEmployeeFound(String staffNumber) {
		try {
			isNoEmployeeFound = browser.verifyText(NOEMPLOYEEFOUND, "True");
			logger.info("Employee not found--StaffNumber:" + staffNumber);
		} catch (Exception e) {
			isNoEmployeeFound = false;
		}
		return isNoEmployeeFound;
	}

	/**
	 * Checks whether employee already exists in database
	 * 
	 * @return boolean isEmployeeAlreadyExist
	 */
	private boolean checkEmployeeAlreadyExist() {
		try {
			isEmployeeAlreadyExist = browser.verifyButton("Finish", "1");
		} catch (Exception e) {
			isEmployeeAlreadyExist = false;
		}
		return isEmployeeAlreadyExist;
	}

	/**
	 * Checks whether I agree checkbox is available on the page or not
	 * 
	 * @return boolean isIAgreeCheckboxAvailable
	 */
	private boolean checkIagreeCheckbox() {
		try {
			isIAgreeCheckboxAvailable = browser.verifyCheckbox("* I agree", "");
		} catch (Exception e) {
			isIAgreeCheckboxAvailable = false;
		}
		return isIAgreeCheckboxAvailable;
	}

}