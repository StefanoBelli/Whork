package test.selenium;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * @author Magliari Elio
 */
public class SeleniumEditAccount {

	@Test
	public void testEdtAccountJobSeeker() {
		WebDriver driver = ChromeWebDriver.getWebDriver();
		driver.get("http://localhost:8080/logout");
		driver.get("http://localhost:8080/login.jsp");
		
		WebElement email = driver.findElement(By.xpath("/html/body/div[3]/div/div/div/div/div[2]/div/form/div/div[1]/input"));
		WebElement password = driver.findElement(By.xpath("/html/body/div[3]/div/div/div/div/div[2]/div/form/div/div[2]/input"));
		email.clear(); email.sendKeys("email@gmail.com");
		password.clear(); password.sendKeys("password");

		driver.findElement(By.xpath("/html/body/div[3]/div/div/div/div/div[2]/div/form/div/div[4]/button")).click();
		driver.findElement(By.xpath("/html/body/div/div[1]/div/div/div/div/ul/li[1]/a")).click();
		driver.findElement(By.xpath("/html/body/form/div/div/div[1]/div[2]/div[1]/div/div[7]/div/a[1]")).click();
		WebElement name = driver.findElement(By.xpath("/html/body/form/div/div/div[1]/div[2]/div[1]/div/div[1]/div[2]/div/input"));
		WebElement surname = driver.findElement(By.xpath("/html/body/form/div/div/div[1]/div[2]/div[1]/div/div[2]/div[2]/div/input"));
		WebElement phone = driver.findElement(By.xpath("/html/body/form/div/div/div[1]/div[2]/div[1]/div/div[4]/div[2]/div/input"));
		WebElement address = driver.findElement(By.xpath("/html/body/form/div/div/div[1]/div[2]/div[1]/div/div[6]/div[2]/div/input"));
		name.clear(); name.sendKeys("Anuar");
		surname.clear(); surname.sendKeys("anuar");
		phone.clear(); phone.sendKeys("1111111111");
		address.clear(); address.sendKeys("different address");
		driver.findElement(By.xpath("/html/body/form/div/div/div[1]/div[2]/div[1]/div/div[5]/div[2]")).click();
		driver.findElement(By.xpath("/html/body/form/div/div/div[1]/div[2]/div[1]/div/div[7]/div/button")).click();

		WebElement successEdit = driver.findElement(By.xpath("/html/body/form/div/div/div[1]"));
		assertEquals("Information updated successfully!", successEdit.getText());
		driver.close();
	}
}
