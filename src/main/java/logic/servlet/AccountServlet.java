package logic.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import logic.bean.CandidatureBean;
import logic.bean.UserAuthBean;
import logic.bean.UserBean;
import logic.controller.AccountController;
import logic.controller.CandidatureController;
import logic.exception.DataAccessException;
import logic.exception.DataLogicException;
import logic.exception.InternalException;
import logic.exception.InvalidPasswordException;
import logic.factory.BeanFactory;
import logic.util.ServletUtil;

@MultipartConfig
public final class AccountServlet extends HttpServlet {	
	private static final long serialVersionUID = 5039870100867000103L;
	private static final String ERROR_MSG = 
			"An internal error happened, this is totally our fault. Please report, we have logs and will try to fix asap";
	private static final String SUBMIT_INFO_BUTTON = 
			"editInfoButton";	

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
	    UserBean userBean = ServletUtil.getUserForSession(req);
	    String descriptiveError = null;
	    String success = null;
	    		
		if(req.getParameter("editSocialAccountForm") != null) {			
			
			String website = req.getParameter("websiteForm");
			String twitter = req.getParameter("twitterForm");
			String facebook = req.getParameter("facebookForm");
			String instagram = req.getParameter("instagramForm");
			
			if(website != null && twitter != null && facebook != null && instagram != null) {
			
				if(!website.equals(userBean.getWebsite()) || !twitter.equals(userBean.getTwitter()) || 
						!facebook.equals(userBean.getFacebook()) || !instagram.equals(userBean.getInstagram())) {								
					
					userBean.setWebsite(website);
					userBean.setTwitter(twitter);
					userBean.setFacebook(facebook);
					userBean.setInstagram(instagram);
					
					try {
						AccountController.editAccountController("SocialAccounts", userBean, null, null);
						success = "Social updated successfully!";
					} catch (DataLogicException | InternalException | DataAccessException e) {
						descriptiveError = ERROR_MSG;
					}
					
				}
			}
		}
				
		if(req.getParameter(SUBMIT_INFO_BUTTON) != null && req.getParameter(SUBMIT_INFO_BUTTON).equals("editInfo")) {
			
			String name = req.getParameter("nameForm");
			String surname = req.getParameter("surnameForm");
			String email = req.getParameter("emailForm");
			String phone = req.getParameter("phoneForm");
			String address = req.getParameter("addressForm");
						
			if(!name.equals(userBean.getName()) || !surname.equals(userBean.getSurname()) || !email.equals(ServletUtil.getUserEmailForSession(req)) || 
					!phone.equals(userBean.getPhoneNumber()) ||  !address.equals(userBean.getHomeAddress())) {				
				
				userBean.setName(name);
				userBean.setSurname(surname);
				req.getSession().setAttribute("user-email", email);
				userBean.setPhoneNumber(phone);
				userBean.setHomeAddress(address);			
				
				try {
					AccountController.editAccountController("JobSeekerInfoAccount", userBean, BeanFactory.buildUserAuthBean(email, ""), null);
					success = "Information updated successfully!";
				} catch (DataLogicException | InternalException | DataAccessException e) {
					descriptiveError = ERROR_MSG;
				}
			}
			
		
		}
		
		if(req.getParameter("editBioButton") != null) {
			
			String bio = req.getParameter("editBioTextForm");		
			
			if(!bio.equals(userBean.getBiography())) {
				if(bio.length() == 0) bio = null;
				userBean.setBiography(bio);
				
				try {
					UserAuthBean userAuthBeanBio = BeanFactory.buildUserAuthBean(ServletUtil.getUserEmailForSession(req), "");
					AccountController.editAccountController("JobSeekerBiography", userBean, userAuthBeanBio, null);
					success = "Biography changed successfully!";
				} catch (DataLogicException | InternalException | DataAccessException e) {
					descriptiveError = ERROR_MSG;
				}	
			}
		
		}
		
		if(req.getParameter(SUBMIT_INFO_BUTTON) != null && req.getParameter(SUBMIT_INFO_BUTTON).equals("editPassword")) {
			
			String oldPassword = req.getParameter("oldPasswordForm");
			String newPassword = req.getParameter("newPasswordForm");
			
			UserAuthBean userAuthBean = BeanFactory.buildUserAuthBean((String) req.getSession().getAttribute("user-email"), oldPassword);
			try {					
				AccountController.editAccountController("ChangePasswordAccount", userBean, userAuthBean, newPassword);
				success = "Password changed successfully!";
			} catch (DataLogicException | InternalException | DataAccessException e) {
				descriptiveError = ERROR_MSG;
			} catch (InvalidPasswordException e) {
				descriptiveError = e.getMessage();
			}
		}
		
		if(req.getParameter("deleteCandidatureButton") != null) {

			try {
				int i = Integer.parseInt(req.getParameter("deleteCandidatureButton"));
				List<CandidatureBean> listCandidatureBean = AccountController.getSeekerCandidature(userBean);
				CandidatureController.deleteCandidature(userBean, listCandidatureBean.get(i));
			} catch (DataAccessException | DataLogicException e) {
				descriptiveError = ERROR_MSG;
			}
		}
		
		if(req.getParameter("changePicture") != null) {
			try {
				userBean = AccountController.changePictureAccountJobSeeker(ServletUtil.saveUserFile(req, "changePhotoInput", userBean.getCf()), userBean);
			} catch (DataAccessException | IOException | ServletException e) {
				descriptiveError = ERROR_MSG;
			}
		}
		
		if (descriptiveError != null) req.getSession().setAttribute("descriptive_error", descriptiveError);
		if (success != null) req.getSession().setAttribute("change_alert", success);
		
		req.getSession().setAttribute("user", userBean);
		
		resp.sendRedirect("account.jsp");
		
	}
}
