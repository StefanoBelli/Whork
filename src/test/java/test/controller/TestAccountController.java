package test.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.SQLException;

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
import logic.factory.ModelFactory;
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
	static UserBean userRecruiter;
	static UserAuthBean userAuthRecruiter;

	@BeforeClass
	public static void initDb() 
			throws ClassNotFoundException, SQLException, DataAccessException, 
			InvalidVatCodeException, InternalException, DataLogicException, 
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
		userRecruiter = userRecr;

		UserAuthBean userAuth = new UserAuthBean();
		userAuth.setEmail("admin@gmail.com");
		userAuth.setPassword("password");

		try {
			RegisterController.register(new Pair<>(user, userAuth));
		} catch (InternalException e) {
			if(!e.getMessage().equals("Unable to send you an email!")) {
				throw e;
			}
		} catch (AlreadyExistantCompanyException | AlreadyExistantUserException e) {
			//
		}

		userAuth.setEmail("recruiter@gmail.com");
		try {
			RegisterController.registerEmployeeForExistingCompany(new Pair<>(userRecr, userAuth));
		} catch (InternalException e) {
			if (!e.getMessage().equals("Unable to send you an email!")) {
				throw e;
			}
		} catch (AlreadyExistantUserException e) {
			//
		}
		userAuthRecruiter = userAuth;

		OfferBean offer = new OfferBean();
		offer.setId(1);
		offer.setCompany(companyBean);
		offer.setDescription("descrizione offerta 1");
		offer.setEmployee(userRecr);
		offer.setJobCategory(BeanFactory.buildJobCategoryBean("Engineering"));
		offer.setJobPhysicalLocationFullAddress("via tuscolana 8");
		offer.setJobPosition(BeanFactory.buildJobPositionBean("Engineer"));
		offer.setOfferName("offer 1");
		offer.setQualification(BeanFactory.buildQualificationBean("Master's degree"));
		offer.setSalaryEUR(2100);
		offer.setTypeOfContract(BeanFactory.buildTypeOfContractBean("Full Time"));
		offer.setWorkShift("10:00 - 19:00");

		try {
			OfferController.postOffer(offer);
		} catch (InternalException e) {
			//
		}

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

		String regToken = Util.generateToken();

		UserDao.registerUserDetails(ModelFactory.buildUserModel(userBean));
		UserAuthDao.registerUserAuth(ModelFactory.buildUserModel(userBean), ModelFactory.buildUserAuthModel(userAuthBean), regToken);
		UserAuthDao.confirmRegistration(userAuthBean.getEmail(), regToken);

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

	@Test
	public void testIGetNumberOfClick() throws DataAccessException, IOException, DataLogicException {
		assertEquals(1, AccountController.getNumberOfClick(userAdmin));
	}

	@Test
	public void testLGetEmploymentStatusBtCompanyVAT() throws DataAccessException, IOException, DataLogicException {
		assertTrue(1 == AccountController.getEmploymentStatusBtCompanyVAT(company).get(userJobSeeker.getEmploymentStatus().getStatus()));
	}

	@Test
	public void testMGetCountryCandidateByFiscalCode() throws DataAccessException, IOException, DataLogicException {
		assertTrue(1 == AccountController.getCountryCandidateByFiscalCode(company).get("Italy"));
	}

	@Test
	public void testNGetEmployeeByCompanyVAT() throws DataAccessException, IOException, DataLogicException {
		UserBean user = AccountController.getEmployeeByCompanyVAT(company).get(userAuthRecruiter.getEmail());
		assertTrue(userRecruiter.getCf().equals(user.getCf()));
	}

	@Test
	public void testOGetNumberOfferOfAnEmployee() throws DataAccessException, IOException, DataLogicException {
		assertEquals(1, AccountController.getNumberOfferOfAnEmployee(userRecruiter));
	}

	@Test
	public void testPGetNUmberClickOfAnEmployee() throws DataAccessException, IOException, DataLogicException {
		assertEquals(1, AccountController.getNUmberClickOfAnEmployee(userRecruiter));
	}

	@Test
	public void testQGetEmailJobSeekerByCF() throws DataAccessException, IOException, DataLogicException {
		assertEquals(userAuthJobSeeker.getEmail(), AccountController.getEmailJobSeekerByCF(userJobSeeker));
	}

	@Test
	public void testRGetEmailEmployeeByCF() throws DataAccessException, IOException, DataLogicException {
		assertEquals(userAuthRecruiter.getEmail(), AccountController.getEmailEmployeeByCF(userRecruiter));
	}
}









