package com.lifelens.automation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.lifelens.exceptions.ExceptionErrorCodes;
import com.lifelens.exceptions.TestAutomationException;
import com.lifelens.globals.Global;

public class ProperitesFile {

	private static Logger logger = Logger.getLogger(ProperitesFile.class.getName());
	Properties prop = new Properties();
	InputStream input = null;
	String propertiesFileName;

	public ProperitesFile(String propertiesFileName) throws TestAutomationException {
		logger.debug("Start of ProperitesFile: ProperitesFile constructor :propertiesFileName == " + propertiesFileName);
		this.propertiesFileName = propertiesFileName;
		String propertiesFilePath = Global.getCurrentDirectory() + "/properties/" + propertiesFileName;
		try {
			this.input = new FileInputStream(new File(propertiesFilePath));
			this.prop.load(input);
		} catch (FileNotFoundException e) {
			throw new TestAutomationException(ExceptionErrorCodes.TA001_PROPERTIES_FILE_NOT_FOUND_ERROR, e.getMessage()
					+ " : Properties file not found in the path " + propertiesFilePath);
		} catch (IOException e) {
			throw new TestAutomationException(ExceptionErrorCodes.TA002_PROPERTIES_FILE_LOAD_ERROR, e.getMessage()
					+ ": Unable to load the properties file in the path " + propertiesFilePath);
		} finally {
			if (input != null) {

				try {
					input.close();
				} catch (IOException e) {
					throw new TestAutomationException(ExceptionErrorCodes.TA003_PROPERTIES_FILE_CLOSE_ERROR,
							e.getMessage() + ": Unable to close the properties file input stream");
				}
			}
		}
		logger.debug("End of ProperitesFile: ProperitesFile constructor ");
	}

	public String getProperty(String key) {
		logger.debug("Start of ProperitesFile: getProperty: Key == " + key);
		return prop.getProperty(key);
	}

	public void setSettings() {
		logger.debug("Start of ProperitesFile: setSettings");
		Global.setCompany(getProperty(Global.compnayKey));
		Global.setRelease(getProperty(Global.releaseKey));
		Global.setIteration(getProperty(Global.iterationKey));
		Global.setBrowser(getProperty(Global.browserKey));
		Global.setTimeout(getProperty(Global.timeoutKey));
		Global.setTestlab(getProperty(Global.testlabKey));
		Global.setDriversDirectory(getProperty(Global.driversFolderKey));
		Global.setTestdataFileName(getProperty(Global.testdataFilenameKey));
		Global.setTestdataSheetName(getProperty(Global.testdataSheetNameKey));
		Global.setAdminUserName(getProperty(Global.AdminUsernamekey));
		Global.setAdminPassword(getProperty(Global.AdminPasswordKey));
		Global.setUrl(getProperty(Global.urlKey));
		Global.setNode1(getProperty(Global.node1));
		Global.setNode2(getProperty(Global.node2));
		Global.setNode3(getProperty(Global.node3));
		Global.setLoadIndicatorCSS();
		logger.debug("End of ProperitesFile: setSettings");
	}
}
