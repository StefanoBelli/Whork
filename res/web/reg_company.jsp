<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
	<head>
		<title>Register as a company - Whork</title>
		<link rel="stylesheet" href="css/reg_company.css">
		<script src="js/reg_company.js"></script>
		<script src="js/common.js"></script>
		<link href="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
		<script src="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js"></script>
		<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
	</head>

	<body>
<%
String descError = (String) request.getAttribute("descriptive_error");
if(descError != null) {
%>
		<h3><%=descError%></h3>
<%
}
%>
		<form enctype="multipart/form-data" action="/completeRegistration" method="post"
				onsubmit="return check_company_fields();">
			<h2>Authentication</h2>
			
			<label for="email">Email</label>
			<input type="email" name="email" placeholder="Enter your email here ..." 
				maxlength=255 required>
			
			<label for="password">Password</label>
			<input type="password" id="pwd" name="pwd" 
				placeholder="Enter your password here ..." required>
			
			<label for="confirm_password">Confirm your password</label>
			<input type="password" id="conf_pwd" name="conf_pwd" 
				placeholder="Confirm your password here..." required>

			<h2>About you</h2>
			
			<label for="name">Name</label>
			<input type="text" name="name" 
				placeholder="Enter your name here..." maxlength=45 required>
			
			<label for="surname">Surname</label>
			<input type="text" name="surname" 
				placeholder="Enter your surname here..." maxlength=45 required>

			<label for="fiscal_code">Fiscal code</label>
			<input type="text" id="my_fc" name="fiscal_code" 
				placeholder="Enter your fiscal code here..." maxlength=16 minlength=16 required>
			
			<label for="phone_number">Phone number</label>
			<input type="text" name="phone_number" onkeypress="return is_digit(event);"
				placeholder="Enter phone number here..." minlength=9 maxlength=10 required>

			<label for="are_you_recruiter">Are you a recruiter for your company?</label>
			<input type="checkbox" name="are_you_recruiter" checked="true">

			<label for="your_photo">Your profile photo</label>
			<input type="file" name="your_photo" accept="image/png, image/jpeg">

			<h2>About the company</h2>
			
			<label for="business_name">Business name</label>
			<input type="text" name="business_name" 
				placeholder="Enter business name here..." maxlength=45 required>

			<label for="vat_number">VAT number</label>
			<input type="text" name="vat_number" onkeypress="return is_digit(event);"
				placeholder="Enter company VAT number here ..." maxlength=11 minlength=11 required>

			<label for="company_fiscal_code">Fiscal code</label>
			<input type="text" id="company_fc" name="company_fiscal_code" 
				placeholder="Enter company fiscal code here ..." maxlength=16 required>

			<label for="company_logo">Company logo</label>
			<input type="file" name="company_logo" accept="image/png, image/jpeg" required>

			<h2>Privacy policy</h2>
			
			<input type="checkbox" id="ppolicy" 
				name="privacy_policy" onchange="reg_toggle_submit();">
			<label for="privacy_policy">
			I have read the Customer Privacy Policy and consent to the processing
			of my personal data for the purposes related to the management of the contractual relationship and
			the provision of services
			</label><br>
			
			<input disabled type="submit"
				id="submit" name="submit" value="Confirm">
		</form>

		<br/>
		<p id="error"></p>
		
		
		


<!------ Include the above in your HEAD tag ---------->

<div class="container">
  <div class="overlay" id="overlay">
    <div class="sign-in" id="sign-in">
      <h1>Welcome Back!</h1>
      <p>To keep connected with us please login with your personal info</p>
      <button class="switch-button" id="slide-right-button">Sign In</button>
    </div>
    <div class="sign-up" id="sign-up">
      <h1>Hello, Friend!</h1>
      <p>Enter your personal details and start a journey with us</p>
      <button class="switch-button" id="slide-left-button">Sign Up</button>
    </div>
  </div>
  <div class="form">
    <div class="sign-in" id="sign-in-info">
      <h1>Sign In</h1>
      <form id="sign-in-form">      
        <input type="email" placeholder="Email"/>
        <input type="password" placeholder="Password"/>
        <p class="forgot-password">Forgot your password?</p>
        <button class="control-button in">Sign In</button>
      </form>
    </div>
    <div class="sign-up" id="sign-up-info">
      <h1>Create Account</h1>      
		<form enctype="multipart/form-data" action="/completeRegistration" method="post" onsubmit="return check_company_fields();">
		  
		  
		 
		
		  <!-- Submit button -->
		  <button type="submit" class="btn btn-primary btn-block mb-4">Sign up</button>
		
		  <!-- Register buttons -->
      </form>
    </div>
  </div>
</div>
		
		
	</body>
</html>