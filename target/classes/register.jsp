<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
	<head>
		<title>Sign Up - Whork</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" type="text/css" href="css/register.css">
		<link rel="stylesheet" type="text/css" href="css/whork.css">
		<link rel="preconnect" href="https://fonts.gstatic.com">
	 	<link href="https://fonts.googleapis.com/css2?family=Kameron&display=swap" rel="stylesheet">
	 	<link href="https://fonts.googleapis.com/css2?family=Bungee+Shade&display=swap" rel="stylesheet">
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
		<div>
			<div class="rectangle">
				<h1 id="signup_title">Sign Up</h1>
				<form action="/reg_jobseeker.jsp" method="get">
					<input type="submit" value="Are you a job seeker?">
				</form>
				<form action="/reg_company.jsp" method="get">
					<input type="submit" value="Are you a company?">
				</form>
				<form action="/login.jsp" method="get">
					<input type="submit" value="I already have an account">
				</form>
			</div>
			
			<div class="right-container">
				<span class="whork"> W<span class="hred">h</span>ork</span>
				<div class="catchphrase">
					After all, work is still the best way to get life going.
					Gustave Flaubert
				</div>
			</div>
		</div>
	</body>
</html>