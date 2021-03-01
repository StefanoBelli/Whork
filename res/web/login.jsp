<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE HTML>
<html xml:lang="en">
	<head>
		<title>Login to Whork</title>
		<link rel="stylesheet" href="css/login.css">
		<link rel="preconnect" href="https://fonts.gstatic.com">
		<link href="https://fonts.googleapis.com/css2?family=Ubuntu:wght@300&display=swap" 
				rel="stylesheet">
	</head>

	<body lang="en">

<%
		if(request.getAttribute("showMustLoginInfo") != null) {
%>
		<p id="mustlogin">You must be logged in to do that</p>
<%
		}
%>
		<h1 id="title">Login to Whork</h1>

		<div id="loginform">
			<form id="loginf" action="validateLogin" method="post">
				<div id="credInput">
					<label for="email">Email</label>
					<input placeholder="Enter email here..." type="email" name="email" required>

					<br/><br/>

					<label for="passwd">Password</label>
					<input placeholder="Enter password here..." type="password" name="passwd" required>

					<br/><br/>

					<label for="stayLoggedIn">Stay logged in</label>
					<input type="checkbox" name="stayLoggedIn">
				</div>

				<br/><br/>
				<input type="submit" value="Login">
			</form>
		</div>

		<div id="errors">
<%
		String errorMessage = (String) request.getAttribute("errorMessage");

		if(errorMessage != null) {
%>
			<p id="errmsg"><%=errorMessage%></p>
<%
		}

		if(request.getAttribute("showPasswordRecoveryButton") != null) {
%>
			<form id="forgotbtn" action="forgotpwd.jsp" method="get">
				<input type="submit" value="I forgot my password">
			</form>
<%
		}
%>
		</div>

	</body>
</html>