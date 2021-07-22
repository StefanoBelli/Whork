package logic.controller;

import logic.bean.CandidatureBean;
import logic.dao.CandidatureDao;
import logic.dao.OfferDao;
import logic.exception.DataAccessException;
import logic.exception.DataLogicException;
import logic.factory.BeanFactory;

public final class CandidatureController {

	public static void insertCandidature(int id, String cf) 
			throws DataAccessException {
		
		CandidatureDao.insertCandidature(id, cf);
		OfferDao.updateClickStats(id);
	}
	
	public static CandidatureBean getCandidature(int id, String cf) throws DataAccessException, DataLogicException {
		return BeanFactory.buildCandidatureBean(CandidatureDao.getCandidature(id, cf));
	}
	
}
