package logic.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import logic.bean.UserAuthBean;
import logic.bean.UserBean;
import logic.dao.UserAuthDao;
import logic.dao.UserDao;
import logic.exception.DataAccessException;
import logic.exception.InternalException;
import logic.exception.SendMailException;
import logic.factory.ModelFactory;
import logic.model.UserAuthModel;
import logic.model.UserModel;
import logic.util.Util;

public final class RegisterCandidateController {
	private RegisterCandidateController(){}

	private static final String CHECK_CF_AND_EMAIL =
		"Check your CF and/or email address, probably another registered account has the same of both or one of them";
	
	private static final String UNABLE_TO_SEND_EMAIL =
		"Unable to send you an email!";

	public static void register(UserBean userBean, UserAuthBean userAuthBean) 
			throws InternalException {
		//upload photo, logo and cv
		UserModel user = ModelFactory.buildUserModel(userBean, null, null, null); 
		UserAuthModel userAuth = ModelFactory.buildUserAuthModel(userAuthBean);
		String confirmToken = Util.generateToken();
		
		try {
			UserDao.registerUserDetails(user);
			UserAuthDao.registerUserAuth(user, userAuth, confirmToken);
		} catch(DataAccessException e) {
			throw new InternalException(CHECK_CF_AND_EMAIL);
		}

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
