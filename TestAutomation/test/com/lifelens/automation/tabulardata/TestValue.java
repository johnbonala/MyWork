package com.lifelens.automation.tabulardata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * JUnit test class for Value
 * 
 * @author Pratik.Gilda(CO50636)
 * 
 * @version 1.0
 * 
 * @since 22.10.2014
 * 
 */
public class TestValue {

	@Test
	public void testConstructor() {
		Value value = new Value("The Value");
		assertEquals("The Value", value.toString());
		assertEquals(new Value("??"), Value.acceptAny());
	}

	@Test
	public void testEqualsAndHashCode() {
		Value value1 = new Value("The Value");
		Value value2 = new Value("The Value");

		assertTrue(value1.equals(value2));
		assertEquals(value1.hashCode(), value2.hashCode());
	}
}
