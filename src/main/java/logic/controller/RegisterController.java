package logic.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONObject;

import logic.bean.UserAuthBean;
import logic.bean.UserBean;
import logic.dao.CompanyDao;
import logic.dao.UserAuthDao;
import logic.dao.UserDao;
import logic.exception.AlreadyExistantCompanyException;
import logic.exception.AlreadyExistantUserException;
import logic.exception.DataAccessException;
import logic.exception.DataLogicException;
import logic.exception.InternalException;
import logic.exception.InvalidVatCodeException;
import logic.exception.SendMailException;
import logic.factory.ModelFactory;
import logic.model.CompanyModel;
import logic.model.UserAuthModel;
import logic.model.UserModel;
import logic.model.EmployeeUserModel;
import logic.util.Util;
import logic.util.tuple.Pair;

public final class RegisterController {
	private RegisterController(){}

	private static final String DATA_ACCESS_ERROR =
		"Data access error";
	
	private static final String UNABLE_TO_SEND_EMAIL =
		"Unable to send you an email!";

	private static final String ISVATEU_REST_API_ENDPOINT =
		"https://www.isvat.eu/live/IT/";

	/**
	 * to avoid redundant checks, please note that it is method caller responsibility
	 * to ensure that:
	 *  * companyBean is NOT null
	 *  * companyBean holds data for a valid and registered company (SQL integrity constraint violation othw)
	 * @param pairedBeans
	 * @throws InternalException
	 * @throws AlreadyExistantUserException
	 */
	public static void registerEmployeeForExistingCompany(Pair<UserBean, UserAuthBean> pairedBeans) 
			throws InternalException, AlreadyExistantUserException {
		UserModel user = ModelFactory.buildUserModel(pairedBeans.getFirst());
		UserAuthModel userAuth = ModelFactory.buildUserAuthModel(pairedBeans.getSecond());
		String confirmToken = Util.generateToken();

		if(alreadyExistantUser(user, userAuth)) {
			throw new AlreadyExistantUserException();
		}

		try {
			UserDao.registerUserDetails(user);
			UserAuthDao.registerUserAuth(user, userAuth, confirmToken);
		} catch(DataAccessException e) {
			Util.exceptionLog(e);
			throw new InternalException(DATA_ACCESS_ERROR);
		}

		sendMailWithConfirmationToken(userAuth, confirmToken);
	}

	public static void register(Pair<UserBean, UserAuthBean> pairedBeans) 
			throws InternalException, InvalidVatCodeException, 
				AlreadyExistantCompanyException, AlreadyExistantUserException {

		UserModel user = ModelFactory.buildUserModel(pairedBeans.getFirst()); 
		UserAuthModel userAuth = ModelFactory.buildUserAuthModel(pairedBeans.getSecond());
		String confirmToken = Util.generateToken();
		CompanyModel company = null;

		if (user.isEmployee()) {
			company = ((EmployeeUserModel) user).getCompany();
			if(alreadyExistantCompany(company)) {
				throw new AlreadyExistantCompanyException();
			}

			if(!isValidVatCode(company)) {
				throw new InvalidVatCodeException();
			}
		}

		if(alreadyExistantUser(user, userAuth)) {
			throw new AlreadyExistantUserException();
		}

		try {
			if(company != null) {
				CompanyDao.registerCompany(company);
			}

			UserDao.registerUserDetails(user);
			UserAuthDao.registerUserAuth(user, userAuth, confirmToken);
		} catch(DataAccessException e) {
			Util.exceptionLog(e);
			throw new InternalException(DATA_ACCESS_ERROR);
		}

		sendMailWithConfirmationToken(userAuth, confirmToken);
	}

	public static void confirm(String email, String regToken) 
			throws InternalException {
		try {
			UserAuthDao.confirmRegistration(email, regToken);
		} catch (DataAccessException e) {
			Util.exceptionLog(e);
			throw new InternalException(DATA_ACCESS_ERROR);
		} catch (DataLogicException e) {
			throw new InternalException(e.getMessage());
		}
	}

	private static boolean isValidVatCode(CompanyModel company) {
		String completeUrl = ISVATEU_REST_API_ENDPOINT + company.getVat();
		String jsonResponse;

		try {
			jsonResponse = Util.simpleHttpGet(completeUrl);
		} catch(FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			Util.exceptionLog(e);
			return false;
		}

		JSONObject rootObject = new JSONObject(jsonResponse);
		if(rootObject.has("valid")) {
			return rootObject.getBoolean("valid");
		}
		
		return false;
	}

	private static boolean alreadyExistantUser(UserModel user, UserAuthModel userAuth)
			throws InternalException {
		try {
			boolean userDetailsExist = UserDao.getUserByCf(user.getCf()) != null;
			boolean userEmailExists = UserAuthDao.getUserCfAndBcryPwdByEmailIgnRegPending(userAuth.getEmail()) != null;

			return userEmailExists || userDetailsExist;
		} catch(DataAccessException | DataLogicException e) {
			Util.exceptionLog(e);
			throw new InternalException(DATA_ACCESS_ERROR);
		}
	}

	private static boolean alreadyExistantCompany(CompanyModel company)
			throws InternalException {
		try {
			boolean companyVatExists = CompanyDao.getCompanyByVat(company.getVat()) != null;
			boolean companyFcExists = CompanyDao.getCompanyByCf(company.getCf()) != null;
			boolean companyNameExists = CompanyDao.getCompanyByName(company.getSocialReason()) != null;

			return companyNameExists || companyVatExists || companyFcExists;
		} catch(DataAccessException | DataLogicException e) {
			Util.exceptionLog(e);
			throw new InternalException(DATA_ACCESS_ERROR);
		}
	}

	private static void sendMailWithConfirmationToken(UserAuthModel userAuth, String confirmToken) 
			throws InternalException {
		String subject = "Whork confirm registration";

		StringBuilder messageBuilder = new StringBuilder();
		messageBuilder.append("Whork recieved a request to register an account with this email address - ");
		messageBuilder.append(" if you requested so, click on the link below:\n\n");
		messageBuilder.append("/confirm.jsp?token=");
		messageBuilder.append(confirmToken);
		messageBuilder.append("&email=");

		try {
			messageBuilder.append(URLEncoder.encode(userAuth.getEmail(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			Util.exceptionLog(e);
		}

		String body = messageBuilder.toString();

		try {
			Util.Mailer.sendMail(userAuth.getEmail(), subject, body);
		} catch (SendMailException e) {
			Util.exceptionLog(e);
			throw new InternalException(UNABLE_TO_SEND_EMAIL);
		}
	}
}
