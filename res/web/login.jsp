<%@ page contentType="text/html; charset=UTF-8" %>
<html>
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
			<form action="validateLogin" method="post">
				<label for="email">Email</label>
				<input placeholder="Enter email here..." type="email" name="email" required>

				<br/><br/>

				<label for="passwd">Password</label>
				<input placeholder="Enter password here..." type="password" name="passwd" required>

				<br/><br/>

				<input type="submit" value="Login"/>
			</form>
		</div>

<%
		String errorMessage = (String) request.getAttribute("errorMessage");

		if(errorMessage != null) {
%>
		<p id="errmsg"><%=errorMessage%></p>
<%
		}
%>

	</body>
</html>