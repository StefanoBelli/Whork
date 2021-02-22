package logic.util;

import javax.servlet.http.HttpServletRequest;

import logic.bean.UserBean;

public final class ServletUtil {
	private ServletUtil() {}
	
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
