package logic.controller;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import logic.bean.CandidatureBean;
import logic.bean.UserAuthBean;
import logic.bean.UserBean;
import logic.dao.AccountDao;
import logic.dao.UserAuthDao;
import logic.exception.DataAccessException;
import logic.exception.DataLogicException;
import logic.exception.InternalException;
import logic.exception.InvalidPasswordException;
import logic.factory.BeanFactory;
import logic.factory.ModelFactory;
import logic.model.CandidatureModel;
import logic.model.JobSeekerUserModel;
import logic.model.UserAuthModel;
import logic.util.Util;
import logic.util.tuple.Pair;

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
	
	public static void editAccountController(String function, UserBean userBean, UserAuthBean userAuthBean, String newPassword) throws DataAccessException, InternalException, InvalidPasswordException, DataLogicException {
		JobSeekerUserModel userModel = (JobSeekerUserModel) ModelFactory.buildUserModel(userBean);
		UserAuthModel userAuthModel = null;
		
		if(userAuthBean != null) userAuthModel = ModelFactory.buildUserAuthModel(userAuthBean);
		if(function == null) throw new InternalException("Function value cannot be null");
		
		if (function.compareTo("SocialAccounts") == 1) 
			AccountDao.editSocialAccountDao(userBean.getCf(), userBean.getWebsite(), userBean.getTwitter(), userBean.getFacebook(), userBean.getInstagram());		
		if (function.compareTo("JobSeekerInfoAccount") == 1)
			AccountDao.editJobSeekerInfoAccountDao(userModel, userAuthModel.getEmail());
		if (function.compareTo("JobSeekerBiography") == 1)
			AccountDao.editJobSeekerBiographyDao(userModel);
		if (function.compareTo("ChangePasswordAccount") == 1) {		
			Pair<String, ByteArrayInputStream> user = UserAuthDao.getUserCfAndBcryPwdByEmail(userAuthModel.getEmail());						
			System.out.println("password cambiata");
			if(!Util.Bcrypt.equals(userAuthBean.getPassword(), user.getSecond().readAllBytes())) { //oldPassword == passwordSavedInDB
				throw new InvalidPasswordException();							
			} else {
				System.out.println("password cambiata");
				userAuthBean.setPassword(newPassword);
				UserAuthModel newUserAuthModel = ModelFactory.buildUserAuthModel(userAuthBean);
				UserAuthDao.changeUserAuthPassword(newUserAuthModel);
			}
			
		}
	}

}
