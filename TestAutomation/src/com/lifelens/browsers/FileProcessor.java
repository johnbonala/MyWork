package com.lifelens.browsers;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.record.cf.PatternFormatting;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;

import com.lifelens.automation.tabulardata.CSVData;
import com.lifelens.automation.tabulardata.Comparison;
import com.lifelens.automation.tabulardata.ComparisonResult;
import com.lifelens.automation.tabulardata.FileBuilder;
import com.lifelens.automation.tabulardata.TabularData;
import com.lifelens.automation.tabulardata.Value;
import com.lifelens.automation.tabulardata.ValueFactory;
import com.lifelens.automation.tabulardata.WorksheetData;
import com.lifelens.automation.tabulardata.WorksheetDataExample;
import com.lifelens.exceptions.ExceptionErrorCodes;
import com.lifelens.exceptions.TestAutomationException;
import com.lifelens.globals.Global;
import com.lifelens.globals.GlobalCSSIdentifiers;
import com.lifelens.globals.GlobalCommonConstants;
import com.lifelens.interpreter.ArgList;
import com.lifelens.interpreter.ExtractInstructions;
import com.lifelens.interpreter.TestContext;
import com.lifelens.testset.TestSet;
import com.lifelens.webelements.Button;
import com.lifelens.webelements.Table;

public class FileProcessor extends RemoteWebElement {
	private static Logger logger = Logger.getLogger(FileProcessor.class.getName());

	// private String ajaxXPath =
	// "//div[@id='loadIndicator' and (contains(concat(' ', @style, ' '), ' display: none; ') or contains(concat(' ', @style, ' '), ' display:none '))]";
	// private String ajaxCSS = Global.loadIndicatorCSS;

	private WebDriver browser;

	private TestContext testContext;

	public FileProcessor(WebDriver browser, TestContext testContext) {
		this.browser = browser;
		this.testContext = testContext;
	}

	// Used for only unit tests
	public FileProcessor(TestContext testContext) {
		this.testContext = testContext;
	}

	/**
	 * Issue a HttpGet request to download a file from the given URL.
	 * 
	 * @param downloadUrl
	 *            - the URL from where to download the file
	 * @param outputFilePath
	 *            - where to save the file
	 * @throws TestAutomationException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	public boolean downloadFile(String downloadUrl, String outputFilePath) throws TestAutomationException {
		logger.debug("Start of FileProcessor: downloadFile");
		logger.debug("Downloding file from: " + downloadUrl);

		FileOutputStream fileOutputStream = null;
		boolean isFileDownloaded = false;

		// Ensure output directory exists
		int endOfDirectoryPosition = Math.max(outputFilePath.lastIndexOf("/"), outputFilePath.lastIndexOf("\\"));
		String outputFilePathWithoutFilename = outputFilePath.substring(0, endOfDirectoryPosition);
		File outputDirectory = new File(outputFilePathWithoutFilename);
		if (!outputDirectory.exists()) {
			outputDirectory.mkdirs();
		}

		// Download the file
		CloseableHttpResponse response = httpGet(downloadUrl);

		// Save downloaded file
		HttpEntity entity = response.getEntity();
		try {
			if (entity != null) {
				File outputFile = new File(outputFilePath);
				fileOutputStream = new FileOutputStream(outputFile);
				entity.writeTo(fileOutputStream);

				logger.debug("Downloded " + outputFile.length() + " bytes to " + outputFilePath + ". "
						+ entity.getContentType());
			} else {
				throw new TestAutomationException(ExceptionErrorCodes.TA029_FILE_PROCESSOR_UNABLE_TO_CLOSE_FILE_ERROR,
						"Download failed - unable to connect with the URL " + downloadUrl);
			}
			isFileDownloaded = true;
		} catch (FileNotFoundException e) {
			throw new TestAutomationException(
					ExceptionErrorCodes.TA027_FILE_PROCESSOR_FILE_NOT_FOUND_TO_DOWNLOAD_ERROR, e.getMessage()
							+ " : File not found in the path " + outputFilePath, e);
		} catch (IOException e) {
			throw new TestAutomationException(ExceptionErrorCodes.TA028_FILE_PROCESSOR_UNABLE_TO_READ_FILE_ERROR,
					e.getMessage() + ": Unable to write to " + outputFilePath, e);
		} finally {
			if (fileOutputStream != null) {

				try {
					response.close();
					fileOutputStream.close();
				} catch (IOException e) {
					throw new TestAutomationException(
							ExceptionErrorCodes.TA029_FILE_PROCESSOR_UNABLE_TO_CLOSE_FILE_ERROR, e.getMessage()
									+ ": Unable to close the file input stream", e);
				}
			}
		}
		logger.debug("End of FileProcessor: downloadFile");
		return isFileDownloaded;
	}

	/**
	 * Issue a httpGet on the supplied url, and send the response back.
	 * 
	 * @param downloadUrl
	 * @return CloseableHttpResponse
	 * @throws TestAutomationException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	public CloseableHttpResponse httpGet(String downloadUrl) throws TestAutomationException {
		logger.debug("Start of FileProcessor: httpGet");
		CookieStore cookieStore = WebElementProcessorCommon.storeSeleniumCookies(browser);

		// This proxy server has been set up by EUS Internet. It has a white
		// list of external domains it can access, so any new domains will have
		// to be added.
		// New external domains will also have to be added to the code below.
		// It also has a white list of IP addresses which are allowed access it,
		// so any PCs or servers needing to run this code must be added to the
		// white list.
		// EUS Internet are the contact for updating the white lists.
		HttpHost proxy = new HttpHost("wgapp.dmz.standardlife.com", 8080);
		DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);

		// Set up HttpClient
		// Only use proxy server for external domains. EUS Internet say there is
		// no way they can distinguish internal or external domains on the proxy
		// server, so we must do it here in the code.
		CloseableHttpResponse response = null;
		CloseableHttpClient httpClient = null;
		if (downloadUrl.contains("factsheets.financialexpress.net")
				|| downloadUrl.contains("uk.standardlifeinvestments.com")) {
			httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).setRoutePlanner(routePlanner).build();

			// Download from url
			HttpGet httpGet = new HttpGet(downloadUrl);
			try {
				response = httpClient.execute(httpGet);
			} catch (IOException e1) {
				throw new TestAutomationException(ExceptionErrorCodes.TA062_FILE_PROCESSOR_UNABLE_TO_DOWNLOAD_URL,
						e1.getMessage() + ": Unable to retrive url " + downloadUrl, e1);
			}
		} else {
			SSLContext sslContext;
			try {
				sslContext = SSLContext.getInstance("SSL");

				// set up a TrustManager that trusts everything
				sslContext.init(null, new TrustManager[] { new X509TrustManager() {
					public X509Certificate[] getAcceptedIssuers() {
						System.out.println("getAcceptedIssuers =============");
						return null;
					}

					public void checkClientTrusted(X509Certificate[] certs, String authType) {
						System.out.println("checkClientTrusted =============");
					}

					@Override
					public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
					}
				} }, new SecureRandom());

				SSLSocketFactory sf = new SSLSocketFactory(sslContext, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
				Scheme httpsScheme = new Scheme("https", 443, sf);
				SchemeRegistry schemeRegistry = new SchemeRegistry();
				schemeRegistry.register(httpsScheme);

				schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));

				// apache HttpClient version >4.2 should use
				// BasicClientConnectionManager
				ClientConnectionManager cm = new SingleClientConnManager(schemeRegistry);
				DefaultHttpClient defaultHttpClient = new DefaultHttpClient(cm);
				defaultHttpClient.setCookieStore(cookieStore);

				// Download from url
				HttpGet httpGet = new HttpGet(downloadUrl);
				try {
					response = defaultHttpClient.execute(httpGet);
				} catch (IOException e1) {
					throw new TestAutomationException(ExceptionErrorCodes.TA062_FILE_PROCESSOR_UNABLE_TO_DOWNLOAD_URL,
							e1.getMessage() + ": Unable to retrive url " + downloadUrl, e1);
				}
			} catch (NoSuchAlgorithmException e) {
				throw new TestAutomationException(ExceptionErrorCodes.TA062_FILE_PROCESSOR_UNABLE_TO_DOWNLOAD_URL,
						e.getMessage() + ": Unable to retrive url " + downloadUrl, e);
			} catch (KeyManagementException e) {
				throw new TestAutomationException(ExceptionErrorCodes.TA062_FILE_PROCESSOR_UNABLE_TO_DOWNLOAD_URL,
						e.getMessage() + ": Unable to retrive url " + downloadUrl, e);
			}
		}

		logger.debug("End of FileProcessor: httpGet");
		return response;
	}

	/**
	 * Extract the 'filename' parameter from the 'Content-Disposition' http
	 * response header.
	 * 
	 * @param response
	 * @return filename returns null if no name found
	 * @throws TestAutomationException
	 */
	public String extractFilenameFromHttpResponse(CloseableHttpResponse response) throws TestAutomationException {
		logger.debug("Start of FileProcessor: extractFilenameFromHttpResponse");
		String filename = null;

		Header contentDisposition = response.getFirstHeader("Content-Disposition");
		if (contentDisposition != null) {
			HeaderElement[] contentDispositionElements = contentDisposition.getElements();
			for (int i = 0; i < contentDispositionElements.length; i++) {
				NameValuePair nameValuePair = contentDispositionElements[i].getParameterByName("filename");
				if (nameValuePair != null && nameValuePair.getValue() != null) {
					filename = nameValuePair.getValue();
				}
			}
		}

		logger.debug("End of FileProcessor: extractFilenameFromHttpResponse");
		return filename;
	}

	/**
	 * Check the 'Content-Type' http response header to see if the document is a
	 * PDF.
	 * 
	 * @param response
	 * @return boolean indicating if document is a PDF
	 * @throws TestAutomationException
	 */
	public boolean isPdf(CloseableHttpResponse response) throws TestAutomationException {
		logger.debug("Start of FileProcessor: isPdf");
		String contentTypeOutput = null;
		boolean typeIsPdf = false;

		Header contentType = response.getFirstHeader("Content-Type");
		if (contentType != null) {
			HeaderElement[] contentTypeElements = contentType.getElements();
			for (int i = 0; i < contentTypeElements.length; i++) {
				contentTypeOutput = contentTypeElements[i].getName();
				if (contentTypeOutput != null && contentTypeOutput.contains("pdf")) {
					typeIsPdf = true;
				}
			}
		}

		logger.debug("End of FileProcessor: isPdf");
		return typeIsPdf;
	}

	/**
	 * Download the file pointed to by the 'Download' button.
	 * 
	 * @param downloadLinkCell
	 *            It has tableIndex, rowIndex and ColumnIndex
	 * 
	 * 
	 * @param outputFilePath
	 *            Either specify just the filename, a partial file path with
	 *            filename (e.g. "my test files/my file.csv" or a full file
	 *            path. If a filename only or a partial file path is specified
	 *            it is saved in a /Downloads/ folder under the current working
	 *            folder.
	 * @return boolean
	 * @throws TestAutomationException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	public boolean downloadLogFile(String downloadLinkCell, String outputFilePath) throws TestAutomationException {
		logger.debug("Start of FileProcessor: downloadLogFile");

		ArgList downloadUrlParams = new ArgList(downloadLinkCell);
		Table jobListTable;
		String jobStatus, downloadUrl;

		// Set output file path
		if (!outputFilePath.contains("/") && !outputFilePath.contains("\\")) {
			// File name only has been supplied - e.g. "my file.csv"
			outputFilePath = Global.getTestLabPath() + "/Downloads/" + outputFilePath;
		} else if (!outputFilePath.contains(":") && !outputFilePath.startsWith("//")
				&& !outputFilePath.startsWith("\\\\")) {
			// Partial file path & file name has been supplied - e.g.
			// "my test files/my file.csv"
			if (outputFilePath.startsWith("/") || outputFilePath.startsWith("\\")) {
				outputFilePath = Global.getTestLabPath() + "/Downloads" + outputFilePath;
			} else {
				outputFilePath = Global.getTestLabPath() + "/Downloads/" + outputFilePath;
			}
		}

		// Retrieve job status from Job list table if table indices are supplied
		// in test script else retrieve 1st row download link
		if (downloadUrlParams.hasTableKeyword()) {
			jobListTable = new Table(browser, testContext, downloadUrlParams);
			jobStatus = jobListTable.getTableCellValue("1," + downloadUrlParams.getTableParameter(1) + ",3");
		} else {
			jobListTable = new Table(browser, testContext, new ArgList("Table(1,2,8)"));
			jobStatus = jobListTable.getTableCellValue("1,1,3");
		}

		// The new time out is 30 so increasing the end time
		long endTime = System.currentTimeMillis() + (Global.TIMEOUT_REPORT * 1000);

		if (jobStatus.equalsIgnoreCase("job status")) {
			while (!jobStatus.equalsIgnoreCase("Failed") && !jobStatus.equalsIgnoreCase("Complete")
					&& System.currentTimeMillis() < endTime) {

				WebElementProcessorCommon.waitForWebProcessing(browser, Global.timeout, Global.loadIndicatorCSS,
						Global.lazyLoadIndicatorClass);
				new Button(browser, testContext, "Refresh", "1").clickOnButton();
				WebElementProcessorCommon.waitForWebProcessing(browser, Global.timeout, Global.loadIndicatorCSS,
						Global.lazyLoadIndicatorClass);

				// Clicking Refresh seems to make jobListContainer inaccessible
				// so must get it again
				if (downloadUrlParams.hasTableKeyword()) {
					jobListTable = new Table(browser, testContext, downloadUrlParams);
					jobStatus = jobListTable.getTableCellValue("1," + downloadUrlParams.getTableParameter(1) + ",3");
				} else {
					jobListTable = new Table(browser, testContext, new ArgList("Table(1,2,8)"));
					jobStatus = jobListTable.getTableCellValue("1,2,3");
				}
			}
		}

		if (jobStatus == "Failed") {
			logger.warn("Job marked as failed in Admin > Log Files");
		}

		// Retrieve download url from Job list table if table indices are
		// supplied in test script else retrieve 1st row download link
		if (downloadUrlParams.hasTableKeyword()) {
			jobListTable = new Table(browser, testContext, downloadUrlParams);
			downloadUrl = jobListTable.getLink().getAttribute("href");
		} else if (downloadUrlParams.getParmetersCount() > 1 && downloadUrlParams.getParameter(2).contains("6")) {
			jobListTable = new Table(browser, testContext, new ArgList("Table(1,2,6)"));
			downloadUrl = jobListTable.getLink().getAttribute("href");
		} else {
			jobListTable = new Table(browser, testContext, new ArgList("Table(1,2,8)"));
			downloadUrl = jobListTable.getLink().getAttribute("href");
		}

		// Download the file
		boolean result = downloadFile(downloadUrl, outputFilePath);

		logger.debug("End of FileProcessor: downloadLogFile");
		return result;
	}

	/**
	 * @param testSet
	 *            Contains details of the current testset workbook being used
	 * @param expectedDataWorkBook
	 *            workbook having expected data sheet
	 * @param workSheetName
	 *            the worksheet to markup with red for unmatched rows
	 * @param expectedSheetName
	 *            the expected sheet name
	 * @param isCriteriaBasedReport
	 *            the isCriteriaBasedReport
	 * @param actualCsvFilename
	 *            the actual cvs data file name
	 * @throws TestAutomationException
	 * @throws IOException
	 * @see com.lifelens.automation.tabulardata.Comparison#compare(TabularData,
	 *      TabularData, boolean)
	 */
	public boolean compareFiles(TestSet testSet, File expectedDataWorkBook, String expectedSheetName,
			String isCriteriaBasedReport, String actualCsvFilename) throws TestAutomationException, IOException {
		logger.debug("Start of FileProcessor: compareFiles");
		boolean isSuccessful;

		// Set actual results file path
		if (!actualCsvFilename.contains("/") && !actualCsvFilename.contains("\\")) {
			// File name only has been supplied - e.g. "my file.csv"
			actualCsvFilename = Global.getTestLabPath() + "/Downloads/" + actualCsvFilename;
		} else if (!actualCsvFilename.contains(":") && !actualCsvFilename.startsWith("//")
				&& !actualCsvFilename.startsWith("\\\\")) {
			// Partial file path & file name has been supplied - e.g.
			// "my test files/my file.csv"
			if (actualCsvFilename.startsWith("/") || actualCsvFilename.startsWith("\\")) {
				actualCsvFilename = Global.getTestLabPath() + "/Downloads" + actualCsvFilename;
			} else {
				actualCsvFilename = Global.getTestLabPath() + "/Downloads/" + actualCsvFilename;
			}
		}

		logger.info("Comparing expected: " + expectedDataWorkBook.getAbsolutePath() + " " + expectedSheetName
				+ " with actual: " + actualCsvFilename);

		TabularData expectedWorksheetData = new WorksheetData(expectedDataWorkBook.getAbsolutePath(), expectedSheetName);

		StringBuilder sb = WorksheetDataExample.outputWorksheetData(expectedWorksheetData);
		logger.debug("Expected Data :\n");
		logger.debug(sb);

		TabularData actualCSVData;
		if (expectedWorksheetData.hasNamedHeaders()) {
			actualCSVData = new CSVData(actualCsvFilename, true);
		} else {
			actualCSVData = new CSVData(actualCsvFilename, false);
		}
		sb = WorksheetDataExample.outputWorksheetData(actualCSVData);
		logger.debug("Actual Data :\n");
		logger.debug(sb);

		ComparisonResult result = Comparison.compare(expectedWorksheetData, actualCSVData);
		logger.debug("Number of matched rows: " + result.getMatchedRows().size() + "; Number of unmatched rows: "
				+ result.getUnmatchedRows().size() + "; Number of expected rows: " + expectedWorksheetData.numRows()
				+ "; Number of actual rows: " + actualCSVData.numRows());

		for (int i = 0; i < result.getUnmatchedRows().size(); i++) {
			logger.info("Unmatched row " + i + ": " + result.getUnmatchedRows().get(i));
		}

		markupWorksheet(result, testSet, expectedDataWorkBook.getAbsolutePath(), expectedSheetName,
				expectedWorksheetData, actualCSVData, isCriteriaBasedReport);

		if (result.getMatchedRows().size() == actualCSVData.numRows()) {
			isSuccessful = true;
		} else {
			isSuccessful = result.isSuccessful();
		}

		logger.debug("End of FileProcessor: compareFiles");
		return isSuccessful;
	}

	/**
	 * Markup the expected results worksheet with any unmatched cells. Unmatched
	 * cells are marked with red background colour.
	 * 
	 * @param testSet
	 *            Contains details of the current testset workbook being used
	 * @param result
	 *            List of all unmatched rows
	 * @param workSheetName
	 *            the worksheet to markup with red for unmatched rows
	 * @param expectedCSVData
	 *            the expected cvs data
	 * @param actualCSVData
	 *            the actual cvs data
	 * @param isCriteriaBasedReport
	 *            the isCriteriaBasedReport
	 * @throws TestAutomationException
	 * @throws InvalidFormatException
	 */
	private void markupWorksheet(ComparisonResult result, TestSet testSet, String workBookName, String workSheetName,
			TabularData expectedCSVData, TabularData actualCSVData, String isCriteriaBasedReport)
			throws TestAutomationException {
		logger.debug("Start of FileProcessor: markupWorksheet");
		FileInputStream fis = null;
		Workbook workbook = null;
		FileOutputStream fos = null;

		try {
			// Either use current workbook or open a different one.
			String currentWorkbookName = testSet.getWorkbookNameAbsolutePath();
			if (currentWorkbookName != null && currentWorkbookName.equals(workBookName)) {
				workbook = testSet.getWorkbook();
			} else {
				fis = new FileInputStream(new File(workBookName));
				workbook = WorkbookFactory.create(fis);
			}
			Sheet sheet = workbook.getSheet(workSheetName);

			// Clear colour for matching cells.
			CellStyle clearCellStyle = workbook.createCellStyle();
			clearCellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
			clearCellStyle.setFillPattern(PatternFormatting.NO_FILL);

			// Red colour for unmatched cells.
			CellStyle redCellStyle = workbook.createCellStyle();
			redCellStyle.setFillForegroundColor(IndexedColors.RED.index);
			redCellStyle.setFillPattern(PatternFormatting.SOLID_FOREGROUND);

			// Green colour for matched cells.
			CellStyle greenCellStyle = workbook.createCellStyle();
			greenCellStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index);
			greenCellStyle.setFillPattern(PatternFormatting.SOLID_FOREGROUND);

			// If result is successful clear the mark up.
			if (result.isSuccessful()) {
				for (int i = 0; i <= sheet.getLastRowNum(); i++) {
					// to handle extra rows in the end of expected data sheet
					if (sheet.getRow(i) == null) {
						break;
					}
					for (int cell = 1; cell < sheet.getRow(i).getLastCellNum(); cell++) {
						sheet.getRow(i);
						sheet.getRow(i).getCell(cell, Row.CREATE_NULL_AS_BLANK).setCellStyle(clearCellStyle);
					}
				}
			}
			// Criteria based report, if user in interested only in some rows
			// only mark up matched rows
			else if ((isCriteriaBasedReport != null) && isCriteriaBasedReport.equalsIgnoreCase("True")) {

				// Mark up matched cells with green colour.
				if (result.getMatchedRows().size() > 0) {

					matchedRowsMarking(result, expectedCSVData, actualCSVData, sheet, greenCellStyle, redCellStyle);

				}
				if (result.getUnmatchedRows().size() > 0) {
					// Mark up unmatched cells with no fill.
					for (int i = 0; i < result.getUnmatchedRows().size(); i++) {
						Value unmatchedRowValue = result.getUnmatchedRows().get(i).get("__sourceRow");
						int unmatchedRowNumber = Integer.parseInt(unmatchedRowValue.toString());

						Row unmatchedRow = sheet.getRow(unmatchedRowNumber);
						for (int j = 0; j < unmatchedRow.getLastCellNum(); j++) {
							Cell unMatchedCell = unmatchedRow.getCell(j);
							if (unMatchedCell != null) {
								unMatchedCell.setCellStyle(clearCellStyle);
							}
						}
					}
				}
			}

			// For full report comparison mark up unmatched cells
			else {
				unmatchedCellValuesMarkUp(result, expectedCSVData, actualCSVData, sheet, redCellStyle, clearCellStyle);
			}
			if (currentWorkbookName != null && !currentWorkbookName.equals(workBookName)) {
				fos = new FileOutputStream(new File(workBookName));
				workbook.write(fos);
			}
		} catch (FileNotFoundException ex) {
			throw new TestAutomationException(
					ExceptionErrorCodes.TA038_FILE_PROCESSOR_COMPARE_FILES_UNABLE_TO_FIND_WORKBOOK_ERROR,
					ex.getMessage() + ": Unable to find the file in the path " + workBookName, ex);
		} catch (IOException ex) {
			throw new TestAutomationException(
					ExceptionErrorCodes.TA039_FILE_PROCESSOR_COMPARE_FILES_UNABLE_TO_READ_WORKBOOK_ERROR,
					ex.getMessage() + ": Unable to load the file in the path " + workBookName, ex);
		} catch (InvalidFormatException ex) {
			throw new TestAutomationException(
					ExceptionErrorCodes.TA039_FILE_PROCESSOR_COMPARE_FILES_UNABLE_TO_READ_WORKBOOK_ERROR,
					ex.getMessage() + ": Unable to load the file in the path " + workBookName, ex);
		} finally {
			IOUtils.closeQuietly(fis);
			IOUtils.closeQuietly(fos);
		}
		logger.debug("End of FileProcessor: markupWorksheet");
	}

	/**
	 * Method to clear mark up in matched cells from matched rows.
	 * 
	 * @param result
	 * @param expectedCSVData
	 * @param actualCSVData
	 * @param sheet
	 * @param cellStyle
	 */
	private void matchedRowsMarking(ComparisonResult result, TabularData expectedCSVData, TabularData actualCSVData,
			Sheet sheet, CellStyle clearStyle, CellStyle redCellStyle) {
		logger.debug("Start of FileProcessor: matchedRowsMarking");
		for (int i = 0; i < result.getMatchedRows().size(); i++) {
			Value matchedRowValue = result.getMatchedRows().get(i).get("__sourceRow");
			int matchedRowNumber = Integer.parseInt(matchedRowValue.toString());
			String matchedCellValue = null;
			Map<String, Value> csvRow = null;

			Row matchedRow = sheet.getRow(matchedRowNumber);
			csvRow = actualCSVData.getRow(i);

			Map<String, Value> expectedRow = expectedCSVData.getRow(matchedRowNumber - 1);

			// get only the cells which are matched from actual file and apply
			// clear style.
			List<Value> matchedCellValues = getMatchedCellValues(expectedRow, csvRow);
			for (int j = 0; j < matchedRow.getLastCellNum(); j++) {
				Value matchedValue = null;
				Cell matchedCell = matchedRow.getCell(j);

				if (matchedCell != null) {
					matchedCellValue = getCellValue(matchedCell);
					matchedValue = ValueFactory.valueFor(matchedCellValue.trim());

					if (matchedCellValues.contains(matchedValue)) {
						matchedCell.setCellStyle(clearStyle);
					}
				}
			}
		}
		logger.debug("End of FileProcessor: matchedRowsMarking");
	}

	/**
	 * @param matchedCell
	 * @return
	 */
	private String getCellValue(Cell matchedCell) {
		String matchedCellValue;
		Value matchedValue;
		if (matchedCell.getCellType() == Cell.CELL_TYPE_NUMERIC && DateUtil.isCellDateFormatted(matchedCell)) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			matchedCellValue = sdf.format(matchedCell.getDateCellValue());
		} else {
			matchedValue = ValueFactory.valueFor(matchedCell);
			matchedCellValue = matchedValue.toString();
		}
		return matchedCellValue;
	}

	/**
	 * Method to mark up unmatched cell values in expected data after comparison
	 * , this is used only when user is interested to do comparison for whole
	 * actual report i.e. without any criteria lie staff number,payroll number
	 * etc.,
	 * 
	 * @param result
	 * @param expectedCSVData
	 * @param actualCSVData
	 * @param sheet
	 * @param redCellStyle
	 */
	private void unmatchedCellValuesMarkUp(ComparisonResult result, TabularData expectedCSVData,
			TabularData actualCSVData, Sheet sheet, CellStyle redCellStyle, CellStyle clearCellStyle) {
		logger.debug("Start of FileProcessor: unmatchedCellValuesMarkUp");

		// Mark up matched cells with no fill.
		if (result.getMatchedRows().size() > 0) {
			matchedRowsMarking(result, expectedCSVData, actualCSVData, sheet, clearCellStyle, redCellStyle);
		}

		if (result.getMatchedRows().size() > 0) {
			// Mark up unmatched cells with red colour.
			for (int i = 0; i < result.getUnmatchedRows().size(); i++) {
				Value unmatchedRowValue = result.getUnmatchedRows().get(i).get("__sourceRow");
				int unmatchedRowNumber = Integer.parseInt(unmatchedRowValue.toString());
				String unmatchedCellValue = null;
				Map<String, Value> csvRow = new HashMap<String, Value>();
				Map<String, Value> emptyRow = new HashMap<String, Value>();

				Row unmatchedRow = sheet.getRow(unmatchedRowNumber);

				if (actualCSVData.numRows() >= (unmatchedRowNumber)) {
					csvRow = actualCSVData.getRow(unmatchedRowNumber - 1);
				} else {
					// If actual number of rows less than expected data
					// comparing with empty actual row
					csvRow = emptyRow;
				}

				Map<String, Value> expectedRow = expectedCSVData.getRow(unmatchedRowNumber - 1);

				// get unmatched cell values.
				List<Value> unmatchedCellValues = getUnmatchedCellValues(expectedRow, csvRow);
				for (int j = 0; j < unmatchedRow.getLastCellNum(); j++) {
					Value unmatchedValue = null;
					Cell unmatchedCell = unmatchedRow.getCell(j);

					if (unmatchedCell != null) {
						unmatchedCellValue = getCellValue(unmatchedCell);
						unmatchedValue = ValueFactory.valueFor(unmatchedCellValue.trim());
					}
					if (unmatchedValue != null && unmatchedCellValues.contains(unmatchedValue)) {
						// if {any} is used do not mark up in red
						if (unmatchedValue.equals(new Value("??"))) {
							unmatchedCell.setCellStyle(clearCellStyle);
						} else {
							unmatchedCell.setCellStyle(redCellStyle);
						}
					}
				}
			}
		}
		logger.debug("End of FileProcessor: unmatchedCellValuesMarkUp");
	}

	/**
	 * Method to compare expected and actual unmatched rows and get cell values
	 * for unmatched data and get unmatched cells
	 * 
	 * @param expected
	 * @param actual
	 * @return unmatched cell values
	 */
	private static List<Value> getUnmatchedCellValues(Map<String, Value> expected, Map<String, Value> actual) {
		logger.debug("Start of FileProcessor: getUnmatchedCellValues");
		List<Value> unmatchedCellValues = new ArrayList<Value>();
		for (String key : expected.keySet()) {
			if (key.equals("__sourceRow")) {
				continue;
			}
			Value expectedValue = expected.get(key);
			Value actualValue = null;
			if (actual != null) {
				actualValue = actual.get(key);
			}
			if (!expectedValue.equals(actualValue)) {
				unmatchedCellValues.add(expectedValue);
			}
		}
		logger.debug("End of FileProcessor: getUnmatchedCellValues");
		return unmatchedCellValues;
	}

	/**
	 * Method to compare expected and actual unmatched rows and get cell values
	 * for unmatched data and get unmatched cells
	 * 
	 * @param expected
	 * @param actual
	 * @return unmatched cell values
	 */
	private static List<Value> getMatchedCellValues(Map<String, Value> expected, Map<String, Value> actual) {
		logger.debug("Start of FileProcessor: getMatchedCellValues");
		List<Value> matchedCellValues = new ArrayList<Value>();
		for (String key : expected.keySet()) {
			if (key.equals("__sourceRow")) {
				continue;
			}
			Value expectedValue = expected.get(key);
			Value actualValue = null;
			if (actual != null) {
				actualValue = actual.get(key);
			}
			if (expectedValue.equals(actualValue)) {
				matchedCellValues.add(expectedValue);
			}
		}
		logger.debug("End of FileProcessor: getMatchedCellValues");
		return matchedCellValues;
	}

	/**
	 * This is to verify the value inside the downloaded csv file.
	 * 
	 * @param actualCsvFilename
	 *            the csv file name and row,column number of the cell
	 * @param textToVerify
	 *            the text to be verified in the above cell
	 * @throws TestAutomationException
	 * @throws IOException
	 */
	public boolean verifyCsvText(String actualCsvFilename, String textToVerify) throws TestAutomationException {

		logger.debug("Start of FileProcessor: verifyCsvText");

		int row = 0, col = 0;
		ArgList arglist = new ArgList(actualCsvFilename);
		String checkValue = "True";

		// User has specified only filename, row, column
		if (arglist.getParmetersCount() == 3) {
			actualCsvFilename = arglist.getParameter(0);
			row = arglist.getNumericParameter(1);
			col = arglist.getNumericParameter(2);
		}

		// User has specified only filename, row, column,check value
		if (arglist.getParmetersCount() == 4) {
			actualCsvFilename = arglist.getParameter(0);
			row = arglist.getNumericParameter(1);
			col = arglist.getNumericParameter(2);
			checkValue = arglist.getParameter(3);
		}

		// Set actual results file path
		if (!actualCsvFilename.contains("/") && !actualCsvFilename.contains("\\")) {
			// File name only has been supplied - e.g. "my file.csv"
			actualCsvFilename = Global.getTestLabPath() + "/Downloads/" + actualCsvFilename;
		} else if (!actualCsvFilename.contains(":") && !actualCsvFilename.startsWith("//")
				&& !actualCsvFilename.startsWith("\\\\")) {
			// Partial file path & file name has been supplied - e.g.
			// "my test files/my file.csv"
			if (actualCsvFilename.startsWith("/") || actualCsvFilename.startsWith("\\")) {
				actualCsvFilename = Global.getTestLabPath() + "/Downloads" + actualCsvFilename;
			} else {
				actualCsvFilename = Global.getTestLabPath() + "/Downloads/" + actualCsvFilename;
			}
		}

		if (checkCsvFileName(actualCsvFilename)) {
			testContext.setCurrentDownloadedFile(null);
		}

		// only spend time converting if current download not already
		// converted
		if (testContext.getCurrentDownloadedFile() == null) {
			testContext.setDownLoadedcsvFileName(actualCsvFilename);
			XSSFWorkbook convertedWb;
			String convertedCSVFile;
			if (actualCsvFilename.contains(GlobalCommonConstants.CSVEXTENSION)) {
				convertedWb = FileBuilder.csvToExcelConversion(actualCsvFilename);
			} else {
				convertedCSVFile = FileBuilder.txtToCSVConversion(actualCsvFilename);
				convertedWb = FileBuilder.csvToExcelConversion(convertedCSVFile);
				if (new File(convertedCSVFile).isFile()) {
					new File(convertedCSVFile).delete();
				}
			}
			testContext.setCurrentDownloadedFile(convertedWb);
		}

		// After conversion comparing the test to verify with the cell number
		// specified
		boolean result = excelCellComparision(testContext.getCurrentDownloadedFile(), row, col, textToVerify,
				checkValue);

		logger.debug("End of FileProcessor: verifyCsvText");
		return result;
	}

	// Comparing previous csv file name with current csv file name
	private boolean checkCsvFileName(String actualCsvFilename) {
		boolean fileAvailable = false;
		if (testContext.getDownLoadedcsvFileName() != null) {
			fileAvailable = (testContext.getDownLoadedcsvFileName().equalsIgnoreCase(actualCsvFilename)) ? false : true;
		}
		return fileAvailable;
	}

	/**
	 * Method to compare text to verify with the cell specified
	 * 
	 * @param convertedWb
	 * @param row
	 * @param col
	 * @param textToVerify
	 * @param checkValue
	 * 
	 * @return boolean
	 * @throws TestAutomationException
	 */
	private boolean excelCellComparision(XSSFWorkbook convertedWb, int row, int col, String textToVerify,
			String checkValue) throws TestAutomationException {
		logger.debug("Start of FileProcessor: excelCellComparision");
		boolean isSuccessful;
		String value, cellValue = null;
		Cell cellToCompare = null;

		XSSFSheet convertedSheet = convertedWb.getSheet(convertedWb.getSheetName(0));

		if (convertedSheet.getRow(row - 1) != null) {
			cellToCompare = convertedSheet.getRow(row - 1).getCell((col - 1), Row.CREATE_NULL_AS_BLANK);
		} else {
			logger.error("The row at number :" + row + " is empty row.");
			// If user wants to verify blank value for empty report with only
			// column headers
			return true;
		}
		if (textToVerify.contains("/")) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			if (cellToCompare.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				value = sdf.format(cellToCompare.getDateCellValue());
			}
			if (cellToCompare.getCellType() == Cell.CELL_TYPE_STRING) {
				cellValue = cellToCompare.getStringCellValue();
			}
			cellToCompare.setCellValue(cellValue);
		} else {
			cellValue = cellToCompare.getStringCellValue();
		}
		value = retriveParamValue(cellValue);

		if (textToVerify.equalsIgnoreCase(value) || value.contains(textToVerify)) {
			isSuccessful = true;
		} else {
			isSuccessful = false;
		}

		if (checkValue.equalsIgnoreCase("False")) {
			isSuccessful = !isSuccessful;
		}

		logger.debug("End of FileProcessor: excelCellComparision");
		return isSuccessful;
	}

	/**
	 * This is to verify the value inside the table.
	 * 
	 * @param sheetAndCell
	 *            - (optional workbook, worksheet, row, column) all
	 *            comma-separated
	 * @param textToVerify
	 *            the text to be verified in the above cell
	 * @throws TestAutomationException
	 */
	public boolean verifyExcelText(TestSet testSet, File excelFile, String worksheetName, int row, int col,
			String textToVerify) throws TestAutomationException {
		logger.debug("Start of FileProcessor: verifyExcelText");

		FileInputStream fis = null;
		Workbook workbook = null;
		FileOutputStream fos = null;
		String expectedBookname = null;
		boolean testStepResult = false;

		try {

			fis = new FileInputStream(excelFile);
			workbook = new XSSFWorkbook(fis);

			Sheet sheet = workbook.getSheet(worksheetName);
			String value = null;
			String cellValue = null;

			// Clear colour.
			CellStyle clearCellStyle = workbook.createCellStyle();
			clearCellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
			clearCellStyle.setFillPattern(PatternFormatting.NO_FILL);

			// Green colour for verified cells.
			CellStyle greenCellStyle = workbook.createCellStyle();
			greenCellStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index);
			greenCellStyle.setFillPattern(PatternFormatting.SOLID_FOREGROUND);

			Row dataRow = sheet.getRow(sheet.getFirstRowNum());
			Cell firstCell = dataRow.getCell(dataRow.getFirstCellNum());
			String type = firstCell.getStringCellValue();

			if (type.equals("<header>")) {
				col = col - 1;
			}
			Cell cellToCompare = sheet.getRow(row - 1).getCell(col, Row.CREATE_NULL_AS_BLANK);

			// If a value contains parameter replace the parameter
			// value from the string
			if (textToVerify.contains(Global.parameterIdentifier)) {
				int beginIndex = textToVerify.indexOf(Global.parameterIdentifier);
				String testDataParameter = textToVerify.substring(beginIndex);
				String retrivedValue = retriveParamValue(testDataParameter);
				if (retrivedValue != null) {
					textToVerify = textToVerify.replaceAll(testDataParameter, retrivedValue);
				}
			}

			cellValue = getCellValue(cellToCompare);
			value = retriveParamValue(cellValue);

			// If a value contains parameter replace the parameter
			// value from the string
			if (value.contains(Global.parameterIdentifier)) {
				int beginIndex = value.indexOf(Global.parameterIdentifier);
				String testDataParameter = value.substring(beginIndex);
				String retrivedValue = retriveParamValue(testDataParameter);
				if (retrivedValue != null) {
					value = value.replaceAll(testDataParameter, retrivedValue);
				}
			}

			if (textToVerify.equals(value)) {
				cellToCompare.setCellStyle(greenCellStyle);
				testStepResult = true;
			} else {
				cellToCompare.setCellStyle(clearCellStyle);
			}

			// After updating style writing to the file
			fos = new FileOutputStream(excelFile);
			workbook.write(fos);
		} catch (FileNotFoundException ex) {
			throw new TestAutomationException(
					ExceptionErrorCodes.TA041_FILE_PROCESSOR_VERIFY_FILES_UNABLE_TO_FIND_WORKBOOK_ERROR,
					ex.getMessage() + ": Unable to find the file in the path " + expectedBookname, ex);
		} catch (IOException ex) {
			throw new TestAutomationException(
					ExceptionErrorCodes.TA042_FILE_PROCESSOR_VERIFY_FILES_UNABLE_TO_READ_WORKBOOK_ERROR,
					ex.getMessage() + ": Unable to load the file in the path " + expectedBookname, ex);
		} finally {
			IOUtils.closeQuietly(fis);
			IOUtils.closeQuietly(fos);
		}
		logger.debug("End of FileProcessor: verifyExcelText");
		return testStepResult;
	}

	/**
	 * Get Value of the parameter from testData sheet and replace to
	 * expectedData sheet.
	 * 
	 * @param value
	 * @return String
	 * @throws TestAutomationException
	 * @throws Exception
	 */
	private String retriveParamValue(String value) throws TestAutomationException {
		logger.debug("Start of FileProcessor: retriveParamValue");
		if (isParameter(value)) {
			value = testContext.getTestSet().getTestData().getParameterValue(value);
		}
		logger.debug("End of FileProcessor: retriveParamValue");
		return value;
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

	/**
	 * Save a screenshot of the active web page in a /Screenshots/ folder under
	 * the current folder.
	 * 
	 * @param fileName
	 *            Screenshot filename
	 * @param subDirectory
	 *            Optional sub-directory under the /Screenshots/ folder.
	 * @return boolean indicating success or failure
	 * @throws TestAutomationException
	 */
	public boolean takeScreenshot(String fileName, String subDirectory, String workBookName)
			throws TestAutomationException {
		logger.debug("Start of FileProcessor: takeScreenshot");

		boolean result;

		if (!fileName.endsWith(".png")) {
			fileName = fileName + ".png";
		}

		String screenShotDirPath = Global.getTestLabPath() + "/Screenshots/";
		if (StringUtils.isNotBlank(subDirectory)) {
			if (subDirectory.startsWith("/") || subDirectory.startsWith("\\")) {
				subDirectory = subDirectory.substring(1);
			}
			if (!workBookName.endsWith("/") && !workBookName.endsWith("\\")) {
				workBookName = workBookName + "/";
			}
			if (!subDirectory.endsWith("/") && !subDirectory.endsWith("\\")) {
				subDirectory = subDirectory + "/";
			}
			// below change will create separate folder per testSet in sub
			// directories
			screenShotDirPath = screenShotDirPath + subDirectory + workBookName;
		} else {
			// below change will create separate folder per testSet in there no
			// sub directories then in "Screenshots" directory
			screenShotDirPath = screenShotDirPath + workBookName;
		}

		File screenShotDir = new File(screenShotDirPath);
		if (!screenShotDir.exists()) {
			screenShotDir.mkdirs();
		}

		String filePath = screenShotDirPath + "/" + fileName;

		File scrFile;
		try {
			// See if any frames exists in window.
			// This causes a delay of 60 seconds waiting for a frame to appear,
			// so set a low wait time and then reset back to normal.
			browser.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
			int frameCount = browser.findElements(By.className(GlobalCSSIdentifiers.TOOLFRAME)).size();

			if (frameCount == 0 && !ExtractInstructions.controlInIframe) {
				// Ordinary window
				scrFile = ((TakesScreenshot) browser).getScreenshotAs(OutputType.FILE);
				FileUtils.copyFile(scrFile, new File(filePath));
				result = true;
			} else {
				// Embedded frame
				try {
					BufferedImage stitchedImage = takeFrameScreenshot();
					ImageIO.write(stitchedImage, "png", new File(filePath));
					result = true;
				} catch (TestAutomationException tae) {
					result = false;
					throw new TestAutomationException(
							ExceptionErrorCodes.TA044_FILE_PROCESSOR_UNABLE_TO_SAVE_SCREENSHOT_ERROR,
							tae.getMessage()
									+ ": Unable to Save the screen shot, please check whether the switch is in fst or window",
							tae);
				}
			}

		} catch (IOException ex) {
			result = false;
			throw new TestAutomationException(ExceptionErrorCodes.TA044_FILE_PROCESSOR_UNABLE_TO_SAVE_SCREENSHOT_ERROR,
					ex.getMessage() + ": Unable to Save the screen shot", ex);
		} finally {
			// make sure we go back to the default timeout
			browser.manage().timeouts().implicitlyWait(Global.timeout, TimeUnit.SECONDS);
		}

		logger.debug("End of FileProcessor: takeScreenshot");
		return result;
	}

	/**
	 * Save a screenshot of the active web page which contains an embedded
	 * frame. The frame is scrolled so all of it is visible.
	 * 
	 * @return Stitched image
	 * @throws TestAutomationException
	 */
	private BufferedImage takeFrameScreenshot() throws TestAutomationException {
		// TODO this is temporary code which takes 5 screenshots. Needs to be
		// scrolled correctly from top to bottom. Larry
		logger.debug("Start of FileProcessor: takeFrameScreenshot");

		WebElement frame;
		// int totalWidth = browser.manage().window().getSize().getWidth();
		// int totalHeight = browser.manage().window().getSize().getHeight();
		// System.out.println("totalWidth: " + totalWidth);
		// System.out.println("totalHeight: " + totalHeight);
		if (ExtractInstructions.controlInIframe) {
			browser.switchTo().defaultContent();
			// ExtractInstructions.loadIndicatorXPath =
			// Global.loadIndicatorXPath;
		}

		if (testContext.isImpersonateScreen()) {
			frame = browser.findElement(By.className(GlobalCSSIdentifiers.WICKETMODEL));
		} else {
			frame = browser.findElement(By.className(GlobalCSSIdentifiers.TOOLFRAME));
		}

		// int frameWidth = frame.getSize().getWidth();
		int frameHeight = frame.getSize().getHeight();
		// System.out.println("window.outerHeight: " +
		// browser.executeScript("window.outerHeight"));
		// System.out.println("window.innerHeight: " +
		// browser.executeScript("window.innerHeight"));
		// System.out.println("window.document.body.scrollHeight: "
		// + browser.executeScript("window.document.body.scrollHeight"));
		// System.out.println("frameWidth: " + frameWidth);
		// System.out.println("frameHeight: " + frameHeight);
		// JavascriptExecutor js = (JavascriptExecutor) browser;
		// System.out.println("JavascriptExecutor window.outerHeight: " +
		// js.executeScript("window.outerHeight;"));
		// System.out.println("JavascriptExecutor window.innerHeight: " +
		// js.executeScript("window.innerHeight;"));
		// System.out.println("JavascriptExecutor window.document.body.scrollHeight: "
		// + js.executeScript("window.document.body.scrollHeight"));

		// if (!ExtractInstructions.controlInIframe) {
		browser.switchTo().frame(frame);
		// ExtractInstructions.loadIndicatorXPath = "";
		// }
		// System.out.println("window.outerHeight: " +
		// browser.executeScript("window.outerHeight"));
		// System.out.println("window.innerHeight: " +
		// browser.executeScript("window.innerHeight"));
		// System.out.println("window.document.body.scrollHeight: "
		// + browser.executeScript("window.document.body.scrollHeight"));
		// System.out.println("JavascriptExecutor window.outerHeight: " +
		// js.executeScript("window.outerHeight;"));
		// System.out.println("JavascriptExecutor window.innerHeight: " +
		// js.executeScript("window.innerHeight;"));
		// System.out.println("JavascriptExecutor window.document.body.scrollHeight: "
		// + js.executeScript("window.document.body.scrollHeight"));

		// System.out
		// .println("new scrollHeight: "
		// +
		// js.executeScript("document.getElementsByClassName('wicket_modal')[0].contentWindow.document.body.scrollHeight"));

		List<File> fileList = new ArrayList<File>();
		((RemoteWebDriver) browser).executeScript("window.scrollTo(0,0)");

		fileList.add(((TakesScreenshot) browser).getScreenshotAs(OutputType.FILE));
		for (int i = 0; i < 4; i++) {
			((RemoteWebDriver) browser).executeScript("window.scrollBy(0," + (frameHeight - 50) + ")");
			fileList.add(((TakesScreenshot) browser).getScreenshotAs(OutputType.FILE));
		}

		BufferedImage stitchedImage = stitchImages(fileList);

		if (!ExtractInstructions.controlInIframe) {
			browser.switchTo().defaultContent();
			// WebElementProcessorCommon.waitForWebProcessing(browser,
			// Global.getTimeout(), "", "");
			// ExtractInstructions.loadIndicatorXPath =
			// Global.loadIndicatorXPath;
		}

		logger.debug("End of FileProcessor: takeFrameScreenshot");
		return stitchedImage;
	}

	/**
	 * Stitch multiple images together vertically into a single png.
	 * 
	 * @param imageList
	 *            List of image Files to stitch together
	 * @return BufferedImage - the stitched image
	 * @throws IOException
	 */
	private static BufferedImage stitchImages(List<File> imageList) throws TestAutomationException {
		logger.debug("Start of FileProcessor: stitchImages");
		int stitchedWidth = 0;
		int stitchedHeight = 0;
		BufferedImage stitchedImage;

		final List<Integer> imageStartHeight = new ArrayList<Integer>();
		imageStartHeight.add(0, 0);
		try {
			// Find total height and maximum width of final stitched image.
			// Store height of each image.
			for (int i = 0; i < imageList.size(); i++) {
				int imageWidth = ImageIO.read(imageList.get(i)).getWidth();
				int imageHeight = ImageIO.read(imageList.get(i)).getHeight();
				if (imageWidth > stitchedWidth) {
					stitchedWidth = imageWidth;
				}
				stitchedHeight = stitchedHeight + imageHeight;
				imageStartHeight.add(i + 1, stitchedHeight);
			}

			// Create new image of the correct size
			stitchedImage = new BufferedImage(stitchedWidth, stitchedHeight, ImageIO.read(imageList.get(0)).getType());

			// Stitch each separate image into the final image
			Graphics2D g2dImage = stitchedImage.createGraphics();
			for (int i = 0; i < imageList.size(); i++) {
				BufferedImage image = ImageIO.read(imageList.get(i));
				g2dImage.drawImage(image, 0, imageStartHeight.get(i), null);
			}
			g2dImage.dispose();

		} catch (IOException ex) {
			throw new TestAutomationException(ExceptionErrorCodes.TA061_FILE_PROCESSOR_UNABLE_TO_STITCH_IMAGES_ERROR,
					ex.getMessage() + ": Unable to read screen shot image", ex);
		}
		logger.debug("End of FileProcessor: stitchImages");
		return stitchedImage;
	}
}
