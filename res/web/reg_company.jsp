<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
	<head>
		<title>Register as a company - Whork</title>
		<script src="js/reg_company.js"></script>
	</head>

	<body>
<% %> <!-- root error-->
		<form action="/completeRegistration" method="post" onsubmit="return check_company_fields();">
			<h2>Authentication</h2>
<% %> <!-- error -->
			<label for="email">Email</label>
			<input type="email" name="email" placeholder="Enter your email here ..." 
				maxlength=255 required>
			
			<label for="password">Password</label>
			<input type="password" id="pwd" name="password" 
				placeholder="Enter your password here ..." required>
			
			<label for="confirm_password">Confirm your password</label>
			<input type="password" id="conf_pwd" name="confirm_password" 
				placeholder="Confirm your password here..." required>

			<h2>About you</h2>
			
			<label for="name">Name</label>
			<input type="text" name="name" 
				placeholder="Enter your name here..." maxlength=45 required>
			
			<label for="surname">Surname</label>
			<input type="text" name="surname" 
				placeholder="Enter your surname here..." maxlength=45 required>

<% %> <!-- error -->
			<label for="fiscal_code">Fiscal code</label>
			<input type="text" id="my_fc" name="fiscal_code" 
				placeholder="Enter your fiscal code here..." maxlength=16 minlength=16 required>
			
			<label for="phone_number">Phone number</label>
			<input type="text" name="phone_number" 
				placeholder="Enter phone number here..." minlength=9 maxlength=10 required>

			<label for="are_you_recruiter">Are you a recruiter for your company?</label>
			<input type="checkbox" name="are_you_recruiter" checked="true">

<% %> <!-- error -->
			<label for="your_photo">Your profile photo</label>
			<input type="file" name="your_photo" accept="image/png, image/jpeg">

			<h2>About the company</h2>
			
			<label for="business_name">Business name</label>
			<input type="text" name="business_name" 
				placeholder="Enter business name here..." maxlength=45 required>

			<label for="vat_number">VAT number</label>
			<input type="text" name="vat_number"
				placeholder="Enter company VAT number here ..." maxlength=11 minlength=11 required>

<% %> <!-- error -->
			<label for="company_fiscal_code">Fiscal code</label>
			<input type="text" id="company_fc" name="company_fiscal_code" 
				placeholder="Enter company fiscal code here ..." maxlength=16 required>

<% %> <!-- error -->
			<label for="company_logo">Company logo</label>
			<input type="file" name="company_logo" accept="image/png, image/jpeg" required>

			<input type="checkbox" id="ppolicy" 
				name="privacy_policy" onchange="toggle_submit();">
			<label for="privacy_policy">
			I have read the Customer Privacy Policy and consent to the processing
			of my personal data for the purposes related to the management of the contractual relationship and
			the provision of services
			</label><br>
			
			<input disabled type="submit"
				id="submit" name="submit" value="Confirm">
		</form>
	</body>
</html>