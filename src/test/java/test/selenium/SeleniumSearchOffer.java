package test.selenium;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import logic.Database;
import logic.bean.CompanyBean;
import logic.bean.OfferBean;
import logic.bean.UserAuthBean;
import logic.bean.UserBean;
import logic.controller.OfferController;
import logic.controller.RegisterController;
import logic.exception.AlreadyExistantCompanyException;
import logic.exception.AlreadyExistantUserException;
import logic.exception.DataAccessException;
import logic.exception.InternalException;
import logic.exception.InvalidVatCodeException;
import logic.factory.BeanFactory;
import logic.util.Util;
import logic.util.tuple.Pair;
import test.DbmsConfig;

public class SeleniumSearchOffer {
	
	@Test
	public void testSearchOffer() throws DataAccessException, InternalException, InvalidVatCodeException, AlreadyExistantCompanyException, AlreadyExistantUserException, ClassNotFoundException, SQLException {
		
		
		Database db = Database.getInstance(
				DbmsConfig.DB_HOST + ":" + Integer.toString(DbmsConfig.DB_PORT),
				DbmsConfig.DB_USER, DbmsConfig.DB_PWD);
		db.getConnection().setCatalog(DbmsConfig.DB_NAME);
		
		Util.InstanceConfig.setConf(Util.InstanceConfig.KEY_MAILTLS, true);
		Util.InstanceConfig.setConf(Util.InstanceConfig.KEY_MAILHOST, "smtp.gmail.com");
		Util.InstanceConfig.setConf(Util.InstanceConfig.KEY_MAILFROM, "whork.noreply@gmail.com");
		Util.InstanceConfig.setConf(Util.InstanceConfig.KEY_MAILSMTP_PORT, "587");
		Util.InstanceConfig.setConf(Util.InstanceConfig.KEY_MAILPWD, "whorkelione456");

		
		
		CompanyBean company= new CompanyBean();
		company=BeanFactory.buildCompanyBean("LMBTTR04T45A662J", 
				"data/seide.png", "Lamborghini", "00591801204");
		
		UserBean user=new UserBean();
		user.setAdmin(true);
		user.setEmployee(true);
		user.setRecruiter(true);
		user.setCompany(company);
		user.setName("nome1");
		user.setSurname("cognome1");
		user.setPhoneNumber("3335519346");
		user.setCf("SRRSND04R45A412X");
		
		UserAuthBean userAuth=new UserAuthBean();
		userAuth.setEmail("email1@libero.it");
		userAuth.setPassword("password");
		
		
		RegisterController.register(new Pair<>(user, userAuth));
		
		OfferBean offer=new OfferBean();
		offer.setCompany(company);
		offer.setDescription("descrizione offerta 1");
		offer.setEmployee(user);
		offer.setJobCategory(BeanFactory.buildJobCategoryBean("Engineering"));
		offer.setJobPhysicalLocationFullAddress("via gela 8");
		offer.setJobPosition(BeanFactory.buildJobPositionBean("Engineer"));
		offer.setOfferName("offer 1");
		offer.setQualification(BeanFactory.buildQualificationBean("Master's degree"));
		offer.setSalaryEUR(2000);
		offer.setTypeOfContract(BeanFactory.buildTypeOfContractBean("Full Time"));
		offer.setWorkShit("9:00 - 19:00");
		
		OfferController.postOffer(offer);
		
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
		WebElement position = driver.findElement(By.className("jobPosition"));
		assertEquals("Engineer",position.getText());
		WebElement category = driver.findElement(By.className("jobCategory"));
		assertEquals("Engineering",category.getText());
		WebElement qualification = driver.findElement(By.className("qualification"));
		assertEquals("Master's degree",qualification.getText());
		WebElement typeOfContract = driver.findElement(By.className("typeOfContract"));
		assertEquals("Full Time",typeOfContract.getText());
		driver.close();

		
		
	}

}
