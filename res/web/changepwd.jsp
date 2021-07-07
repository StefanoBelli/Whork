<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE HTML>
<html xml:lang="en">
	<head>
		<title>Whork - Change my password</title>
		<script src="js/changepwd.js"></script>
	</head>
	
	<body>
<%
	String token = (String) request.getParameter("token");
	if(token != null) {
%>
		<form action="changePassword" method="post">
			<label for="pwdFirst">Password</label>
			<input placeholder="New password..." type="password" id="pwd" 
				name="pwdFirst" 
				onchange='check_passwd_match();' required>

			<label for="pwdSecond">Retype password</label>
			<input placeholder="Retype password..." type="password" id="conf_pwd" 
				onchange='check_passwd_match();' required>
			
			<input type="hidden" name="token" value="<%=token%>">

			<br /><br />
			<input id="submit" type="submit" value="Change my password" disabled>
		</form>
<%
	} else {
%>
		<h1>No token supplied</h1>
<%
	}
%>
	</body>

</html>