package test.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.sql.SQLException;
import java.util.Date;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import logic.bean.CandidatureBean;
import logic.bean.CompanyBean;
import logic.bean.OfferBean;
import logic.bean.UserAuthBean;
import logic.bean.UserBean;
import logic.controller.CandidatureController;
import logic.controller.OfferController;
import logic.controller.RegisterController;
import logic.exception.AlreadyExistantCompanyException;
import logic.exception.AlreadyExistantUserException;
import logic.exception.DataAccessException;
import logic.exception.DataLogicException;
import logic.exception.InternalException;
import logic.exception.InvalidVatCodeException;
import logic.factory.BeanFactory;
import logic.util.Util;
import logic.util.tuple.Pair;
import test.Db;

/**
 * @author Michele Tosi
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestCandidatureController {
	
	@BeforeClass
	public static void insertData() 
		throws InternalException, DataAccessException, InvalidVatCodeException, 
			AlreadyExistantCompanyException, AlreadyExistantUserException, 
			ClassNotFoundException, SQLException {

		Db.init();
		
		Util.InstanceConfig.setConf(Util.InstanceConfig.KEY_MAILTLS, false);
		Util.InstanceConfig.setConf(Util.InstanceConfig.KEY_MAILHOST, "smtp.more.fake.than.this");
		Util.InstanceConfig.setConf(Util.InstanceConfig.KEY_MAILFROM, "fake@fake.fakefakefake");
		Util.InstanceConfig.setConf(Util.InstanceConfig.KEY_MAILSMTP_PORT, "587");		

	}
	
	
	@Test
	public void testAInsertCandidature() 
		throws InternalException, InvalidVatCodeException, 
			AlreadyExistantCompanyException, AlreadyExistantUserException, 
			DataAccessException, DataLogicException {
		
		UserBean user=new UserBean();
		user.setAdmin(false);
		user.setEmployee(false);
		user.setRecruiter(false);
		user.setName("nome5");
		user.setSurname("cognome5");
		user.setPhoneNumber("334671346");
		user.setCf("SRRPQR04R45A422Y");
		user.setBiography("bio");
		user.setHomeAddress("address");
		user.setEmploymentStatus(BeanFactory.buildEmploymentStatusBean("Unemployed"));
		user.setCv("fake/path/to/cv.pdf");
		user.setBirthday(new Date());
		user.setComune(BeanFactory.buildComuneBean("Cave RM - 00033, Lazio"));
		
		UserAuthBean userAuth=new UserAuthBean();
		userAuth.setEmail("email4@gmail.com");
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
		candidature.setOffer(OfferController.getOfferById(3));
		
		try {
			CandidatureController.insertCandidature(candidature);
		} catch (InternalException e) { // see test method desc
			if (!e.getMessage().equals("Unable to send you an email!")) {
				throw e;
			}
		}
		assertEquals(1, OfferController.getOfferById(3).getClickStats());
		
	}
	
	
	@Test
	public void testBGetCandidature() throws InternalException {
		assertNotEquals(null, CandidatureController.getCandidature(3, "SRRPQR04R45A422Y"));
	}
	
	@Test
	public void testCDeleteCandidature() throws InternalException {
		
		CandidatureBean candidature=CandidatureController.getCandidature(3, "SRRPQR04R45A422Y");
		
		CandidatureController.deleteCandidature(candidature.getJobSeeker(),candidature);
		assertEquals(null, CandidatureController.getCandidature(3, "SRRPQR04R45A422Y"));
	}
	
}
