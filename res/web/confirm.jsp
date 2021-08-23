<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="logic.controller.RegisterController" %>
<%@ page import="logic.exception.InternalException" %>
<%
String token = (String) request.getParameter("token");
String email = (String) request.getParameter("email");
if(token != null && email != null) {
%>
<!DOCTYPE HTML>
<html lang="en">
	<head>
		<title>Confirm registration - Whork</title>
		<meta name="author" content="Stefano Belli">
		<meta name="author" content="Magliari Elio">
		<link rel="stylesheet" href="css/cpoutcome.css">
		<script src="js/common.js"></script>
		<script>
			redirect("login.jsp", 3000);
		</script>
	</head>
<%
	try {
		RegisterController.confirm(email, token);
%>
	  <body style="background-color: #90EE90">
		<div class="card">
	      <div style="border-radius:200px; height:200px; width:200px; background: #F8FAF5; margin:0 auto;">
	        <i class="checkmark">&#10003;</i>
	      </div>
	      <h1>Success</h1> 
	      <p>Your account is confirmed!<br/>Redirecting you to login page in 3 seconds..</p>
	      <div style="padding-top:40px">
		      <a href="login.jsp">
		      	<button type="submit" class="button button2">Go to Login page</button>
		      </a>
	      </div>
        </div>
      </body>
<%
	} catch(InternalException e) {
%>
	 <body style="background-color: #FFA07A">
		<div class="card">
	      <div style="border-radius:200px; height:200px; width:200px; background: #ffcccb; margin:0 auto;">
	        <i style="color:#FF0000">&#10007;</i>
	      </div>
	      <h1 style="color:#FF0000">Error</h1> 
	      <p><%=e.getMessage()%><br/>Redirecting you to login page in 3 seconds...</p>
	      <div style="padding-top:40px">
		      <a href="login.jsp">
		      	<button type="submit" class="button button1">Go to Login page</button>
		      </a>
	      </div>
	     </div>
	 </body>
<%
	}
%>

</html>
<%
}
%>
