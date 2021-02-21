package logic.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import logic.util.Util;

public final class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = -1148126128216681856L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Util.setUserForSession(req, null);
		Cookie ckEmail = new Cookie("email", null);
		Cookie ckPassword = new Cookie("password", null);
		ckEmail.setMaxAge(0);
		ckPassword.setMaxAge(0);
		resp.addCookie(ckEmail);
		resp.addCookie(ckPassword);
		resp.sendRedirect("index.jsp");
	}
	
}
