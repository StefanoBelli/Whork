package logic.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import logic.bean.UserBean;
import logic.controller.LoginController;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

	public static boolean checkboxToBoolean(String value) {
		return value != null && value.equals("on");
	}

	public static boolean cookieLogin(HttpServletRequest req, HttpServletResponse resp, String res) 
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
			UserBean userBean = null;
			try {
				userBean = LoginController.login(email, password);
			} catch(Exception e) {
				Util.exceptionLog(e);
			}

			if(userBean != null) {
				req.getSession().setAttribute("user", userBean);
				req.getRequestDispatcher(res).forward(req, resp);
				return true;
			}
		}

		return false;
	}
}
