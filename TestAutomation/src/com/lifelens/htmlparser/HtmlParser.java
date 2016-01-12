package com.lifelens.htmlparser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.WebElement;

public class HtmlParser {

	private WebElement we;
	public String html;
	public Element element;
	private Document doc;

	public HtmlParser(WebElement we) {
		this.setWe(we);
		this.setHtml();
		this.doc = Jsoup.parse(html);
	}

	public String getinnerText() {
		return doc.body().text();
	}

	public WebElement getWe() {
		return we;
	}

	public void setWe(WebElement we) {
		this.we = we;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml() {
		this.html = we.getAttribute("innerHTML");
	}

	public Element getTagElement(String tagName) {
		return doc.getElementsByTag(tagName).first();
	}

	public String getAttributeValue(String tagName, String attributeKey) {
		if (!hasTag(tagName))
			return "";
		else
			return doc.getElementsByTag(tagName).first().attr(attributeKey);
	}

	public boolean hasTag(String tagName) {

		return (html.contains("<" + tagName) || html.contains("<" + tagName.toUpperCase()));
	}

	public boolean hasAttribute(String attribute) {

		return (getAttribute(attribute).isEmpty() ? false : true);
	}

	public String getAttribute(String attribute) {
		if (we.getAttribute(attribute) == null)
			return "";
		else
			return we.getAttribute(attribute);
	}

}
