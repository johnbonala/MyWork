package com.lifelens.exceptions;

public class TestAutomationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code;

	/**
	 * Test Automation Exception
	 */
	public TestAutomationException() {
	}

	/**
	 * @param errorCode
	 * @param message
	 */
	public TestAutomationException(final String errorCode, final String message) {
		super(message);
		this.code = errorCode;
	}

	/**
	 * 
	 * @param errorCode
	 * @param message
	 * @param cause
	 */
	public TestAutomationException(final String errorCode, final String message, final Throwable cause) {
		super(message, cause);
		this.code = errorCode;
	}

	/**
	 * Returns the value of code as a String.
	 * 
	 * @return value of code
	 */
	public String getCode() {
		return this.code;
	}

}
