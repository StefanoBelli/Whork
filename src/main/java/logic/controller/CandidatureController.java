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

public final class CandidatureController {
	
	private CandidatureController() {}

	public static void insertCandidature(int id, String cf) 
			throws DataAccessException {
		
		CandidatureDao.insertCandidature(id, cf);
		OfferDao.updateClickStats(id);
	}
	
	public static CandidatureBean getCandidature(int id, String cf) throws DataAccessException, DataLogicException {
		return BeanFactory.buildCandidatureBean(CandidatureDao.getCandidature(id, cf));
	}
	
	public static String GetEmployeeEmailByCf(UserBean userBean) throws DataLogicException, DataAccessException {
		return UserDao.getEmployeeEmailByCf(ModelFactory.buildUserModel(userBean));
	}
	
}
