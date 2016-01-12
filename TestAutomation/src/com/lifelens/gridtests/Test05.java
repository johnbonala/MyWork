package com.lifelens.gridtests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

public class Test05 extends TestBase {

	@Test
	public void testLink() throws Exception {
		getDriver().get("https://test5vm.vebnet.com/ReFlexWeb/DnslVtQV.wTaUsI4h/public/page/login");
		WebElement textBox = getDriver().findElement(By.name("usernameContainer:username"));
		textBox.click();
		textBox.sendKeys("Test 5!");
		Thread.sleep(10000);
	}

}