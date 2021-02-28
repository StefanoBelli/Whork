package logic.controller;

import logic.bean.UserAuthBean;
import logic.bean.UserBean;
import logic.exception.DataAccessException;
import logic.exception.DataLogicException;
import logic.exception.InternalException;
import logic.exception.SyntaxException;
import logic.factory.BeanFactory;
import logic.dao.UserAuthDao;
import logic.dao.UserDao;
import logic.util.Pair;
import logic.util.Util;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public final class LoginController {
	private LoginController() {}

	/**
	 * This method will interact with UserDao to fetch data from
	 * DB and attempt a login for user (standard email and password).
	 * 
	 * Password are stored using BCrypt salted hash.
	 * 
	 * Once email / password are compared and matched, this controller
	 * method will load user personal data interacting, again, with UserDao.
	 * (see return).
	 * 
	 * @param userAuthBean : object containing email / password
	 * @return UserBean object representing user data or null if no matching
	 */
	public static UserBean basicLogin(UserAuthBean userAuthBean) 
			throws InternalException {
		try {
			Pair<String, ByteArrayInputStream> pair = UserAuthDao.getUserCfAndBcryPwdByEmail(
													userAuthBean.getEmail());
			if(pair == null) {
				return null; // email was not found
			}

			ByteArrayInputStream bcryptedPwdIstream = pair.getSecond();
			byte[] bcryptedPwd = bcryptedPwdIstream.readAllBytes();
			bcryptedPwdIstream.close();

			if(Util.Bcrypt.equals(userAuthBean.getPassword(), bcryptedPwd)) {
				return BeanFactory.buildUserBean(
							UserDao.getUserByCf(pair.getFirst()));
			}

			return null; // passwords are not matching
		} catch(IOException e) {
			Util.exceptionLog(e);
			throw new InternalException("General I/O error");
		} catch(DataAccessException e) {
			Util.exceptionLog(e);
			throw new InternalException("Data access error");
		} catch(DataLogicException e) {
			Util.exceptionLog(e);
			throw new InternalException("Data logic error");
		} catch(SyntaxException e) {
			Util.exceptionLog(e);
			throw new InternalException("Data syntax error");
		}
	}
}
