package com.lifelens.interpreter;

public enum Keyword {
	LOGIN_TO_URL("geturl", "TYPE1"),

	INPUT("input", "TYPE2"),

	INPUT_FILE("inputFile", "TYPE2"),

	CLICK_ON_BUTTON("clickOnButton", "TYPE2"),

	NAVIGATE_TO("navigateTo", "TYPE2"),

	SELECT_VALUE_FROM_DROPDOWN("selectDropdownValue", "TYPE2"),

	SELECT_RADIO_BUTTON("selectRadioButton", "TYPE1"),

	TAKE_SCREENSHOT("takeScreenshot", "TYPE2"),

	VERIFY_TEXT("verifyText", "TYPE2"),

	VERIFY_TABLE_TEXT("verifyTableCellValue", "TYPE2"),

	CLICK_ON_NAVIGATION_LINK("clickOnNavigationLink", "TYPE2"),

	CLICK_ON_TAB("clickOnTab", "TYPE2"),

	SELECT_CHECKBOX("selectCheckBox", "TYPE2"),

	OPEN_ACCORDION("openAccordion", "TYPE1"),

	CLICK_ON_ID("clickOnId", "TYPE1"),

	RUN_TC("runTC", "TYPE1"),

	DOWNLOAD_LOG_FILE("downloadLogFile", "TYPE2"),

	CLICK_ON_DOWNLOAD("clickOnDownload", "TYPE2"),

	COMPARE_FILES("compareFiles", "TYPE2"),

	CLICK_ON_FST_BUTTON("clickOnFSTButton", "TYPE2"),

	SELECT_FST_PROFILE("selectFSTProfile", "TYPE1"),

	SELECT_FST_FUND("selectFSTFund", "TYPE2"),

	INSERT_FST_FUND_PERCENTAGE("insetFSTFundPercentage", "TYPE2"),

	VERIFY_EXCEL_TEXT("verifyExcelText", "TYPE2"),

	SELECT_FUND_PERCENTAGE_FROM_DROPDOWN("selectFundPercentageFromDropdown", "TYPE2"),

	CLICK_ON_LINK("clickOnLink", "TYPE2"),

	VERIFY_BUTTON("verifyButton", "TYPE2"),

	SAVE_PDF("savePdf", "TYPE2"),

	CLICK_ON_LISTBOX_OPTION("clickOnListBoxOption", "TYPE2"),

	SWITCH_WINDOW("switchWindow", "TYPE1"),

	VERIFY_DROPDOWN_VALUES("verifyDropdownValues", "TYPE2"),

	VERIFY_CHECKBOX("verifyCheckbox", "TYPE2"),

	VERIFY_SELECT("verifySelect", "TYPE2"),

	VERIFY_ELEMENT("verifyElement", "TYPE2"),

	CLICK_ON_FACT_SHEET_LINK("clickOnFactSheetLink", "TYPE1"),

	COLLAPSE_PANEL("collapsePanel", "TYPE1"),

	EXPAND_PANEL("expandPanel", "TYPE1"),

	EDIT_PANEL("editPanel", "TYPE1"),

	SWITCH_TO_IFRAME("switchToIframe", "TYPE2"),

	ADD("addPanel", "TYPE1"),

	DELETE("deletePanel", "TYPE1"),

	VERIFY_INPUT_FIELD("verifyInputField", "TYPE2"),

	VERIFY_RADIO_BUTTON("verifyRadioButton", "TYPE2"),

	VERIFY_DROPDOWN_DEFAULT_VALUE("verifyDropDownDefaultValue", "TYPE2"),

	VERIFY_DOWNLOAD_FILENAME("verifyDownloadFilename", "TYPE2"),

	WAIT("wait", "TYPE1"),

	INSERT_FST_PROFILE_PERCENTAGE("insetFSTProfilePercentage", "TYPE2"),

	SELECT_AND_MOVE_OPTION_FROM_LISTBOX("selectAndMoveOptionFromListbox", "TYPE2"),

	SELECT_AND_MOVE_OPTION_TO_LISTBOX("selectAndMoveOptionToListbox", "TYPE2"),

	VERIFY_CSV_TEXT("verifyCsvText", "TYPE2"),

	CLICK_ENROLMENT_FORM_BENEFIT_BUTTON("clickEnrolmentFormBenefitButton", "TYPE1"),

	CLICK_COLUMN_CUSTOMISATION_EDIT_BUTTON("clickColumnCustomisationEditButton", "TYPE1"),

	CHANGE_PASSWORD("changePassword", "TYPE2"),

	SPIN("spin", "TYPE1"),

	VERIFY_SWIMLANE_OPTION("verifySwimlaneOption", "TYPE2"),

	VERIFY_ON_FST_BUTTON("verifyFSTButton", "TYPE2"),

	SELECT_DATE_FROM_CALENDAR("selectDateFromCalendar", "TYPE2");

	private final String methodName;
	private final String type;

	Keyword(String methodName, String type) {
		this.methodName = methodName;
		this.type = type;
	}

	public String getMethodName() {
		return methodName;
	}

	public String getType() {
		return type;
	}

}
