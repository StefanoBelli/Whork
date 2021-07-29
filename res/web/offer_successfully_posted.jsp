<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String name = (String) request.getAttribute("name");
String offer_name = (String) request.getAttribute("offer_name");

if(name != null && offer_name != null) {
%>
<!DOCTYPE html>
<html lang="en">
	<head>
		<title>Success - Whork</title>
		<script src="js/common.js"></script>
		<script>
			redirect("account.jsp", 10000);
		</script>
	</head>
	<body>
		<h1>Success</h1>
		Yay! <%=name%> You did it! You successfully post offer <%=offer_name%>.<br/>
		The Whork team.
		You will be redirected to login page in 10 seconds...
	</body>
</html>
<%
}
%>