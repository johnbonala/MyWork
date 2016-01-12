package com.lifelens.webelements;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.lifelens.globals.GlobalAttributes;
import com.lifelens.globals.GlobalCSSIdentifiers;

public class FSTNavigationLink {
	public WebDriver browser;
	private String navigtionLinkName;
	private String[] navLinkClasses = { GlobalCSSIdentifiers.FCRNOTSELECTEDAVAILABLE,
			GlobalCSSIdentifiers.FCR2NOTSELECTEDAVAILABLE };
	private static Logger logger = Logger.getLogger(FSTFundTable.class.getName());

	/**
	 * 
	 * @param browser
	 *            browser object
	 */
	public FSTNavigationLink(WebDriver browser, String navigationLinkName) {
		this.browser = browser;
		this.navigtionLinkName = navigationLinkName;
	}

	public boolean clickOnFSTNavlink() {
		logger.debug("Start of FSTNavigationLink: clickOnFSTNavlink ");

		for (int i = 0; i < navLinkClasses.length; i++) {
			List<WebElement> navLinks = browser.findElements(By.className(navLinkClasses[i]));

			for (int j = 0; j < navLinks.size(); j++) {
				WebElement navlink = navLinks.get(j);
				if (navigtionLinkName.equalsIgnoreCase(getNavLinkValue(navlink))) {
					navlink.click();
					logger.debug("End of FSTNavigationLink: clickOnFSTNavlink ");
					return true;
				}

			}

		}

		logger.error("FST Navigation link : " + navigtionLinkName + " not found in the FST iframe");

		logger.debug("End of FSTNavigationLink: clickOnFSTNavlink ");

		return false;

	}

	private String getNavLinkValue(WebElement navLink) {

		return navLink.getAttribute(GlobalAttributes.VALUE);
	}
}
