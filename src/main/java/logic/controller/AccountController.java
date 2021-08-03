package logic.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import logic.bean.CandidatureBean;
import logic.bean.ChatLogEntryBean;
import logic.bean.CompanyBean;
import logic.bean.UserAuthBean;
import logic.bean.UserBean;
import logic.dao.AccountDao;
import logic.dao.ChatLogDao;
import logic.dao.OfferDao;
import logic.dao.UserAuthDao;
import logic.dao.UserDao;
import logic.exception.DataAccessException;
import logic.exception.DataLogicException;
import logic.exception.InternalException;
import logic.exception.InvalidPasswordException;
import logic.factory.BeanFactory;
import logic.factory.ModelFactory;
import logic.model.CandidatureModel;
import logic.model.ChatLogEntryModel;
import logic.model.EmployeeUserModel;
import logic.model.EmploymentStatusModel;
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
	
	public static void editAccountController(String function, UserBean userBean, UserAuthBean userAuthBean, String newPassword) 
			throws DataAccessException, InternalException, InvalidPasswordException, DataLogicException {
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
	
	public static Map<String, Double> getEmploymentStatusBtCompanyVAT(CompanyBean company) throws DataAccessException, DataLogicException {
		List<EmploymentStatusModel> listEmployments =  UserDao.getEmploymentStatusByCompanyVat(ModelFactory.buildCompanyModel(company));
		Map<String, Double> map = new HashMap<>();
		double tot = 0;
			
		for(int i=0; i<listEmployments.size(); i++) {
			if(map.containsKey(listEmployments.get(i).getStatus())) {
				map.replace(listEmployments.get(i).getStatus(), map.get(listEmployments.get(i).getStatus())+1.0);				
			} else {
				map.put(listEmployments.get(i).getStatus(), 1.0);				
			}
			tot += 1.0;
		}		
		
		if(tot == 0) { //avoid division by zero
			return map;
		}

		Iterator<String> keys = map.keySet().iterator();		
		
		while(keys.hasNext()) {
			String key = keys.next();
			map.replace(key, map.get(key)/tot);
		}		
		
		return map;
	}
	
	public static Map<String, Double> getCountryCandidateByFiscalCode(CompanyBean companyBean) throws DataAccessException {
		List<String> listCountry = AccountDao.getCountryFiscalCodeDecode(ModelFactory.buildCompanyModel(companyBean));
		
		Map<String, Double> map = new HashMap<>();
		double tot = 0;
		
		for(int i=0; i<listCountry.size(); i++) {
			if(map.containsKey(listCountry.get(i))) {
				map.replace(listCountry.get(i), map.get(listCountry.get(i))+1.0);				
			} else {
				map.put(listCountry.get(i), 1.0);
			}
			tot += 1.0;
		}
		
		if(tot == 0) { //avoid division by zero
			return map;
		}
		
		Iterator<String> keys = map.keySet().iterator();		
		
		while(keys.hasNext()) {
			String key = keys.next();
			map.replace(key, map.get(key)/tot);
		}
		
		return map;		
	}
	
	public static Map<String, UserBean> getEmployeeByCompanyVAT(CompanyBean companyBean) throws DataAccessException {
		Map<String, EmployeeUserModel> map = UserDao.getEmployeeByCompanyVAT(ModelFactory.buildCompanyModel(companyBean));		
		Map<String, UserBean> mapBean = new HashMap<>();
		
		Iterator<String> keys = map.keySet().iterator();		
		
		while(keys.hasNext()) {
			String key = keys.next();
			mapBean.put(key, BeanFactory.buildUserBean(map.get(key)));
		}
		
		return mapBean;		
	}
	
	public static int getNumberOfferOfAnEmployee(UserBean userBean) throws DataAccessException {
		return AccountDao.getNumberOfferOfAnEmployee(ModelFactory.buildUserModel(userBean));
	}
	
	public static List<ChatLogEntryBean> getLastMessage(String email) throws DataAccessException {
		List<ChatLogEntryModel> listMessageModel = ChatLogDao.getLastMessage(email);
		List<ChatLogEntryBean> listMessage = new ArrayList<>();
		
		for(int i=0; i<listMessageModel.size(); i++) {
			listMessage.add(BeanFactory.buildChatEntryLogBean(listMessageModel.get(i)));
		}
		
		return listMessage;
	}
}
