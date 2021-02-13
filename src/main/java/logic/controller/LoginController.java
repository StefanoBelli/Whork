package logic.controller;

import logic.model.UserModel;
import logic.bean.UserAuthBean;
import logic.exception.DataAccessException;
import logic.exception.DataLogicException;
import logic.exception.InternalException;
import logic.exception.SyntaxException;
import logic.factory.BeanFactory;
import logic.dao.UserDao;
import logic.util.Pair;
import logic.util.Util;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.favre.lib.crypto.bcrypt.BCrypt;

public final class LoginController {
	private static final Logger LOGGER = LoggerFactory.getLogger("WhorkLoginController");
	private LoginController() {}

	public static boolean basicLogin(HttpServletRequest req, HttpServletResponse resp, UserAuthBean userAuthBean, boolean stayLoggedIn) 
			throws InternalException, SyntaxException {

		byte[] bcryptedPwdFromUserInput = 
			BCrypt.withDefaults().hash(12, userAuthBean.getPassword().toCharArray());

		UserModel userModel = null;

		try {
			Pair<String, byte[]> pair = UserDao.getUserCfAndBcrypwdByEmail(userAuthBean.getEmail());
			if(pair == null) {
				req.getSession().setAttribute("user", null);
				return false;
			}

			byte[] bcryptedPwdFromDb = pair.getSecond();

			BCrypt.Result result = BCrypt.verifyer().verify(bcryptedPwdFromDb, bcryptedPwdFromUserInput);
			if(result.verified == true) {
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
			req.getSession().setAttribute("user", BeanFactory.buildUserBean(userModel));
			if(stayLoggedIn) {
				Cookie ckEmail = new Cookie("email", userAuthBean.getEmail());
				Cookie ckPwd = new Cookie("password", userAuthBean.getPassword());
				resp.addCookie(ckEmail);
				resp.addCookie(ckPwd);
			}

			return true;
		} catch(SyntaxException e) {
			Util.exceptionLog(e);
			throw new InternalException("Data syntax error");
		}
	}

	public static boolean cookieLogin(HttpServletRequest req, HttpServletResponse resp) 
			throws IOException, ServletException {
		Cookie[] cks = req.getCookies();

		if(cks == null) {
			return false;
		}
		
		String email = null;
		String password = null;

		for(int i = 0; i < cks.length; ++i) {
			String ckName = cks[i].getName();
			if(ckName.equals("email")) {
				email = cks[i].getValue();
			} else if(ckName.equals("password")) {
				password = cks[i].getValue();
			}
		}

		if(email != null && password != null) {
			boolean loggedIn = false;
			try {
				loggedIn = LoginController.basicLogin(
					req, resp, BeanFactory.buildUserAuthBean(email, password), false);
			} catch(Exception e) {
				Util.exceptionLog(e);
				loggedIn = false;
			}

			return loggedIn;
		}

		return false;
	}

}
