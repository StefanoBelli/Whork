package test.controller;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import logic.bean.CompanyBean;
import logic.bean.OfferBean;
import logic.bean.UserAuthBean;
import logic.bean.UserBean;
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
 * @author Michele Tosi
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestOfferController {

	@BeforeClass
	static public void insertData() throws InternalException, InvalidVatCodeException, 
		AlreadyExistantCompanyException, AlreadyExistantUserException, 
		ClassNotFoundException, SQLException, DataAccessException {

		Db.init();
		
		Util.InstanceConfig.setConf(Util.InstanceConfig.KEY_MAILTLS, false);
		Util.InstanceConfig.setConf(Util.InstanceConfig.KEY_MAILHOST, "smtp.more.fake.than.this");
		Util.InstanceConfig.setConf(Util.InstanceConfig.KEY_MAILFROM, "fake@fake.fakefakefake");
		Util.InstanceConfig.setConf(Util.InstanceConfig.KEY_MAILSMTP_PORT, "587");
		
		CompanyBean company= new CompanyBean();
		company=BeanFactory.buildCompanyBean("LMBTTR04T45A662J", 
				"data/seide.png", "Lamborghini", "00591801204");
		
		UserBean user=new UserBean();
		user.setAdmin(true);
		user.setEmployee(true);
		user.setRecruiter(true);
		user.setCompany(company);
		user.setName("nome1");
		user.setSurname("cognome1");
		user.setPhoneNumber("3335519346");
		user.setCf("SRRSND04R45A412X");
		
		UserAuthBean userAuth=new UserAuthBean();
		userAuth.setEmail("email1@libero.it");
		userAuth.setPassword("password");
		
		try {
			RegisterController.register(new Pair<>(user, userAuth));
		} catch(InternalException e) { // see test method desc
			if (!e.getMessage().equals("Unable to send you an email!")) {
				throw e;
			}
		}

		OfferBean offer=new OfferBean();
		offer.setCompany(company);
		offer.setDescription("descrizione offerta 1");
		offer.setEmployee(user);
		offer.setJobCategory(BeanFactory.buildJobCategoryBean("Engineering"));
		offer.setJobPhysicalLocationFullAddress("via gela 8");
		offer.setJobPosition(BeanFactory.buildJobPositionBean("Engineer"));
		offer.setOfferName("offer 1");
		offer.setQualification(BeanFactory.buildQualificationBean("Master's degree"));
		offer.setSalaryEUR(2000);
		offer.setTypeOfContract(BeanFactory.buildTypeOfContractBean("Full Time"));
		offer.setWorkShit("09:00 - 19:00");
		
		OfferController.postOffer(offer);
	}
	
	@Test
	public void testASearchOffers() throws InternalException {
		List<OfferBean> offers=new ArrayList<>();
		
		offers=OfferController.searchOffers("offer 1", "Engineering", "Engineer", "Master's degree", "Full Time");
		
		assertEquals(1, offers.size());
		
	}
	
	@Test
	public void testBPostOffer() throws InternalException {
		
		CompanyBean company= new CompanyBean();
		company=BeanFactory.buildCompanyBean("LMBTTR04T45A662J", 
				"data/seide.png", "Lamborghini", "00591801204");
		
		UserBean user=new UserBean();
		user.setAdmin(true);
		user.setEmployee(true);
		user.setRecruiter(true);
		user.setCompany(company);
		user.setName("nome1");
		user.setSurname("cognome1");
		user.setPhoneNumber("3335519346");
		user.setCf("SRRSND04R45A412X");
		
		
		OfferBean offer=new OfferBean();
		offer.setCompany(company);
		offer.setDescription("descrizione offerta 2");
		offer.setEmployee(user);
		offer.setJobCategory(BeanFactory.buildJobCategoryBean("Engineering"));
		offer.setJobPhysicalLocationFullAddress("via alessandrino 8");
		offer.setJobPosition(BeanFactory.buildJobPositionBean("Engineer"));
		offer.setOfferName("offer 12");
		offer.setQualification(BeanFactory.buildQualificationBean("Master's degree"));
		offer.setSalaryEUR(2000);
		offer.setTypeOfContract(BeanFactory.buildTypeOfContractBean("Full Time"));
		offer.setWorkShit("10:00 - 19:00");
		
		OfferController.postOffer(offer);
		
		List<OfferBean> offers=new ArrayList<>();
		

		offers=OfferController.searchOffers("offer 1", "Engineering", "Engineer", "Master's degree", "Full Time");
		
		assertEquals(2, offers.size());
	}
}
