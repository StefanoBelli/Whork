<!DOCTYPE html>
<html xml:lang="en">
	<head>
		<title>Whork - Password Change Outcome</title>
	</head>

	<body>
		<form action="login.jsp" method="get">
			<input type="submit" value="Login">
		</form>
<%
	String outcomeStr = (String) request.getParameter("ok");
	boolean outcome = 
		outcomeStr != null && outcomeStr.equals("true");

	if(outcome) {
%>
		<h1>OK! Your password was changed!</h1>
<%
	} else {
%>
		<h1>Something went wrong!</h1> 
<%
	}
%>
	</body>
</html>