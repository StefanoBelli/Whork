package test.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.Date;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import logic.bean.CompanyBean;
import logic.bean.UserAuthBean;
import logic.bean.UserBean;
import logic.controller.RegisterController;
import logic.dao.CompanyDao;
import logic.dao.UserDao;
import logic.exception.AlreadyExistantCompanyException;
import logic.exception.AlreadyExistantUserException;
import logic.exception.DataAccessException;
import logic.exception.DataLogicException;
import logic.exception.InternalException;
import logic.exception.InvalidVatCodeException;
import logic.factory.BeanFactory;
import logic.factory.ModelFactory;
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
	 * @throws DataLogicException
	 * @throws DataAccessException
	 */
	@Test
	public void testRegisterAnEntirelyNewJobSeeker() 
			throws InternalException, InvalidVatCodeException, AlreadyExistantCompanyException, 
					DataAccessException, DataLogicException {
		Util.InstanceConfig.setConf(Util.InstanceConfig.KEY_MAILTLS, false);
		Util.InstanceConfig.setConf(Util.InstanceConfig.KEY_MAILHOST, "smtp.more.fake.than.this");
		Util.InstanceConfig.setConf(Util.InstanceConfig.KEY_MAILFROM, "fake@fake.fakefakefake");
		Util.InstanceConfig.setConf(Util.InstanceConfig.KEY_MAILSMTP_PORT, "587");

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
			//unhandled
		} catch (InternalException e) { //see test method desc
			if(!e.getMessage().equals("Unable to send you an email!")) {
				throw e;
			}
		}
		
		assertNotEquals(null, UserDao.getUserByCf("XXXXYYYYZZZZTTTT"));
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
	 * @throws DataLogicException
	 * @throws DataAccessException
	 */
	@Test
	public void testRegisterInvalidVATCompany() 
			throws InternalException, AlreadyExistantCompanyException, AlreadyExistantUserException, 
				DataAccessException, DataLogicException {
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
			// unhandled
		}

		assertEquals(null, CompanyDao.getCompanyByName("12345678900"));
	}

	/**
	 * In this test, register controller will try to check (using a third-party
	 * service) that this Ferrero's VAT is actually valid. It is and
	 * RegisterController will accept regisration request.
	 * 
	 * @throws InternalException
	 * @throws AlreadyExistantCompanyException
	 * @throws AlreadyExistantUserException
	 * @throws DataLogicException
	 * @throws DataAccessException
	 */
	@Test
	public void testRegisterValidVATCompany()
			throws InternalException, AlreadyExistantCompanyException, AlreadyExistantUserException, 
				DataAccessException, DataLogicException {
		String email = "user@company";
		String password = "ciao123";
		String userCf = "XXXXMMMMZZZZKKKK";

		UserBean userBean = new UserBean();
		UserAuthBean userAuthBean = BeanFactory.buildUserAuthBean(email, password);

		userBean.setCf(userCf);
		userBean.setName("FERRERO Employee");
		userBean.setSurname("User");
		userBean.setPhoto(null);
		userBean.setPhoneNumber("1234567890");
		userBean.setAdmin(true);
		userBean.setEmployee(true);
		userBean.setRecruiter(true);
		userBean.setCompany(BeanFactory.buildCompanyBean("ZZZZYYYYXXXXTTTT", null, "FERRERO", "03629090048"));
		userBean.setNote(null);

		try {
			RegisterController.register(new Pair<>(userBean, userAuthBean));
		} catch (InvalidVatCodeException e) {
			// unhandled
		} catch (InternalException e) {
			if (!e.getMessage().equals("Unable to send you an email!")) {
				throw e;
			}
		}

		assertNotEquals(null, CompanyDao.getCompanyByVat("03629090048"));
	}

	/**
	 * In this test, register controller will register another employee user which
	 * works for the already registered "Ferrero"
	 * 
	 * @throws InternalException
	 * @throws AlreadyExistantUserException
	 * @throws DataAccessException
	 * @throws DataLogicException
	 */
	@Test
	public void testRegisterValidVATExistingCompanyRecruiter()
			throws InternalException, AlreadyExistantUserException, DataAccessException, DataLogicException {
		String email = "recr@ferrari";
		String password = "ciao123";
		String userCf = "MMMMXXXXZZZZKKKK";

		UserBean userBean = new UserBean();
		UserAuthBean userAuthBean = BeanFactory.buildUserAuthBean(email, password);
		CompanyBean companyBean = BeanFactory.buildCompanyBean("ZZZZYYYYXXXXTTTT", null, "FERRERO", "03629090048");

		userBean.setCf(userCf);
		userBean.setName("FERRERO Employee");
		userBean.setSurname("User");
		userBean.setPhoto(null);
		userBean.setPhoneNumber("1234567890");
		userBean.setAdmin(true);
		userBean.setEmployee(true);
		userBean.setRecruiter(true);
		userBean.setCompany(companyBean);
		userBean.setNote(null);
		
		try {
			RegisterController.registerEmployeeForExistingCompany(new Pair<>(userBean, userAuthBean));
		} catch(InternalException e) {
			if (!e.getMessage().equals("Unable to send you an email!")) {
				throw e;
			}
		}

		assertEquals("recr@ferrari", UserDao.getEmployeeEmailByCf(ModelFactory.buildUserModel(userBean)));
	}

	/**
	 * In this test, register controller will fail to register another user because another one
	 * exists with same criteria
	 * 
	 * @throws InternalException
	 */
	@Test
	public void testRegisterValidVATExistingCompanyRecruiterAlreadyExistantUser() 
			throws InternalException {
		
		boolean passTest = false;

		String email = "recr@ferrari";
		String password = "ciao123";
		String userCf = "MMMMXXXXZZZZKKKK";

		UserBean userBean = new UserBean();
		UserAuthBean userAuthBean = BeanFactory.buildUserAuthBean(email, password);

		userBean.setCf(userCf);
		userBean.setName("FERRERO Employee");
		userBean.setSurname("User");
		userBean.setPhoto(null);
		userBean.setPhoneNumber("1234567890");
		userBean.setAdmin(true);
		userBean.setEmployee(true);
		userBean.setRecruiter(true);
		userBean.setCompany(BeanFactory.buildCompanyBean("ZZZZYYYYXXXXTTTT", null, "FERRERO", "03629090048"));
		userBean.setNote(null);

		try {
			RegisterController.registerEmployeeForExistingCompany(new Pair<>(userBean, userAuthBean));
		} catch (AlreadyExistantUserException e) {
			passTest = true;
		}

		assertTrue(passTest);
	}

	/**
	 * In this test, register controller will fail to register another user because
	 * company is non-existant and should trigger a SQL integrity violation while attempting
	 * to insert the user in DB
	 * 
	 * @throws AlreadyExistantUserException
	 */
	@Test
	public void testRegisterValidVATExistingCompanyRecruiterNonExistingCompany() 
			throws AlreadyExistantUserException {

		boolean passTest = false;

		String email = "recr@fererero";
		String password = "ciao123";
		String userCf = "MMMMXXXXKKKKZZZZ";

		UserBean userBean = new UserBean();
		UserAuthBean userAuthBean = BeanFactory.buildUserAuthBean(email, password);

		userBean.setCf(userCf);
		userBean.setName("FERRERO Employee");
		userBean.setSurname("User");
		userBean.setPhoto(null);
		userBean.setPhoneNumber("1234567890");
		userBean.setAdmin(true);
		userBean.setEmployee(true);
		userBean.setRecruiter(true);
		userBean.setCompany(BeanFactory.buildCompanyBean("ZZZZYYYYXXXXTTTT", null, "DOESNOTEXIST", "03629090041"));
		userBean.setNote(null);

		try {
			RegisterController.registerEmployeeForExistingCompany(new Pair<>(userBean, userAuthBean));
		} catch (InternalException e) {
			passTest = true;
		}

		assertTrue(passTest);
	}
}
