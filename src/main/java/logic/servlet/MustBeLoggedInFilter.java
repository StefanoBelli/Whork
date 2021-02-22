package logic.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import logic.util.ServletUtil;
import logic.util.Util;

public final class MustBeLoggedInFilter implements Filter {

	@Override
	public void destroy() {
		// method stub for MustBeLoggedInFilter#destroy()
	}
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		//method stub for MustBeLoggedInFilter#init()
	}

	/**
	 * if User is already logged in (active session)
	 *   - get original resource and forward user to it (original request, NO redirect)
	 *   - END: NO FURTHER ACTION REQUIRED
	 * else
	 *   - if for this request we got cookies auth successful, then
	 * 	    - get original resource and forward user to it (original request, NO redirect)
	 *      - END: NO FURTHER ACTION REQUIRED
	 *   - else
	 *      - forward user to login.jsp (original request, NO redirect)
	 *      - END: NO FURTHER ACTION REQURED
	 *  
	 * @param request
	 * @param response
	 * @param chain
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;

		if(ServletUtil.getUserForSession(req) != null || Util.cookieLogin(req)) {
			req.getRequestDispatcher(req.getRequestURI()).forward(request, response);
		} else {
			req.setAttribute("showMustLoginInfo", true);
			req.getRequestDispatcher("login.jsp").forward(request, response);
		}
	}
}
