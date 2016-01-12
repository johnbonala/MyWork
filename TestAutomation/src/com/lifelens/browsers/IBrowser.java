package com.lifelens.browsers;

import java.io.IOException;

import com.lifelens.automation.tabulardata.TabularData;
import com.lifelens.exceptions.TestAutomationException;

/**
 * 
 * Interface for all the web browsers Any new browser that need to be supported
 * by this framework should implement this interface and extend its native
 * webdriver and corresponding drivers should be placed in the drivers folder
 * 
 * @author Srinivas Pasupulati(co48633)
 * 
 * @version 1.2
 * 
 */

public interface IBrowser {

	/**
	 * Open/navigate to the url supplied
	 * 
	 * @param url
	 *            the url to be opened/navigated
	 * 
	 */
	public boolean geturl(String url) throws TestAutomationException;

	/**
	 * Insert the value in the 'input text box' corresponds to the label name
	 * supplied.
	 * 
	 * @param LabelName
	 *            LabelName of the input text box
	 * @param InputValue
	 *            The value to be inserted in the input textbox corresponds to
	 *            the label name.
	 */
	public boolean input(String LabelName, String InputValue) throws TestAutomationException;

	/**
	 * Insert the value in the 'file input text box' corresponds to the label
	 * name supplied.
	 * 
	 * @param LabelName
	 *            LabelName of the input text box
	 * @param InputValue
	 *            The value to be inserted in the file input textbox corresponds
	 *            to the label name. This must be a real file
	 */
	public boolean inputFile(String LabelName, String InputValue) throws TestAutomationException;

	/**
	 * Click on the element with the supplied Id
	 * 
	 * @param id
	 *            the id of the element to be clicked
	 */
	public boolean clickOnId(String id) throws TestAutomationException;

	/**
	 * Click on the Navigation link supplied
	 * 
	 * @param NavavigationLinkName
	 * 
	 */
	public boolean clickOnNavigationLink(String NavavigationLinkName, String index) throws TestAutomationException;

	/**
	 * Click on the Tab supplied
	 * 
	 * @param tabName
	 * 
	 */

	public boolean clickOnTab(String tabName, String index) throws TestAutomationException;

	/**
	 * This method is designed to work with the Reports download button. This
	 * does not have a 'href' attribute, but instead the href is embedded in the
	 * 'onclick' attribute.
	 * 
	 * @param buttonLabel
	 *            the label of the download button
	 * @param outputFilePath
	 *            Either specify just the filename, a partial file path with
	 *            filename (e.g. "my test files/my file.csv" or a full file
	 *            path. If a filename only or a partial file path is specified
	 *            it is saved in a /Downloads/ folder under the current working
	 *            folder.
	 * @return boolean success/failure
	 */
	public boolean clickOnDownload(String buttonLabel, String outputFilePath) throws TestAutomationException;

	/**
	 * Click on an option in a List Box.
	 * 
	 * @param optionText
	 *            Text of the option to select
	 * @param index
	 *            Occurrence number of the option, where 1 is the first.
	 *            Defaults to 1 if not supplied.
	 * @return boolean success/failure
	 */
	public boolean clickOnListBoxOption(String optionText, String index) throws TestAutomationException;

	/**
	 * Select the given value from the drop-down list with the given label
	 * 
	 * @param dropdownLabel
	 *            the label of the drop-down list
	 * @param dropdownValue
	 *            the value to choose in the drop-down list
	 */
	public boolean selectDropdownValue(String dropdownLabel, String dropdownValue) throws TestAutomationException;

	/**
	 * Selects the date provided from calendar
	 * 
	 * @param calendarLabel
	 * @param ddmmyyyyFormat
	 * @return
	 * @throws TestAutomationException
	 */
	public boolean selectDateFromCalendar(String calendarLabel, String ddmmyyyyFormat) throws TestAutomationException;

	/**
	 * Select radio button corresponds to the label name supplied
	 * 
	 * @param RadioButtonLabel
	 *            Radio button label name
	 */
	public boolean selectRadioButton(String RadioButtonLabel) throws TestAutomationException;

	/**
	 * Selects the Check box
	 * 
	 * @param label
	 *            Label name of the checkbox
	 */
	public boolean selectCheckBox(String label, String booleanValue) throws TestAutomationException;

	/**
	 * Download a log from the Admin > Log Files screen. This method finds the
	 * row which matches the jobName passed in, and downloads the log file to
	 * the file path specified.
	 * 
	 * @param jobName
	 *            Job name to match in the table of log files
	 * @param outputFilePath
	 *            Either specify just the filename, a partial file path with
	 *            filename (e.g. "my test files/my file.csv" or a full file
	 *            path. If a filename only or a partial file path is specified
	 *            it is saved in a /Downloads/ folder under the current working
	 *            folder.
	 * @return boolean success/failure
	 */
	public boolean downloadLogFile(String jobName, String outputFilePath) throws TestAutomationException;

	/**
	 * Take a screenshot of the active webpage. Saved in a /Screenshots/ folder
	 * under the current working folder.
	 * 
	 * @param fileName
	 *            File Name with which screen shot should be saved.
	 * @param subDirectory
	 *            Optional subdirectory under the standard /Screenshots/
	 *            directory.
	 */
	public boolean takeScreenshot(String fileName, String subDirectory) throws TestAutomationException;

	/**
	 * Navigates to the path provided Eg. Admin > Manage Employee
	 * 
	 * @param NavigationPath
	 *            Navigation path
	 */
	public boolean navigateTo(String NavigationPath, String index) throws TestAutomationException;

	/**
	 * Opens the Accordion
	 * 
	 * @param AccordionName
	 *            Accordion Name
	 */
	public boolean openAccordion(String AccordionName) throws TestAutomationException;

	/**
	 * Verify the text whether it exist or not.
	 * 
	 * @param textToVerify
	 *            Text that need to be verified
	 * 
	 * @param existsIndicator
	 *            test exists or not (True\False)
	 */
	public boolean verifyText(String textToVerify, String existsIndicator) throws TestAutomationException;

	/**
	 * Compares an expected .xlsx worksheet with an actual .csv file
	 * 
	 * @see com.lifelens.automation.tabulardata.Comparison#compare(TabularData,
	 *      TabularData, boolean)
	 * 
	 * @param expectedBookAndOrSheetName
	 *            worksheet name only (assumes current workbook), or workbook &
	 *            worksheet name separated by a semi-colon
	 * @param actualFilename
	 *            full path & filename of actual csv file
	 * @return boolean true if files match
	 * @throws TestAutomationException
	 *             ,IOException
	 */
	public boolean compareFiles(String expectedBookAndOrSheetName, String actualFilename)
			throws TestAutomationException, IOException;

	/**
	 * Verifies the web table text on the active page
	 * 
	 * @param cell
	 *            cell location eg. (1,2,1) which represents 1st table, 2nd row
	 *            and 1st column.
	 * @param textToVerify
	 *            text to be verified
	 */
	public boolean verifyTableCellValue(String cell, String textToVerify) throws TestAutomationException;

	/**
	 * Verifies FST button text
	 * 
	 * @param buttonNameAndExistsIndicator
	 * @param indexAndVisibilityMode
	 * @return
	 * @throws TestAutomationException
	 */
	public boolean verifyFSTButton(String buttonNameAndExistsIndicator, String indexAndVisibilityMode)
			throws TestAutomationException;

	/**
	 * Click on link present in the active page
	 * 
	 * @param hyperLinkName
	 *            Hyperlink name on which click action to be performed
	 * @return true if the huyperlink present
	 * 
	 */
	public boolean clickOnLink(String hyperLinkName, String index) throws TestAutomationException;

	/**
	 * Click on link present in the active page
	 * 
	 * @param hyperLinkName
	 *            Hyperlink name on which click action to be performed
	 * @return true if the huyperlink present
	 * 
	 */
	public boolean clickOnFactSheetLink(String cell) throws TestAutomationException;

	/**
	 * This is to verify the value inside the table.
	 * 
	 * @param cell
	 *            - it is the position of the table cell to be verified. E.g.
	 *            (1,2) It refers to the cell , 1nd row > 2nd column
	 * @param textToVerify
	 *            the text to be verified in the above cell
	 */
	public boolean verifyExcelText(String cell, String textToVerify) throws TestAutomationException;

	/**
	 * Switches to the given window
	 * 
	 * @param tabNumberParam
	 *            1 for the first window (not 0)
	 * @return true if window is switched
	 */
	public boolean switchWindow(String tabNumberParam) throws TestAutomationException;

	/**
	 * Save the Pdf which is open on the current screen. It returns false if the
	 * current url does not end in '.pdf'
	 * 
	 * @param saveFilePath
	 * @return boolean
	 */
	public boolean savePdf(String fileName, String subDirectory) throws TestAutomationException;

	/**
	 * Expands the child panel
	 * 
	 * @param headerText
	 * @return boolean
	 */
	public boolean expandPanel(String headerText) throws TestAutomationException;

	/**
	 * Collapse the child panel
	 * 
	 * @param headerText
	 * @return boolean
	 */
	public boolean collapsePanel(String headerText) throws TestAutomationException;

	/**
	 * Click's on edit button if panel is editable
	 * 
	 * @param headerText
	 * @return boolean
	 */
	public boolean editPanel(String headerText) throws TestAutomationException;

	/**
	 * Toggle between Fund select Tool iframe and lifelens webpage
	 * 
	 * @param iframeTitle
	 * @param action
	 * @return
	 * @throws TestAutomationException
	 */
	boolean switchToIframe(String iframeTitle, String action) throws TestAutomationException;

	/**
	 * Click's on add button
	 * 
	 * @param headerText
	 * @return boolean
	 */
	public boolean addPanel(String headerText) throws TestAutomationException;

	/**
	 * Click's on delete button
	 * 
	 * @param headerText
	 * @return boolean
	 */
	public boolean deletePanel(String headerText) throws TestAutomationException;

	/**
	 * Verifies the value in input filed
	 * 
	 * @param labelName
	 * @param inputValue
	 * @return
	 * @throws TestAutomationException
	 */
	boolean verifyInputField(String labelName, String inputValue) throws TestAutomationException;

	/**
	 * Verifies the radio button selection
	 * 
	 * @param label
	 * @param booleanValue
	 * @return
	 * @throws TestAutomationException
	 * 
	 */
	public boolean verifyRadioButton(String label, String booleanValue) throws TestAutomationException;

	/**
	 * Verifies the passed value is default value for the drop down or not
	 * 
	 * @param labelName
	 * @param inputValue
	 * @return
	 * @throws TestAutomationException
	 */
	public boolean verifyDropDownDefaultValue(String dropdownLabel, String dropdownValue)
			throws TestAutomationException;

	/**
	 * insert FST profile percentage in checkout table
	 * 
	 * @param ProfileName
	 * @param percentage
	 * @return
	 * @throws TestAutomationException
	 */
	public boolean insetFSTProfilePercentage(String ProfileName, String percentage) throws TestAutomationException;

	// public void click();
	// public void clickOnLink(String LinkName);
	// public void select();
	// public void clickOnButton(String ButtonName, int SequenceNumber);
	// public void clickOnLink(String LinkName, int Sequence);
	// public void clickOnNavigationLink(String NavavigationLinkName, int
	// SequenceNumber);
	// public void clickOnDropdown(String DropdownLabel, int SequenceNumber);
	// public void selectLable(String label);
	// public void selectRadiobutton(String label, int SequenceNumber);
	// public void selectCheckBox(String label, int SequenceNumber);
	// public void selectLable(String label, int SequenceNumber);
	// public void selectPopup();
	// public <T extends WebElement> T clickOnDropdown(String DropdownLabel);
	// public boolean tableDetails(int tableIndex);

	public boolean wait(String inMilliSeconds) throws TestAutomationException;

	/**
	 * 
	 * Move's selected benefit to given container name
	 * 
	 * @param benefitName
	 * @param containerName
	 * @return boolean
	 * @throws TestAutomationException
	 */

	public boolean selectAndMoveOptionToListbox(String benefitName, String containerName)
			throws TestAutomationException;

	/**
	 * 
	 * Move's selected benefit to back to available list
	 * 
	 * @param benefitName
	 * @param containerName
	 * @return boolean
	 * @throws TestAutomationException
	 */

	public boolean selectAndMoveOptionFromListbox(String benefitName, String containerName)
			throws TestAutomationException;

	/**
	 * This is to verify the value inside the table.
	 * 
	 * @param cell
	 *            - it is the position of the table cell to be verified. E.g.
	 *            (1,2) It refers to the cell , 1nd row > 2nd column
	 * @param textToVerify
	 *            the text to be verified in the above cell
	 */
	public boolean verifyCsvText(String fileName, String textToVerify) throws TestAutomationException;

	/**
	 * change password for employees whose staff number is available in testdata
	 * sheet
	 * 
	 * @return boolean
	 * @throws TestAutomationException
	 */
	public boolean changePassword(String sheetName, String labelNames) throws TestAutomationException;

	/**
	 * Fills current hour and minute in scheduled import Start time field
	 * 
	 * @param spinnerName
	 * @param spinValue
	 * @return
	 * @throws TestAutomationException
	 */
	public boolean spin(String spinnerName) throws TestAutomationException;

	/**
	 * Verifies whether the the provided swimlane option is available in the
	 * list or not
	 * 
	 * @param option
	 * @param containerName
	 * @return
	 * @throws TestAutomationException
	 */
	public boolean verifySwimlaneOption(String option, String containerName) throws TestAutomationException;

	/**
	 * Clicks on the edit button corresponding to the default header.
	 * 
	 * @param defaultHeader
	 * @return
	 * @throws TestAutomationException
	 */
	public boolean clickColumnCustomisationEditButton(String defaultHeader) throws TestAutomationException;

}
