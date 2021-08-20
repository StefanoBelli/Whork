package logic.controller;

import java.util.ArrayList;
import java.util.List;

import logic.bean.CandidatureBean;
import logic.bean.CompanyBean;
import logic.bean.OfferBean;
import logic.bean.UserBean;
import logic.dao.CandidatureDao;
import logic.dao.OfferDao;
import logic.dao.UserDao;
import logic.exception.DataAccessException;
import logic.exception.DataLogicException;
import logic.exception.InternalException;
import logic.exception.SendMailException;
import logic.factory.BeanFactory;
import logic.factory.ModelFactory;
import logic.model.CandidatureModel;
import logic.model.CompanyModel;
import logic.model.JobSeekerUserModel;
import logic.util.Util;

/**
 * @author Michele Tosi
 */

public final class CandidatureController {
	
	private static final String DATA_ACCESS_ERROR =
			"Data access error";
	private static final String UNABLE_TO_SEND_EMAIL =
			"Unable to send you an email!";
	
	private CandidatureController() {}

	public static void insertCandidature(CandidatureBean candidatureBean) 
			throws InternalException {
		
		try {
			CandidatureDao.insertCandidature(ModelFactory.buildCandidatureModel(candidatureBean));
		} catch(DataAccessException e) {
			return; //ignore, browser may resend same form, triggering SQL integrity violation
		}
		
		try {
			OfferDao.updateClickStats(ModelFactory.buildCandidatureModel(candidatureBean));
			sendMail(UserDao.getJobSeekerEmailByCf(ModelFactory.buildUserModel(candidatureBean.getJobSeeker()))
					, candidatureBean.getOffer());
		} catch (DataAccessException | DataLogicException e) {
			Util.exceptionLog(e);
			throw new InternalException(DATA_ACCESS_ERROR);
		}
	}
	
	public static CandidatureBean getCandidature(int id, String cf) throws InternalException {
		try {
			if(CandidatureDao.getCandidature(id, cf)==null) {
				return null;
			}else {
				return BeanFactory.buildCandidatureBean(CandidatureDao.getCandidature(id, cf));				
			}
		} catch (DataAccessException | DataLogicException e) {
			Util.exceptionLog(e);
			throw new InternalException(DATA_ACCESS_ERROR);
		}
	}
	
	public static String getEmployeeEmailByCf(UserBean userBean) throws InternalException {
		try {
			return UserDao.getEmployeeEmailByCf(ModelFactory.buildUserModel(userBean));
		} catch (DataLogicException | DataAccessException e) {
			Util.exceptionLog(e);
			throw new InternalException(DATA_ACCESS_ERROR);
		}
	}
	
	public static void deleteCandidature(UserBean userBean, CandidatureBean candidatureBean) throws InternalException {		
		try {
			CandidatureDao.deleteCandidature((JobSeekerUserModel) ModelFactory.buildUserModel(userBean), ModelFactory.buildCandidatureModel(candidatureBean));
		} catch (DataAccessException e) {
			Util.exceptionLog(e);
			throw new InternalException(DATA_ACCESS_ERROR);
		}		
	}
	
	public static List<Integer> getCandidatureByCompanyVat(CompanyBean companyBean) throws DataAccessException, DataLogicException {
		CompanyModel company = ModelFactory.buildCompanyModel(companyBean);
		List<CandidatureModel> listModel = CandidatureDao.getCandidatureByCompanyVat(company);
		List<Integer> month = new ArrayList<>();
		
		for(int j=0; j<12; j++)
			month.add(0);			
		
		int i = 0;
		while (i<listModel.size()) {
			String date = listModel.get(i).getCandidatureDate().toString().substring(5, 7);

			final int mIdx = Integer.parseInt(date) - 1;
			month.set(mIdx, month.get(mIdx) + 1);

			++i;
		}
		
		return month;
	}
	
	private static void sendMail(String email, OfferBean offerBean) 
			throws InternalException {
		String subject = "Whork confirm candidacy";

		StringBuilder messageBuilder = new StringBuilder();
		messageBuilder.append("Whork recieved a request to candidature for the offer \"");
		messageBuilder.append(offerBean.getOfferName());
		messageBuilder.append("\" whit this email address.\n\n");
		messageBuilder.append("Whork Staff");

		String body = messageBuilder.toString();

		try {
			Util.Mailer.sendMail(email, subject, body);
		} catch (SendMailException e) {
			Util.exceptionLog(e);
			throw new InternalException(UNABLE_TO_SEND_EMAIL);
		}
	}
}