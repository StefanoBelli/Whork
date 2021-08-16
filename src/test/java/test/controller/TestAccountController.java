package test.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import logic.bean.CandidatureBean;
import logic.bean.CompanyBean;
import logic.bean.OfferBean;
import logic.bean.UserAuthBean;
import logic.bean.UserBean;
import logic.controller.AccountController;
import logic.controller.CandidatureController;
import logic.controller.OfferController;
import logic.controller.RegisterController;
import logic.dao.UserAuthDao;
import logic.dao.UserDao;
import logic.exception.AlreadyExistantCompanyException;
import logic.exception.AlreadyExistantUserException;
import logic.exception.DataAccessException;
import logic.exception.DataLogicException;
import logic.exception.InternalException;
import logic.exception.InvalidPasswordException;
import logic.exception.InvalidVatCodeException;
import logic.factory.BeanFactory;
import logic.model.JobSeekerUserModel;
import logic.util.Util;
import logic.util.tuple.Pair;
import test.Db;

/**
 * @author Magliari Elio
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestAccountController {
	static UserBean userJobSeeker;
	static UserAuthBean userAuthJobSeeker;
	static UserBean userAdmin;
	static CompanyBean company;

	@BeforeClass
	public static void initDb() 
			throws ClassNotFoundException, SQLException, DataAccessException, 
			InvalidVatCodeException, InternalException, AlreadyExistantCompanyException, AlreadyExistantUserException {

		Db.init();

		Util.InstanceConfig.setConf(Util.InstanceConfig.KEY_MAILTLS, false);
		Util.InstanceConfig.setConf(Util.InstanceConfig.KEY_MAILHOST, "smtp.more.fake.than.this");
		Util.InstanceConfig.setConf(Util.InstanceConfig.KEY_MAILFROM, "whork.noreply@gmail.com");
		Util.InstanceConfig.setConf(Util.InstanceConfig.KEY_MAILSMTP_PORT, "587");

		//Admin
		CompanyBean companyBean = new CompanyBean();
		companyBean = BeanFactory.buildCompanyBean("MDDSS123467890XD", 
				"data/seide.png", "mediaset", "09032310154");
		company = companyBean;

		UserBean user = new UserBean();
		user.setAdmin(true);
		user.setEmployee(true);
		user.setRecruiter(true);
		user.setCompany(companyBean);
		user.setName("name admin");
		user.setSurname("surname admin");
		user.setPhoneNumber("3333333333");
		user.setCf("TPLPLT00A01D612T");
		userAdmin = user;

		// Recruiter
		UserBean userRecr = new UserBean();
		userRecr.setAdmin(false);
		userRecr.setEmployee(true);
		userRecr.setRecruiter(true);
		userRecr.setCompany(companyBean);
		userRecr.setName("name recruiter");
		userRecr.setSurname("surname recruiter");
		userRecr.setPhoneNumber("3333333333");
		userRecr.setCf("TPLPLT00A01D612R");

		UserAuthBean userAuth = new UserAuthBean();
		userAuth.setEmail("admin@gmail.com");
		userAuth.setPassword("password");

		try {
			RegisterController.register(new Pair<>(user, userAuth));
		} catch (InternalException e) {
			if(!e.getMessage().equals("Unable to send you an email!")) {
				throw e;
			}
		}

		OfferBean offer = new OfferBean();
		offer.setId(1);
		offer.setCompany(companyBean);
		offer.setDescription("descrizione offerta 1");
		offer.setEmployee(user);
		offer.setJobCategory(BeanFactory.buildJobCategoryBean("Engineering"));
		offer.setJobPhysicalLocationFullAddress("via tuscolana 8");
		offer.setJobPosition(BeanFactory.buildJobPositionBean("Engineer"));
		offer.setOfferName("offer 1");
		offer.setQualification(BeanFactory.buildQualificationBean("Master's degree"));
		offer.setSalaryEUR(2100);
		offer.setTypeOfContract(BeanFactory.buildTypeOfContractBean("Full Time"));
		offer.setWorkShift("10:00 - 19:00");

		OfferController.postOffer(offer);

		//JobSeeker
		UserBean userBean = new UserBean();
		userBean.setAdmin(false);
		userBean.setEmployee(false);
		userBean.setRecruiter(false);
		userBean.setCompany(null);
		userBean.setName("name");
		userBean.setSurname("surname");
		userBean.setNote(null);
		userBean.setPhoto(null);
		userBean.setPhoneNumber("3333333333");
		userBean.setBirthday(new java.sql.Date(972647238));
		userBean.setCf("TPLPLT00A01D612A");
		userBean.setCv(null);
		userBean.setComune(BeanFactory.buildComuneBean("Cave RM - 00033, Lazio"));
		userBean.setBiography(null);
		userBean.setEmploymentStatus(BeanFactory.buildEmploymentStatusBean("Student"));
		userBean.setWebsite(null);
		userBean.setTwitter(null);
		userBean.setFacebook(null);
		userBean.setInstagram(null);

		UserAuthBean userAuthBean = new UserAuthBean();
		userAuthBean.setEmail("email@gmail.com");
		userAuthBean.setPassword("password");

		try {
			RegisterController.register(new Pair<>(userBean, userAuthBean));
		} catch (InternalException e) {
				if(!e.getMessage().equals("Unable to send you an email!")) {
					throw e;
				}
		}

		userAuth.setEmail("recruiter@gmail.com");
		try {
			RegisterController.registerEmployeeForExistingCompany(new Pair<>(userRecr, userAuth));
		} catch (InternalException e) {
			if (!e.getMessage().equals("Unable to send you an email!")) {
				throw e;
			}
		}

		CandidatureBean candidature = new CandidatureBean();
		candidature.setJobSeeker(userBean);
		candidature.setOffer(OfferController.getOfferById(1));

		try {
			CandidatureController.insertCandidature(candidature);
		} catch (InternalException e) {
			if(!e.getMessage().equals("Unable to send you an email!")) {
				throw e;
			}
		}

		userJobSeeker = userBean;
		userAuthJobSeeker = userAuthBean;
	}

	@Test
	public void testAGetSeekerCandidature() throws InternalException {
		assertEquals(1, AccountController.getSeekerCandidature(userJobSeeker).size());
	}

	@Test
	public void testBEditAccount() throws DataAccessException, InternalException, InvalidPasswordException, DataLogicException {
		String variablePassword = "different password";
		AccountController.editAccountController("ChangePasswordAccount", userJobSeeker, userAuthJobSeeker, variablePassword);
		userAuthJobSeeker.setPassword(variablePassword);
		ByteArrayInputStream password = UserAuthDao.getUserCfAndBcryPwdByEmailIgnRegPending(userAuthJobSeeker.getEmail()).getSecond();
		assertTrue(Util.Bcrypt.equals(userAuthJobSeeker.getPassword(), password.readAllBytes()));
	}

	@Test
	public void testCEditAccount() throws InternalException, DataAccessException, InvalidPasswordException, DataLogicException {
		String variable = "website://whork.it";
		userJobSeeker.setWebsite(variable);
		AccountController.editAccountController("SocialAccounts", userJobSeeker, userAuthJobSeeker, null);
		JobSeekerUserModel user = (JobSeekerUserModel) UserDao.getUserByCf(userJobSeeker.getCf());
		assertEquals(variable, user.getWebsite());
	}

	@Test
	public void testDEditAccount() throws InternalException, DataAccessException, InvalidPasswordException, DataLogicException {
		String variable = "name job seeker";
		userJobSeeker.setName(variable);
		AccountController.editAccountController("JobSeekerInfoAccount", userJobSeeker, userAuthJobSeeker, null);
		JobSeekerUserModel user = (JobSeekerUserModel) UserDao.getUserByCf(userJobSeeker.getCf());
		assertEquals(variable, user.getName());
	}

	@Test
	public void testEEditAccount() throws InternalException, DataAccessException, InvalidPasswordException, DataLogicException {
		String variable = "my bio is different now";
		userJobSeeker.setBiography(variable);
		AccountController.editAccountController("JobSeekerBiography", userJobSeeker, userAuthJobSeeker, null);
		JobSeekerUserModel user = (JobSeekerUserModel) UserDao.getUserByCf(userJobSeeker.getCf());
		assertEquals(variable, user.getBiography());
	}

	@Test
	public void testFChangePictureAccountJobSeeker() throws DataAccessException, IOException, DataLogicException {
		String variable = "/this/is/a/new/path";
		userJobSeeker.setBiography(variable);
		AccountController.changePictureAccountJobSeeker(variable, userJobSeeker);
		JobSeekerUserModel user = (JobSeekerUserModel) UserDao.getUserByCf(userJobSeeker.getCf());
		assertEquals(variable, user.getPhoto());
	}

	@Test
	public void testGGetNumberOfEmployees() throws DataAccessException, IOException, DataLogicException {
		assertEquals(1, AccountController.getNumberOfEmployees(userAdmin));
	}

	@Test
	public void testHGetNumberOfOffers() throws DataAccessException, IOException, DataLogicException {
		assertEquals(1, AccountController.getNumberOfOffers(userAdmin));
	}
}









