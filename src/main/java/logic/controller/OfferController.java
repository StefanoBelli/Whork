package logic.controller;

import java.util.List;

import logic.bean.OfferBean;
import logic.dao.OfferDao;
import logic.exception.DataAccessException;
import logic.exception.DataLogicException;
import logic.factory.BeanFactory;
import logic.factory.ModelFactory;

public final class OfferController {
	private OfferController() {}
	
	public static List<OfferBean> searchOffers(OfferBean offerBean) 
				throws DataAccessException, DataLogicException{

		return BeanFactory.buildOffersBean(
				OfferDao.getOffers(ModelFactory.buildOfferFilterModel(offerBean)));
	}
	
	public static OfferBean getOfferById(int offerId) throws DataAccessException, DataLogicException {
		return BeanFactory.buildOfferBean(OfferDao.getOfferById(offerId));
	}
	
	
}
