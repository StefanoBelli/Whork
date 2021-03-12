package logic.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import logic.controller.LoginController;

public final class ChangePasswordServlet extends HttpServlet {
	private static final long serialVersionUID = 1673909481982302008L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		
		String password = req.getParameter("pwd");
		String token = req.getParameter("token");

		if(password == null || token == null) {
			resp.sendRedirect("cpoutcome.jsp?ok=false");
		} else {
			if(LoginController.changePassword(token, password)) {
				resp.sendRedirect("cpoutcome.jsp?ok=true");
			} else {
				resp.sendRedirect("cpoutcome.jsp?ok=false");
			}
		}
	}
}
