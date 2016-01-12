package com.lifelens.globals;

public class Global {

	// Keys from properties file
	public static String browserKey = "browser";
	public static String urlKey = "url";
	public static String AdminUsernamekey = "admin.username";
	public static String AdminPasswordKey = "admin.password";
	public static String timeoutKey = "timeout";
	public static String compnayKey = "company";
	public static String releaseKey = "release";
	public static String iterationKey = "iteration";
	public static String testlabKey = "testlab";
	public static String propertiesFolderNameKey = "propertiesFolderName";
	public static String driversFolderKey = "driversFolderName";
	public static String testdataFilenameKey = "testdataFilename";
	public static String testdataSheetNameKey = "testdataSheetName";
	public static String node1 = "node1";
	public static String node2 = "node2";
	public static String node3 = "node3";
	public static String testNGParallel = "tests";
	public static String testNGThreadCount = "1";

	// Css Class Names for Link and Tab .
	public static String navigationLink = "link";
	public static String tabName = "tabLabel";

	// Global Strings
	// public static String pathSeperator = "//";
	// Path seperator chagned for Linux
	public static String pathSeperator = "/";
	public static String parameterIdentifier = "@";
	public static String browser = "FIREFOX";

	// timeouts
	public static int timeout = 30;
	public static final int TIMEOUT_SHORT = 2;
	public static final int TIMEOUT_REPORT = 2000;
	public static final int TIMEOUT_ZERO = 0;

	public static String IEDriverName = "IEDriverServer.exe";
	public static String ChromeDriverName = "chromedriver.exe";
	// public static String loadIndicatorXPath =
	// "//div[@id='loadIndicator' and (contains(concat(' ', @style, ' '), "
	// +
	// "' display: none; ') or contains(concat(' ', @style, ' '), ' display:none '))]";
	public static String lazyLoadIndicatorClass = "lazyLoadingIndicator";
	public static String loadIndicatorCSS;
	public static String browserLoadIndicatorCSS = "div#loadIndicator[style*='display: none'],"
			+ " div#loadIndicator[style*='display:none']";
	// For IE browser - tags, attributes and values are appearing in upper case
	// when HTML is extracted so the above LoadIndicatorCSS is not working for
	// IE hence for IE alone below load indicator variable is created and this
	// need to be updated in the future similar to the above variable
	// - browserLoadIndicatorCSS -
	public static String ieLoadIndicatorCSS = "div#loadIndicator";

	// Project parameters
	public static String release = "RC";
	public static String company = "CORE";
	public static String iteration = "Iteration";
	public static String testsuiteName = "RC CORE Test Execution - Iteration";
	public static String testlab = "testlab";
	public static String AdminUserName = "superuser";
	public static String AdminPassword = "muppet";
	public static String url = "url";

	// Global Settings
	public static String propertiesFileName = "config.properties";
	public static String propertiesFolderName = "properties";
	public static String driversFolderName = "drivers";
	public static String testlabName = "Testlab";
	public static String testngxml = "testng.xml";
	public static String testdataFileName = "testdata.xlsx";
	public static String testdataSheetName = "Sheet1";
	private static String testLabPath = getTestlabAbsolutepath();

	public static String getCurrentDirectory() {
		return System.getProperty("user.dir");
	}

	public static String getPropertiesDirectory() {
		return getCurrentDirectory() + pathSeperator + propertiesFolderName;
	}

	public static String getIEdriver() {
		return getDriversDirectory() + pathSeperator + IEDriverName;
	}

	public static String getChromeDriver() {
		return getDriversDirectory() + pathSeperator + ChromeDriverName;
	}

	public static void setPropertiesDirectory(String propFileName) {
		Global.propertiesFolderName = propFileName;
	}

	public static String getDriversDirectory() {
		return getCurrentDirectory() + pathSeperator + driversFolderName;
	}

	public static void setDriversDirectory(String driversFoldername) {
		Global.driversFolderName = driversFoldername;
	}

	public String getTeslabName() {
		return testlabName;
	}

	public void setTestlabName(String testlabname) {
		Global.testlabName = testlabname;
	}

	public static String getTestlabAbsolutepath() {
		return Global.getCurrentDirectory() + pathSeperator + Global.getTestlab();
	}

	public static String getRelease() {
		return release;
	}

	public static void setRelease(String rc) {
		Global.release = rc;
	}

	public static String getCompany() {
		return company;
	}

	public static void setCompany(String companyName) {
		Global.company = companyName;
	}

	public static String getIteration() {
		return iteration;
	}

	public static void setIteration(String iterationNumber) {
		Global.iteration = iterationNumber;
	}

	public static String getTestsuiteName() {
		return Global.release + "_Iteration" + Global.iteration + "_" + Global.company + "_Execution";
	}

	public static void setTestsuiteName(String testsuitename) {
		Global.testsuiteName = testsuitename;
	}

	public static String getBrowser() {
		return Global.browser;
	}

	public static void setBrowser(String browserName) {
		Global.browser = browserName;
	}

	public static int getTimeout() {
		return timeout;
	}

	public static void setTimeout(String timeoutinseconds) {
		Global.timeout = Integer.parseInt(timeoutinseconds);
	}

	public static String getTestlab() {
		return testlab;
	}

	public static void setTestlab(String testlabname) {
		Global.testlab = testlabname;
	}

	public static String getTestdataFileName() {
		return testdataFileName;
	}

	public static void setTestdataFileName(String testdataFileName) {
		Global.testdataFileName = testdataFileName;
	}

	public static String getTestdatFileAbsolutepath() {

		return getTestLabPath() + pathSeperator + getTestdataFileName();
	}

	public static String getTestdataSheetName() {
		return testdataSheetName;
	}

	public static void setTestdataSheetName(String testdataSheetName) {
		Global.testdataSheetName = testdataSheetName;
	}

	public static String getAdminUserName() {
		return AdminUserName;
	}

	public static void setAdminUserName(String adminUserName) {
		AdminUserName = adminUserName;
	}

	public static String getAdminPassword() {
		return AdminPassword;
	}

	public static void setAdminPassword(String adminPassword) {
		AdminPassword = adminPassword;
	}

	public static String getUrl() {
		return url;
	}

	public static void setUrl(String url) {
		Global.url = url;
	}

	public static String getTestLabPath() {
		return testLabPath;
	}

	public static void setTestLabPath(String testLabPath) {
		Global.testLabPath = testLabPath;
	}

	public static String getLoadIndicatorCSS() {
		return loadIndicatorCSS;
	}

	public static String getNode1() {
		return node1;
	}

	public static void setNode1(String node1) {
		Global.node1 = node1;
	}

	public static String getNode2() {
		return node2;
	}

	public static void setNode2(String node2) {
		Global.node2 = node2;
	}

	public static String getNode3() {
		return node3;
	}

	public static void setNode3(String node3) {
		Global.node3 = node3;
	}

	public static void setLoadIndicatorCSS() {
		System.out.println(Global.getBrowser());
		if (Global.getBrowser().equalsIgnoreCase("INTERNET_EXPLORER"))
			Global.loadIndicatorCSS = Global.ieLoadIndicatorCSS;
		else
			Global.loadIndicatorCSS = Global.browserLoadIndicatorCSS;
	}

}