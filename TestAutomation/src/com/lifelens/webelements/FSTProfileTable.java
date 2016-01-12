package com.lifelens.webelements;

import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

import com.lifelens.browsers.WebElementProcessorCommon;
import com.lifelens.exceptions.ExceptionErrorCodes;
import com.lifelens.exceptions.TestAutomationException;
import com.lifelens.globals.GlobalAttributes;
import com.lifelens.globals.GlobalCSSIdentifiers;
import com.lifelens.globals.GlobalCSSSelectors;
import com.lifelens.globals.GlobalTags;
import com.lifelens.htmlparser.HtmlParser;

/**
 * It represents the web FST table object. Web tables are present in FST tool in
 * lifelens both in pre join and post join Eg. Lifestyle profiles, core funds
 * table
 * 
 * This helps to select funds and profiles from the FST Tables. This will be
 * further extended to select the webelements (buttons, checkbox, redio boxes)
 * inside the table.
 * 
 * @author Srinivas Pasupulati (co48633)
 * 
 * @version 1.0
 * 
 * @since 15.05.2014
 * 
 */
public class FSTProfileTable extends RemoteWebElement {

	private static Logger logger = Logger.getLogger(FSTProfileTable.class.getName());
	private WebDriver browser;
	private WebElement table;
	private String profileTableId;
	private final int profileNameIndex = 0;
	private final int profileButtonIndex = 1;
	private final int profilePercentageInputBoxIndex = 1;

	/**
	 * 
	 * @param browser
	 *            browser object
	 */
	public FSTProfileTable(WebDriver browser) {
		this.browser = browser;

	}

	public boolean selectFSTProfile(String profileName) throws TestAutomationException {
		logger.debug("Start of FSTProfileTable: selectFSTProfile");
		// browser.switchTo().frame(WebElementRetriever.getWebElementByClassName(browser,
		// profileTableId, "toolIframe"));

		setProfileTableId(GlobalCSSIdentifiers.PROFILELISTTABLEID);

		setTable(getProfileTable());

		WebElement profileButton = getProfileTableWebElement(profileName, profileButtonIndex);

		WebElementProcessorCommon.scrollToWebElement(browser, profileButton);

		WebElementRetriever.getWebElementByCSSSelector(profileButton, " profileName ",
				GlobalCSSSelectors.CMSPRIMARYBUTTON).click();

		// browser.switchTo().defaultContent();
		logger.debug("End of FSTProfileTable: selectFSTProfile");
		return true;

	}

	/**
	 * This extract the table with id "profileList" returns the table
	 * 
	 * @param browser
	 *            Active web browser
	 * 
	 * @return
	 * @throws TestAutomationException
	 */
	public WebElement getFSTProfileTable(WebDriver browser) throws TestAutomationException {
		return WebElementRetriever.getWebElementById(browser, profileTableId, profileTableId);
	}

	private List<WebElement> getTableRows() throws TestAutomationException {
		return WebElementRetriever.getWebElementsByTagName(getTable(), profileTableId, GlobalTags.TR);
	}

	public WebElement getProfileTableWebElement(String profileName, int elementIndex) throws TestAutomationException {
		logger.debug("End of FSTProfileTable: getProfileButtonElement");
		List<WebElement> tableCols;
		List<WebElement> tableRows;

		tableRows = getTableRows();
		WebElement element = null;
		for (int i = 0; i < tableRows.size(); i++) {
			tableCols = WebElementRetriever.getWebElementsByCSSSelector(tableRows.get(i), profileName, GlobalTags.TH
					+ " , " + GlobalTags.TD);
			if (new HtmlParser(tableCols.get(profileNameIndex)).getinnerText().equalsIgnoreCase(profileName)) {
				return tableCols.get(elementIndex);
			}
		}
		WebElementProcessorCommon.logWebElementStatus("Profile table element", profileName, (element == null ? false
				: true));

		if (element == null)
			throw new TestAutomationException(
					ExceptionErrorCodes.TA090_FST_PROFILETABLE_ELEMENT_NOT_FOUND_BY_PROFILENAME_AND_INDEX,
					"Profile table Web elements with profile name '" + profileName + "' and index '" + elementIndex
							+ "' not found.");

		logger.debug("End of FSTProfileTable: getProfileButtonElement");
		return element;
	}

	public boolean insetFSTProfilePercentage(String profileName, String percentage) throws TestAutomationException {
		logger.debug("Start of FSTFundTable: insetFSTFundPercentage");

		setProfileTableId(GlobalCSSIdentifiers.PROFILECHECKOUTTABLEID);

		setTable(getProfileTable());

		WebElement profileInputbox = getProfileTableWebElement(profileName, profilePercentageInputBoxIndex);
		WebElementProcessorCommon.scrollToWebElement(browser, profileInputbox);
		profileInputbox.click();
		// .SENDKEYS is not working to input insertFST fund percentage. so
		// extracting the html and changing the value attribute and updating
		// the webelement html
		String html = new HtmlParser(profileInputbox).getHtml();
		String htmlUpdated = Jsoup.parse(html).getElementsByTag(GlobalTags.INPUT)
				.attr(GlobalAttributes.VALUE, percentage).toString();
		((JavascriptExecutor) browser).executeScript("arguments[0].innerHTML = arguments[1]", profileInputbox,
				htmlUpdated);
		logger.debug("End of FSTFundTable: insetFSTFundPercentage");
		return true;
	}

	private String getProfileTableId() {
		return profileTableId;
	}

	private void setProfileTableId(String profileTableId) {
		this.profileTableId = profileTableId;
	}

	private WebElement getTable() {
		return table;
	}

	private void setTable(WebElement table) {
		this.table = table;
	}

	private WebElement getProfileTable() throws TestAutomationException {
		return WebElementRetriever.getWebElementById(browser, "profile table", getProfileTableId());
	}
}
