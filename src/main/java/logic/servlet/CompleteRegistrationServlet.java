package logic.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@MultipartConfig
public final class CompleteRegistrationServlet extends HttpServlet {
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		//TODO NOTES:
		// * derivate birthday from fiscal code
		// * recruiter for company is also an admin
		// * null biography
		
		//common
		req.getParameter("email");
		req.getParameter("password");
		req.getParameter("name");
		req.getParameter("surname");
		req.getParameter("fiscal_code");
		req.getParameter("your_photo");
		req.getParameter("phone_number");
		//specific JobSeeker
		req.getParameter("town");
		req.getParameter("address");
		req.getParameter("employment_status");
		req.getParameter("cv"); // file to download
		//specific Company
		req.getParameter("are_you_recruiter");
		req.getParameter("business_name");
		req.getParameter("vat_number");
		req.getParameter("company_fiscal_code");
		req.getParameter("company_logo"); // file to download
	}
}
