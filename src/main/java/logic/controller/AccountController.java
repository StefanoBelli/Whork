package logic.controller;

import java.util.ArrayList;

import logic.bean.CandidatureBean;
import logic.bean.UserBean;
import logic.dao.AccountDao;
import logic.exception.DataAccessException;
import logic.factory.BeanFactory;
import logic.model.CandidatureModel;

public final class AccountController {
	private AccountController() {}
	
	public static ArrayList<CandidatureBean> getSeekerCandidature(String cf) throws DataAccessException {		
		ArrayList<CandidatureBean> listCandidatureBean = new ArrayList<CandidatureBean>();
		ArrayList<CandidatureModel> listCandidatureModel = new ArrayList<CandidatureModel>();
		
		listCandidatureModel = AccountDao.getSeekerCandidature(cf);
		int i = 0;
		while (i<listCandidatureModel.size()) {
			listCandidatureBean.add(BeanFactory.buildCandidatureBean(listCandidatureModel.get(i).getSocialReason(), listCandidatureModel.get(i).getCandidatureDate(), listCandidatureModel.get(i).getTypeOfContract(), listCandidatureModel.get(i).getJobOccupation(), listCandidatureModel.get(i).getEmail()));
			i ++;
		}
		
		return listCandidatureBean;
		
	}
	
	public static void editSocialAccountController(UserBean userbean) throws DataAccessException {
		AccountDao.editSocialAccountDao(userbean.getCf(), userbean.getWebsite(), userbean.getTwitter(), userbean.getFacebook(), userbean.getInstagram());		
	}

}
