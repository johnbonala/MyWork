package com.lifelens.testset;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public interface IWorkSheet {

	public void applyConditionalFormatting();

	public void updateTestStepResult(boolean returnValue, Row row, long timeTaken);

	public TestCase setForamttingRange(String range);

	public XSSFSheet getWorkSheet();

	public void setTestSheet(XSSFSheet testsheet);

}
