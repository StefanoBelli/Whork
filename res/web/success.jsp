<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
	<head>
		<title>Success - Whork</title>
		<meta name="author" content="Stefano Belli">
		<meta name="author" content="Magliari Elio">
		<script src="js/common.js"></script>
		<link href="https://fonts.googleapis.com/css?family=Nunito+Sans:400,400i,700,900&display=swap" rel="stylesheet">
		<link rel="stylesheet" href="css/success.css">
		<script>
			redirect("login.jsp", 10000);
		</script>
	</head>
	
<%
	String email = (String) request.getAttribute("email");
	String name = (String) request.getAttribute("name");
	String companyName = (String) request.getAttribute("company");

	if(email != null && name != null) {
%>
	<body style="background-color: #90EE90">
		<div class="card">
	      <div style="border-radius:200px; height:200px; width:200px; background: #F8FAF5; margin:0 auto;">
	        <i class="checkmark">&#10003;</i>
	      </div>
	      <h1>Success</h1> 
	      <p>Yay! <%=name%> You did it! You successfully signed up for Whork,<br/>
			now it is time to confirm your request to join us by checking for a mail<br/>
			we sent you at the address you just gave us: <%=email%>, be sure to check spam also.<br/>
			The Whork team.
			You will be redirected to login page in 10 seconds...</p>
	      <div style="padding-top:40px">
		      <a href="login.jsp">
		      	<button type="submit" class="button button2">Go to Login page</button>
		      </a>
	      </div>
<%
		if(companyName != null) {
%>			
			<h1>About your company</h1>
			<p> Just to confirm, we correctly registered and verified your company: <%=companyName%> </p>
<%
		}
%>
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

