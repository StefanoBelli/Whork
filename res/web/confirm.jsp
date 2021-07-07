<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="logic.controller.RegisterConfirmationController" %>
<%@ page import="logic.exception.InternalException" %>
<!DOCTYPE HTML>
<html lang="en">
	<head>
		<title>Confirm registration - Whork</title>
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
				Your account is confirmed! <!-- auto redirect to login -->
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