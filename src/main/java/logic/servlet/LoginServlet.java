package logic.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import logic.exception.SyntaxException;
import logic.factory.BeanFactory;
import logic.util.ServletUtil;
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
		boolean stayLoggedIn = ServletUtil.checkboxToBoolean(
			req.getParameter("stayLoggedIn"));

		String errorMessage = null;
		UserBean userBean = null;

		try {
			UserAuthBean userAuthBean = BeanFactory.buildUserAuthBean(email, password);
			userBean = LoginController.basicLogin(userAuthBean);
		} catch(InternalException e) { /* should never happen */
			errorMessage = "Internal processing error: " + e.getMessage();
		} catch(SyntaxException e) { /* should never happen */
			errorMessage = e.getMessage(); 
		}

		if(userBean == null) { /* wrong creds or internal error */
			if(errorMessage == null) { /* userBean = null && errorMessage = null */
				errorMessage = "Wrong username and/or password";
				req.setAttribute("showPasswordRecoveryButton", true);
			}

			req.setAttribute("errorMessage", errorMessage);
			req.getRequestDispatcher("login.jsp").forward(req, resp);
		} else { /* successful login */
			ServletUtil.setUserForSession(req, userBean, email);

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
