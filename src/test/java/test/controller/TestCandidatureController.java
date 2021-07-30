package test.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.Date;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import logic.bean.CandidatureBean;
import logic.bean.UserAuthBean;
import logic.bean.UserBean;
import logic.controller.CandidatureController;
import logic.controller.OfferController;
import logic.controller.RegisterController;
import logic.dao.ComuniDao;
import logic.dao.EmploymentStatusDao;
import logic.dao.JobCategoryDao;
import logic.dao.JobPositionDao;
import logic.dao.QualificationDao;
import logic.dao.TypeOfContractDao;
import logic.exception.AlreadyExistantCompanyException;
import logic.exception.AlreadyExistantUserException;
import logic.exception.DataAccessException;
import logic.exception.DataLogicException;
import logic.exception.InternalException;
import logic.exception.InvalidVatCodeException;
import logic.factory.BeanFactory;
import logic.util.Util;
import logic.util.tuple.Pair;

/**
 * @author Michele Tosi
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestCandidatureController {
	
	@Test
	public void testAInsertCandidature() 
		throws InternalException, InvalidVatCodeException, 
			AlreadyExistantCompanyException, AlreadyExistantUserException, 
			DataAccessException, DataLogicException {

		Util.InstanceConfig.setConf(Util.InstanceConfig.KEY_MAILTLS, false);
		Util.InstanceConfig.setConf(Util.InstanceConfig.KEY_MAILHOST, "smtp.more.fake.than.this");
		Util.InstanceConfig.setConf(Util.InstanceConfig.KEY_MAILFROM, "fake@fake.fakefakefake");
		Util.InstanceConfig.setConf(Util.InstanceConfig.KEY_MAILSMTP_PORT, "587");
		
		ComuniDao.populatePool();
		EmploymentStatusDao.populatePool();
		JobCategoryDao.populatePool();
		JobPositionDao.populatePool();
		QualificationDao.populatePool();
		TypeOfContractDao.populatePool();

		UserBean user=new UserBean();
		user.setAdmin(false);
		user.setEmployee(false);
		user.setRecruiter(false);
		user.setName("nome2");
		user.setSurname("cognome2");
		user.setPhoneNumber("3346519346");
		user.setCf("SRRSND04R45A422Y");
		user.setBiography("bio");
		user.setHomeAddress("address");
		user.setEmploymentStatus(BeanFactory.buildEmploymentStatusBean("Unemployed"));
		user.setCv("fake/path/to/cv.pdf");
		user.setBirthday(new Date());
		user.setComune(BeanFactory.buildComuneBean("Cave RM - 00033, Lazio"));
		
		UserAuthBean userAuth=new UserAuthBean();
		userAuth.setEmail("email2@gmail.com");
		userAuth.setPassword("password");
		
		try {
			RegisterController.register(new Pair<>(user, userAuth));
		} catch (InternalException e) { // see test method desc
			if (!e.getMessage().equals("Unable to send you an email!")) {
				throw e;
			}
		}
		
		CandidatureBean candidature= new CandidatureBean();
		candidature.setJobSeeker(user);
		candidature.setOffer(OfferController.getOfferById(1));
		
		CandidatureController.insertCandidature(candidature);
		assertEquals(OfferController.getOfferById(1).getClickStats(), 1);
		
	}
	
	
	@Test
	public void testBGetCandidature() throws DataAccessException, DataLogicException {
		assertNotEquals(CandidatureController.getCandidature(1, "SRRSND04R45A422Y"),null);
	}
	
	@Test
	public void testCDeleteCandidature() throws DataAccessException, DataLogicException {
		
		CandidatureBean candidature=CandidatureController.getCandidature(1, "SRRSND04R45A422Y");
		
		CandidatureController.deleteCandidature(candidature.getJobSeeker(),candidature);
		assertEquals(CandidatureController.getCandidature(1, "SRRSND04R45A422Y"),null);
	}
	
}
