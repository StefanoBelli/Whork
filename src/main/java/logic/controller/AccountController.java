package logic.controller;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import logic.bean.CandidatureBean;
import logic.bean.UserAuthBean;
import logic.bean.UserBean;
import logic.dao.AccountDao;
import logic.dao.CandidatureDao;
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
	
	public static List<CandidatureBean> getSeekerCandidature(UserBean userBean) throws DataAccessException, DataLogicException {		
		List<CandidatureBean> listCandidatureBean = new ArrayList<>();
		List<CandidatureModel> listCandidatureModel = new ArrayList<>();
		
		listCandidatureModel = AccountDao.getSeekerCandidature(ModelFactory.buildUserModel(userBean));
		
		if(listCandidatureModel == null) return null;
		
		int i = 0;
		while (i<listCandidatureModel.size()) {
			listCandidatureBean.add(BeanFactory.buildCandidatureBean(listCandidatureModel.get(i)));
			i ++;
		}
		
		return listCandidatureBean;
		
	}
	
	public static void deleteCandidature(UserBean userBean, CandidatureBean candidatureBean) throws DataAccessException {		
		CandidatureDao.deleteCandidatureDao((JobSeekerUserModel) ModelFactory.buildUserModel(userBean), ModelFactory.buildCandidatureModel(candidatureBean));		
	}
	
	public static void editAccountController(String function, UserBean userBean, UserAuthBean userAuthBean, String newPassword) throws DataAccessException, InternalException, InvalidPasswordException, DataLogicException {
		JobSeekerUserModel userModel = (JobSeekerUserModel) ModelFactory.buildUserModel(userBean);
		UserAuthModel userAuthModel = null;
		
		if(userAuthBean != null) userAuthModel = ModelFactory.buildUserAuthModel(userAuthBean);
		if(function == null) throw new InternalException("Function value cannot be null");
		
		if (function.equals("SocialAccounts")) 
			AccountDao.editSocialAccountDao(userModel);		
		else if (function.equals("JobSeekerInfoAccount"))
			AccountDao.editJobSeekerInfoAccountDao(userModel, userAuthModel.getEmail());
		else if (function.equals("JobSeekerBiography"))
			AccountDao.editJobSeekerBiographyDao(userModel);
		else if (function.equals("ChangePasswordAccount")) {	
			System.out.println(userAuthModel.getEmail());
			Pair<String, ByteArrayInputStream> user = UserAuthDao.getUserCfAndBcryPwdByEmail(userAuthModel.getEmail());						
			if(!Util.Bcrypt.equals(userAuthBean.getPassword(), user.getSecond().readAllBytes())) { //oldPassword == passwordSavedInDB
				throw new InvalidPasswordException();							
			} else {
				userAuthBean.setPassword(newPassword);
				UserAuthModel newUserAuthModel = ModelFactory.buildUserAuthModel(userAuthBean);
				UserAuthDao.changeUserAuthPassword(newUserAuthModel);
			}
			
		}
	}

}
