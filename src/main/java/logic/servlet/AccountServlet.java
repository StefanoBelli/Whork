package logic.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import logic.util.Util;

@MultipartConfig
public final class AccountServlet extends HttpServlet {	
	private static final long serialVersionUID = 5039870100867000103L;
	private static final String ERROR_MSG = 
			"An internal error happened, this is totally our fault. Please report, we have logs and will try to fix asap";
	private static final String SUBMIT_INFO_BUTTON = 
			"editInfoButton";

	private static final Map<String, UserAccountPropertyAlterer> UAPS = new HashMap<>();

	static {
		UAPS.put("social account", new UapSocial());
		UAPS.put("info", new UapInfo());
		UAPS.put("bio", new UapBio());
		UAPS.put("password", new UapPassword());
		UAPS.put("candidature", new UapCandidature());
		UAPS.put("photo", new UapPhoto());
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
	    final UserBean userBean = ServletUtil.getUserForSession(req);

	    final StringBuilder descriptiveErrorBuilder = new StringBuilder();
	    final StringBuilder successBuilder = new StringBuilder();

		for(final Map.Entry<String,UserAccountPropertyAlterer> uap : UAPS.entrySet()) {
			try {
				successBuilder.append(uap.getValue().doAlterProperty(req, userBean)).append("<br>");
			} catch(DataAccessException | DataLogicException | IOException | ServletException | InternalException e) {
				Util.exceptionLog(e);
				descriptiveErrorBuilder.append(uap.getKey()).append(" says: ").append(ERROR_MSG).append("<br>");
			} catch(InvalidPasswordException e) {
				descriptiveErrorBuilder.append(e.getMessage()).append("<br>");
			}
		}

		final String descriptiveError = descriptiveErrorBuilder.toString();
		final String success = successBuilder.toString();
		
		if (!descriptiveError.isEmpty()) req.getSession().setAttribute("descriptive_error", descriptiveError);
		if (!success.isEmpty()) req.getSession().setAttribute("change_alert", success);
		
		req.getSession().setAttribute("user", userBean);
		
		resp.sendRedirect("account.jsp");
	}

	private static final class UapSocial implements UserAccountPropertyAlterer {

		@Override
		public String doAlterProperty(HttpServletRequest req, UserBean userBean) 
				throws DataAccessException, DataLogicException, InternalException, InvalidPasswordException {
			if (req.getParameter("editSocialAccountForm") != null) {
				String website = req.getParameter("websiteForm");
				String twitter = req.getParameter("twitterForm");
				String facebook = req.getParameter("facebookForm");
				String instagram = req.getParameter("instagramForm");

				if (website != null && twitter != null && facebook != null && instagram != null
						&& (!website.equals(userBean.getWebsite()) || !twitter.equals(userBean.getTwitter())
								|| !facebook.equals(userBean.getFacebook())
								|| !instagram.equals(userBean.getInstagram()))) {

					userBean.setWebsite(website);
					userBean.setTwitter(twitter);
					userBean.setFacebook(facebook);
					userBean.setInstagram(instagram);
					
					AccountController.editAccountController("SocialAccounts", userBean, null, null);
					return "Social updated successfully!";
				}
			}

			return null;
		}
	}

	private static final class UapInfo implements UserAccountPropertyAlterer {

		@Override
		public String doAlterProperty(HttpServletRequest req, UserBean userBean) 
				throws DataAccessException, DataLogicException, InternalException, InvalidPasswordException {
			if (req.getParameter(SUBMIT_INFO_BUTTON) != null
					&& req.getParameter(SUBMIT_INFO_BUTTON).equals("editInfo")) {
				String name = req.getParameter("nameForm");
				String surname = req.getParameter("surnameForm");
				String email = req.getParameter("emailForm");
				String phone = req.getParameter("phoneForm");
				String address = req.getParameter("addressForm");

				if (!name.equals(userBean.getName()) || !surname.equals(userBean.getSurname())
						|| !email.equals(ServletUtil.getUserEmailForSession(req))
						|| !phone.equals(userBean.getPhoneNumber()) || !address.equals(userBean.getHomeAddress())) {

					userBean.setName(name);
					userBean.setSurname(surname);
					req.getSession().setAttribute("user-email", email);
					userBean.setPhoneNumber(phone);
					userBean.setHomeAddress(address);

					AccountController.editAccountController("JobSeekerInfoAccount", userBean,
						BeanFactory.buildUserAuthBean(email, ""), null);
					return "Information updated successfully!";
				}
			}
			
			return null;
		}
	}

	private static final class UapBio implements UserAccountPropertyAlterer {

		@Override
		public String doAlterProperty(HttpServletRequest req, UserBean userBean) 
				throws DataAccessException, DataLogicException, InternalException, InvalidPasswordException {
			if (req.getParameter("editBioButton") != null) {
				String bio = req.getParameter("editBioTextForm");

				if (!bio.equals(userBean.getBiography())) {
					if (bio.isBlank())
						bio = null;
					userBean.setBiography(bio);

					UserAuthBean userAuthBeanBio = BeanFactory
							.buildUserAuthBean(ServletUtil.getUserEmailForSession(req), "");
					
					AccountController.editAccountController("JobSeekerBiography", userBean, userAuthBeanBio, null);
					return "Biography changed successfully!";
				}
			}

			return null;
		}
	}

	private static final class UapPassword implements UserAccountPropertyAlterer {

		@Override
		public String doAlterProperty(HttpServletRequest req, UserBean userBean) 
				throws DataAccessException, DataLogicException, InternalException, InvalidPasswordException {
			if (req.getParameter(SUBMIT_INFO_BUTTON) != null
					&& req.getParameter(SUBMIT_INFO_BUTTON).equals("editPassword")) {
				String oldPassword = req.getParameter("oldPasswordForm");
				String newPassword = req.getParameter("newPasswordForm");

				UserAuthBean userAuthBean = BeanFactory.buildUserAuthBean(ServletUtil.getUserEmailForSession(req),
						oldPassword);

				AccountController.editAccountController("ChangePasswordAccount", userBean, userAuthBean,
					newPassword);

				return "Password changed successfully!";
			}

			return null;
		}
	}

	private static final class UapCandidature implements UserAccountPropertyAlterer {

		@Override
		public String doAlterProperty(HttpServletRequest req, UserBean userBean) 
				throws InternalException {
			if (req.getParameter("deleteCandidatureButton") != null) {
				int i = Integer.parseInt(req.getParameter("deleteCandidatureButton"));
				List<CandidatureBean> listCandidatureBean = AccountController.getSeekerCandidature(userBean);
				CandidatureController.deleteCandidature(userBean, listCandidatureBean.get(i));
				return "Candidature deleted successfully!";
			}

			return null;
		}
	}

	private static final class UapPhoto implements UserAccountPropertyAlterer {

		@Override
		public String doAlterProperty(HttpServletRequest req, UserBean userBean) 
				throws DataAccessException, IOException, ServletException {
			if(req.getParameter("changePicture") != null) {
				AccountController.changePictureAccountJobSeeker(
					ServletUtil.saveUserFile(req, "changePhotoInput", userBean.getCf()), userBean);
				return "Picture changed successfully!";
			}

			return null;
		}
	}

	private interface UserAccountPropertyAlterer {
		String doAlterProperty(HttpServletRequest req, UserBean userBean) 
			throws DataAccessException, DataLogicException, InternalException,
				InvalidPasswordException, IOException, ServletException;
	}
}
