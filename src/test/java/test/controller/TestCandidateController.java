package test.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import logic.bean.CandidatureBean;
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
import logic.util.tuple.Pair;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestCandidateController {
	
	@Test
	public void testAInsertCandidature() throws InternalException, InvalidVatCodeException, AlreadyExistantCompanyException, AlreadyExistantUserException, DataAccessException, DataLogicException {
		UserBean user=new UserBean();
		user.setAdmin(false);
		user.setEmployee(false);
		user.setRecruiter(false);
		user.setName("nome2");
		user.setSurname("cognome2");
		user.setPhoneNumber("3346519346");
		user.setCf("SRRSND04R45A422Y");
		
		UserAuthBean userAuth=new UserAuthBean();
		userAuth.setEmail("email2@gmail.com");
		userAuth.setPassword("password");
		
		RegisterController.register(new Pair<>(user, userAuth));
		
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
