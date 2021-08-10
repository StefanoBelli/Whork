<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String name = (String) request.getAttribute("name");
String offer_name = (String) request.getAttribute("offer_name");
%>

<!DOCTYPE html>
<html lang="en">
	<head>
		<title>Success - Whork</title>
		<script src="js/common.js"></script>
		<link href="https://fonts.googleapis.com/css?family=Nunito+Sans:400,400i,700,900&display=swap" rel="stylesheet">
		<link rel="stylesheet" href="css/success.css">
		<script>
			redirect("account.jsp", 10000);
		</script>
	</head>
<%
	if(name != null && offer_name != null) {
%>
	<body style="background-color: #90EE90">
		<div class="card">
	      <div style="border-radius:200px; height:200px; width:200px; background: #F8FAF5; margin:0 auto;">
	        <i class="checkmark">&#10003;</i>
	      </div>
	      <h1>Success</h1> 
	      <p>Yay! <%=name%> You did it! You successfully post offer <%=offer_name%>.<br/>
			The Whork team.<br/>
			You will be redirected to login page in 10 seconds...</p>
	      <div style="padding-top:40px">
		      <a href="login.jsp">
		      	<button type="submit" class="button button2">Go to Login page</button>
		      </a>
	      </div>
	    </div>

	</body>
<%
	} else {
%>
	<body style="background-color: #FFA07A">
		<div class="card">
	      <div style="border-radius:200px; height:200px; width:200px; background: #ffcccb; margin:0 auto;">
	        <i style="color:#FF0000">&#10007;</i>
	      </div>
	      <h1 style="color:#FF0000">Error</h1> 
	      <p>Something went wrong.</p>
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

