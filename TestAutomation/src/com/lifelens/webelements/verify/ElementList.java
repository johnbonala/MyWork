package com.lifelens.webelements.verify;

/**
 * Enum holding values for the element value and the search type associated with
 * it.
 * 
 * @author Alok Chatterji
 * 
 */
public enum ElementList {

	H1("h1", "tag"),

	H2("h2", "tag"), H3("h3", "tag"),

	HYPERLINK("a", "tag"),

	WIZARD_LABEL("wizardStep", "class"),

	FST_WIZARD_LABEL("progress", "id"),

	CAROUSEL("carouselContent", "class"),

	ACCORDIAN("expandableAreaOpen", "class"),

	BUTTON("button", "tag"),

	CHECKBOX("checkbox", "byLabel"),

	RADIO_BUTTON("radio", "byLabel"),

	LABEL("radio", "byLabel"),

	XPATH("xpath", "other"),

	CSS("css", "other"),

	CLASS("class", "other");

	private String elementValue;
	private String searchType;

	ElementList(String valueParam, String typeParam) {
		this.setTagValue(valueParam);
		this.setSearchType(typeParam);
	}

	public String getTagValue() {
		return elementValue;
	}

	private void setTagValue(String tagValue) {
		this.elementValue = tagValue;
	}

	public static ElementList getTagName(String tagInput) {
		if (tagInput != null)
			for (ElementList tag : ElementList.values()) {
				if (tag.toString().equalsIgnoreCase(tagInput))
					return tag;
			}
		return null;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
}
