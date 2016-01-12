package com.lifelens.gridtests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

public class Test04 extends TestBase {

	@Test
	public void testLink() throws Exception {
		getDriver().get("https://test5vm.vebnet.com/ReFlexWeb/9x6r739Z.9x6r739Z/public/page/login");
		WebElement textBox = getDriver().findElement(By.name("usernameContainer:username"));
		textBox.click();
		textBox.sendKeys("Test 4!");
		Thread.sleep(10000);
	}

}
