package test.selenium;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

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
import test.Db;

/**
 * @author Michele Tosi
 */
public class SeleniumSearchOffer {

	@BeforeClass
	public static void initDb() throws ClassNotFoundException, SQLException, DataAccessException {
		Db.init();
	}
	
	@Test
	public void testSearchOffer() throws DataAccessException, InternalException, InvalidVatCodeException, AlreadyExistantCompanyException, AlreadyExistantUserException, ClassNotFoundException, SQLException {
		
		Util.InstanceConfig.setConf(Util.InstanceConfig.KEY_MAILTLS, false);
		Util.InstanceConfig.setConf(Util.InstanceConfig.KEY_MAILHOST, "smtp.more.fake.than.this");
		Util.InstanceConfig.setConf(Util.InstanceConfig.KEY_MAILFROM, "fake@fake.fakefakefake");
		Util.InstanceConfig.setConf(Util.InstanceConfig.KEY_MAILSMTP_PORT, "587");		
		
		CompanyBean company= new CompanyBean();
		company=BeanFactory.buildCompanyBean("ERSTTR04T45A662J", 
				"data/seide.png", "Eurospin", "02536510239");
		
		UserBean user=new UserBean();
		user.setAdmin(true);
		user.setEmployee(true);
		user.setRecruiter(true);
		user.setCompany(company);
		user.setName("nome3");
		user.setSurname("cognome3");
		user.setPhoneNumber("3395520346");
		user.setCf("SRRSND04R70A543X");
		
		UserAuthBean userAuth=new UserAuthBean();
		userAuth.setEmail("email3@libero.it");
		userAuth.setPassword("password");
		
		try {
			RegisterController.register(new Pair<>(user, userAuth));
		} catch(InternalException e) { // see test method desc
			if (!e.getMessage().equals("Unable to send you an email!")) {
				throw e;
			}
		}

		OfferBean offer=new OfferBean();
		offer.setCompany(company);
		offer.setDescription("descrizione offerta 3");
		offer.setEmployee(user);
		offer.setJobCategory(BeanFactory.buildJobCategoryBean("Engineering"));
		offer.setJobPhysicalLocationFullAddress("via tuscolana 180");
		offer.setJobPosition(BeanFactory.buildJobPositionBean("Engineer"));
		offer.setOfferName("offer 3");
		offer.setQualification(BeanFactory.buildQualificationBean("Master's degree"));
		offer.setSalaryEUR(2100);
		offer.setTypeOfContract(BeanFactory.buildTypeOfContractBean("Full Time"));
		offer.setWorkShit("08:00 - 19:00");
		
		OfferController.postOffer(offer);
		
		WebDriver driver = ChromeWebDriver.getWebDriver();
		driver.get("http://localhost:8080/index.jsp");

		WebElement searchVal = driver.findElement(By.xpath("/html/body/div/form/input[1]"));
		Select selectCategories = new Select(driver.findElement(By.id("categories")));
		Select selectPositions = new Select(driver.findElement(By.id("positions")));
		Select selectQualifies = new Select(driver.findElement(By.id("qualifies")));
		Select selectContracts = new Select(driver.findElement(By.id("contracts")));
		WebElement searchBtn = driver.findElement(By.id("search"));	
		searchVal.sendKeys("offer 3");
		selectCategories.selectByVisibleText("Engineering");
		selectPositions.selectByVisibleText("Engineer");
		selectQualifies.selectByVisibleText("Master's degree");
		selectContracts.selectByVisibleText("Full Time");
		searchBtn.click();
		
		WebElement outcome = driver.findElement(By.className("name"));
		assertEquals("Name: offer 3", outcome.getText());
		driver.close();

		
		
	}

}
