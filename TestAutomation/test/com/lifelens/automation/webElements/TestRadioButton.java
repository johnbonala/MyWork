package com.lifelens.automation.webElements;

import static org.junit.Assert.assertNotNull;

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
import com.lifelens.webelements.RadioButton;

/**
 * JUnit test class for RadioButton
 * 
 * @author Pratik.Gilda(CO50636)
 * 
 * @version 1.0
 * 
 * @since 24.03.2015
 * 
 */
public class TestRadioButton {

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
		WebDriver browser = new MockWebDriver("true");
		String labelName = "test-inputtextbox";
		RadioButton radioButton = new RadioButton(browser, testContext, labelName);
		assertNotNull(radioButton);
	}

	/**
	 * Test method to check whether the checkbox is unticked or not when "False"
	 * value is passed
	 * 
	 * @throws TestAutomationException
	 */
	/*
	 * @Test public void testSelectUnChecking() throws TestAutomationException {
	 * WebDriver browser = new MockWebDriver("false"); String labelName =
	 * "test-radiobutton"; RadioButton radioButton = new RadioButton(browser,
	 * testContext, labelName); assertTrue(radioButton.select()); }
	 */

	/***
	 * Mock class for web driver
	 * 
	 * @author CO50636
	 */
	private class MockWebDriver extends RemoteWebDriver implements WebDriver {

		private String booleanValue;

		public MockWebDriver(String booleanValue) {
			this.booleanValue = booleanValue;
		}

		@Override
		public void get(String url) {
			this.get("https://test5vm.vebnet.com/ReFlexWeb/auto_slu.originSL/public/page/login");
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
			MockWebElement wb = new MockWebElement(booleanValue);
			wb.setParent(this);
			elements.add(wb);
			return elements;
		}

		@Override
		public WebElement findElement(By by) {
			MockWebElement wb = new MockWebElement(booleanValue);
			wb.setParent(this);
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
		private String booleanValue;

		public MockWebElement(String booleanValue) {
			this.booleanValue = booleanValue;
		}

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
			attribute = "test-checkbox";
			return attribute;
		}

		@Override
		public boolean isSelected() {
			if (booleanValue != null && (booleanValue.equalsIgnoreCase("True") || booleanValue.equalsIgnoreCase(""))) {
				return true;
			}
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
			List<WebElement> elements = new ArrayList<WebElement>();
			MockWebElement wb = new MockWebElement(booleanValue);
			// wb.setParent(this);
			elements.add(wb);
			return elements;
		}

		@Override
		public WebElement findElement(By by) {
			MockWebElement wb = new MockWebElement(booleanValue);
			return wb;
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
