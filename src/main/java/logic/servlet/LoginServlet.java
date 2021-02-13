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

public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = -9153792726136664317L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
		throws ServletException, IOException {
	
		String email = req.getParameter("email");
		String password = req.getParameter("passwd");
		boolean stayLoggedIn = Util.checkboxToBoolean(req.getParameter("stayLoggedIn"));

		String errorMessage = null;
		String file = "login.jsp";

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
					Cookie ckEmail = new Cookie("email", userAuthBean.getEmail());
					Cookie ckPwd = new Cookie("password", userAuthBean.getPassword());
					resp.addCookie(ckEmail);
					resp.addCookie(ckPwd);
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