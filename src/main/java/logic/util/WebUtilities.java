package logic.util;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WebUtilities {
	
	private static String currentPage;
	
	/* SERVLETS */
	public static final String REDIRECT_REGISTER_CANDIDATE_PAGE_SERVLET = "/RegisterChoiceServlet";	

	/* JSP PAGES */
	public static final String REDIRECT_REGISTER_CANDIDATE_PAGE = "/Register-candidate.jsp";
	
	
	private WebUtilities() {
		/* non istanziabile */
	}
	
	
	public static void redirectToErrorPage(HttpServletRequest request, HttpServletResponse response,String message) throws ServletException, IOException {
		request.setAttribute("error-message", message);
		request.getRequestDispatcher(WebUtilities.ERROR_PAGE_URL).forward(request, response);
	}


	public static String getCurrentPage() {
		return currentPage;
	}

	public static void setCurrentPage(String currentPage) {
		WebUtilities.currentPage = currentPage;
	}
	
	public static String getUsernameFromSession(HttpServletRequest request) {
		return (String) request.getSession().getAttribute("currUser");
	}

}
