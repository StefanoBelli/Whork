package logic.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import logic.bean.CandidatureBean;
import logic.bean.UserAuthBean;
import logic.bean.UserBean;
import logic.dao.AccountDao;
import logic.dao.OfferDao;
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
import logic.util.ServletUtil;
import logic.util.Util;
import logic.util.tuple.Pair;

public final class AccountController {
	
	private static final String DATA_ACCESS_ERROR =
			"Data access error";
	
	private AccountController() {}
	
	public static List<CandidatureBean> getSeekerCandidature(UserBean userBean) throws InternalException {		
		List<CandidatureBean> listCandidatureBean = new ArrayList<>();		
		List<CandidatureModel> listCandidatureModel;
		try {
			listCandidatureModel = AccountDao.getSeekerCandidature(ModelFactory.buildUserModel(userBean));
		} catch (DataAccessException | DataLogicException e) {
			throw new InternalException(DATA_ACCESS_ERROR);
		}
		
		if(listCandidatureModel == null) return listCandidatureBean;
		
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
		
		if (function.equals("SocialAccounts")) 
			AccountDao.editSocialAccount (userModel);
		
		else if (function.equals("JobSeekerInfoAccount")) {
			if(userAuthModel == null) 
				throw new InternalException("userAuthModel is null");			

			AccountDao.editJobSeekerInfoAccount (userModel, userAuthModel.getEmail());
		} else if (function.equals("JobSeekerBiography"))
			AccountDao.editJobSeekerBiography (userModel);
		
		else if (function.equals("ChangePasswordAccount")) {	
			if(userAuthModel == null) 
				throw new InternalException("userAuthModel is null");			
			
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
	
	public static UserBean changePictureAccountJobSeeker(String newPath, UserBean userBean) throws DataAccessException, IOException {
		JobSeekerUserModel userModel = (JobSeekerUserModel) ModelFactory.buildUserModel(userBean);
		AccountDao.editJobSeekerPicture (userModel, newPath);
		if(userBean.getPhoto() != null)	ServletUtil.deleteUserFile(userBean.getPhoto());
		userBean.setPhoto(newPath);
		return userBean;
	}
	
	public static int getNumberOfEmployees(UserBean userBean) throws DataAccessException, DataLogicException {
		return AccountDao.countOfEmployees(ModelFactory.buildCompanyModel(userBean.getCompany()));		
	}
	
	public static int getNumberOfOffers(UserBean userBean) throws DataAccessException, DataLogicException {
		return OfferDao.totalNumberOffers(ModelFactory.buildCompanyModel(userBean.getCompany()));		
	}
	
	public static int getNumberOfClick(UserBean userBean) throws DataAccessException, DataLogicException {
		return OfferDao.totalNumberOfClick(ModelFactory.buildCompanyModel(userBean.getCompany()));		
	}

}
