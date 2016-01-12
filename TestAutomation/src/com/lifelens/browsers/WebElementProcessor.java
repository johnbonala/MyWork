package com.lifelens.browsers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.lifelens.automation.tabulardata.FileBuilder;
import com.lifelens.automation.tabulardata.TabularData;
import com.lifelens.exceptions.ExceptionErrorCodes;
import com.lifelens.exceptions.TestAutomationException;
import com.lifelens.globals.Global;
import com.lifelens.globals.GlobalCommonConstants;
import com.lifelens.globals.GlobalTags;
import com.lifelens.htmlparser.HtmlParser;
import com.lifelens.interpreter.ArgList;
import com.lifelens.interpreter.ExtractInstructions;
import com.lifelens.interpreter.TestContext;
import com.lifelens.testset.TestSet;
import com.lifelens.webelements.Accordion;
import com.lifelens.webelements.AdminPanel;
import com.lifelens.webelements.Button;
import com.lifelens.webelements.Calendar;
import com.lifelens.webelements.CheckBox;
import com.lifelens.webelements.Dropdown;
import com.lifelens.webelements.FSTFundTable;
import com.lifelens.webelements.FSTIframe;
import com.lifelens.webelements.FSTProfileTable;
import com.lifelens.webelements.Hyperlink;
import com.lifelens.webelements.InputTextbox;
import com.lifelens.webelements.Password;
import com.lifelens.webelements.RadioButton;
import com.lifelens.webelements.Spinner;
import com.lifelens.webelements.SwimLane;
import com.lifelens.webelements.Table;
import com.lifelens.webelements.TopNavigation;
import com.lifelens.webelements.WebElementRetriever;
import com.lifelens.webelements.v3.ColumnCustomisation;
import com.lifelens.webelements.v3.EnrolmentForm;
import com.lifelens.webelements.verify.VerifyText;

public class WebElementProcessor implements IBrowser {

	private static Logger logger = Logger.getLogger(WebElementProcessor.class.getName());
	public String workSheetName;
	private WebDriver browser;
	private TestContext testContext;

	public WebElementProcessor(WebDriver webBrowser, TestContext testContext) {
		browser = webBrowser;
		this.testContext = testContext;
	}

	@Override
	public boolean geturl(String url) {
		browser.get(url);
		return true;
	}

	@Override
	public boolean input(String labelName, String inputValue) throws TestAutomationException {

		return new InputTextbox(browser, labelName, testContext).sendKeys(inputValue);

	}

	@Override
	public boolean inputFile(String labelName, String inputValue) throws TestAutomationException {

		// the path from test script may or may not have a slash at the start
		// we need to make sure the slash exists
		if (!inputValue.startsWith("\\") || !inputValue.startsWith("/")) {
			inputValue = "\\" + inputValue;
		}

		// input file paths should be relative to the TestLab path
		// so add the TestLab path onto the start of the specified file path
		String inputFilePath = Global.getTestLabPath() + inputValue;

		// windows file chooser does not like forward slashes
		inputFilePath = inputFilePath.replaceAll("/", Matcher.quoteReplacement("\\"));

		logger.info("Input File path: " + inputFilePath);

		// When using a file input field, we want to make sure this file exists
		// and is not a directory
		File inputFile = new File(inputFilePath);
		if (!inputFile.exists() || !inputFile.isFile()) {
			throw new TestAutomationException(ExceptionErrorCodes.TA121_INPUT_FILE_DOES_NOT_EXIST,
					"input file error, file does not exist: " + inputFilePath);
		}

		return new InputTextbox(browser, labelName, testContext).sendKeys(inputFilePath);
	}

	@Override
	public boolean clickOnLink(String hyperLinkName, String index) throws TestAutomationException {

		return new Hyperlink(browser, hyperLinkName, retrieveIndex(index), testContext)
				.clickOnlinkByCSS("a, input[type='submit']");

	}

	@Override
	public boolean clickOnFactSheetLink(String cell) throws TestAutomationException {

		return new FSTFundTable(browser).clickOnFactSheetLink(cell);

	}

	@Override
	public boolean selectRadioButton(String radioButtonLabel) throws TestAutomationException {

		return new RadioButton(browser, testContext, radioButtonLabel).select();

	}

	@Override
	public boolean selectCheckBox(String label, String booleanValue) throws TestAutomationException {
		return new CheckBox(browser, testContext, label).select(booleanValue);
	}

	/**
	 * @throws TestAutomationException
	 * @see com.lifelens.browsers.IBrowser#selectDropdownValue(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public boolean selectDropdownValue(String dropdownLabel, String dropdownValue) throws TestAutomationException {
		boolean isSelected = false;

		try {
			isSelected = new Dropdown(browser, dropdownLabel, testContext).selectValueFromDropdown(dropdownValue);
		} catch (TestAutomationException te) {
			isSelected = false;
			throw te;
		}

		return isSelected;
	}

	/**
	 * Selects the date provided from calendar
	 * 
	 * @param calendarLabel
	 * @param ddmmyyyyFormat
	 * @return
	 * @throws TestAutomationException
	 */
	@Override
	public boolean selectDateFromCalendar(String calendarLabel, String ddmmyyyyFormat) throws TestAutomationException {
		return new Calendar(browser, calendarLabel, testContext).selectDateFromCalendar(ddmmyyyyFormat);
	}

	@Override
	public boolean switchWindow(String tabNumberParam) throws TestAutomationException {

		int tabNumber = 1;
		try {
			tabNumber = (int) Float.parseFloat(tabNumberParam);
		} catch (NumberFormatException e) {
			throw new TestAutomationException(
					ExceptionErrorCodes.TA057_WEB_ELEMENT_PROCESSING_COMMON_OINVALID_INPUT_INDEX_FOR_SWITCH_ERROR,
					"Invalid input parameter passed to switch window", e);
		}
		// Allow script to specify 1 for first window instead of 0:
		tabNumber = tabNumber - 1;
		ArrayList<String> tabs = new ArrayList<String>(browser.getWindowHandles());
		if (tabs != null && tabs.get(tabNumber) != null) {
			browser.switchTo().window(tabs.get(tabNumber)).manage().window().maximize();
			ExtractInstructions.resetIframeControlFlag();
			logger.info("Switch window executed successfully to window number: " + (tabNumber + 1));

			if (tabNumber == 0) {

				ExtractInstructions.loadIndicatorCSS = Global.loadIndicatorCSS;

				if (new FSTIframe(browser).hasIframe()) {
					new FSTIframe(browser).switchToFSTIframe("TRUE");
					ExtractInstructions.controlInIframe = true;
					ExtractInstructions.loadIndicatorCSS = "";
				}

			} else
				ExtractInstructions.loadIndicatorCSS = "";

			return true;
		}
		return false;
	}

	/**
	 * 
	 */
	@Override
	public boolean savePdf(String fileName, String subDirectory) throws TestAutomationException {
		// Check the current page is a pdf
		String url = browser.getCurrentUrl();

		if (!url.toLowerCase().contains(".pdf")) {
			// Sometimes we get an encrypted url, so we have to extract the
			// filename from the httpClient response.
			FileProcessor fileProcessor = new FileProcessor(browser, testContext);
			CloseableHttpResponse response = fileProcessor.httpGet(url);

			if (!fileProcessor.isPdf(response)) {
				throw new TestAutomationException(ExceptionErrorCodes.TA097_WEB_ELEMENT_PROCESSOR_FILE_IS_NOT_A_PDF,
						url + " is not a PDF.");
			}
		}

		if (!fileName.endsWith(".pdf")) {
			fileName = fileName + ".pdf";
		}

		// Set up directory name where pdf will be saved
		String pdfSavePath = Global.getTestLabPath() + "/Downloads/";
		if (StringUtils.isNotBlank(subDirectory)) {
			if (subDirectory.startsWith("/") || subDirectory.startsWith("\\")) {
				subDirectory = subDirectory.substring(1);
			}
			if (!subDirectory.endsWith("/") && !subDirectory.endsWith("\\")) {
				subDirectory = subDirectory + "/";
			}
			pdfSavePath = pdfSavePath + subDirectory;
		}

		// Create output directory
		File screenShotDir = new File(pdfSavePath);
		if (!screenShotDir.exists()) {
			screenShotDir.mkdirs();
		}

		String filePath = pdfSavePath + fileName;

		// Download the pdf
		if (new FileProcessor(browser, testContext).downloadFile(url, filePath)) {
			logger.info("Pdf successfully saved to " + filePath);
			return true;
		} else {
			throw new TestAutomationException(ExceptionErrorCodes.TA098_WEB_ELEMENT_PROCESSOR_PDF_NOT_DOWNLOADED, url
					+ " not downloaded to " + filePath);
		}
	}

	public boolean navigateTo(String navigationPath, String index) throws TestAutomationException {
		return new TopNavigation(browser, testContext, navigationPath, retrieveIndex(index)).navigate();
	}

	public boolean openAccordion(String accordionName) throws TestAutomationException {
		return new Accordion(browser, accordionName).open();
	}

	public boolean clickOnButton(String buttonName, String index) throws TestAutomationException {

		return new Button(browser, testContext, buttonName, retrieveIndex(index)).clickOnButton();

	}

	public boolean clickOnFSTButton(String buttonName, String index) throws TestAutomationException {

		return new Button(browser, testContext, buttonName, retrieveIndex(index)).clickOnFSTButton();
	}

	public boolean selectFSTProfile(String profileName) throws TestAutomationException {

		return new FSTProfileTable(browser).selectFSTProfile(profileName);

	}

	public boolean selectFSTFund(String fundName, String booleanValue) throws TestAutomationException {
		this.wait(new String("30"));
		return new FSTFundTable(browser, fundName).selectFSTFund(booleanValue);

	}

	public boolean insetFSTFundPercentage(String fundName, String percentage) throws TestAutomationException {

		return new FSTFundTable(browser).insetFSTFundPercentage(fundName, percentage);

	}

	/**
	 * @throws TestAutomationException
	 * @see com.lifelens.browsers.IBrowser#clickOnId(java.lang.String)
	 */
	@Override
	public boolean clickOnId(String id) throws TestAutomationException {
		WebElement webElement = WebElementRetriever.getWebElementById(browser, id, id);
		if (webElement != null) {
			webElement.click();
			return true;
		}
		return false;
	}

	@Override
	public boolean clickOnNavigationLink(String navigationLinkName, String index) throws TestAutomationException {
		return new Hyperlink(browser, navigationLinkName, retrieveIndex(index), testContext)
				.clickOnlinkByStyleClass(Global.navigationLink);
	}

	/**
	 * @throws TestAutomationException
	 * @see com.lifelens.browsers.IBrowser#clickOnTab(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public boolean clickOnTab(String TabName, String index) throws TestAutomationException {
		return new Hyperlink(browser, TabName, retrieveIndex(index), testContext)
				.clickOnlinkByStyleClass(Global.tabName);
	}

	/**
	 * @throws TestAutomationException
	 * @see com.lifelens.browsers.IBrowser#clickOnDownload(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public boolean clickOnDownload(String buttonLabel, String outputFilePath) throws TestAutomationException {
		return new Button(browser, testContext, buttonLabel, "1").clickOnDownloadButton(outputFilePath);
	}

	public boolean verifyDownloadFilename(String buttonLabel, String expectedFilename) throws TestAutomationException {
		return new Button(browser, testContext, buttonLabel, "1").verifyDownloadFilename(expectedFilename);
	}

	/**
	 * @throws TestAutomationException
	 */
	@Override
	public boolean clickOnListBoxOption(String optionText, String index) throws TestAutomationException {
		List<WebElement> options = WebElementRetriever.getWebElementsByXPath(browser, optionText,
				"//select/option[text()='" + optionText + "']");
		WebElement option = null;
		if (StringUtils.isBlank(index) || index == "0") {
			index = "1";
		}

		try {
			option = options.get(Integer.parseInt(index) - 1);
		} catch (NumberFormatException e) {
			throw new TestAutomationException(
					ExceptionErrorCodes.TA055_WEB_ELEMENT_PROCESSING_COMMON_INVALID_INPUT_INDEX_FOR_SELECT_ERROR,
					"Invalid input parameter passed to select option text", e);
		}

		if (option != null) {
			option.click();
			return true;
		} else {
			throw new TestAutomationException(
					ExceptionErrorCodes.TA056_WEB_ELEMENT_PROCESSING_COMMON_OPTION_NOT_AVAILABLE_FOR_SELECT_ERROR,
					"Invalid input parameter passed to select option text");
		}
	}

	/**
	 * @throws TestAutomationException
	 * @see com.lifelens.browsers.IBrowser#selectDropdownValue(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public boolean downloadLogFile(String downloadLinkCell, String outputFilePath) throws TestAutomationException {
		return new FileProcessor(browser, testContext).downloadLogFile(downloadLinkCell, outputFilePath);
	}

	@Override
	public boolean takeScreenshot(String fileName, String subDirectory) throws TestAutomationException {
		return new FileProcessor(browser, testContext).takeScreenshot(fileName, subDirectory, testContext.getTestSet()
				.getWorkbookName());
	}

	/**
	 * @throws TestAutomationException
	 * @throws IOException
	 * @throws InvalidFormatException
	 *             ,IOException
	 * @see com.lifelens.automation.tabulardata.Comparison#compare(TabularData,
	 *      TabularData, boolean)
	 */
	@Override
	public boolean compareFiles(String expectedBookAndOrSheetName, String actualCsvFilename)
			throws TestAutomationException, IOException {
		String expectedBookName, expectedSheetName, isCriteriaBasedReport = null;
		ArgList arglist = new ArgList(expectedBookAndOrSheetName);
		TestSet testSet = testContext.getTestSet();
		// get expected worksheet and workbook
		if (arglist.getParmetersCount() > 2) {
			expectedBookName = testSet.getWorkbookNameAbsolutePath();
			expectedSheetName = arglist.getParameter(1);
			isCriteriaBasedReport = arglist.getParameter(2);
		}
		if (arglist.getParmetersCount() == 2 && arglist.getParameter(1).equalsIgnoreCase("True")) {
			expectedBookName = testSet.getWorkbookNameAbsolutePath();
			expectedSheetName = arglist.getParameter(0);
			isCriteriaBasedReport = arglist.getParameter(1);
		} else {
			if (expectedBookAndOrSheetName.contains(GlobalCommonConstants.EXCELEXTENSION)) {
				expectedBookName = testSet.getWorkbookLocation() + "\\" + arglist.getParameter(0);
			} else {
				expectedBookName = testSet.getWorkbookLocation() + "\\" + testSet.getWorkbookName();
			}
			if (arglist.getParmetersCount() > 1) {
				expectedSheetName = arglist.getParameter(1);
			} else {
				expectedSheetName = arglist.getParameter(0);
			}
		}
		// remove any whitespace
		expectedBookName = expectedBookName.trim();
		expectedSheetName = expectedSheetName.trim();

		File workBook = new File(expectedBookName);
		if (!workBook.exists()) {
			throw new TestAutomationException(ExceptionErrorCodes.TA125_TEST_FILE_NOT_FOUND,
					" file compare expected output workbook not found: " + expectedBookName);
		}

		if (actualCsvFilename.contains(GlobalCommonConstants.CSVEXTENSION)) {
			return new FileProcessor(browser, testContext).compareFiles(testSet, workBook, expectedSheetName,
					isCriteriaBasedReport, actualCsvFilename);
		} else {
			String convertedCSVFile = FileBuilder.txtToCSVConversion(actualCsvFilename);
			return new FileProcessor(browser, testContext).compareFiles(testSet, workBook, expectedSheetName,
					isCriteriaBasedReport, convertedCSVFile);
		}
	}

	/**
	 * This is to verify the value inside the table.
	 * 
	 * @param cell
	 *            - it is the position of the table cell to be verified. E.g.
	 *            (1,2) It refers to the cell , 1nd row > 2nd column
	 * @param textToVerify
	 *            the text to be verified in the above cell
	 * @throws TestAutomationException
	 */
	public boolean verifyExcelText(String sheetAndCell, String textToVerify) throws TestAutomationException {
		ArgList arglist = new ArgList(sheetAndCell);
		String workbookName = null;
		String worksheetName = null;
		int row;
		int col;

		TestSet testSet = testContext.getTestSet();

		if (arglist.getParmetersCount() == 3) {
			// User has specified only worksheet, row, column
			workbookName = testSet.getWorkbookNameAbsolutePath();
			worksheetName = arglist.getParameter(0);
			row = arglist.getNumericParameter(1);
			col = arglist.getNumericParameter(2);
		} else {
			// User has specified workbook, worksheet, row, column
			workbookName = testSet.getWorkbookLocation() + File.separator + arglist.getParameter(0);
			worksheetName = arglist.getParameter(1);
			row = arglist.getNumericParameter(2);
			col = arglist.getNumericParameter(3);
		}

		File workBook = new File(workbookName);
		return new FileProcessor(browser, testContext).verifyExcelText(testSet, workBook, worksheetName, row, col,
				textToVerify);
	}

	public boolean verifyText(String textToVerify, String existsIndicator) throws TestAutomationException {
		String resolvedText;
		if (textToVerify.contains(Global.parameterIdentifier)) {
			resolvedText = testContext.resolveParameters(textToVerify);
		} else {
			resolvedText = textToVerify;
		}

		String bodyText = new HtmlParser(WebElementRetriever.getWebElementByTagName(browser, textToVerify,
				GlobalTags.BODY)).getinnerText();

		if (bodyText.contains(resolvedText)) {
			logger.info("String found!!" + resolvedText);
			return true;
		} else if (existsIndicator.equalsIgnoreCase("False")) {
			return true;
		}
		throw new TestAutomationException(
				ExceptionErrorCodes.TA047_WEB_ELEMENT_PROCESSOR_ELEMENT_WITH_TEXT_NOT_FOUND_ERROR,
				" Web elements with name '" + resolvedText + "' not found.");

	}

	/**
	 * browser is to verify the value inside the table.
	 * 
	 * @param cell
	 *            - it is the position of the table cell to be verified. Eg.
	 *            (1,2,3) It refers to the cell , 1st table (in the active page)
	 *            > 2nd row > 3rd column
	 * @param textToVerify
	 *            It is the text to be verified in the above cell
	 * @throws TestAutomationException
	 * @throws Exception
	 */
	public boolean verifyTableCellValue(String cell, String textToVerify) throws TestAutomationException {
		boolean result = false;

		try {
			result = new Table(browser, testContext, new ArgList(cell).getNumericParameter(0)).verifyTableCellValue(
					cell, textToVerify);
		} catch (TestAutomationException te) {
			result = false;
			throw te;
		}

		return result;
	}

	public boolean verifyDropdownValues(String dropdownLabel, String drodownValuesToVerify)
			throws TestAutomationException {
		return new Dropdown(browser, dropdownLabel, testContext).verifyDropdownValues(drodownValuesToVerify);
	}

	public boolean selectFundPercentageFromDropdown(String fundName, String dropdownValue)
			throws TestAutomationException {

		return new Dropdown(browser, testContext).selectFundPercentageFromDropdown(fundName, dropdownValue);

	}

	/**
	 * Verify if the specified Button occurrence exists.
	 * 
	 * @param buttonNameAndExistsIndicator
	 *            Button label name and optional "false" to check that button
	 *            does not exist.
	 * @param indexAndEnabled
	 *            Optional index number of button, and optional
	 *            'enabled'/'disabled'
	 * @return boolean Success or Failure
	 * @throws TestAutomationException
	 */
	public boolean verifyButton(String buttonNameAndExistsIndicator, String indexAndEnabled)
			throws TestAutomationException {
		String existsIndicator = "True";
		String index = "1";
		String enabledIndicator = null;

		// Parameter 1
		ArgList params = new ArgList(buttonNameAndExistsIndicator);
		String buttonName = params.getParameter(0);
		if (params.getParmetersCount() == 2) {
			existsIndicator = params.getParameter(1);
		}

		// Parameter 2
		params = new ArgList(indexAndEnabled);
		if (params.getParmetersCount() == 1) {
			if (params.getParameter(0).equalsIgnoreCase("Enabled")
					|| params.getParameter(0).equalsIgnoreCase("Disabled")) {
				enabledIndicator = params.getParameter(0);
			} else {
				index = params.getParameter(0);
			}
		}
		if (params.getParmetersCount() == 2) {
			if (params.getParameter(0).equalsIgnoreCase("Enabled")
					|| params.getParameter(0).equalsIgnoreCase("Disabled")) {
				enabledIndicator = params.getParameter(0);
				index = params.getParameter(1);
			} else {
				enabledIndicator = params.getParameter(1);
				index = params.getParameter(0);
			}
		}

		return new Button(browser, testContext, buttonName, retrieveIndex(index)).verifyButton(existsIndicator,
				enabledIndicator);
	}

	/**
	 * Verify if the specified Checkbox exists. If parameter 2 is set to
	 * true/false it will additionally check that the checkbox value is
	 * ticked/unticked. Or if set to enabled/disabled it will check that the
	 * checkbox is enabled/disabled. If blank it simply verifies that the
	 * checkbox exists regardless of its value.
	 * 
	 * @param checkboxNameAndExistsIndicator
	 *            Checkbox label name and optional "false" to check that button
	 *            does not exist.
	 * @param checkboxTicked
	 *            "true", "false", "enabled", "disabled" or blank
	 * @return boolean Success or Failure
	 * @throws TestAutomationException
	 */
	public boolean verifyCheckbox(String checkboxNameAndExistsIndicator, String checkboxTicked)
			throws TestAutomationException {
		String existsIndicator = "True";
		String buttonName;

		ArgList params = new ArgList(checkboxNameAndExistsIndicator);
		if (params.hasTableKeyword() || params.hasDecisionTableKeyword()) {
			buttonName = params.getArglistString();
			if (params.getParmetersCount() == 1) {
				existsIndicator = params.getParameter(0);
			}
		} else {
			buttonName = checkboxNameAndExistsIndicator;
			if ((checkboxNameAndExistsIndicator.contains("False") || checkboxNameAndExistsIndicator.contains("false"))
					&& params.getParmetersCount() == 2) {
				existsIndicator = params.getParameter(1);
			}
		}

		return new CheckBox(browser, testContext, buttonName, existsIndicator).verifyCheckbox(existsIndicator,
				checkboxTicked);
		// return new CheckBox(browser,
		// checkboxNameAndExistsIndicator).verifyCheckbox(checkboxTicked);
	}

	/**
	 * This method takes input in two formats and based on the operation it
	 * checks if the text is found or not.
	 * 
	 * @param argumentList
	 * @param textToVerifyParam
	 * @return boolean
	 * @throws TestAutomationException
	 */
	public boolean verifyElement(String argumentList, String textToVerifyParam) throws TestAutomationException {

		return new VerifyText(browser).verify(argumentList, textToVerifyParam);
	}

	/**
	 * Verifies whether the dropdown value is associated with the dropdown label
	 * name.
	 * 
	 * @param labelName
	 * @param selectValue
	 * @return boolean
	 * @throws TestAutomationException
	 */
	public boolean verifySelect(String labelName, String selectValue) throws TestAutomationException {
		String labelValue = new HtmlParser(WebElementRetriever.getWebElementByLabelName(browser, labelName))
				.getinnerText().trim();
		if (labelValue.isEmpty())
			labelValue = WebElementRetriever.getWebElementByLabelName(browser, labelName).getAttribute("value");

		return labelValue.equals(selectValue);
	}

	/**
	 * Expands the child panel
	 * 
	 * @param headerText
	 * @return boolean
	 * @throws TestAutomationException
	 */
	@Override
	public boolean expandPanel(String headerText) throws TestAutomationException {
		return new AdminPanel(browser, headerText).expandPanel();
	}

	/**
	 * Collapse the child panel
	 * 
	 * @param headerText
	 * @return boolean
	 * @throws TestAutomationException
	 */
	@Override
	public boolean collapsePanel(String headerText) throws TestAutomationException {
		return new AdminPanel(browser, headerText).collapsePanel();
	}

	/**
	 * Click's on edit button if panel is editable
	 * 
	 * @param headerText
	 * @return boolean
	 */
	public boolean editPanel(String headerText) throws TestAutomationException {
		return new AdminPanel(browser, headerText).clickOnEditButton();
	}

	/**
	 * Toggle between Fund select Tool iframe and lifelens webpage
	 * 
	 * @param iframeTitle
	 * @param action
	 * @return
	 * @throws TestAutomationException
	 */
	public boolean switchToIframe(String iframeTitle, String action) throws TestAutomationException {

		return new FSTIframe(browser, testContext, iframeTitle).switchToFSTIframe(action);

	}

	/**
	 * Click's on add button
	 * 
	 * @param headerText
	 * @return boolean
	 */
	public boolean addPanel(String headerText) throws TestAutomationException {
		return new AdminPanel(browser, headerText).clickOnAddButton();
	}

	/**
	 * Click's on delete button
	 * 
	 * @param headerText
	 * @return boolean
	 */
	public boolean deletePanel(String headerText) throws TestAutomationException {
		return new AdminPanel(browser, headerText).clickOnDeleteButton();
	}

	/**
	 * Verifies the default value in input filed
	 * 
	 * @param LabelName
	 * @param InputValue
	 * @return
	 * @throws TestAutomationException
	 */
	@Override
	public boolean verifyInputField(String labelName, String inputValue) throws TestAutomationException {
		return new InputTextbox(browser, labelName, testContext).verifyDefaultValue(inputValue);
	}

	/**
	 * Verifies the radio button selection
	 * 
	 * @param label
	 * @param booleanValue
	 * @return
	 * @throws TestAutomationException
	 * 
	 */
	@Override
	public boolean verifyRadioButton(String label, String booleanValue) throws TestAutomationException {
		return new RadioButton(browser, testContext, label).verifyRadioButton(booleanValue);
	}

	/**
	 * Verifies the passed value is default value for the drop down or not
	 * 
	 * @param dropdownLabel
	 * @param dropdownValue
	 * @return
	 * @throws TestAutomationException
	 */
	@Override
	public boolean verifyDropDownDefaultValue(String dropdownLabel, String dropdownValue)
			throws TestAutomationException {
		return new Dropdown(browser, dropdownLabel, testContext).verifyDropDownDefaultValue(dropdownValue);
	}

	/**
	 * insert FST profile percentage in checkout table
	 * 
	 * @param ProfileName
	 * @param percentage
	 * @return
	 * @throws TestAutomationException
	 */
	@Override
	public boolean insetFSTProfilePercentage(String profileName, String percentage) throws TestAutomationException {
		return new FSTProfileTable(browser).insetFSTProfilePercentage(profileName, percentage);
	}

	@Override
	public boolean wait(String inMilliSeconds) {

		try {
			Thread.sleep(Integer.parseInt(inMilliSeconds));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return false;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * 
	 * To move available value from swim lane to selected list
	 * 
	 * @param benefitName
	 * @param containerName
	 * 
	 * @throws TestAutomationException
	 * 
	 * @see com.lifelens.browsers.OpenBrowser#clickOnDownload(java.lang.String,
	 *      java.lang.String)
	 */

	@Override
	public boolean selectAndMoveOptionToListbox(String option, String containerName) throws TestAutomationException {

		return new SwimLane(browser, testContext, containerName).selectAndMoveOptionToListBox(option);

	}

	/**
	 * 
	 * To move selected value from swim lane to available list
	 * 
	 * @param benefitName
	 * @param containerName
	 * @throws TestAutomationException
	 * 
	 * @see com.lifelens.browsers.OpenBrowser#clickOnDownload(java.lang.String,
	 *      java.lang.String)
	 */

	@Override
	public boolean selectAndMoveOptionFromListbox(String optionName, String containerName)
			throws TestAutomationException {

		return new SwimLane(browser, testContext, containerName).selectAndMoveOptionFromListbox(optionName);

	}

	/**
	 * This is to verify the value inside the table.
	 * 
	 * @param cell
	 *            - it is the position of the table cell to be verified. E.g.
	 *            (1,2) It refers to the cell , 1nd row > 2nd column
	 * @param textToVerify
	 *            the text to be verified in the above cell
	 * @throws TestAutomationException
	 * @throws IOException
	 */
	public boolean verifyCsvText(String fileName, String textToVerify) throws TestAutomationException {
		return new FileProcessor(browser, testContext).verifyCsvText(fileName, textToVerify);
	}

	/**
	 * Clicks on the button corresponding to the benefit in the Enrolment form.
	 * 
	 * @param benefitName
	 * @return
	 * @throws TestAutomationException
	 */
	public boolean clickEnrolmentFormBenefitButton(String benefitName) throws TestAutomationException {
		return new EnrolmentForm(browser, benefitName).clickEnrolmentFormBenefitButton();
	}

	/**
	 * Clicks on the edit button corresponding to the default header.
	 * 
	 * @param defaultHeader
	 * @return
	 * @throws TestAutomationException
	 */
	public boolean clickColumnCustomisationEditButton(String defaultHeader) throws TestAutomationException {
		return new ColumnCustomisation(browser, testContext, defaultHeader).clickColumnCustomisationEditButton();
	}

	/**
	 * Change password of employees whose staffnumber is available in the given
	 * testdata sheet
	 * 
	 * @return boolean
	 * @throws TestAutomationException
	 */
	public boolean changePassword(String sheetName, String labelNames) throws TestAutomationException {
		return new Password(browser, testContext, sheetName, labelNames).changePassword();
	}

	/**
	 * Fills current hour and minute in scheduled import Start time field
	 * 
	 * @param spinnerName
	 * @param spinValue
	 * @return
	 * @throws TestAutomationException
	 */
	@Override
	public boolean spin(String spinnerName) throws TestAutomationException {
		return new Spinner(browser, spinnerName).sendKeys();

	}

	/**
	 * Verifies whether the the provided swimlane option is available in the
	 * list or not
	 * 
	 * @param option
	 * @param containerName
	 * @return
	 * @throws TestAutomationException
	 */
	@Override
	public boolean verifySwimlaneOption(String swimlaneOption, String containerName) throws TestAutomationException {
		return new SwimLane(browser, testContext, containerName).verifySwimlaneOption(swimlaneOption);
	}

	/**
	 * Verify if the specified FST Button occurrence exists or not.
	 * 
	 * @param buttonNameAndExistsIndicator
	 *            : Button label name and optional "false" to check that button
	 *            does not exist.
	 * 
	 * @param indexAndVisibilityMode
	 *            : Optional index number of button, and optional
	 *            'enabled'/'disabled'
	 * 
	 * @return boolean Success or Failure
	 * 
	 * @throws TestAutomationException
	 */
	public boolean verifyFSTButton(String buttonNameAndExistsIndicator, String indexAndVisibilityMode)
			throws TestAutomationException {

		String existsIndicator = "True";
		String index = "1";
		String enabledIndicator = null;

		// Parameter 1
		ArgList params = new ArgList(buttonNameAndExistsIndicator);
		String buttonName = params.getParameter(0);
		if (params.getParmetersCount() == 2) {
			existsIndicator = params.getParameter(1);
		}

		// Parameter 2
		params = new ArgList(indexAndVisibilityMode);
		if (params.getParmetersCount() == 1) {
			if (params.getParameter(0).equalsIgnoreCase("Enabled")
					|| params.getParameter(0).equalsIgnoreCase("Disabled")) {
				enabledIndicator = params.getParameter(0);
			} else {
				index = params.getParameter(0);
			}
		}
		if (params.getParmetersCount() == 2) {
			if (params.getParameter(0).equalsIgnoreCase("Enabled")
					|| params.getParameter(0).equalsIgnoreCase("Disabled")) {
				enabledIndicator = params.getParameter(0);
				index = params.getParameter(1);
			} else {
				enabledIndicator = params.getParameter(1);
				index = params.getParameter(0);
			}
		}
		return new Button(browser, testContext, buttonName, retrieveIndex(index)).verifyFSTButton(existsIndicator,
				enabledIndicator);
	}

	private String retrieveIndex(String index) throws TestAutomationException {
		try {
			return (StringUtils.isNotBlank(index) && (Integer.parseInt(index) > 0) ? index : "1");
		} catch (NumberFormatException ex) {
			throw new TestAutomationException(
					ExceptionErrorCodes.TA026_WEB_ELEMENT_PROCESSING_COMMON_INVALID_BUTTON_INDEX_ERROR, ex.getMessage()
							+ " : Invalid button index passed", ex);
		}
	}
}
