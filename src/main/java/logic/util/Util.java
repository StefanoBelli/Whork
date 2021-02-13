package logic.util;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import logic.bean.UserBean;
import logic.controller.LoginController;
import logic.factory.BeanFactory;

public final class Util {
	private Util() {}

	public static void exceptionLog(Exception e) {
		Logger logger = LoggerFactory.getLogger("WhorkExceptionLogger");

		logger.error("***************************");
		logger.error("* EXCEPTION LOGGING START *");
		logger.error("***************************");
		logger.error("");

		StackTraceElement[] trace = Thread.currentThread().getStackTrace();

		StringBuilder builder = new StringBuilder();
		builder.append(trace[1].getMethodName())
			.append(" got called by: ")
			.append(trace[2].getClassName())
			.append("#")
			.append(trace[2].getMethodName())
			.append("()");

		logger.error(builder.toString());
		logger.error("");
		e.printStackTrace();
		logger.error("");
		logger.error("*************************");
		logger.error("* EXCEPTION LOGGING END *");
		logger.error("*************************");
	}
	
	public static boolean cookieLogin(HttpServletRequest req) 
			throws IOException {
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
			UserBean userBean;
			try {
				userBean = LoginController.basicLogin(
					BeanFactory.buildUserAuthBean(email, password));
			} catch(Exception e) {
				Util.exceptionLog(e);
				userBean = null;
			}

			if(userBean == null) {
				return false;
			}

			Util.setUserForSession(req, userBean);
			return true;
		}

		return false;
	}

	public static boolean checkboxToBoolean(String value) {
		return value != null && value.equals("on");
	}

	public static void setUserForSession(HttpServletRequest req, UserBean userBean) {
		req.getSession().setAttribute("user", userBean);
	}

	public static UserBean getUserForSession(HttpServletRequest req) {
		return (UserBean) req.getSession().getAttribute("user");
	}
}
