package test.controller;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import logic.bean.CandidatureBean;
import logic.bean.CompanyBean;
import logic.bean.ComuneBean;
import logic.bean.EmploymentStatusBean;
import logic.bean.OfferBean;
import logic.bean.ProvinciaBean;
import logic.bean.RegioneBean;
import logic.bean.UserAuthBean;
import logic.bean.UserBean;
import logic.controller.AccountController;
import logic.controller.CandidatureController;
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
 * @author Magliari Elio
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestAccountController {
	static UserBean userJobSeeker;

	@BeforeClass
	public static void initDb() 
			throws ClassNotFoundException, SQLException, DataAccessException, 
			InvalidVatCodeException, InternalException {

		Db.init();

		Util.InstanceConfig.setConf(Util.InstanceConfig.KEY_MAILTLS, false);
		Util.InstanceConfig.setConf(Util.InstanceConfig.KEY_MAILHOST, "smtp.more.fake.than.this");
		Util.InstanceConfig.setConf(Util.InstanceConfig.KEY_MAILFROM, "whork.noreply@gmail.com");
		Util.InstanceConfig.setConf(Util.InstanceConfig.KEY_MAILSMTP_PORT, "587");

		//Recruiter
		CompanyBean company = new CompanyBean();
		company = BeanFactory.buildCompanyBean("FRRTTR04T45A662J", 
				"data/seide.png", "FERRARI", "00159560366");

		UserBean user = new UserBean();
		user.setAdmin(true);
		user.setEmployee(true);
		user.setRecruiter(true);
		user.setCompany(company);
		user.setName("name recruiter");
		user.setSurname("surname recruiter");
		user.setPhoneNumber("3333333333");
		user.setCf("TPLPLT00A01D612T");

		UserAuthBean userAuth = new UserAuthBean();
		userAuth.setEmail("recruiter@gmail.com");
		userAuth.setPassword("password");

		try {
			RegisterController.register(new Pair<>(user, userAuth));
		} catch (InternalException e) {
			if(!e.getMessage().equals("Unable to send you an email!")) {
				throw e;
			}
		} catch (AlreadyExistantCompanyException e) {
			//
		} catch (AlreadyExistantUserException e) {
			//
		}

		OfferBean offer = new OfferBean();
		offer.setId(1);
		offer.setCompany(company);
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
		} catch (AlreadyExistantCompanyException e) {
			//
		} catch (AlreadyExistantUserException e) {
			//
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

	}

	@Test
	public void testAGetSeekerCandidature() throws InternalException {
		assertEquals(1, AccountController.getSeekerCandidature(userJobSeeker).size());
	}
}
