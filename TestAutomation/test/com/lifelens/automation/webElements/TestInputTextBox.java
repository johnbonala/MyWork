package com.lifelens.automation.webElements;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;

import com.google.common.collect.ImmutableMap;
import com.lifelens.exceptions.TestAutomationException;
import com.lifelens.interpreter.TestContext;
import com.lifelens.testdata.Testdata;
import com.lifelens.testset.TestSet;
import com.lifelens.webelements.InputTextbox;

/**
 * JUnit test class for InputTextBox
 * 
 * @author Pratik.Gilda(CO50636)
 * 
 * @version 1.0
 * 
 * @since 18.03.2015
 * 
 */
public class TestInputTextBox {

	@Rule
	public TemporaryFolder temporaryFolder = new TemporaryFolder();

	private TestSet testSet;

	private TestContext testContext;

	/**
	 * Set up the data used for the test
	 */
	@Before
	public void createTestData() throws InvalidFormatException, IOException, TestAutomationException {
		File sampleFile = new File("test/resources/TestSampleScripts.xlsx");
		File temporarySampleFile = temporaryFolder.newFile("TemporarySampleScripts.xlsx");

		// copying sample script to temporary file
		FileUtils.copyFile(sampleFile, temporarySampleFile);

		Testdata testData = new Testdata("test/resources/testdata_not_fetched.xlsx", "Sheet1");
		testData.readTestdata();
		testSet = new TestSet(temporarySampleFile, "1", testData);
		testContext = new TestContext(testSet);
	}

	/**
	 * Test method to test constructor
	 * 
	 * @throws TestAutomationException
	 */
	@Test
	public void testConstructor() throws TestAutomationException {
		WebDriver browser = new MockWebDriver();
		String labelName = "test-inputtextbox";
		InputTextbox inputBox = new InputTextbox(browser, labelName, testContext);
		assertNotNull(inputBox);
	}

	/**
	 * Test method to test sendKeys()
	 * 
	 * @throws TestAutomationException
	 */
	@Test
	public void testSendKeys() throws TestAutomationException {
		WebDriver browser = new MockWebDriver();
		String labelName = "test-inputtextbox";

		InputTextbox inputBox = new InputTextbox(browser, labelName, testContext);
		inputBox.setId("test-inputtextbox");
		assertTrue(inputBox.sendKeys("testSendKeys"));

		String inputTextBoxValue = browser.findElement(By.id("test-inputtextbox")).getAttribute("value");
		assertTrue(inputTextBoxValue.equals("testSendKeys"));
	}

	/**
	 * Test method to test verifyDefaultValue()
	 * 
	 * TO DO : Need to work on verify default value
	 * 
	 * @throws TestAutomationException
	 */
	/*
	 * @Test public void testVerifyDefaultValue() throws TestAutomationException
	 * { WebDriver browser = new MockWebDriver(); String labelName =
	 * "test-inputtextbox";
	 * 
	 * InputTextbox inputBox = new InputTextbox(browser, labelName,
	 * testContext); inputBox.setId("test-inputtextbox");
	 * assertTrue(inputBox.sendKeys("testSendKeys"));
	 * assertTrue(inputBox.verifyDefaultValue("testSendKeys")); }
	 */

	/***
	 * Mock class for web driver
	 * 
	 * @author CO50636
	 */
	private class MockWebDriver extends RemoteWebDriver implements WebDriver {

		@Override
		public void get(String url) {
			// TODO Auto-generated method stub

		}

		@Override
		public String getCurrentUrl() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getTitle() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<WebElement> findElements(By by) {
			List<WebElement> elements = new ArrayList<WebElement>();
			MockWebElement wb = new MockWebElement();
			// wb.setParent(this);
			elements.add(wb);
			return elements;
		}

		@Override
		public WebElement findElement(By by) {
			MockWebElement wb = new MockWebElement();
			// wb.setParent(this);
			return wb;
		}

		@Override
		public String getPageSource() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void close() {
			// TODO Auto-generated method stub

		}

		@Override
		public void quit() {
			// TODO Auto-generated method stub

		}

		@Override
		public Set<String> getWindowHandles() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getWindowHandle() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public TargetLocator switchTo() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Navigation navigate() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Options manage() {

			return new RemoteWebDriverOptions() {
				@Override
				public Timeouts timeouts() {
					return new RemoteTimeouts() {
						public Timeouts implicitlyWait(long time, TimeUnit unit) {
							execute(DriverCommand.SET_TIMEOUT, ImmutableMap.of("type", "implicit", "ms",
									TimeUnit.MILLISECONDS.convert(time, unit)));
							return this;
						}

						public Timeouts setScriptTimeout(long time, TimeUnit unit) {
							execute(DriverCommand.SET_TIMEOUT,
									ImmutableMap.of("type", "script", "ms", TimeUnit.MILLISECONDS.convert(time, unit)));
							return this;
						}
					};
				}
			};

		}

	}

	/***
	 * Mock class for web element
	 * 
	 * @author CO50636
	 */
	private static class MockWebElement extends RemoteWebElement implements WebElement {

		private CharSequence[] sentKeys;
		private String attribute;

		@Override
		public void click() {
			// TODO Auto-generated method stub

		}

		public void setParent(RemoteWebDriver parent) {
			this.parent = parent;
		}

		@Override
		public void submit() {
			// TODO Auto-generated method stub

		}

		@Override
		public void sendKeys(CharSequence... keysToSend) {
			sentKeys = keysToSend;
		}

		@Override
		public void clear() {
			// TODO Auto-generated method stub

		}

		@Override
		public String getTagName() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getAttribute(String name) {
			attribute = "testSendKeys";
			return attribute;
		}

		@Override
		public boolean isSelected() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isEnabled() {
			return true;
		}

		@Override
		public String getText() {
			return sentKeys.toString();
		}

		@Override
		public List<WebElement> findElements(By by) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public WebElement findElement(By by) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean isDisplayed() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Point getLocation() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Dimension getSize() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getCssValue(String propertyName) {
			// TODO Auto-generated method stub
			return null;
		}
	}
}
