package com.lifelens.testdata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.lifelens.webelements.verify.ElementList;

/**
 * JUnit test class for ElementList
 * 
 * @author venkata.kintali(C054151)
 * 
 * @version 1.0
 * 
 * @since 11.11.2014
 * 
 */
public class TestElementList {

	@Test
	public void testGetSearchType() {
		assertEquals("tag", ElementList.H1.getSearchType());
		assertEquals("tag", ElementList.H2.getSearchType());
		assertEquals("tag", ElementList.H3.getSearchType());
		assertEquals("tag", ElementList.HYPERLINK.getSearchType());
		assertEquals("class", ElementList.WIZARD_LABEL.getSearchType());
		assertEquals("byLabel", ElementList.RADIO_BUTTON.getSearchType());
		assertEquals("id", ElementList.FST_WIZARD_LABEL.getSearchType());
		assertEquals("class", ElementList.ACCORDIAN.getSearchType());
		assertEquals("tag", ElementList.BUTTON.getSearchType());
		assertEquals("byLabel", ElementList.CHECKBOX.getSearchType());
		assertEquals("other", ElementList.XPATH.getSearchType());
		assertEquals("other", ElementList.CSS.getSearchType());
		assertEquals("other", ElementList.CLASS.getSearchType());
	}

	@Test
	public void testGetTagValue() {
		assertEquals("h1", ElementList.H1.getTagValue());
		assertEquals("h2", ElementList.H2.getTagValue());
		assertEquals("h3", ElementList.H3.getTagValue());
		assertEquals("a", ElementList.HYPERLINK.getTagValue());
		assertEquals("wizardStep", ElementList.WIZARD_LABEL.getTagValue());
		assertEquals("radio", ElementList.RADIO_BUTTON.getTagValue());
		assertEquals("progress", ElementList.FST_WIZARD_LABEL.getTagValue());
		assertEquals("expandableAreaOpen", ElementList.ACCORDIAN.getTagValue());
		assertEquals("button", ElementList.BUTTON.getTagValue());
		assertEquals("checkbox", ElementList.CHECKBOX.getTagValue());
		assertEquals("xpath", ElementList.XPATH.getTagValue());
		assertEquals("css", ElementList.CSS.getTagValue());
		assertEquals("class", ElementList.CLASS.getTagValue());
	}

	@Test
	public void testEnumValuesAndTagNames() {
		for (ElementList tag : ElementList.values()) {
			assertNotNull(ElementList.getTagName(tag.toString()));
			assertNotNull(tag.getSearchType());
		}
	}

	@Test
	public void testGetTagName() {
		for (ElementList tag : ElementList.values()) {
			assertEquals(tag, ElementList.getTagName(tag.toString()));
		}
	}

	@Test
	public void testGetTagNameParamAsNull() {
		assertNull(ElementList.getTagName(null));
	}

	@Test
	public void testSetAndGetSearchType() {
		ElementList.H1.setSearchType("class");
		assertEquals("class", ElementList.H1.getSearchType());

		ElementList.H1.setSearchType("tag");
		assertEquals("tag", ElementList.H1.getSearchType());
	}
}
