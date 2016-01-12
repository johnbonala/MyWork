package com.lifelens.automation.browsers.interpreter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.lifelens.exceptions.TestAutomationException;
import com.lifelens.interpreter.TestContext;
import com.lifelens.testdata.Testdata;
import com.lifelens.testset.TestSet;

/**
 * JUnit test class for TestContext
 * 
 * @author Pratik.Gilda(CO50636)
 * 
 * @version 1.0
 * 
 * @since 22.10.2014
 * 
 */
public class TestTestContext {

	private Testdata testData;

	private TestSet testSet;

	private TestContext testContext;

	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();

	@Before
	public void createTestData() throws InvalidFormatException, IOException, TestAutomationException {
		testData = new Testdata("test/resources/testdata_not_fetched.xlsx", "Sheet1");
		testData.readTestdata();
		testSet = new TestSet(new File("test/resources/SampleScripts.xlsx"), "1", testData);

		testContext = new TestContext(testSet);
	}

	@Test
	public void testConstructor() {
		assertFalse(testContext.isImpersonateScreen());
		assertFalse(testContext.getLoadIndicator());
		assertEquals(testSet, testContext.getTestSet());
		assertNull(testContext.getCurrentDownloadedFile());
	}

	@Test
	public void GIVEN_One_Parameter_Should_Resolve_Parameter() {
		String uiStr = "Total contributions for 9240801";
		String excelStr = "Total contributions for @Username1";

		assertEquals(testContext.resolveParameters(excelStr), uiStr);
	}

	@Test
	public void GIVEN_One_Parameter_With_Some_Extra_Chracters_Should_Resolve_Parameter() {

		// parameter succeeding with dot(.)
		String uiStr = "9240801.";
		String excelStr = "@Username1.";

		assertEquals(testContext.resolveParameters(excelStr), uiStr);

		// parameter succeeding with colon(:)
		uiStr = "9240801:";
		excelStr = "@Username1:";

		assertEquals(testContext.resolveParameters(excelStr), uiStr);

		// parameter succeeding with closing parenthesis
		uiStr = "Your employee number is 9240801)";
		excelStr = "Your employee number is @Username1)";

		assertEquals(testContext.resolveParameters(excelStr), uiStr);

		// email address verification
		uiStr = "David.O'Neill@O2.COM)";
		excelStr = "David.O'Neill@O2.COM)";

		assertEquals(testContext.resolveParameters(excelStr), uiStr);
	}

	@Test
	public void GIVEN_Two_Parameters_Should_Resolve_Parameters() {
		String uiStr = "Employee1 is 9240801 employee2 is 9240802";
		String excelStr = "Employee1 is @Username1 employee2 is @Username2";

		assertEquals(testContext.resolveParameters(excelStr), uiStr);
	}
}
