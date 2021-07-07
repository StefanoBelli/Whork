<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="logic.controller.RegisterController" %>
<%@ page import="logic.exception.InternalException" %>
<!DOCTYPE HTML>
<html lang="en">
	<head>
		<title>Confirm registration - Whork</title>
		<script src="js/redirect.js"></script>
	</head>
	
	<body>
<%
		boolean isOk = true;
		String token = (String) request.getParameter("token");
		String email = (String) request.getParameter("email");

		if(token != null && email != null) {
			try {
				RegisterConfirmationController.confirm(email, token);
			} catch(InternalException e) {
				isOk = false;
%>
		Error: <%=e.getMessage()%>
<%
			}

			if(isOk) {
%>
				Your account is confirmed! Redirecting you to login page in 3 seconds...
				<script>redirect("login.jsp", 3000);</script>
<%
			}
		} else {
%>
		Error: missing one or both of needed parameters: token, email
<%
		}
%>

	</body>
</html>