package logic.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import logic.bean.CompanyBean;
import logic.bean.UserAuthBean;
import logic.bean.UserBean;
import logic.controller.RegisterController;
import logic.exception.AlreadyExistantCompanyException;
import logic.exception.AlreadyExistantUserException;
import logic.exception.InvalidVatCodeException;
import logic.factory.BeanFactory;
import logic.util.ServletUtil;
import logic.util.Util;
import logic.util.tuple.Pair;

@MultipartConfig
public final class CompleteRegistrationServlet extends HttpServlet {
	private static final long serialVersionUID = 6025349555035440533L;
	private static final String WHORK = "whork";

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
		String descriptiveError = null;
		Pair<UserBean, UserAuthBean> beans = null;

		try {
			beans = createBeansFromRequest(req);
			RegisterController.register(beans);
		} catch(AlreadyExistantCompanyException e) {
			descriptiveError = 
				"VAT number, fiscal code or name (or some/all three of them) is already registered for this company!";
		} catch(AlreadyExistantUserException e) {
			descriptiveError = 
				"An email address and/or fiscal code is already registered for this user!";
		} catch(InvalidVatCodeException e) {
			descriptiveError = 
				"We tried to check for your VAT code, but it is invalid, therefore we are rejecting your signup request";
		} catch(Exception e) {
			Util.exceptionLog(e);
			descriptiveError = 
				"An internal error happened, this is totally our fault. Please report, we have logs and will try to fix asap";
		}

		if(descriptiveError == null) {
			req.setAttribute("email", beans.getSecond().getEmail());
			req.setAttribute("name", beans.getFirst().getName());
			CompanyBean company = beans.getFirst().getCompany();
			if(company != null) {
				req.setAttribute("company", company.getSocialReason());
			}

			dispatchSuccess(req, resp); //end
		} else {
			req.setAttribute("descriptive_error", descriptiveError);
			dispatchErrored(beans, req, resp); //end
		}
	}

	private void dispatchSuccess(HttpServletRequest req, HttpServletResponse resp) {
		try {
			req.getRequestDispatcher("success.jsp").forward(req, resp);
		} catch(ServletException | IOException e) {
			Util.exceptionLog(e);
		}
	}

	private void dispatchErrored(Pair<UserBean,UserAuthBean> beans, HttpServletRequest req, HttpServletResponse resp) {
		try {
			if (beans == null) {
				req.getRequestDispatcher("register.jsp").forward(req, resp);
			} else {
				req.getRequestDispatcher(
						beans.getFirst().getCompany() == null ? "reg_jobseeker.jsp" : "reg_company.jsp")
						.forward(req, resp);
			}
		} catch (ServletException | IOException e) {
			Util.exceptionLog(e);
		}
	}

	/**
	 * @param req
	 * @return
	 * @throws SyntaxException - not supposed to be thrown at all
	 * @throws IOException - not supposed to be thrown (assuming enough disk space) just log, show InternalException
	 * @throws ServletException - ??? just log, show InternalException
	 */
	private Pair<UserBean, UserAuthBean> createBeansFromRequest(HttpServletRequest req) 
			throws IOException, ServletException {
		String userCf = req.getParameter("fiscal_code");

		UserBean user = new UserBean();
		user.setCf(userCf);
		user.setName(req.getParameter("name"));
		user.setSurname(req.getParameter("surname"));
		user.setPhoto(ServletUtil.saveUserFile(req, "your_photo", userCf));
		user.setPhoneNumber(req.getParameter("phone_number"));

		String businessName = req.getParameter("business_name");
		if(businessName != null) {
			user.setAdmin(true);
			user.setEmployee(true);
			user.setRecruiter(ServletUtil.checkboxToBoolean(req.getParameter("are_you_recruiter")));
			user.setCompany(
				BeanFactory.buildCompanyBean(
					req.getParameter("company_fiscal_code"), ServletUtil.saveUserFile(req, "company_logo", userCf),
					businessName, req.getParameter("vat_number")));
			user.setNote(null);
		} else {
			user.setBiography(null);
			user.setHomeAddress(req.getParameter("address"));
			user.setEmploymentStatus(BeanFactory.buildEmploymentStatusBean(req.getParameter("employment_status")));
			user.setCv(ServletUtil.saveUserFile(req, "cv", userCf));
			user.setBirthday(Util.deriveBirthdayFromFiscalCode(userCf));
			user.setComune(BeanFactory.buildComuneBean(req.getParameter("town")));
			user.setWebsite("https://whork.it");
			user.setTwitter(WHORK);
			user.setFacebook(WHORK);
			user.setInstagram(WHORK);

		}
		
		UserAuthBean userAuth = 
			BeanFactory.buildUserAuthBean(req.getParameter("email"), req.getParameter("pwd"));

		return new Pair<>(user, userAuth);
	}
}
