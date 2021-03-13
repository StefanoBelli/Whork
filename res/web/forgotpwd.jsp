<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE HTML>
<html xml:lang="en">
	<head>
		<title>Whork - Forgot my password</title>
	</head>

	<body>
		<form action="forgotPassword" method="post">
			<label for="email">Email</label>
			<input placeholder="Email address..." type="email" 
				name="email" required>
			
			<br /><br />
			<input type="submit" value="Recover my password">
		</form>
<%
		String email = (String) request.getAttribute("reqEmail");
		if(email != null) {
%>
		A password reset request has been made.
		If "<%=email%>" is a valid and registered email address, then you will find an
		email in your inbox (check spam) with instructions.
<%
		}
%>
	</body>
</html>