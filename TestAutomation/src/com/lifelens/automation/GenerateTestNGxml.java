package com.lifelens.automation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.lifelens.exceptions.TestAutomationException;
import com.lifelens.globals.Global;
import com.lifelens.testset.TestLab;

/**
 * It generates the testng.xml file which can drive the framework to execute all
 * the testsets present in Testlab.
 * 
 * 1. It calls testlab module to extract all testsets from testlab.
 * 
 * 2. Create a testsuite xml (testng.xml) file in properties folder.
 * 
 * 3. Each test in teng.xml contains one testset and priority filter (Critical,
 * High, Medium, Low or ALL).
 * 
 * 4. Priority filter used to run only the test cases with that priority
 * Priority of the test case is available in index sheet of testset.
 * 
 * 
 * @author Srinivas Pasupulati (CO48633)
 * @version 1.0
 * @Since 02.05.2014
 * 
 */

public class GenerateTestNGxml {

	static Logger logger = Logger.getLogger(GenerateTestNGxml.class.getName());

	private static String rootElementName = "suite";
	private static String nameElement = "name";
	private static String valueElement = "value";
	private static String parameterElement = "parameter";
	private static String classesElement = "classes";
	private static String classElement = "class";
	private static String testElement = "test";
	private static String testNGParallel = "parallel";
	private static String testNGThreadCount = "thread-count";

	public static void main(String argv[]) throws Exception {
		logger.info("Start GenerateTestNGxml main with testLab: " + argv[0] + " and testNGPath: " + argv[1]);

		try {
			ProperitesFile configProperties = new ProperitesFile(Global.propertiesFileName);
			configProperties.setSettings();

			if (StringUtils.isNotBlank(argv[0]) && StringUtils.isNotBlank(argv[1])) {
				GenerateTestNGxml testNGXml = new GenerateTestNGxml();
				testNGXml.generateXML("All", argv[0], argv[1]);
			}

		} catch (Exception e) {
			// log error and throw out
			// we want errors to be visible
			e.printStackTrace();
			throw e;
		}

		logger.info("End GenerateTestNGxml main");
	}

	/**
	 * Default constructor.
	 * 
	 */
	public GenerateTestNGxml() {
	}

	/**
	 * Creates the testng.xml file
	 * 
	 * @param testPriority
	 *            Filter to execute only the testcase with certain priority
	 * @param testLabsPath
	 * @param testNGPath
	 * @throws ParserConfigurationException
	 * @throws TestAutomationException
	 * @throws TransformerException
	 * @throws FileNotFoundException
	 */
	public void generateXML(String testPriority, String testLabsPath, String testNGPath)
			throws ParserConfigurationException, TestAutomationException, TransformerException, FileNotFoundException {
		logger.info("Start Generating generateXML testLabsPath" + testLabsPath + " testNGPath " + testNGPath);

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		// root elements (TestSuite)
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement(rootElementName);
		doc.appendChild(rootElement);
		rootElement.setAttribute(nameElement, Global.getTestsuiteName());
		rootElement.setAttribute(testNGParallel, Global.testNGParallel);
		rootElement.setAttribute(testNGThreadCount, Global.testNGThreadCount);

		if (StringUtils.isNotBlank(testLabsPath) && testLabsPath.length() > 0) {
			String[] testLabDirArray = testLabsPath.split(";");
			for (String testLabDir : testLabDirArray) {
				TestLab testlab = new TestLab(testLabDir);
				List<String> testsets = testlab.extractTestSets();

				for (int i = 0; i < testsets.size(); i++) {
					// System.out.println(testsets.get(i));
					// test elements
					Element test = doc.createElement(testElement);
					rootElement.appendChild(test);
					test.setAttribute(nameElement, testsets.get(i));
					logger.info("GenerateTestNGxml generateXML testngPath " + testLabDir + " testsuite "
							+ testsets.get(i));

					// parameter elements
					Element parameter1 = doc.createElement(parameterElement);
					test.appendChild(parameter1);
					parameter1.setAttribute(nameElement, "testsuite");
					parameter1.setAttribute(valueElement, testsets.get(i));
					Element parameter2 = doc.createElement(parameterElement);
					test.appendChild(parameter2);
					parameter2.setAttribute(nameElement, "testPriority");
					parameter2.setAttribute(valueElement, testPriority);
					Element parameter3 = doc.createElement(parameterElement);
					test.appendChild(parameter3);
					parameter3.setAttribute(nameElement, "testLabPath");
					parameter3.setAttribute(valueElement, testLabDir);

					// Classes elements
					Element classes = doc.createElement(classesElement);
					test.appendChild(classes);

					// Class elements
					Element pramClass = doc.createElement(classElement);
					classes.appendChild(pramClass);
					pramClass.setAttribute(nameElement, "com.lifelens.automation.ExecuteTestsFromWorkbook");
				}
			}
		}

		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);

		File testNGXmlFile = new File(testNGPath + Global.pathSeperator + Global.testngxml);
		StreamResult result = new StreamResult(new FileOutputStream(testNGXmlFile));

		// StreamResult result = new StreamResult(new File(testNGPath +
		// Global.pathSeperator + Global.testngxml));

		// Output to console for testing
		// StreamResult result = new StreamResult(System.out);
		transformer.transform(source, result);

		// System.out.println(fileName + " file is created !!");
		logger.info(Global.testngxml + " file generated!!");
		logger.info("End Generating generateXML " + testLabsPath);
	}
}
