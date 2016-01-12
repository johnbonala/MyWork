package com.lifelens.webelements;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

import com.lifelens.exceptions.TestAutomationException;
import com.lifelens.globals.GlobalAttributes;
import com.lifelens.htmlparser.HtmlParser;

public class InnerElement extends RemoteWebElement {

	WebElement we;
	HtmlParser htmlparser;

	public InnerElement(WebElement we) {
		this.we = we;
		htmlparser = new HtmlParser(we);
	}

	public String getText() {
		return htmlparser.getinnerText();
	}

	public String getHeader(String headerTag) {
		return htmlparser.getTagElement(headerTag).text();
	}

	public String getAttribute(String attribute) {
		if (we.getAttribute(attribute) == null)
			return "";
		else
			return we.getAttribute(attribute);
	}

	public String getVisibleName() {

		String visibleName = getText();

		if (visibleName.isEmpty())
			if (!getAttribute(GlobalAttributes.VALUE).isEmpty())
				visibleName = getAttribute(GlobalAttributes.VALUE).trim();
		return visibleName;
	}

	public boolean hasTag(String tagName) throws TestAutomationException {

		return (getTagElement(tagName) == null ? false : true);
	}

	public boolean hasAttribute(String attribute) {

		return (getAttribute(attribute).isEmpty() ? false : true);
	}

	public WebElement getTagElement(String tagName) throws TestAutomationException {

		List<WebElement> tagElements = WebElementRetriever.getWebElementsByTagName(we, "Table Element", tagName);
		return tagElements.size() == 0 ? null : tagElements.get(0);

	}

}