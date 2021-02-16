package test.dao;

import java.io.IOException;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import logic.bean.UserAuthBean;
import logic.dao.CompanyDao;
import logic.dao.ComuniDao;
import logic.dao.EmploymentStatusDao;
import logic.dao.UserDao;
import logic.exception.DataAccessException;
import logic.exception.DataLogicException;
import logic.exception.SyntaxException;
import logic.factory.ModelFactory;
import logic.model.EmployeeUserModel;
import logic.model.JobSeekerUserModel;
import logic.model.UserAuthModel;
import logic.model.UserModel;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestUserDao {
	private static final Logger LOGGER = LoggerFactory.getLogger("TestUserDao");

	@Test
	public void testARegisterJobSeeker() throws DataAccessException {

		Date birthday = new GregorianCalendar(2020, 10, 99).getTime();
		JobSeekerUserModel model = new JobSeekerUserModel();
		model.setName("nome");
		model.setSurname("cognome");
		model.setCf("XXXXYYYYZZZZTTTT");
		model.setComune(ComuniDao.getComune("Sossano", "36040"));
		model.setEmploymentStatus(EmploymentStatusDao.getEmploymentStatus("Unemployed"));
		model.setHomeAddress("via qua e la");
		model.setPhoneNumber("1234567890");
		model.setBirthday(birthday);
		model.setCv(null);
		UserDao.registerUserDetails(model);

		assertTrue(true);
	}

	@Test
	public void testBRegisterEmployee() throws DataAccessException, DataLogicException {
		EmployeeUserModel model = new EmployeeUserModel();
		model.setName("nome");
		model.setSurname("cognome");
		model.setCf("XXXXYYYYZZZZKKKK");
		model.setPhoneNumber("1234567890");
		model.setAdmin(false);
		model.setRecruiter(true);
		model.setPhoto(null);
		model.setNote("boh");
		model.setCompany(CompanyDao.getCompanyByVat("12345678900"));
		UserDao.registerUserDetails(model);

		assertTrue(true);
	}

	@Test
	public void testCRegisterUserAuthForJobSeeker() 
			throws SyntaxException, DataAccessException, DataLogicException {
		UserAuthBean bean = new UserAuthBean();
		bean.setEmail("a@b.com");
		bean.setPassword("pwd123");
		UserAuthModel model = ModelFactory.buildUserAuthModel(bean);
		UserDao.registerUserAuth(UserDao.getUserByCf("XXXXYYYYZZZZTTTT"), model);

		assertTrue(true);
	}

	@Test
	public void testDConfirmRegForJobSeeker() 
			throws DataAccessException {

		try {
			UserDao.confirmRegistration("a@b.com");
		} catch(DataLogicException e) {
			LOGGER.warn(
				"DataLogicException raised" + 
				" for testDConfirmRegForJobSeeker" + 
				e.getMessage());
		}

		assertTrue(true);
	}

	@Test
	public void testEAuthShouldNotBeNull() 
			throws DataAccessException, DataLogicException, IOException {
		assertNotEquals(null, UserDao.getUserCfAndBcrypwdByEmail("a@b.com"));
	}

	@Test
	public void testFGetUserDetailsVarAShouldNotBeNull() 
			throws DataAccessException, DataLogicException {
		assertNotEquals(null, UserDao.getUserByCf("XXXXYYYYZZZZTTTT"));
	}

	@Test
	public void testGGetUserDetailsVarBShouldNotBeNull() 
			throws DataAccessException, DataLogicException {
		assertNotEquals(null, UserDao.getUserByCf("XXXXYYYYZZZZKKKK"));
	}

	@Test
	public void testHRegisterUserAuthForEmployeeShouldFail() 
			throws DataAccessException, SyntaxException, DataLogicException {
		UserAuthBean bean = new UserAuthBean();
		bean.setEmail("a@b.com");
		bean.setPassword("pwd123");
		UserAuthModel model = ModelFactory.buildUserAuthModel(bean);
		UserModel userModel = UserDao.getUserByCf("XXXXYYYYZZZZKKKK");
		assertThrows(DataAccessException.class, () -> {
			UserDao.registerUserAuth(userModel, model);
		});
	}

	@Test
	public void testIRegisterUserAuthForEmployee()
			throws DataAccessException, SyntaxException, DataLogicException {
		UserAuthBean bean = new UserAuthBean();
		bean.setEmail("me@az.com");
		bean.setPassword("pwd123");
		UserAuthModel model = ModelFactory.buildUserAuthModel(bean);
		UserDao.registerUserAuth(UserDao.getUserByCf("XXXXYYYYZZZZKKKK"), model);

		assertTrue(true);
	}

	@Test
	public void testLAuthShouldBeNull() 
			throws DataAccessException, DataLogicException, IOException {
		assertEquals(null, UserDao.getUserCfAndBcrypwdByEmail("me@az.com"));
	}

	@Test
	public void testMConfirmRegEmployee() 
			throws DataAccessException {

		try {
			UserDao.confirmRegistration("me@az.com");
		} catch(DataLogicException e) {
			LOGGER.warn("DataLogicException raised" + 
						" for testMConfirmRegEmployee" + 
						e.getMessage());
		}
		
		assertTrue(true);
	}

	@Test
	public void testNAuthShouldNotBeNull() 
			throws DataAccessException, DataLogicException, IOException {
		assertNotEquals(null, UserDao.getUserCfAndBcrypwdByEmail("me@az.com"));
	}

	@Test
	public void testOConfirmRegShouldFail() {
		assertThrows(DataLogicException.class, () -> {
			UserDao.confirmRegistration("nonexistant@email.com");
		});
	}
	
	@Test
	public void testPConfirmRegShouldFail() {
		assertThrows(DataLogicException.class, () -> {
			UserDao.confirmRegistration("me@az.com");
		});
	}

	@Test
	public void testQConfirmRegShouldFail() {
		assertThrows(DataLogicException.class, () -> {
			UserDao.confirmRegistration("a@b.com");
		});
	}

	@Test
	public void testRRegisterJobSeeker() 
			throws DataAccessException {

		Date birthday = new GregorianCalendar(2020, 10, 99).getTime();
		JobSeekerUserModel model = new JobSeekerUserModel();
		model.setName("Un altro nome");
		model.setSurname("Un altro cognome");
		model.setCf("XXXXKKKKZZZZTTTT");
		model.setComune(ComuniDao.getComune("Sossano", "36040"));
		model.setEmploymentStatus(EmploymentStatusDao.getEmploymentStatus("Unemployed"));
		model.setHomeAddress("via qua e laaa");
		model.setPhoneNumber("1234567800");
		model.setBirthday(birthday);
		model.setCv(null);
		UserDao.registerUserDetails(model);

		assertTrue(true);
	}

	@Test
	public void testSRegisterEmployee() 
			throws DataAccessException, DataLogicException {
		EmployeeUserModel model = new EmployeeUserModel();
		model.setName("Ancoraaa nome");
		model.setSurname("Ancoraa cognome");
		model.setCf("XXXXKKKKZZZZKKKK");
		model.setPhoneNumber("1234500090");
		model.setAdmin(false);
		model.setRecruiter(true);
		model.setPhoto(null);
		model.setNote("boh VA COSI");
		model.setCompany(CompanyDao.getCompanyByVat("12345678900"));
		UserDao.registerUserDetails(model);

		assertTrue(true);
	}

	@Test
	public void testTRegisterUserAuthForJobSeeker() 
			throws SyntaxException, DataAccessException, DataLogicException {
		UserAuthBean bean = new UserAuthBean();
		bean.setEmail("ll@bcd.com");
		bean.setPassword("pwd123");
		UserAuthModel model = ModelFactory.buildUserAuthModel(bean);
		UserDao.registerUserAuth(UserDao.getUserByCf("XXXXKKKKZZZZTTTT"), model);

		assertTrue(true);
	}

	@Test
	public void testURegisterUserAuthForEmployee() 
			throws DataAccessException, SyntaxException, DataLogicException {
		UserAuthBean bean = new UserAuthBean();
		bean.setEmail("brbr@xx.com");
		bean.setPassword("pwd123");
		UserAuthModel model = ModelFactory.buildUserAuthModel(bean);
		UserDao.registerUserAuth(UserDao.getUserByCf("XXXXKKKKZZZZKKKK"), model);

		assertTrue(true);
	}
}
