package com.lifelens.automation.browsers.interpreter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.lifelens.exceptions.TestAutomationException;
import com.lifelens.interpreter.ArgList;

public class TestArgList {

	@Test(expected = TestAutomationException.class)
	public void GIVEN_null_argument_list_SHOULD_throw_exception() throws TestAutomationException {

		new ArgList(null);

	}

	@Test
	public void GIVEN_empty_argument_list_SHOULD_have_no_values() throws TestAutomationException {

		ArgList args = new ArgList("");
		assertEquals(0, args.getParmetersCount());

	}

	@Test
	public void GIVEN_argument_list_with_more_than_one_closing_brackets_SHOULD_have_no_values()
			throws TestAutomationException {

		ArgList args = new ArgList("()");
		assertEquals(0, args.getParmetersCount());

	}

	@Test
	public void GIVEN_argument_list_with_no_brackets_SHOULD_be_happy() throws TestAutomationException {

		new ArgList("1, 2, 3");

	}

	@Test
	public void GIVEN_argument_list_with_brackets_SHOULD_be_happy() throws TestAutomationException {

		ArgList args = new ArgList("(1, 2, 3)");
		assertEquals(3, args.getParmetersCount());
		assertEquals(1, args.getNumericParameter(0));
		assertEquals(2, args.getNumericParameter(1));
		assertEquals(3, args.getNumericParameter(2));

	}

	@Test
	public void GIVEN_argument_list_with_only_opening_bracket_SHOULD_be_happy() throws TestAutomationException {

		new ArgList("(1, 2, 3");

	}

	@Test
	public void GIVEN_argument_list_with_3_numeric_parmamters_SHOULD_have_3_values() throws TestAutomationException {

		ArgList args = new ArgList("1, 2, 3");
		assertEquals(3, args.getParmetersCount());
		assertEquals(1, args.getNumericParameter(0));
		assertEquals(2, args.getNumericParameter(1));
		assertEquals(3, args.getNumericParameter(2));

	}

	@Test
	public void GIVEN_argument_list_with_3_string_parmamters_SHOULD_have_3_values() throws TestAutomationException {

		ArgList args = new ArgList("one, two, three");
		assertEquals(3, args.getParmetersCount());
		assertEquals("one", args.getParameter(0));
		assertEquals("two", args.getParameter(1));
		assertEquals("three", args.getParameter(2));

	}

	@Test(expected = TestAutomationException.class)
	public void GIVEN_argument_list_with_string_parmamters_SHOULD_fail_if_ask_for_numeric_parameter()
			throws TestAutomationException {

		ArgList args = new ArgList("one, two, three");
		assertEquals("one", args.getNumericParameter(0));

	}

	@Test
	public void GIVEN_argument_list_with_Table_SHOULD_have_correct_values() throws TestAutomationException {

		ArgList args = new ArgList("Table (1,2 , 3)");
		assertEquals(0, args.getParmetersCount());
		assertEquals("Table (1,2 , 3)", args.getArglistString());
		assertEquals(1, args.getNumericTableParameter(0));
		assertEquals(2, args.getNumericTableParameter(1));
		assertEquals(3, args.getNumericTableParameter(2));
		assertTrue(args.hasTableKeyword());
		assertTrue(!args.hasDecisionTableKeyword());

		args = new ArgList("Table (1,2 , 3), word");
		assertEquals(1, args.getParmetersCount());
		assertEquals("Table (1,2 , 3), word", args.getArglistString());
		assertEquals(1, args.getNumericTableParameter(0));
		assertEquals(2, args.getNumericTableParameter(1));
		assertEquals(3, args.getNumericTableParameter(2));
		assertEquals("word", args.getParameter(0));
		assertTrue(args.hasTableKeyword());
		assertTrue(!args.hasDecisionTableKeyword());

		args = new ArgList("Table (1,2 ,3), more,words  , last one!  ");
		assertEquals(3, args.getParmetersCount());
		assertEquals("Table (1,2 ,3), more,words  , last one!", args.getArglistString());
		assertEquals(1, args.getNumericTableParameter(0));
		assertEquals(2, args.getNumericTableParameter(1));
		assertEquals(3, args.getNumericTableParameter(2));
		assertEquals("more", args.getParameter(0));
		assertEquals("words", args.getParameter(1));
		assertEquals("last one!", args.getParameter(2));
		assertTrue(args.hasTableKeyword());
		assertTrue(!args.hasDecisionTableKeyword());
	}

	@Test
	public void GIVEN_argument_list_with_closing_brackets_SHOULD_have_seperated_values() throws TestAutomationException {

		ArgList args = new ArgList("DecisionTable (Option (cost voucher):Option 1, :<Employee cost>)");

		args.extractDecisionTableNameAndValueParameters();
		assertEquals(0, args.getParmetersCount());
		assertEquals("Option (cost voucher)", args.getDecisionTableArgNameArray()[0]);
		assertEquals("<Employee cost>", args.getDecisionTableArgValueArray()[1]);
		assertTrue(!args.hasTableKeyword());
		assertTrue(args.hasDecisionTableKeyword());
	}

	@Test
	public void GIVEN_argument_list_with_DecisionTable_SHOULD_have_3_values() throws TestAutomationException {

		ArgList args = new ArgList(
				"DecisionTable(DimensionName1 : DimensionValue1, DimensionName2 : DimensionValue2, DimensionName3 : DimensionValue3)");
		args.extractDecisionTableNameAndValueParameters();
		assertEquals(0, args.getParmetersCount());
		assertEquals(
				"DecisionTable(DimensionName1 : DimensionValue1, DimensionName2 : DimensionValue2, DimensionName3 : DimensionValue3)",
				args.getArglistString());
		assertEquals("DimensionName1", args.getDecisionTableArgNameArray()[0]);
		assertEquals("DimensionValue1", args.getDecisionTableArgValueArray()[0]);
		assertEquals("DimensionName2", args.getDecisionTableArgNameArray()[1]);
		assertEquals("DimensionValue2", args.getDecisionTableArgValueArray()[1]);
		assertEquals("DimensionName3", args.getDecisionTableArgNameArray()[2]);
		assertEquals("DimensionValue3", args.getDecisionTableArgValueArray()[2]);
		assertTrue(!args.hasTableKeyword());
		assertTrue(args.hasDecisionTableKeyword());

		args = new ArgList(
				"DecisionTable(DimensionName1 : DimensionValue1, DimensionName2 : DimensionValue2, DimensionName3 : DimensionValue3) ,word");
		args.extractDecisionTableNameAndValueParameters();
		assertEquals(1, args.getParmetersCount());
		assertEquals(
				"DecisionTable(DimensionName1 : DimensionValue1, DimensionName2 : DimensionValue2, DimensionName3 : DimensionValue3) ,word",
				args.getArglistString());
		assertEquals("DimensionName1", args.getDecisionTableArgNameArray()[0]);
		assertEquals("DimensionValue1", args.getDecisionTableArgValueArray()[0]);
		assertEquals("DimensionName2", args.getDecisionTableArgNameArray()[1]);
		assertEquals("DimensionValue2", args.getDecisionTableArgValueArray()[1]);
		assertEquals("DimensionName3", args.getDecisionTableArgNameArray()[2]);
		assertEquals("DimensionValue3", args.getDecisionTableArgValueArray()[2]);
		assertEquals("word", args.getParameter(0));
		assertTrue(!args.hasTableKeyword());
		assertTrue(args.hasDecisionTableKeyword());

		args = new ArgList(
				"DecisionTable(DimensionName1 : DimensionValue1, DimensionName2 : DimensionValue2) , lots ,  more,words,!");
		args.extractDecisionTableNameAndValueParameters();
		assertEquals(4, args.getParmetersCount());
		assertEquals(
				"DecisionTable(DimensionName1 : DimensionValue1, DimensionName2 : DimensionValue2) , lots ,  more,words,!",
				args.getArglistString());
		assertEquals("DimensionName1", args.getDecisionTableArgNameArray()[0]);
		assertEquals("DimensionValue1", args.getDecisionTableArgValueArray()[0]);
		assertEquals("DimensionName2", args.getDecisionTableArgNameArray()[1]);
		assertEquals("DimensionValue2", args.getDecisionTableArgValueArray()[1]);
		assertEquals("lots", args.getParameter(0));
		assertEquals("more", args.getParameter(1));
		assertEquals("words", args.getParameter(2));
		assertEquals("!", args.getParameter(3));
		assertTrue(!args.hasTableKeyword());
		assertTrue(args.hasDecisionTableKeyword());
	}
}
