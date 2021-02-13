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

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws IOException, ServletException {
		
		String errorMessage = null;
		String file = "login.jsp";
		String email = req.getParameter("email");
		String password = req.getParameter("passwd");
		boolean stayLoggedIn = Util.checkboxToBoolean(
			req.getParameter("stayLoggedIn"));

		try {
			UserAuthBean userAuthBean = BeanFactory.buildUserAuthBean(email, password);
			UserBean userBean = LoginController.basicLogin(userAuthBean);

			if(userBean == null) {
				errorMessage = "Wrong username and/or password";
				req.setAttribute("showPasswordRecoveryButton", true);
			} else {
				String reqRes = req.getRequestURI();
				file = reqRes.equals("login.jsp") ? "index.jsp" : reqRes;

				Util.setUserForSession(req, userBean);

				if(stayLoggedIn) {
					resp.addCookie(new Cookie("email", email));
					resp.addCookie(new Cookie("password", password));
				}
			}
		} catch(InternalException e) {
			errorMessage = "Internal processing error: " + e.getMessage();
		} catch(SyntaxException e) {
			errorMessage = e.getMessage();
		}

		req.setAttribute("errorMessage", errorMessage);
		req.getRequestDispatcher(file).forward(req, resp);
	}
}
