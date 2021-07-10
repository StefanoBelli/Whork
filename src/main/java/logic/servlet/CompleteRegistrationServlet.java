package logic.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import logic.bean.CompanyBean;
import logic.bean.ComuneBean;
import logic.bean.EmploymentStatusBean;
import logic.bean.UserAuthBean;
import logic.bean.UserBean;
import logic.exception.SyntaxException;
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
		
		UserAuthBean userAuth = new UserAuthBean();
		userAuth.setEmail(req.getParameter("email"));
		userAuth.setPassword(req.getParameter("password"));

		String areYouRecruiter = req.getParameter("are_you_recruiter");
		if(areYouRecruiter != null) {
			CompanyBean company = new CompanyBean();
			company.setSocialReason(req.getParameter("business_name"));
			company.setVat(req.getParameter("vat_number"));
			company.setCf(req.getParameter("company_fiscal_code"));
			company.setLogo(ServletUtil.saveUserFile(req, "company_logo", userCf));

			user.setAdmin(true);
			user.setRecruiter(ServletUtil.checkboxToBoolean(areYouRecruiter));
			user.setCompany(company);
			user.setNote(null);
		} else {
			EmploymentStatusBean employmentStatus = new EmploymentStatusBean();
			employmentStatus.setStatus(req.getParameter("employment_status"));

			user.setBiography(null);
			user.setHomeAddress(req.getParameter("address"));
			user.setEmploymentStatus(employmentStatus);
			user.setCv(ServletUtil.saveUserFile(req, "cv", userCf));
			user.setBirthday(Util.deriveBirthdayFromFiscalCode(userCf));
			user.setComune(parseTownString(req.getParameter("town")));
		}

		return new Pair<>(user, userAuth);
	}

	private ComuneBean parseTownString(String town) {
		//instance str: "Cavenago d'Adda LO - 26824, Lombardia"
		return null;
	}
}
