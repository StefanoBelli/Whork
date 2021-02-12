package logic.controller;

import logic.model.UserModel;
import logic.bean.UserBean;
import logic.exception.DataAccessException;
import logic.exception.DataLogicException;
import logic.exception.InternalException;
import logic.exception.SyntaxException;
import logic.factory.BeanFactory;
import logic.dao.UserDao;
import logic.util.Util;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.favre.lib.crypto.bcrypt.BCrypt;

public final class LoginController {
	private static final Logger LOGGER = LoggerFactory.getLogger("WhorkLoginController");
	private LoginController() {}

	public static UserBean login(String email, String password) 
			throws InternalException, SyntaxException {

		// responsabilità di verifica di sintassi
		if(email.length() > 255) {
			throw new SyntaxException("Email is too long (max. allowed 255 chars, no limit for password)");
		}

		byte[] bcryptedPwd = 
			BCrypt.withDefaults().hash(12, password.toCharArray());

		UserModel userModel = null;
		try(ByteArrayInputStream stream = new ByteArrayInputStream(bcryptedPwd)) {
			String cf = UserDao.getUserCfByEmailAndBcryPasswd(email, stream);
			if (cf == null)
				return null;
			
			userModel = UserDao.getUserByCf(cf);
		} catch(IOException e) {
			Util.exceptionLog(e);
			throw new InternalException("General I/O error");
		} catch(DataAccessException e) {
			Util.exceptionLog(e);
			throw new InternalException("Data access error");
		} catch(DataLogicException e) {
			Util.exceptionLog(e);
			throw new InternalException("Data logic error");
		}

		if(userModel == null) { // 99% UNREACHABLE IF BLOCK
			LOGGER.error("in login, userModel is null but it should not be");
			throw new InternalException("Could not find data about you");
		}

		try {
			return BeanFactory.buildUserBean(userModel);
		} catch(SyntaxException e) {
			Util.exceptionLog(e);
			throw new InternalException("Data syntax error");
		}
	}
}
