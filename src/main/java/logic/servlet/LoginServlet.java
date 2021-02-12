package logic.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import logic.exception.SyntaxException;
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

		String errorMessage = null;
		String file = "login.jsp";

		try {
			UserBean user = LoginController.login(email, password);
		
			if(user == null) {
				errorMessage = "Wrong username and/or password";
			} else {
				String reqRes = req.getPathInfo();
				file = reqRes.equals("login.jsp") ? "index.jsp" : reqRes;
				req.getSession().setAttribute("user", user);
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
