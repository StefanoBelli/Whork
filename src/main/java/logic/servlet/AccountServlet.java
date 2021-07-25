package logic.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import logic.bean.UserAuthBean;
import logic.bean.UserBean;
import logic.controller.AccountController;
import logic.exception.DataAccessException;
import logic.exception.DataLogicException;
import logic.exception.InternalException;
import logic.exception.InvalidPasswordException;
import logic.factory.BeanFactory;
import logic.util.ServletUtil;


public final class AccountServlet extends HttpServlet {	
	private static final long serialVersionUID = 5039870100867000103L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
	    UserBean userBean = ServletUtil.getUserForSession(req);
	    String descriptiveError = null;
	    
		String website = req.getParameter("websiteForm") == null ? "https://whork.it" : req.getParameter("websiteForm");
		String twitter = req.getParameter("twitterForm") == null ? "whork" : req.getParameter("twitterForm");
		String facebook = req.getParameter("facebookForm") == null ? "whork" : req.getParameter("facebookForm");
		String instagram = req.getParameter("instagramForm") == null ? "whork" : req.getParameter("instagramForm");
		
		if(userBean.getWebsite() == null) userBean.setWebsite("https://whork.it");
		if(userBean.getTwitter() == null) userBean.setTwitter("whork");
		if(userBean.getFacebook() == null) userBean.setFacebook("whork");
		if(userBean.getInstagram() == null) userBean.setInstagram("whork");

		if(website.compareTo(userBean.getWebsite()) == 0 || twitter.compareTo(userBean.getTwitter()) == 0 || 
				facebook.compareTo(userBean.getFacebook()) == 0 || instagram.compareTo(userBean.getInstagram()) == 0) {			
			
			userBean.setWebsite(website);
			userBean.setTwitter(twitter);
			userBean.setFacebook(facebook);
			userBean.setInstagram(instagram);
			
			try {
				AccountController.editAccountController("SocialAccounts", userBean, null, null);
			} catch (DataLogicException | InternalException | DataAccessException e) {
				descriptiveError = "An internal error happened, this is totally our fault. Please report, we have logs and will try to fix asap";;				
			}
			
		}		
		
		String name = req.getParameter("nameForm");
		String surname = req.getParameter("surnameForm");
		String email = req.getParameter("emailForm");
		String phone = req.getParameter("phoneForm");
		String address = req.getParameter("addressForm");
		
		if(name != null && surname != null && email != null && phone != null && address != null) {
		
			if(name.compareTo(userBean.getName()) == 0 || surname.compareTo(userBean.getSurname()) == 0 || email.compareTo(ServletUtil.getUserEmailForSession(req)) == 0 || 
					phone.compareTo(userBean.getPhoneNumber()) == 0 ||  address.compareTo(userBean.getHomeAddress()) == 0) {			
							
				userBean.setName(name);
				userBean.setSurname(surname);
				req.getSession().setAttribute("user-email", email);
				userBean.setPhoneNumber(phone);
				userBean.setHomeAddress(address);			
				
				try {
					UserAuthBean userAuthBeanInfo = BeanFactory.buildUserAuthBean(email, "");
					AccountController.editAccountController("JobSeekerInfoAccount", userBean, userAuthBeanInfo, null);
				} catch (DataLogicException | InternalException | DataAccessException e) {
					descriptiveError = "An internal error happened, this is totally our fault. Please report, we have logs and will try to fix asap";
				}
			}
			
		}
		
		String bio = req.getParameter("editBioTextForm");
		
		if(bio != null) {
			userBean.setBiography(bio);
			
			try {
				UserAuthBean userAuthBeanBio = BeanFactory.buildUserAuthBean(email, "");
				AccountController.editAccountController("JobSeekerBiography", userBean, userAuthBeanBio, null);
			} catch (DataLogicException | InternalException | DataAccessException e) {
				descriptiveError = "An internal error happened, this is totally our fault. Please report, we have logs and will try to fix asap";
			}	
		}
		
		String oldPassword = req.getParameter("oldPasswordForm");
		String newPassword = req.getParameter("newPasswordForm");
		String confirmPassword = req.getParameter("confirmPasswordForm");		

		if(oldPassword != null && newPassword != null && confirmPassword != null) {
			if(newPassword.equals(confirmPassword)) {
				descriptiveError = "New Password and Confirm Password are not equals!";
			}
			else {
				UserAuthBean userAuthBean = BeanFactory.buildUserAuthBean(email, oldPassword);
				try {					
					AccountController.editAccountController("ChangePasswordAccount", userBean, userAuthBean, newPassword);
					descriptiveError = "Password changed successfully!";
				} catch (DataLogicException | InternalException | DataAccessException e) {
					descriptiveError = "An internal error happened, this is totally our fault. Please report, we have logs and will try to fix asap";
				} catch (InvalidPasswordException e) {
					descriptiveError = e.getMessage();
				}
			}
		}		
		
		if (descriptiveError != null) {
			req.setAttribute("descriptive_error", descriptiveError);
		}		
		
		resp.sendRedirect("account.jsp");
		
	}
}
