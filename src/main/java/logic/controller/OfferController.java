package logic.controller;

import java.util.List;

import logic.bean.OfferBean;
import logic.dao.OfferDao;
import logic.exception.DataAccessException;
import logic.factory.BeanFactory;

public final class OfferController {
	private OfferController() {}
	
	public static List<OfferBean> getOffers() throws DataAccessException{
		return BeanFactory.buildOfferBean(OfferDao.getOffers());
	}

}
