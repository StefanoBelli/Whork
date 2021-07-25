package logic.controller;

import java.util.List;

import logic.bean.OfferBean;
import logic.dao.OfferDao;
import logic.exception.DataAccessException;
import logic.exception.DataLogicException;
import logic.factory.BeanFactory;

public final class OfferController {
	private OfferController() {}
	
	public static List<OfferBean> searchOffers(String searchVal, String category,
			String position, String qualification, String typeOfContract) 
				throws DataAccessException, DataLogicException{

		return BeanFactory.buildOffersBean(
				OfferDao.getOffers(searchVal, category,
						position, qualification, typeOfContract));
	}
	
	public static OfferBean getOfferById(int offerId) throws DataAccessException, DataLogicException {
		return BeanFactory.buildOfferBean(OfferDao.getOfferById(offerId));
	}
	
	
}
