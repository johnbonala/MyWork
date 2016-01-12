package com.lifelens.testset;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.lifelens.exceptions.TestAutomationException;

public interface IWorkBook {

	public String[][] getDataProviderObject();

	public void loadSheetNames();

	public void getIndexTestCaseDetails() throws TestAutomationException;

	public boolean indexSheetFound();

	public void validateIndexSheet();

	public void updateTestStatusInIndex(boolean testCasePassed);

	public XSSFWorkbook getWorkbook();

	public TestCase getTestCase();

	public void setTestCase(String testsheetName) throws TestAutomationException;

}
