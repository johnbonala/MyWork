package com.lifelens.browsers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import com.lifelens.globals.Global;

/**
 * Represents the browsers supported by this lifelens Web testing.
 * 
 * @author Srinivas Pasupulati
 */
public enum EBrowser {

	FIREFOX {
		@Override
		public WebDriver newInstance() {
			return new FirefoxDriver();
		}
	},

	INTERNET_EXPLORER {
		@Override
		public WebDriver newInstance() {
			System.setProperty("webdriver.ie.driver", Global.getIEdriver());
			// Global.browserZeroImplicitWait = 1;
			/**
			 * DesiredCapabilities caps =
			 * DesiredCapabilities.internetExplorer(); caps.setCapability(
			 * InternetExplorerDriver
			 * .INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true); return
			 * new IEBrowser(caps);
			 **/
			return new InternetExplorerDriver();
		}
	},
	CHROME {
		@Override
		public WebDriver newInstance() {
			System.setProperty("webdriver.chrome.driver", Global.getChromeDriver());
			return new ChromeDriver();
		}
	};

	public abstract WebDriver newInstance();
}
