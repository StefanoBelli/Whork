<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
	<head>
		<title>Sign Up as a company - Whork</title>
		<meta name="author" content="Stefano Belli">
		<meta name="author" content="Magliari Elio">
		<link rel="stylesheet" href="css/reg_company.css">
		<script src="js/reg_company.js"></script>
		<script src="js/common.js"></script>
		<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
		<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Roboto:400,700">
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css">
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
		<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
		<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"></script>
		<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js"></script> 
	</head>

	<body style="background:#3598dc">
<%
	String descError = (String) request.getAttribute("descriptive_error");
	if(descError != null) {
%>
		<div class="alert alert-danger" role="alert">
			<%=descError%>
		</div>
<%
		request.setAttribute("descriptive_error", null);
	}
%>
		
		<p id="error"></p>
		
		<div class="signup-form">
			<form enctype="multipart/form-data" action="/completeRegistration" method="post" onsubmit="return check_company_fields();">		
			<h2>Sign Up as a Company</h2>
				<p>Please fill in this form to create an account!</p>
				<hr>
				<h5 style="font-weight:bold;">Authentication</h5>
				<div class="form-group">
		        	<input type="email" name="email" class="form-control" placeholder="Enter your email here ..." maxlength=255 required>
		        </div>
		        <div class="form-group">
		        	<input type="password" id="pwd" name="pwd" class="form-control" placeholder="Enter your password here ..." required>
		        </div>
		        <div class="form-group">
		        	<input type="password" id="conf_pwd" name="conf_pwd" class="form-control" placeholder="Confirm your password here..." required>
		        </div>
		        <hr>
		        <h5 style="font-weight:bold;">About you </h5>
		        <div class="form-group">
					<div class="row">
						<div class="col">
							<input type="text" name="name" class="form-control" placeholder="Enter your name here..." maxlength=45 required>
						</div>
						<div class="col">
							<input type="text" name="surname" class="form-control" placeholder="Enter your surname here..." maxlength=45 required>
						</div>
					</div>        	
		        </div>
				<div class="form-group">
		            <input type="text" id="my_fc" name="fiscal_code" class="form-control" placeholder="Enter your fiscal code here..." maxlength=16 minlength=16 required>
		        </div>
				<div class="form-group">
		            <input type="text" name="phone_number" onkeypress="return is_digit(event);"	class="form-control" placeholder="Enter phone number here..." minlength=9 maxlength=10 required>
		        </div>
		        <div class="form-group">
		        	<label for="are_you_recruiter">
		            	<input type="checkbox" name="are_you_recruiter" checked="true"> Are you a recruiter for your company?
		            </label>
		            <span style="padding-left:40px">
			            <label class="btn btn-default btn-file">                      	
	                     	<font color="white">Your profile photo</font>
	                     	<input type="file" name="your_photo" accept="image/png, image/jpeg" style="display:none">
						</label>
					</span>
		        </div>
		        <hr>
		        <h5 style="font-weight:bold;">About the company</h5>
		        <div class="form-group">
		            <input type="text" name="business_name" class="form-control" placeholder="Enter business name here..." maxlength=45 required>
		        </div>
		        <div class="form-group">
		            <input type="text" name="vat_number" onkeypress="return is_digit(event);" class="form-control" placeholder="Enter company VAT number here ..." maxlength=11 minlength=11 required>
		        </div>
		        <div class="form-group">
		            <input type="text" id="company_fc" name="company_fiscal_code" class="form-control" placeholder="Enter company fiscal code here ..." maxlength=16 required>
		        </div>
		        <div class="form-group">
		        	<label class="btn btn-default btn-file">
                     	<font color="white">Company logo</font>
                     	<input type="file" name="company_logo" accept="image/png, image/jpeg" required style="display:none">
					</label>
		        </div>        
		        <div class="form-group">
					<label for="privacy_policy">
						<input type="checkbox" id="ppolicy" name="privacy_policy" onchange="reg_toggle_submit();">
						I have read the Customer Privacy Policy and consent to the processing
						of my personal data for the purposes related to the management of the contractual relationship and
						the provision of services.
					</label>
				</div>
				<div class="form-group" style="text-align:center">
		            <button disabled type="submit" id="submit" name="submit" value="Confirm" class="btn btn-primary btn-lg">Sign Up</button>
		            <div class="hint-text" style="padding-top:40px">Already have an account? <a href="login.jsp">Login here</a></div>
		        </div>
		    </form>
		</div>

	</body>
</html>