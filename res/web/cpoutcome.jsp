<!DOCTYPE html>
<html xml:lang="en">
	<head>
		<title>Whork - Password Change Outcome</title>
		<script src="js/common.js"></script>
		<script>
			redirect("login.jsp", 5000);
		</script>
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