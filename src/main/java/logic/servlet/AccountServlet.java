package logic.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import logic.bean.UserBean;
import logic.controller.AccountController;
import logic.exception.DataAccessException;
import logic.util.ServletUtil;


public final class AccountServlet extends HttpServlet {	
	private static final long serialVersionUID = 5039870100867000103L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
	    UserBean userBean = ServletUtil.getUserForSession(req);
	    String descriptiveError = null;
	    
		String website = req.getParameter("websiteForm");
		String twitter = req.getParameter("twitterForm");
		String facebook = req.getParameter("facebookForm");
		String instagram = req.getParameter("instagramForm");
		
		System.out.println(website + twitter + facebook + instagram);

		if(website.compareTo(userBean.getWebsite()) == 0 || twitter.compareTo(userBean.getTwitter()) == 0 || 
				facebook.compareTo(userBean.getFacebook()) == 0 || instagram.compareTo(userBean.getInstagram()) == 0) {
			
			userBean.setWebsite(website);
			userBean.setTwitter(twitter);
			userBean.setFacebook(facebook);
			userBean.setInstagram(instagram);
			
			try {
				AccountController.editSocialAccountController(userBean);
			} catch (DataAccessException e) {
				descriptiveError = "An internal error happened, this is totally our fault. Please report, we have logs and will try to fix asap";				
			}
			
		}
		
		if (descriptiveError != null) {
			req.setAttribute("descriptive_error", descriptiveError);
		}
		
		resp.sendRedirect("account.jsp");
		
	}
}
