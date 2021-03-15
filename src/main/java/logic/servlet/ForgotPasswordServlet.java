package logic.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import logic.controller.LoginController;

public final class ForgotPasswordServlet extends HttpServlet {
	private static final long serialVersionUID = -1486000896916636582L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		String email = req.getParameter("email");
		if(email != null) {
			LoginController.recoverPassword(email);
			req.setAttribute("reqEmail", email);
			req.getRequestDispatcher("forgotpwd.jsp").forward(req, resp);
		} else {
			resp.sendRedirect("forgotpwd.jsp");
		}
	}
}
