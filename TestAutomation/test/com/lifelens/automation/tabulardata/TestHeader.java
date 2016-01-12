package com.lifelens.automation.tabulardata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test class for Header.java
 * 
 * @author venkata.kintali(CO54151)
 * 
 * @version 1.0
 * 
 * @since 31.10.2014
 * 
 */

public class TestHeader {

	/* The header instance */
	Header header = null;

	/* The header name */
	private String headerName = "Empolyee Details";
	private int xssColumnNo = 2;

	@Before
	public void createTestData() {
		header = new Header(headerName, xssColumnNo);
	}

	@Test
	public void testConstructor() {

		assertEquals(this.header.hashCode(), header.hashCode());
		assertEquals(0, header.compareTo(header));
		assertTrue(header.equals(header));
		assertEquals(header.xssColumnNo, 2);
		assertEquals(header.headerName, "Empolyee Details");
	}

	@Test
	public void test_CompareTo() {
		assertEquals(0, header.compareTo(header));
		assertFalse(header.compareTo(header) == -1);

		// test for compare to when other object is having different column
		// number
		Header header1 = new Header("Employee Name", 4);
		assertEquals(2, header1.compareTo(header));

		// test for compare to when other object is null or not an instance of
		// header
		assertEquals(-1, header.compareTo(null));
	}

	@Test
	public void test_hashCode() {
		Header header1 = new Header(headerName, xssColumnNo);
		assertEquals(header.hashCode(), header1.hashCode());

		// test for hashcode with null headerName
		Header header2 = new Header(null, xssColumnNo);
		assertEquals(header2.hashCode(), 31);
	}

	@Test
	public void test_Equals() {
		assertTrue(header.equals(header));

		// testing equals with different object with same value
		Header header1 = new Header(headerName, xssColumnNo);
		assertTrue(header.equals(header1));

		// testing for null object
		Object obj = null;
		assertFalse(header.equals(obj));

		// testing for new object instance
		assertFalse(header.equals(new Object()));

		// testing for null header Name
		assertFalse(header.equals(new Header(null, xssColumnNo)));

		// testing equals with different object header name as null
		Header header2 = new Header(null, xssColumnNo);
		assertFalse(header.equals(header2));
	}

	@Test
	public void test_toString() {
		headerName = headerName + " (" + xssColumnNo + ")";
		assertEquals(headerName, header.toString());
	}
}
