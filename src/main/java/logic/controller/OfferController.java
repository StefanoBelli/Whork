package logic.controller;

import java.util.List;

import logic.bean.OfferBean;
import logic.dao.OfferDao;
import logic.exception.DataAccessException;
import logic.exception.DataLogicException;
import logic.exception.InternalException;
import logic.factory.BeanFactory;
import logic.factory.ModelFactory;

public final class OfferController {

	private static final String DATA_ACCESS_ERROR =
			"Data access error";
	
	private OfferController() {}
	
	public static List<OfferBean> searchOffers(String searchVal, String category,
			String position, String qualification, String typeOfContract) 
				throws InternalException{

		try {
			return BeanFactory.buildOffersBean(
					OfferDao.getOffers(searchVal, category,
							position, qualification, typeOfContract));
		} catch (DataAccessException | DataLogicException e) {
			throw new InternalException(DATA_ACCESS_ERROR);
		}
	}
	
	public static OfferBean getOfferById(int offerId) throws InternalException {
		try {
			return BeanFactory.buildOfferBean(OfferDao.getOfferById(offerId));
		} catch (DataAccessException | DataLogicException e) {
			throw new InternalException(DATA_ACCESS_ERROR);
		}
	}

	public static void postOffer(OfferBean offerBean) throws InternalException {
		try {
			OfferDao.postOffer(ModelFactory.buildOfferModel(offerBean));
		} catch (DataAccessException e) {
			throw new InternalException(DATA_ACCESS_ERROR);
		}		
	}
}
