package test.selenium;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * @author Stefano Belli
 */
public class SeleniumRegisterJobSeeker {

	@Test
	public void testRegisterAnEntirelyNewJobSeeker() {
		WebDriver driver = ChromeWebDriver.getWebDriver();
		driver.get("http://localhost:8080/logout");
		driver.get("http://localhost:8080/register.jsp");
		driver.findElement(By.xpath("/html/body/div/div[2]/div[1]/form/button")).click();

		WebElement email = driver.findElement(By.xpath("//*[@id=\"details\"]/div/div[1]/div[1]/div/input"));
		WebElement pwd = driver.findElement(By.xpath("//*[@id=\"pwd\"]"));
		WebElement confPwd = driver.findElement(By.xpath("//*[@id=\"conf_pwd\"]"));
		WebElement name = driver.findElement(By.xpath("//*[@id=\"details\"]/div/div[3]/div[1]/div/input"));
		WebElement surname = driver.findElement(By.xpath("//*[@id=\"details\"]/div/div[3]/div[2]/div/input"));
		WebElement fiscalCode = driver.findElement(By.xpath("//*[@id=\"my_fc\"]"));
		WebElement phoneNumber = driver.findElement(By.xpath("//*[@id=\"details\"]/div/div[3]/div[4]/div/input"));
		WebElement town = driver.findElement(By.xpath("//*[@id=\"town\"]"));
		WebElement address = driver.findElement(By.xpath("//*[@id=\"address\"]"));
		WebElement cvFile = driver.findElement(By.xpath("//*[@id=\"details\"]/div/div[4]/div[4]/div/label/input"));
		WebElement policy = driver.findElement(By.xpath("//*[@id=\"details\"]/div/div[5]/div[1]/div/div/div/label/span/span"));
		WebElement confirm = driver.findElement(By.xpath("//*[@id=\"submit\"]"));

		email.sendKeys("selenium@is.testing");
		pwd.sendKeys("my_password");
		confPwd.sendKeys("my_password");
		name.sendKeys("Selenium");
		surname.sendKeys("WebDriver");
		fiscalCode.sendKeys("SLNWDR96L19H501G");
		phoneNumber.sendKeys("1234567890");
		town.sendKeys("Roma RM - 00118, Lazio");
		address.sendKeys("Via del Politecnico, 1");
		cvFile.sendKeys(new File("data/cv.pdf").getAbsolutePath());
		policy.click();
		confirm.click();
		
		WebElement outcome = driver.findElement(By.xpath("/html/body/div/h1"));
		assertEquals("Success", outcome.getText());

		driver.close();
	}
	
}
