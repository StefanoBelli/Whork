<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String email = (String) request.getAttribute("email");
String name = (String) request.getAttribute("name");
String companyName = (String) request.getAttribute("company");

if(email != null && name != null) {
%>
<!DOCTYPE html>
<html lang="en">
	<head>
		<title>Success - Whork</title>
		<script src="js/common.js"></script>
		<script>
			redirect("login.jsp", 10000);
		</script>
	</head>
	<body>
		<h1>Success</h1>
		Yay! <%=name%> You did it! You successfully signed up for Whork,<br/>
		now it is time to confirm your request to join us by checking for a mail<br/>
		we sent you at the address you just gave us: <%=email%>, be sure to check spam also.<br/>
		The Whork team.
		You will be redirected to login page in 10 seconds...

<%
if(companyName != null) {
%>
		<h2>About your company</h2>
		Just to confirm, we correctly registered and verified your company: <%=companyName%>
<%
}
%>
	</body>
</html>
<%
}
%>