package test.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.ByteArrayInputStream;
import java.sql.Date;
import java.sql.SQLException;

import org.junit.BeforeClass;
import org.junit.Test;

import logic.controller.LoginController;
import logic.dao.UserAuthDao;
import logic.dao.UserDao;
import logic.exception.DataAccessException;
import logic.exception.DataLogicException;
import logic.exception.InternalException;
import logic.factory.BeanFactory;
import logic.model.ComuneModel;
import logic.model.EmploymentStatusModel;
import logic.model.JobSeekerUserModel;
import logic.model.ProvinciaModel;
import logic.model.RegioneModel;
import logic.model.UserAuthModel;
import logic.util.Util;
import test.Db;

/**
 * @author Stefano Belli
 */
public class TestLoginController {
	@BeforeClass
	public static void createUser() 
			throws DataAccessException, DataLogicException, 
				ClassNotFoundException, SQLException {
					
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
		jobSeekerUserModel.setCf("XXXXZZZZYYYYTTTT");
		jobSeekerUserModel.setComune(comuneCaveModel);
		jobSeekerUserModel.setCv("path-to-cv.pdf");
		jobSeekerUserModel.setEmploymentStatus(empStatusModel);
		jobSeekerUserModel.setHomeAddress("aaa");
		jobSeekerUserModel.setName("stex");
		jobSeekerUserModel.setPhoneNumber("1234567890");
		jobSeekerUserModel.setSurname("bex");

		UserAuthModel userAuthModel = new UserAuthModel();
		userAuthModel.setEmail("xyzt@abc.domx");
		userAuthModel.setBcryptedPassword(new ByteArrayInputStream(Util.Bcrypt.hash("ciao123")));

		String regToken = Util.generateToken();

		UserDao.registerUserDetails(jobSeekerUserModel);
		UserAuthDao.registerUserAuth(jobSeekerUserModel, userAuthModel, regToken);
		UserAuthDao.confirmRegistration("xyzt@abc.domx", regToken);
	}

	@Test
	public void testBasicLoginSuccess() throws InternalException {
		assertNotEquals(
			null,
			LoginController.basicLogin(
				BeanFactory.buildUserAuthBean("xyzt@abc.domx", "ciao123")));
	}

	@Test
	public void testBasicLoginFailureNoUser() throws InternalException {
		assertEquals(
			null, 
			LoginController.basicLogin(
				BeanFactory.buildUserAuthBean("nex@user", "ciao123")));
	}

	@Test
	public void testBasicLoginFailureNoPasswordMatch() throws InternalException {
		assertEquals(
			null, 
			LoginController.basicLogin(
				BeanFactory.buildUserAuthBean("xyzt@abc.domx", "ciao1234")));

	}
}
