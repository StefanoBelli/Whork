package test.controller;

import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.Date;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import logic.bean.UserAuthBean;
import logic.bean.UserBean;
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
 * @author Stefano Belli
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestRegisterController {

	@BeforeClass
	public static void initDb() 
			throws ClassNotFoundException, SQLException, DataAccessException {
		Db.init();
	}
	
	/**
	 * This test will show an exception logging because the internal "Util.Mailer"
	 * will try to send a confirmation email, but SMTP server at smtp.more.fake.than.this
	 * obviously does not exist. Internally, Mailer's sendMail() method will log the exception
	 * stack trace, and rethrow a "SendMailException", the SendMailException is caught by the
	 * controller, which will just throw a new "InternalException" with fewer details saying
	 * that we're not able to send you an email. The fact that we are not able to send an email
	 * is irrelevant to the purpose of this test because we already inserted a record entry into the database.
	 * 
	 * Test will pass anyway if user is unique by email and CF, just with some exception log attached.
	 * @throws InternalException - rethrown iff error message != "Unable to send you an email!"
	 * @throws InvalidVatCodeException
	 * @throws AlreadyExistantCompanyException
	 */
	@Test
	public void testRegisterAnEntirelyNewJobSeeker() 
			throws InternalException, InvalidVatCodeException, AlreadyExistantCompanyException {
		Util.InstanceConfig.setConf(Util.InstanceConfig.KEY_MAILTLS, false);
		Util.InstanceConfig.setConf(Util.InstanceConfig.KEY_MAILHOST, "smtp.more.fake.than.this");
		Util.InstanceConfig.setConf(Util.InstanceConfig.KEY_MAILFROM, "fake@fake.fakefakefake");
		Util.InstanceConfig.setConf(Util.InstanceConfig.KEY_MAILSMTP_PORT, "587");

		boolean passTest = true;

		String email = "test@email";
		String password = "ciao123";
		String userCf = "XXXXYYYYZZZZTTTT";

		UserBean userBean = new UserBean();
		UserAuthBean userAuthBean = BeanFactory.buildUserAuthBean(email, password);
		
		userBean.setCf(userCf);
		userBean.setName("Fake");
		userBean.setSurname("User");
		userBean.setPhoto(null);
		userBean.setPhoneNumber("1234567890");
		userBean.setBiography(null);
		userBean.setHomeAddress("Fake address");
		userBean.setEmploymentStatus(BeanFactory.buildEmploymentStatusBean("Unemployed"));
		userBean.setCv("fake/path/to/cv.pdf");
		userBean.setBirthday(new Date());
		userBean.setComune(BeanFactory.buildComuneBean("Cave RM - 00033, Lazio"));

		try {
			RegisterController.register(new Pair<>(userBean, userAuthBean));
		} catch (AlreadyExistantUserException e) {
			passTest = false;
		} catch (InternalException e) { //see test method desc
			if(!e.getMessage().equals("Unable to send you an email!")) {
				throw e;
			}
		}

		assertTrue(passTest);
	}

	/**
	 * In this test, the register controller will not even try to send an email
	 * because it detects that another user with same email already exists and
	 * rejects by throwing an AlreadyExistantUserException, 
	 * which is caught by this test case, making the test pass.
	 * @throws InternalException
	 * @throws InvalidVatCodeException
	 * @throws AlreadyExistantCompanyException
	 */
	@Test
	public void testRegisterDuplicatedJobSeekerByEmail()
			throws InternalException, InvalidVatCodeException, AlreadyExistantCompanyException {
		boolean passTest = false;

		String email = "test@email";
		String password = "ciao123";
		String userCf = "XXXXYYYYZZZZTTTT";

		UserBean userBean = new UserBean();
		UserAuthBean userAuthBean = BeanFactory.buildUserAuthBean(email, password);

		userBean.setCf(userCf);
		userBean.setName("Fake");
		userBean.setSurname("User");
		userBean.setPhoto(null);
		userBean.setPhoneNumber("1234567890");
		userBean.setBiography(null);
		userBean.setHomeAddress("Fake address");
		userBean.setEmploymentStatus(BeanFactory.buildEmploymentStatusBean("Unemployed"));
		userBean.setCv("fake/path/to/cv.pdf");
		userBean.setBirthday(new Date());
		userBean.setComune(BeanFactory.buildComuneBean("Cave RM - 00033, Lazio"));

		try {
			RegisterController.register(new Pair<>(userBean, userAuthBean));
		} catch (AlreadyExistantUserException e) {
			passTest = true;
		}

		assertTrue(passTest);
	}

	/**
	 * In this test, register controller will try to check (using a third-party service)
	 * that this FakeCompany's VAT is actually valid. Of course it is not valid and, therefore,
	 * it is expected that RegisterController will throw an exception (InvalidVatCodeException).
	 * Test will catch the exception - catch block will set the passTest variable to true,
	 * having a valid assertion (assertTrue), the test passes under these conditions.
	 * @throws InternalException
	 * @throws AlreadyExistantCompanyException
	 * @throws AlreadyExistantUserException
	 */
	@Test
	public void testRegisterInvalidVATCompany() 
			throws InternalException, AlreadyExistantCompanyException, AlreadyExistantUserException {
		boolean passTest = false;

		String email = "user@company";
		String password = "ciao123";
		String userCf = "XXXXMMMMZZZZKKKK";

		UserBean userBean = new UserBean();
		UserAuthBean userAuthBean = BeanFactory.buildUserAuthBean(email, password);

		userBean.setCf(userCf);
		userBean.setName("Fake");
		userBean.setSurname("User");
		userBean.setPhoto(null);
		userBean.setPhoneNumber("1234567890");
		userBean.setAdmin(true);
		userBean.setEmployee(true);
		userBean.setRecruiter(true);
		userBean.setCompany(BeanFactory.buildCompanyBean("ZZZZYYYYXXXXTTTT",
				null, "FakeCompany", "12345678900"));
		userBean.setNote(null);

		try {
			RegisterController.register(new Pair<>(userBean, userAuthBean));
		} catch(InvalidVatCodeException e) {
			passTest = true;
		}

		assertTrue(passTest);
	}
}
