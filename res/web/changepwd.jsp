<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE HTML>
<html xml:lang="en">
	<head>
		<title>Whork - Change my password</title>
	</head>
	
	<body>
		<!-- forward token and email to servlet -->
		<form action="changePassword" method="post">
			<label for="pwdFirst">Password</label>
			<input placeholder="New password..." type="password" name="pwdFirst" required>

			<label for="pwdSecond">Retype password</label>
			<input placeholder="Retype password..." type="password" name="pwdSecond" required>
			
			<br /><br />
			<input type="submit" value="Change my password">
		</form>
	</body>

</html>