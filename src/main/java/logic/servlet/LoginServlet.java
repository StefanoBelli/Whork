package logic.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import logic.exception.SyntaxException;
import logic.factory.BeanFactory;
import logic.util.Util;
import logic.bean.UserAuthBean;
import logic.bean.UserBean;
import logic.controller.LoginController;
import logic.exception.InternalException;

public final class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = -9153792726136664317L;
	private static final int COOKIE_MAX_AGE = 43200; /* 30 days */

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws IOException, ServletException {
		String email = req.getParameter("email");
		String password = req.getParameter("passwd");
		boolean stayLoggedIn = Util.checkboxToBoolean(
			req.getParameter("stayLoggedIn"));

		String errorMessage = null;
		UserBean userBean = null;

		try {
			UserAuthBean userAuthBean = BeanFactory.buildUserAuthBean(email, password);
			userBean = LoginController.basicLogin(userAuthBean); /* maybe userBean != null or userBean = null */
		} catch(InternalException e) {
			errorMessage = "Internal processing error: " + e.getMessage(); /* userBean = null (thrown from buildUserAuthBean) */
		} catch(SyntaxException e) {
			errorMessage = e.getMessage(); /* userBean = null (thrown from basicLogin) */
		}

		if(userBean == null) { /* wrong creds or internal error */
			errorMessage = "Wrong username and/or password";
			req.setAttribute("showPasswordRecoveryButton", true);
			req.setAttribute("errorMessage", errorMessage); /* if errorMessage = null then internal error happened */
			req.getRequestDispatcher("login.jsp").forward(req, resp);
		} else { /* successful login */
			Util.setUserForSession(req, userBean);

			if(stayLoggedIn) {
				Cookie ckEmail = new Cookie("email", email);
				Cookie ckPassword = new Cookie("password", password);
				ckEmail.setMaxAge(COOKIE_MAX_AGE);
				ckPassword.setMaxAge(COOKIE_MAX_AGE);

				resp.addCookie(ckEmail);
				resp.addCookie(ckPassword);
			}

			resp.sendRedirect("index.jsp");
		}
	}
}
