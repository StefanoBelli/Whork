<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE HTML>
<html xml:lang="en">
	<script>
		function checkPass() {
			if (document.getElementById('pwd').value ==
				document.getElementById('pwdConfirm').value) {
				document.getElementById('submit').disabled = false;
			} else {
				document.getElementById('submit').disabled = true;
			}
		}
	</script>
	<head>
		<title>Whork - Change my password</title>
	</head>
	
	<body>
<%
	String token = (String) request.getParameter("token");
	if(token != null) {
%>
		<form action="changePassword" method="post">
			<label for="pwdFirst">Password</label>
			<input placeholder="New password..." type="password" id="pwd" name="pwdFirst" onchange="checkPass()" required>

			<label for="pwdSecond">Retype password</label>
			<input placeholder="Retype password..." type="password" id="pwdConfirm" onchange="checkPass()" required>
			
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