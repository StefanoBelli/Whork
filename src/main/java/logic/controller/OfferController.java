package logic.controller;

import java.util.List;

import logic.bean.CompanyBean;
import logic.bean.OfferBean;
import logic.dao.CompanyDao;
import logic.dao.OfferDao;
import logic.exception.DataAccessException;
import logic.exception.DataLogicException;
import logic.factory.BeanFactory;

public final class OfferController {
	private OfferController() {}
	
	public static List<OfferBean> getOffers(String searchVal, String jobCategory, String jobPosition, String qualification, String typeOfContract) throws DataAccessException{
		return BeanFactory.buildOfferBean(OfferDao.getOffers(searchVal, jobCategory, jobPosition, qualification, typeOfContract));
		
	}
	
	public static String getEmployeeEmail(int id) throws DataAccessException, DataLogicException {
		return OfferDao.getEmployeeEmail(id);
	}
	
	public static void insertCandidature(Integer id, String cf) throws DataAccessException, DataLogicException {
		OfferDao.insertCandidature(id, cf);
		return;
	}

	public static void updateClickStats(Integer id) throws DataAccessException, DataLogicException {
		OfferDao.updateClickStats(id);
		return;
	}
	
	public static CompanyBean getCompanyByVAT(OfferBean offerBean) throws DataAccessException, DataLogicException {
		return BeanFactory.buildCompanyBean(CompanyDao.getCompanyByVat(offerBean.getCompanyVat()));
	}

}
