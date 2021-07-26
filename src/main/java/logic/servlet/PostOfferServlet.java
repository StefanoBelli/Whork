package logic.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import logic.bean.OfferBean;
import logic.bean.UserBean;
import logic.controller.OfferController;
import logic.factory.BeanFactory;
import logic.util.ServletUtil;
import logic.util.Util;


public final class PostOfferServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8258513746798360851L;

	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
		String descriptiveError = null;
		OfferBean offerBean = null;
		
		try {
			offerBean= createBeansFromRequest(req);
			OfferController.postOffer(offerBean);
		} catch(Exception e) {
			Util.exceptionLog(e);
			descriptiveError = 
				"An internal error happened, this is totally our fault. Please report, we have logs and will try to fix asap";
		}
		
		if(descriptiveError == null) {
			req.setAttribute("name", offerBean.getEmployee().getName());
			req.setAttribute("offer_name", offerBean.getOfferName());

			dispatchSuccess(req, resp);
		} else {
			req.setAttribute("descriptive_error", descriptiveError);
		}
		
	}
	
	private void dispatchSuccess(HttpServletRequest req, HttpServletResponse resp) {
		try {
			req.getRequestDispatcher("offer_successfully_posted.jsp").forward(req, resp);
		} catch(ServletException | IOException e) {
			Util.exceptionLog(e);
		}
	}
	
	private OfferBean createBeansFromRequest(HttpServletRequest req) throws IOException, ServletException {
		UserBean userBean = ServletUtil.getUserForSession(req);
		OfferBean offerBean= new OfferBean();
		offerBean.setOfferName(req.getParameter("name"));
		offerBean.setDescription(req.getParameter("description"));
		offerBean.setSalaryEUR(Integer.parseInt(req.getParameter("salaryEur")));
		offerBean.setJobPhysicalLocationFullAddress(req.getParameter("address"));
		offerBean.setCompany(userBean.getCompany());
		offerBean.setPhoto(ServletUtil.saveUserFile(req, "offer_photo", userBean.getCf()));
		offerBean.setJobCategory(BeanFactory.buildJobCategoryBean
				(req.getParameter("job_category")));
		offerBean.setJobPosition(BeanFactory.buildJobPositionBean
				(req.getParameter("job_position")));
		offerBean.setTypeOfContract(BeanFactory.buildTypeOfContractBean
				(req.getParameter("type_of_contract")));
		offerBean.setQualification(BeanFactory.buildQualificationBean
				(req.getParameter("qualification")));
		offerBean.setNote(req.getParameter("note"));
		offerBean.setEmployee(userBean);
		
		
		
		return offerBean;
		
	}
}
