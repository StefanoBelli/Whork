package logic.controller;

import logic.bean.CandidatureBean;
import logic.bean.UserBean;
import logic.dao.CandidatureDao;
import logic.dao.OfferDao;
import logic.dao.UserDao;
import logic.exception.DataAccessException;
import logic.exception.DataLogicException;
import logic.exception.InternalException;
import logic.factory.BeanFactory;
import logic.factory.ModelFactory;
import logic.model.JobSeekerUserModel;

public final class CandidatureController {
	
	private static final String DATA_ACCESS_ERROR =
			"Data access error";
	
	private CandidatureController() {}

	public static void insertCandidature(CandidatureBean candidatureBean) 
			throws DataAccessException {
		
		CandidatureDao.insertCandidature(ModelFactory.buildCandidatureModel(candidatureBean));
		OfferDao.updateClickStats(ModelFactory.buildCandidatureModel(candidatureBean));
	}
	
	public static CandidatureBean getCandidature(int id, String cf) throws InternalException {
		try {
			if(CandidatureDao.getCandidature(id, cf)==null) {
				return null;
			}else {
				return BeanFactory.buildCandidatureBean(CandidatureDao.getCandidature(id, cf));				
			}
		} catch (DataAccessException | DataLogicException e) {
			throw new InternalException(DATA_ACCESS_ERROR);
		}
	}
	
	public static String getEmployeeEmailByCf(UserBean userBean) throws InternalException {
		try {
			return UserDao.getEmployeeEmailByCf(ModelFactory.buildUserModel(userBean));
		} catch (DataLogicException | DataAccessException e) {
			throw new InternalException(DATA_ACCESS_ERROR);
		}
	}
	
	public static void deleteCandidature(UserBean userBean, CandidatureBean candidatureBean) throws InternalException {		
		try {
			CandidatureDao.deleteCandidature((JobSeekerUserModel) ModelFactory.buildUserModel(userBean), ModelFactory.buildCandidatureModel(candidatureBean));
		} catch (DataAccessException e) {
			throw new InternalException(DATA_ACCESS_ERROR);
		}		
	}
	
}
