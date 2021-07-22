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
	
	public static List<OfferBean> getOffers(String searchVal, String jobCategory, 
			String jobPosition, String qualification, String typeOfContract) 
				throws DataAccessException{

		return BeanFactory.buildOfferBean(
				OfferDao.getOffers(searchVal, jobCategory, 
					jobPosition, qualification, typeOfContract));
	}
	
	public static String getEmployeeEmailByOffer(int id) 
			throws DataAccessException, DataLogicException {

		return OfferDao.getEmployeeEmailByOffer(id);
	}
	
	
	public static CompanyBean getCompanyByVAT(OfferBean offerBean) 
			throws DataAccessException, DataLogicException {
				
		return BeanFactory.buildCompanyBean(CompanyDao.getCompanyByVat(offerBean.getCompanyVat()));
	}
}
