package logic.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.favre.lib.crypto.bcrypt.BCrypt;
import logic.WhorkDesktopLauncher;
import logic.bean.UserBean;
import logic.controller.LoginController;
import logic.factory.BeanFactory;

public final class Util {
	private Util() {
	}

	public static void exceptionLog(Exception e) {
		Logger logger = LoggerFactory.getLogger("WhorkExceptionLogger");

		logger.error("***************************");
		logger.error("* EXCEPTION LOGGING START *");
		logger.error("***************************");
		logger.error("");

		StackTraceElement[] trace = Thread.currentThread().getStackTrace();
		logger.error("{} got called by: {}#{}()", trace[1].getMethodName(), trace[2].getClassName(),
				trace[2].getMethodName());

		logger.error("++++++++++++STACK TRACE++++++++++++");
		e.printStackTrace();
		logger.error("++++++++++END STACK TRACE++++++++++");

		logger.error("");
		logger.error("*************************");
		logger.error("* EXCEPTION LOGGING END *");
		logger.error("*************************");
	}

	/**
	 * Attempt login using cookies sent back from client. If cookie login is
	 * successful, then this method automatically sets the user for current session.
	 * This method will be called from Filers and/or (if necessary) from Servlets
	 * 
	 * @param req
	 * @return true if login via cookie was successful, false otherwise
	 */
	public static boolean cookieLogin(HttpServletRequest req) {
		Cookie[] cks = req.getCookies();

		if (cks == null) {
			return false;
		}

		String email = null;
		String password = null;

		for (int i = 0; i < cks.length; ++i) {
			String ckName = cks[i].getName();
			if (ckName.equals("email")) {
				email = cks[i].getValue();
			} else if (ckName.equals("password")) {
				password = cks[i].getValue();
			}
		}

		if (email != null && password != null) {
			UserBean userBean = null;

			try {
				userBean = LoginController.basicLogin(BeanFactory.buildUserAuthBean(email, password));
			} catch (Exception e) {
				Util.exceptionLog(e);
			}

			if (userBean == null) {
				return false;
			}

			ServletUtil.setUserForSession(req, userBean);
			return true;
		}

		return false;
	}

	public static final class Files {
		private Files() {}

		public static String readAll(File f) 
				throws IOException {
			StringBuilder builder = new StringBuilder();

			try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line).append('\n');
				}
			}

			return builder.toString();
		}

		public static void overWrite(String content, File f) 
				throws IOException {

			try (BufferedWriter writer = new BufferedWriter(new FileWriter(f))) {
				writer.write(content);
			}
		}

		public static void overWriteJsonAuth(String email, String password) 
				throws IOException {
			File f = new File(WhorkDesktopLauncher.AUTH_FILE_PATH);

			if (!f.exists() && !f.createNewFile()) {
				throw new IOException();
			}

			JSONObject obj = new JSONObject();
			obj.put("email", email);
			obj.put("password", password);

			overWrite(obj.toString(), f);
		}
	}

	public static final class Bcrypt {
		private Bcrypt() {
		}

		public static byte[] hash(String clearText) {
			return BCrypt.withDefaults().hash(12, clearText.toCharArray());
		}

		public static boolean equals(String clearText, byte[] hash2) {
			return BCrypt.verifyer().verify(clearText.getBytes(), hash2).verified;
		}
	}
}
