package logic.servlet.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import logic.util.ServletUtil;

public final class AlreadyLoggedInFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		//method stub for AlreadyLoggedInFilter#init()
	}

	@Override
	public void destroy() {
		//method stub for AlreadyLoggedInFilter#destroy()
	}

	/**
	 * This method ensures the requested URI is reached if and only if necessary (user is not logged in).
	 * If user is registered for current session, then redirect (HTTP REDIRECT) user to "index.jsp"
	 * Else, If cookie login is not successful, allow original request to "login.jsp"
	 * Else, If cookie login is successful, HTTP REDIRECT user to "index.jsp"
	 * 
	 * @param request
	 * @param response
	 * @param chain
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;

		if (ServletUtil.getUserForSession(req) != null || ServletUtil.cookieLogin(req)) {
			((HttpServletResponse)response).sendRedirect("index.jsp");
		} else {
			req.getRequestDispatcher(req.getRequestURI()).forward(request, response);
		}
	}
}
