package logic.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import logic.bean.UserAuthBean;
import logic.bean.UserBean;
import logic.exception.SyntaxException;
import logic.factory.BeanFactory;
import logic.util.ServletUtil;
import logic.util.Util;
import logic.util.tuple.Pair;

@MultipartConfig
public final class CompleteRegistrationServlet extends HttpServlet {
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {

	}

	/**
	 * 
	 * @param req
	 * @return
	 * @throws SyntaxException - not supposed to be thrown at all
	 * @throws IOException - not supposed to be thrown (assuming enough disk space) just log, show InternalException
	 * @throws ServletException - ??? just log, show InternalException
	 */
	private Pair<UserBean, UserAuthBean> createBeansFromRequest(HttpServletRequest req) 
			throws SyntaxException, IOException, ServletException {
		String userCf = req.getParameter("fiscal_code");

		UserBean user = new UserBean();
		user.setCf(userCf);
		user.setName(req.getParameter("name"));
		user.setSurname(req.getParameter("surname"));
		user.setPhoto(ServletUtil.saveUserFile(req, "your_photo", userCf));
		user.setPhoneNumber(req.getParameter("phone_number"));

		String areYouRecruiter = req.getParameter("are_you_recruiter");
		if(areYouRecruiter != null) {
			user.setAdmin(true);
			user.setRecruiter(ServletUtil.checkboxToBoolean(areYouRecruiter));
			user.setCompany(
				BeanFactory.buildCompanyBean(
					req.getParameter("company_fiscal_code"), ServletUtil.saveUserFile(req, "company_logo", userCf),
					req.getParameter("business_name"), req.getParameter("vat_number")));
			user.setNote(null);
		} else {
			user.setBiography(null);
			user.setHomeAddress(req.getParameter("address"));
			user.setEmploymentStatus(BeanFactory.buildEmploymentStatusBean(req.getParameter("employment_status")));
			user.setCv(ServletUtil.saveUserFile(req, "cv", userCf));
			user.setBirthday(Util.deriveBirthdayFromFiscalCode(userCf));
			user.setComune(BeanFactory.buildComuneBean(req.getParameter("town")));
		}
		
		UserAuthBean userAuth = 
			BeanFactory.buildUserAuthBean(req.getParameter("email"), req.getParameter("password"));

		return new Pair<>(user, userAuth);
	}
}
