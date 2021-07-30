package test.selenium;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import logic.exception.AlreadyExistantCompanyException;
import logic.exception.AlreadyExistantUserException;
import logic.exception.DataAccessException;
import logic.exception.InternalException;
import logic.exception.InvalidVatCodeException;

/**
 * @author Michele Tosi
 */
public class SeleniumSearchOffer {
	
	@Test
	public void testSearchOffer() throws DataAccessException, InternalException, InvalidVatCodeException, AlreadyExistantCompanyException, AlreadyExistantUserException, ClassNotFoundException, SQLException {
		
		
		WebDriver driver = ChromeWebDriver.getWebDriver();
		driver.get("http://localhost:8080/index.jsp");

		WebElement searchVal = driver.findElement(By.xpath("/html/body/div/form/input[1]"));
		Select selectCategories = new Select(driver.findElement(By.id("categories")));
		Select selectPositions = new Select(driver.findElement(By.id("positions")));
		Select selectQualifies = new Select(driver.findElement(By.id("qualifies")));
		Select selectContracts = new Select(driver.findElement(By.id("contracts")));
		WebElement searchBtn = driver.findElement(By.id("search"));	
		searchVal.sendKeys("offer");
		selectCategories.selectByVisibleText("Engineering");
		selectPositions.selectByVisibleText("Engineer");
		selectQualifies.selectByVisibleText("Master's degree");
		selectContracts.selectByVisibleText("Full Time");
		searchBtn.click();
		
		WebElement outcome = driver.findElement(By.className("name"));
		assertEquals("Name: offer 1", outcome.getText());
		driver.close();

		
		
	}

}
