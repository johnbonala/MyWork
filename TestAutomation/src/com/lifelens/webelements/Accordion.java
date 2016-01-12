package com.lifelens.webelements;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

import com.lifelens.browsers.WebElementProcessorCommon;
import com.lifelens.exceptions.TestAutomationException;
import com.lifelens.globals.GlobalTags;

public class Accordion extends RemoteWebElement {

	private String accordionLabel;
	public WebDriver browser;
	private static Logger logger = Logger.getLogger(Accordion.class.getName());

	public Accordion(WebDriver browser, String accordionLabel) {
		this.browser = browser;
		this.accordionLabel = accordionLabel;
	}

	public boolean open() throws TestAutomationException {
		logger.debug("Start of Accordion: open ");
		String[] identifiers = { "expandoContainer", "dijitAccordionTitleFocus" };
		List<WebElement> accordionElements;
		String accordionHeader;
		boolean isOpen = false;
		for (int round = 0; round < identifiers.length; round++) {
			accordionElements = WebElementRetriever.getWebElementsByClassName(browser, accordionLabel,
					identifiers[round]);
			Iterator<WebElement> allAccordions = accordionElements.iterator();

			while (allAccordions.hasNext()) {
				WebElement accordion = allAccordions.next();
				InnerElement innerElement = new InnerElement(accordion);

				if (round == 0) {
					accordionHeader = innerElement.getHeader("h2");
					if (accordionLabel.equalsIgnoreCase(accordionHeader)) {
						WebElementRetriever.getWebElementByTagName(accordion, accordionLabel, GlobalTags.A).click();
						isOpen = true;
					}
				} else {
					accordionHeader = WebElementRetriever.getWebElementByClassName(accordion, accordionLabel,
							"dijitAccordionText").getText();
					if (accordionLabel.equalsIgnoreCase(accordionHeader)) {
						if (!accordion.getAttribute("aria-expanded").equalsIgnoreCase("false")) {
							logger.debug("Accordion : " + accordionLabel + " alreday opened");
						} else {
							accordion.click();
						}
						isOpen = true;
					}
				}
			}
		}
		WebElementProcessorCommon.logWebElementStatus("Accordion", accordionLabel, isOpen);

		logger.debug("End of Accordion: open ");
		return isOpen;
	}
}
