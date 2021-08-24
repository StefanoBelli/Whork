package test.selenium;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import logic.util.Util;

import java.io.ByteArrayInputStream;
import java.sql.SQLException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import logic.dao.UserAuthDao;
import logic.dao.UserDao;
import logic.exception.DataAccessException;
import logic.exception.DataLogicException;
import logic.model.ComuneModel;
import logic.model.EmploymentStatusModel;
import logic.model.JobSeekerUserModel;
import logic.model.ProvinciaModel;
import logic.model.RegioneModel;
import logic.model.UserAuthModel;
import test.Db;

/**
 * @author Magliari Elio
 */
public class SeleniumEditAccount {
	@BeforeClass
	public static void createUser()
			throws DataAccessException, DataLogicException, ClassNotFoundException, SQLException {

		Db.init();

		EmploymentStatusModel empStatusModel = new EmploymentStatusModel();
		empStatusModel.setStatus("Unemployed");

		RegioneModel regioneLazioModel = new RegioneModel();
		regioneLazioModel.setNome("Lazio");

		ProvinciaModel provinciaRmModel = new ProvinciaModel();
		provinciaRmModel.setSigla("RM");
		provinciaRmModel.setRegione(regioneLazioModel);

		ComuneModel comuneCaveModel = new ComuneModel();
		comuneCaveModel.setCap("00033");
		comuneCaveModel.setNome("Cave");
		comuneCaveModel.setProvincia(provinciaRmModel);

		JobSeekerUserModel jobSeekerUserModel = new JobSeekerUserModel();
		jobSeekerUserModel.setBirthday(new Date(0));
		jobSeekerUserModel.setCf("TTTTXXXXYYYYZZZZ");
		jobSeekerUserModel.setComune(comuneCaveModel);
		jobSeekerUserModel.setCv("path-to-cv.pdf");
		jobSeekerUserModel.setEmploymentStatus(empStatusModel);
		jobSeekerUserModel.setHomeAddress("aaa");
		jobSeekerUserModel.setName("stex");
		jobSeekerUserModel.setPhoneNumber("1234567890");
		jobSeekerUserModel.setSurname("bex");

		UserAuthModel userAuthModel = new UserAuthModel();
		userAuthModel.setEmail("thisismy@email.comx");
		userAuthModel.setBcryptedPassword(new ByteArrayInputStream(Util.Bcrypt.hash("ciao123")));

		String regToken = Util.generateToken();

		UserDao.registerUserDetails(jobSeekerUserModel);
		UserAuthDao.registerUserAuth(jobSeekerUserModel, userAuthModel, regToken);
		UserAuthDao.confirmRegistration(userAuthModel.getEmail(), regToken);
	}

	@Test
	public void testEdtAccountJobSeeker() {
		WebDriver driver = ChromeWebDriver.getWebDriver();
		driver.get("http://localhost:8080/logout");
		driver.get("http://localhost:8080/login.jsp");
		
		WebElement email = driver.findElement(By.xpath("/html/body/div[3]/div/div/div/div/div[2]/div/form/div/div[1]/input"));
		WebElement password = driver.findElement(By.xpath("/html/body/div[3]/div/div/div/div/div[2]/div/form/div/div[2]/input"));
		email.clear(); email.sendKeys("thisismy@email.comx");
		password.clear(); password.sendKeys("ciao123");

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
