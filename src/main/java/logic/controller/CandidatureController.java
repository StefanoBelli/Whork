package logic.controller;

import logic.bean.CandidatureBean;
import logic.bean.UserBean;
import logic.dao.CandidatureDao;
import logic.dao.OfferDao;
import logic.dao.UserDao;
import logic.exception.DataAccessException;
import logic.exception.DataLogicException;
import logic.factory.BeanFactory;
import logic.factory.ModelFactory;
import logic.model.JobSeekerUserModel;

public final class CandidatureController {
	
	private CandidatureController() {}

	public static void insertCandidature(CandidatureBean candidatureBean) 
			throws DataAccessException {
		
		CandidatureDao.insertCandidature(ModelFactory.buildCandidatureModel(candidatureBean));
		OfferDao.updateClickStats(ModelFactory.buildCandidatureModel(candidatureBean));
	}
	
	public static CandidatureBean getCandidature(int id, String cf) throws DataAccessException, DataLogicException {
		if(CandidatureDao.getCandidature(id, cf)==null) {
			return null;
		}else {
			return BeanFactory.buildCandidatureBean(CandidatureDao.getCandidature(id, cf));
		}
	}
	
	public static String getEmployeeEmailByCf(UserBean userBean) throws DataLogicException {
		return UserDao.getEmployeeEmailByCf(ModelFactory.buildUserModel(userBean));
	}
	
	public static void deleteCandidature(UserBean userBean, CandidatureBean candidatureBean) throws DataAccessException {		
		CandidatureDao.deleteCandidatureDao((JobSeekerUserModel) ModelFactory.buildUserModel(userBean), ModelFactory.buildCandidatureModel(candidatureBean));		
	}
	
}
