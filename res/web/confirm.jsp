<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="logic.controller.RegisterController" %>
<%@ page import="logic.exception.InternalException" %>
<!DOCTYPE HTML> <!-- TODO use same approach as success.jsp -->
<html lang="en">
	<head>
		<title>Confirm registration - Whork</title>
		<script src="js/common.js"></script>
		<script>
			redirect("login.jsp", 3000);
		</script>
	</head>
	
	<body>
<%
		String token = (String) request.getParameter("token");
		String email = (String) request.getParameter("email");

		if(token != null && email != null) {
			try {
				RegisterConfirmationController.confirm(email, token);
%>
		Your account is confirmed! Redirecting you to login page in 3 seconds...
<%
			} catch(InternalException e) {
%>
		Error: <%=e.getMessage()%>
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