package com.lifelens.interpreter;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.lifelens.globals.Global;
import com.lifelens.globals.GlobalCommonConstants;
import com.lifelens.testset.TestSet;

/**
 * This class is used to hold object of TestSet
 * 
 * @author CO50636
 * 
 */
public class TestContext {

	private TestSet testSet;
	private XSSFWorkbook currentDownloadedFile;
	private boolean isLoadIndicator, isImpersonateScreen;
	private String downLoadedcsvFileName;

	public TestContext(TestSet testset) {
		this.testSet = testset;
	}

	/**
	 * @return the testSet
	 */
	public TestSet getTestSet() {
		return testSet;
	}

	public void setCurrentDownloadedFile(XSSFWorkbook convertedWb) {
		this.currentDownloadedFile = convertedWb;
	}

	/**
	 * @return the currentDownloadedFile
	 */
	public XSSFWorkbook getCurrentDownloadedFile() {
		return currentDownloadedFile;
	}

	/**
	 * @return the isLoadIndicator
	 */
	public void setLoadIndicator(boolean isLoadIndicator) {
		this.isLoadIndicator = isLoadIndicator;
	}

	/**
	 * @return the isLoadIndicator
	 */
	public boolean getLoadIndicator() {
		return isLoadIndicator;
	}

	/**
	 * @return the isImpersonateScreen
	 */
	public void setIsImpersonateScreen(boolean isImpersonateScreen) {
		this.isImpersonateScreen = isImpersonateScreen;
	}

	/**
	 * @return the isImpersonateScreen
	 */
	public boolean isImpersonateScreen() {
		return isImpersonateScreen;
	}

	public void setDownLoadedcsvFileName(String downLoadedcsvFileName) {
		this.downLoadedcsvFileName = downLoadedcsvFileName;
	}

	/**
	 * @return the downLoadedcsvFileName
	 */
	public String getDownLoadedcsvFileName() {
		return downLoadedcsvFileName;
	}

	/**
	 * Split text to verify and resolve for parameters in text to search
	 * 
	 * @param textToVerify
	 *            test to verify
	 * 
	 * @return string resolved string
	 */
	public String resolveParameters(String textToVerify) {
		String seperatedStrings[] = StringUtils.split(textToVerify);
		String resolvedParameters = null, extractParameter = null, newStr = null;

		for (String str : seperatedStrings) {
			try {
				// Separate if any extra character coming with parameter string
				// and resolve the parameter
				if (str.contains(GlobalCommonConstants.DOT)) {
					extractParameter = str.substring(0, str.indexOf(GlobalCommonConstants.DOT));
					str = extractParameter;
					newStr = retriveParamValue(str);
				}
				if (str.contains(GlobalCommonConstants.COLON)) {
					extractParameter = str.substring(0, str.indexOf(GlobalCommonConstants.COLON));
					str = extractParameter;
					newStr = retriveParamValue(str);
				}
				if (str.contains(GlobalCommonConstants.CLOSINGPARENTHESES)) {
					extractParameter = str.substring(0, str.indexOf(GlobalCommonConstants.CLOSINGPARENTHESES));
					str = extractParameter;
					newStr = retriveParamValue(str);
				} else if (str.charAt(0) != '@' && str.contains(Global.parameterIdentifier)) {
					extractParameter = str.substring(str.indexOf(Global.parameterIdentifier), str.length());
					newStr = str.substring(0, str.indexOf(Global.parameterIdentifier))
							+ retriveParamValue(extractParameter);
				} else {
					newStr = retriveParamValue(str);
				}
				if (newStr != null) {
					resolvedParameters = StringUtils.replace(textToVerify, str, newStr);
					break;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (resolvedParameters == null || resolvedParameters == "") {
			return textToVerify;
		}

		if (resolvedParameters.contains(Global.parameterIdentifier)
				&& !resolvedParameters.equalsIgnoreCase(textToVerify)) {
			resolvedParameters = resolveParameters(resolvedParameters);

		}

		return resolvedParameters;
	}

	/**
	 * Get Value of the parameter from testData sheet.
	 * 
	 * @param value
	 * @return String
	 * @throws Exception
	 */
	private String retriveParamValue(String value) throws Exception {
		String str = null;
		if (value.charAt(1) == '@') {
			value = StringUtils.remove(value, value.charAt(0));
		}

		if (isParameter(value)) {
			str = getTestSet().getTestData().getParameterValue(value.trim());
			if (str == null) {
				str = value;
			}
		}

		return str;
	}

	/**
	 * Verifies the ParameterValue exist or not
	 * 
	 * @param cellStringValue
	 * @return
	 */
	private boolean isParameter(String cellStringValue) {
		if (cellStringValue.startsWith(Global.parameterIdentifier))
			return true;
		else
			return false;
	}

}
