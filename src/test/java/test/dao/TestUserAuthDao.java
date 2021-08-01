package test.dao;

import static org.junit.Assert.assertNotEquals;

import java.io.ByteArrayInputStream;
import java.util.Date;

import org.junit.Test;

import logic.dao.CompanyDao;
import logic.dao.UserAuthDao;
import logic.dao.UserDao;
import logic.exception.DataAccessException;
import logic.exception.DataLogicException;
import logic.model.CompanyModel;
import logic.model.ComuneModel;
import logic.model.EmployeeUserModel;
import logic.model.EmploymentStatusModel;
import logic.model.JobSeekerUserModel;
import logic.model.ProvinciaModel;
import logic.model.RegioneModel;
import logic.model.UserAuthModel;
import logic.util.Util;

public class TestUserAuthDao {

	@Test
	public void testRegisterUserAuthJobSeekerCompleteOk() throws DataAccessException, DataLogicException {
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
		jobSeekerUserModel.setCf("XXXXYYYYTTTTZZZZ");
		jobSeekerUserModel.setComune(comuneCaveModel);
		jobSeekerUserModel.setCv("path-to-cv.pdf");
		jobSeekerUserModel.setEmploymentStatus(empStatusModel);
		jobSeekerUserModel.setHomeAddress("aaa");
		jobSeekerUserModel.setName("ste");
		jobSeekerUserModel.setPhoneNumber("1234567890");
		jobSeekerUserModel.setSurname("be");

		UserAuthModel userAuthModel = new UserAuthModel();
		userAuthModel.setEmail("xyz@abc.dom");
		userAuthModel.setBcryptedPassword(new ByteArrayInputStream(Util.Bcrypt.hash("ciao123")));

		String regToken = Util.generateToken();

		UserDao.registerUserDetails(jobSeekerUserModel);
		UserAuthDao.registerUserAuth(jobSeekerUserModel, userAuthModel, regToken);
		UserAuthDao.confirmRegistration("xyz@abc.dom", regToken);

		assertNotEquals(null, UserDao.getUserByCf("XXXXYYYYTTTTZZZZ"));
	}

	@Test
	public void testRegisterUserAuthEmployeeCompleteOk() throws DataAccessException, DataLogicException {
		CompanyModel fakeCompany = new CompanyModel();
		fakeCompany.setCf("1234567890ABCDEF");
		fakeCompany.setLogo("fake-company.jpg");
		fakeCompany.setSocialReason("Fake");
		fakeCompany.setVat("00123456789");

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

		EmployeeUserModel employeeUserModel = new EmployeeUserModel();
		employeeUserModel.setAdmin(true);
		employeeUserModel.setCf("YYYYZZZZTTTTXXXX");
		employeeUserModel.setCompany(fakeCompany);
		employeeUserModel.setName("again ste");
		employeeUserModel.setPhoneNumber("1234567890");
		employeeUserModel.setPhoto("fake-photo.jpg");
		employeeUserModel.setRecruiter(true);
		employeeUserModel.setSurname("bbbb");

		UserAuthModel userAuthModel = new UserAuthModel();
		userAuthModel.setEmail("new@abc.dom");
		userAuthModel.setBcryptedPassword(new ByteArrayInputStream(Util.Bcrypt.hash("ciaone123")));

		String regToken = Util.generateToken();
		
		CompanyDao.registerCompany(fakeCompany);
		UserDao.registerUserDetails(employeeUserModel);
		UserAuthDao.registerUserAuth(employeeUserModel, userAuthModel, regToken);
		UserAuthDao.confirmRegistration("new@abc.dom", regToken);

		assertNotEquals(null, UserDao.getUserByCf("YYYYZZZZTTTTXXXX"));
	}
}
