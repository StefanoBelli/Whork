package logic.controller;

import java.util.ArrayList;

import logic.bean.CandidatureBean;
import logic.bean.UserBean;
import logic.dao.AccountDao;
import logic.exception.DataAccessException;
import logic.factory.BeanFactory;
import logic.factory.ModelFactory;
import logic.model.CandidatureModel;
import logic.model.JobSeekerUserModel;

public final class AccountController {
	private AccountController() {}
	
	public static ArrayList<CandidatureBean> getSeekerCandidature(String cf) throws DataAccessException {		
		ArrayList<CandidatureBean> listCandidatureBean = new ArrayList<CandidatureBean>();
		ArrayList<CandidatureModel> listCandidatureModel = new ArrayList<CandidatureModel>();
		
		listCandidatureModel = AccountDao.getSeekerCandidature(cf);
		
		if(listCandidatureModel == null) return null;
		
		int i = 0;
		while (i<listCandidatureModel.size()) {
			listCandidatureBean.add(BeanFactory.buildCandidatureBean(listCandidatureModel.get(i)));
			i ++;
		}
		
		return listCandidatureBean;
		
	}
	
	public static void editAccountController(String function, UserBean userBean, String email) throws DataAccessException {
		JobSeekerUserModel userModel = (JobSeekerUserModel) ModelFactory.buildUserModel(userBean);
		
		if (function.compareTo("SocialAccounts") == 1) 
			AccountDao.editSocialAccountDao(userBean.getCf(), userBean.getWebsite(), userBean.getTwitter(), userBean.getFacebook(), userBean.getInstagram());		
		if (function.compareTo("JobSeekerInfoAccount") == 1)
			AccountDao.editJobSeekerInfoAccountDao(userModel, email);
		if (function.compareTo("JobSeekerBiography") == 1)
			AccountDao.editJobSeekerBiographyDao(userModel);
	}

}
