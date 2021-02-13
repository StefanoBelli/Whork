package logic.controller;

import logic.model.UserModel;
import logic.bean.UserAuthBean;
import logic.bean.UserBean;
import logic.exception.DataAccessException;
import logic.exception.DataLogicException;
import logic.exception.InternalException;
import logic.exception.SyntaxException;
import logic.factory.BeanFactory;
import logic.dao.UserDao;
import logic.util.Pair;
import logic.util.Util;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.favre.lib.crypto.bcrypt.BCrypt;

public final class LoginController {
	private static final Logger LOGGER = LoggerFactory.getLogger("WhorkLoginController");
	private LoginController() {}

	public static UserBean basicLogin(UserAuthBean userAuthBean) 
			throws InternalException {

		byte[] bcryptedPwdFromUserInput = 
			BCrypt.withDefaults().hash(12, userAuthBean.getPassword().toCharArray());

		UserModel userModel = null;

		try {
			Pair<String, byte[]> pair = UserDao.getUserCfAndBcrypwdByEmail(userAuthBean.getEmail());
			if(pair == null) {
				return null;
			}

			byte[] bcryptedPwdFromDb = pair.getSecond();

			BCrypt.Result result = BCrypt.verifyer().verify(bcryptedPwdFromDb, bcryptedPwdFromUserInput);
			if(result.verified) {
				userModel = UserDao.getUserByCf(pair.getFirst());
			}
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
