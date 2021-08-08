package logic.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import logic.bean.UserBean;
import logic.controller.LoginController;
import logic.factory.BeanFactory;

/**
 * @author Stefano Belli
 */
public final class ServletUtil {
	private ServletUtil() {}
	
	public static boolean checkboxToBoolean(String value) {
		return value != null && value.equals("on");
	}

	public static void setUserForSession(HttpServletRequest req, UserBean userBean, String email) {
		req.getSession().setAttribute("user", userBean);
		req.getSession().setAttribute("user-email", email);
	}

	public static UserBean getUserForSession(HttpServletRequest req) {
		return (UserBean) req.getSession().getAttribute("user");
	}

	public static String getUserEmailForSession(HttpServletRequest req) {
		return (String) req.getSession().getAttribute("user-email");
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

			ServletUtil.setUserForSession(req, userBean, email);
			return true;
		}

		return false;
	}

	public static String saveUserFile(HttpServletRequest req, String fieldName, String userCf) 
			throws IOException, ServletException {
		Part filePart = req.getPart(fieldName);
		if(filePart == null) {
			return null;
		}
		
		String userChosenFileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
		if(userChosenFileName.isBlank()) {
			return null;
		}

		String fileName = new StringBuilder().append(userCf).append("_-_")
			.append(Long.toString(new Date().getTime()))
			.append("_-_").append(userChosenFileName).toString();

		String completeFileName = new StringBuilder()
			.append(Util.InstanceConfig.getString(Util.InstanceConfig.KEY_USR_DATA))
			.append("/").append(fileName).toString();
			
		File newSubmittedFile = new File(completeFileName);
		if (newSubmittedFile.createNewFile()) {
			try (InputStream istream = new BufferedInputStream(filePart.getInputStream())) {
				try (OutputStream ostream = new BufferedOutputStream(new FileOutputStream(newSubmittedFile))) {
					ostream.write(istream.readAllBytes());
				}
			}
		} else {
			throw new IOException();
		}

		return fileName;
	}
	
	public static void deleteUserFile(String fileName) throws IOException {
		final String path = new StringBuilder()
				.append(Util.InstanceConfig.getString(Util.InstanceConfig.KEY_USR_DATA))
				.append("/").append(fileName).toString();
		Files.deleteIfExists(Paths.get(path));
	}
}
