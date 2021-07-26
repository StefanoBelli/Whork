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
	    
		String website = req.getParameter("websiteForm");
		String twitter = req.getParameter("twitterForm");
		String facebook = req.getParameter("facebookForm");
		String instagram = req.getParameter("instagramForm");
			
		if(website != null && twitter != null && facebook != null && instagram != null) {
			/*if(userBean.getWebsite() == null) userBean.setWebsite("https://whork.it");
			if(userBean.getTwitter() == null) userBean.setTwitter("whork");
			if(userBean.getFacebook() == null) userBean.setFacebook("whork");
			if(userBean.getInstagram() == null) userBean.setInstagram("whork");*/	
			
			System.out.println("social");
			
			if(!website.equals(userBean.getWebsite()) || !twitter.equals(userBean.getTwitter()) || 
					!facebook.equals(userBean.getFacebook()) || !instagram.equals(userBean.getInstagram())) {			
				
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
		}
		
		String name = req.getParameter("nameForm");
		String surname = req.getParameter("surnameForm");
		String email = req.getParameter("emailForm");
		String phone = req.getParameter("phoneForm");
		String address = req.getParameter("addressForm");
		
		if(name != null && surname != null && email != null && phone != null && address != null) {
		
			System.out.println("user");			
			
			if(!name.equals(userBean.getName()) || !surname.equals(userBean.getSurname()) || !email.equals(ServletUtil.getUserEmailForSession(req)) || 
					!phone.equals(userBean.getPhoneNumber()) ||  !address.equals(userBean.getHomeAddress())) {			
							
				userBean.setName(name);
				userBean.setSurname(surname);
				req.getSession().setAttribute("user-email", email);
				userBean.setPhoneNumber(phone);
				userBean.setHomeAddress(address);			
				
				try {
					AccountController.editAccountController("JobSeekerInfoAccount", userBean, BeanFactory.buildUserAuthBean(email, ""), null);
				} catch (DataLogicException | InternalException | DataAccessException e) {
					descriptiveError = "An internal error happened, this is totally our fault. Please report, we have logs and will try to fix asap";
				}
			}
			
		}
		
		String bio = req.getParameter("editBioTextForm");
		
		if(bio != null) {
			System.out.println("bio");
			if(!bio.equals(userBean.getBiography())) {
				if(bio.length() == 0) bio = null;
				userBean.setBiography(bio);
				
				try {
					UserAuthBean userAuthBeanBio = BeanFactory.buildUserAuthBean(email, "");
					AccountController.editAccountController("JobSeekerBiography", userBean, userAuthBeanBio, null);
				} catch (DataLogicException | InternalException | DataAccessException e) {
					descriptiveError = "An internal error happened, this is totally our fault. Please report, we have logs and will try to fix asap";
				}	
			}
		}
		
		String oldPassword = req.getParameter("oldPasswordForm");
		String newPassword = req.getParameter("newPasswordForm");
		//String confirmPassword = req.getParameter("confirmPasswordForm");		

		if(oldPassword != null && newPassword != null) {
			/*if(newPassword.equals(confirmPassword)) {
				descriptiveError = "New Password and Confirm Password are not equals!";
			}*/
			
			System.out.println("password");
			
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
		
		if (descriptiveError != null) {
			req.setAttribute("descriptive_error", descriptiveError);
		}		
		
		resp.sendRedirect("account.jsp");
		
	}
}
