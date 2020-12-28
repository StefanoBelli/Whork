<%@page import="logic.model.EmploymentStatusModel"%>
<%@page import="java.util.ArrayList"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Sign up</title>
	</head>
	
	<body lang="eng">
		
		<form>
			<p>
				<label for="email"> Email</label>
				<input type="text" name="register" id="email" placeholder="Enter your email">
			</p>
			<p>
				<label for="password"> Password</label>
				<input type="password" name="register" id="password" placeholder="Enter your password">
			</p>
			<p>
				<label for="confirm-password"> Confirm your password</label>
				<input type="password" name="register" id="confirm-password" placeholder="Confirm your password">
			</p>
			
			<p><b>Personal Details</b></p>
			
			<p>
				<label for="name"> Name </label>
				<input type="text" name="register" id="name" placeholder="Enter your name">
			</p>
			<p>
				<label for="surname"> Surname </label>
				<input type="text" name="register" id="surname" placeholder="Enter your surname">
			</p>
			<p>
				<label for="fiscal-code"> Fiscal Code </label>
				<input type="text" name="register" id="fiscal-code" placeholder="Enter your fiscal code">
			</p>
			<p><b>Contact Details</b></p>
			
			<p>
				<label for="town"> Town </label>
				<input type="text" name="register" id="town" placeholder="Enter your town">
			</p>
			<p>
				<label for="address"> Address </label>
				<input type="text" name="register" id="address" placeholder="Enter your address">
			</p>
			<p>
				<label for="phone-number"> Phone Number </label>
				<input type="text" name="register" id="phone-number" placeholder="Enter your phone number">
			</p>
			<p>			
				<input type="text" name="register" id="province" placeholder="Province">
				<input type="text" name="register" id="postal code" placeholder="Postal Code">
			</p>
			
			<p><b>Employment Status</b></p>
			
			<select name="employment-status" size="1">
			
				<% @SuppressWarnings (value="unchecked")
				ArrayList<EmploymentStatusModel> list = (ArrayList<EmploymentStatusModel>) request.getAttribute("listElements");
				for(EmploymentStatusModel el:list){%>
				<option><%=el.getPosition()%></option>
				<%}%>
				
			</select>
			
			<p>
				<input type="submit" name="submit" value="Confirm">
			</p>
						
		</form>
		
	</body>
</html>