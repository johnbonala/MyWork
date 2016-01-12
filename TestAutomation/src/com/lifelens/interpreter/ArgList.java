package com.lifelens.interpreter;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.lifelens.exceptions.ExceptionErrorCodes;
import com.lifelens.exceptions.TestAutomationException;
import com.lifelens.globals.GlobalCommonConstants;

/**
 * This represents the Argument list. A list of arguments can be passed as a
 * single argument to a method. Argument list is expected in the following
 * format. Arguments should be
 * 
 * 1. separated by commas
 * 
 * 2. optionally enclosed in braces
 * 
 * 3. optional preceded by DecisionTable or Table
 * 
 * Eg.
 * 
 * 1,3,2
 * 
 * (1,3,2)
 * 
 * (1)
 * 
 * (1,4,3,5) etc
 * 
 * 
 * It reads the argument list, split, convert them as integers (if they are
 * integers) and returns to the calling module.
 * 
 * @author Srinivas Pasupulati (co48633)
 * 
 * @version 1.0
 * 
 * @since 06.05.2014
 */
public class ArgList {

	private static final String OPEN_BRACKET = "(";
	private static final String CLOSE_BRACKET = ")";
	private static final String DECISION_TABLE = "DecisionTable";
	private static final String TABLE = "Table";
	private static final String DELIMITERS = ",";
	private static final String LABEL_SEPERATOR = ":";
	private static Logger logger = Logger.getLogger(ArgList.class.getName());

	private String arglistString;
	private String nonTableArgListString;

	private String[] argParams;
	private String[] tableArgParams;
	private String[] decisionTableArgNameArray;
	private String[] decisionTableArgValueArray;

	/**
	 * Create new argument list with a string value. Whitespace will be trimmed
	 * from the argument.
	 * 
	 * @param argumentlist
	 * @throws TestAutomationException
	 */
	public ArgList(String argumentlist) throws TestAutomationException {

		if (argumentlist == null) {
			throw new TestAutomationException(ExceptionErrorCodes.TA023_ARG_LIST_INVALID_INPUT_ARGUMENTS_ERROR,
					"Input arguments passed are null");
		}

		arglistString = argumentlist.trim();

		splitValues();

	}

	/**
	 * Removes the braces and split them into a string array. Any Table(...) or
	 * DecisionTable(...) parameters are put into a separate array.
	 * 
	 * @throws TestAutomationException
	 */
	private void splitValues() throws TestAutomationException {
		logger.debug("Start of ArgList: splitvalues");

		nonTableArgListString = arglistString;

		if (hasDecisionTableKeyword() || hasTableKeyword()) {

			// Take last index for ")" from the arg list to support the names
			// with closing parenthesis in between
			int indexOfEndOfTableParam = arglistString.lastIndexOf(GlobalCommonConstants.CLOSINGPARENTHESES);
			String tableArgListString = arglistString.substring(0, indexOfEndOfTableParam + 1);
			nonTableArgListString = nonTableArgListString.substring(indexOfEndOfTableParam + 1).trim();
			if (nonTableArgListString.startsWith(",")) {
				nonTableArgListString = nonTableArgListString.substring(1);
			}

			if (hasDecisionTableKeyword())
				tableArgListString = tableArgListString.replaceAll(DECISION_TABLE, "").trim();
			else if (hasTableKeyword())
				tableArgListString = tableArgListString.replaceAll(TABLE, "").trim();

			tableArgListString = stripBracketsFromArgList(tableArgListString);

			tableArgParams = tableArgListString.split(DELIMITERS);
			for (int i = 0; i < tableArgParams.length; i++) {
				tableArgParams[i] = tableArgParams[i].trim();
			}
		}

		nonTableArgListString = stripBracketsFromArgList(nonTableArgListString);

		if (nonTableArgListString.isEmpty()) {
			argParams = new String[0];
		} else {
			argParams = nonTableArgListString.split(DELIMITERS);
		}

		for (int i = 0; i < argParams.length; i++) {
			argParams[i] = argParams[i].trim();
		}

		logger.debug("End of ArgList: splitvalues");
	}

	/**
	 * Return the integer value at the array index location.
	 * 
	 * @param index
	 *            index of the array
	 * @return integer value at the array index location
	 * @throws TestAutomationException
	 */
	public int getNumericParameter(int index) throws TestAutomationException {
		logger.debug("Start of ArgList: getNumericParameter");
		if (index >= argParams.length) {
			throw new TestAutomationException(ExceptionErrorCodes.TA024_ARG_LIST_INVALID_INPUT_ARGUMENTS_LENGTH_ERROR,
					" argParams length is " + argParams.length + ". But requested to get the value at index " + index);
		}
		logger.debug("End of ArgList: getNumericParameter");
		return convertToInteger(argParams[index]);
	}

	/**
	 * Return the integer value at the array index location.
	 * 
	 * @param index
	 *            index of the array
	 * @return integer value at the array index location
	 * @throws TestAutomationException
	 */
	public int getNumericTableParameter(int index) throws TestAutomationException {
		logger.debug("Start of ArgList: getNumericTableParameter");
		if (index >= tableArgParams.length) {
			throw new TestAutomationException(ExceptionErrorCodes.TA024_ARG_LIST_INVALID_INPUT_ARGUMENTS_LENGTH_ERROR,
					" tableArgParams length is " + tableArgParams.length + ". But requested to get the value at index "
							+ index);
		}
		logger.debug("End of ArgList: getNumericTableParameter");
		return convertToInteger(tableArgParams[index]);
	}

	/*
	 * Return the string value at the array index location.
	 * 
	 * @param index index of the array
	 * 
	 * @return String value of the array index location
	 * 
	 * @throws TestAutomationException
	 */
	public String getParameter(int index) throws TestAutomationException {
		logger.debug("Start of ArgList: getParameter");
		if (index >= argParams.length)
			throw new TestAutomationException(ExceptionErrorCodes.TA024_ARG_LIST_INVALID_INPUT_ARGUMENTS_LENGTH_ERROR,
					" argParams length is " + argParams.length + ". But requested to get the value at index " + index);
		logger.debug("End of ArgList: getParameter");
		return argParams[index];
	}

	/*
	 * Return the string value at the array index location.
	 * 
	 * @param index index of the array
	 * 
	 * @return String value of the array index location
	 * 
	 * @throws TestAutomationException
	 */
	public String getTableParameter(int index) throws TestAutomationException {
		logger.debug("Start of ArgList: getParameter");
		if (index >= tableArgParams.length)
			throw new TestAutomationException(ExceptionErrorCodes.TA024_ARG_LIST_INVALID_INPUT_ARGUMENTS_LENGTH_ERROR,
					" tableArgParams length is " + tableArgParams.length + ". But requested to get the value at index "
							+ index);
		logger.debug("End of ArgList: getParameter");
		return tableArgParams[index];
	}

	public List<String> getArgumentList() {
		return Arrays.asList(argParams);
	}

	/**
	 * @return number of arguments in the argument list
	 */
	public int getParmetersCount() {
		return argParams.length;
	}

	/**
	 * Checks if the given string is an integer value.
	 * 
	 * @param str
	 *            string
	 * @return true if the string has integers alone false if the string has
	 *         characters
	 */
	private static boolean isNumeric(String str) {

		return str.matches("-?\\d+(\\.\\d+)?"); // match a number with optional
												// '-' and decimal.
	}

	/**
	 * Convert String to integer.
	 * 
	 * @param token
	 *            String
	 * @return int value of the given string
	 * @throws TestAutomationException
	 *             if the given string has non numeric characters, exception
	 *             will be thrown
	 */

	private int convertToInteger(String token) throws TestAutomationException {
		logger.debug("Start of ArgList: convertToInteger");
		if (isNumeric(token.trim())) {
			return Integer.parseInt(token.trim());
		} else {
			throw new TestAutomationException(ExceptionErrorCodes.TA025_ARG_LIST_INPUT_ARGUMENT_IS_NOT_A_INTEGER_ERROR,
					"Input parameter is not an Integer: '" + token.trim()
							+ "' error occured while converting to Integer");
		}
	}

	/**
	 * Strip matching first & last brackets from argument list
	 * 
	 * @param argumentList
	 *            Argument list
	 * @return argument list with no surrounding brackets
	 * @throws TestAutomationException
	 */
	private String stripBracketsFromArgList(String argumentList) throws TestAutomationException {

		// if brackets do not match up then throw error
		// Can't have this check, as e.g. "Table(1,2,3),False" gets converted to
		// "(1,2,3),False" which would then fail.
		// if (argumentList.trim().startsWith(OPEN_BRACKET) &&
		// !argumentList.trim().endsWith(CLOSE_BRACKET)) {
		// throw new
		// TestAutomationException(ExceptionErrorCodes.TA023_ARG_LIST_INVALID_INPUT_ARGUMENTS_ERROR,
		// "Brackets enclosing argument do not match up - argument must have opening and closing barcket - argument: "
		// + argumentList);
		// }

		// if start with open bracket and end with close bracket - then strip
		if (argumentList.trim().startsWith(OPEN_BRACKET) && argumentList.trim().endsWith(CLOSE_BRACKET)) {
			String argListWithNoBrackets = argumentList.substring(1, argumentList.length() - 1);
			return argListWithNoBrackets;
		} else {
			return argumentList;
		}
	}

	private boolean hasLabel(String parameter) {
		return parameter.contains(LABEL_SEPERATOR) ? true : false;
	}

	private String getLabel(String parameter) {
		return parameter.substring(0, parameter.indexOf(LABEL_SEPERATOR));
	}

	private String getLabelValue(String parameter) {
		return parameter.substring(parameter.indexOf(LABEL_SEPERATOR) + 1);
	}

	public void extractDecisionTableNameAndValueParameters() throws TestAutomationException {

		String[] argLabelsArray = new String[tableArgParams.length];
		String[] argLabelValueArray = new String[tableArgParams.length];
		int index = 0;

		for (String param : tableArgParams) {
			if (hasLabel(param)) {
				argLabelsArray[index] = getLabel(param).trim();
				argLabelValueArray[index++] = getLabelValue(param).trim();
			} else
				throw new TestAutomationException(
						ExceptionErrorCodes.TA101_DECISION_TABLE_PARAMETERS_NOT_IN_EXPECTED_FORMAT,
						"Decison table parameters are not in expected format: " + arglistString);
		}

		decisionTableArgNameArray = argLabelsArray;
		decisionTableArgValueArray = argLabelValueArray;

	}

	public boolean hasDecisionTableKeyword() {
		return (arglistString.startsWith(DECISION_TABLE) ? true : false);
	}

	public boolean hasTableKeyword() {
		return (arglistString.startsWith(TABLE) ? true : false);
	}

	public String[] getDecisionTableArgNameArray() {
		return decisionTableArgNameArray;
	}

	public String[] getDecisionTableArgValueArray() {
		return decisionTableArgValueArray;
	}

	public String getArglistString() {
		return arglistString;
	}

	/*
	 * Return the string value at the array index location of non table
	 * paramenter.(e.g. Table(1,2,3),4 : will return 4) 
	 * 
	 * @throws TestAutomationException
	 */
	public String getNonTableArgumentListString() throws TestAutomationException {
		return nonTableArgListString;
	}
}
